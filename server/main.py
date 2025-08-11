import os
import json
import firebase_admin
from firebase_admin import credentials, messaging
from fastapi import FastAPI, Depends, HTTPException, Header
from sqlalchemy.orm import Session
from typing import List, Annotated

from . import crud, models, schemas
from .database import SessionLocal, engine

# --- Firebase Admin SDK Initialization ---
try:
    firebase_service_account_json_str = os.getenv("FIREBASE_SERVICE_ACCOUNT")
    if firebase_service_account_json_str:
        firebase_cred_dict = json.loads(firebase_service_account_json_str)
        cred = credentials.Certificate(firebase_cred_dict)
        firebase_admin.initialize_app(cred)
        print("Firebase Admin SDK initialized successfully.")
    else:
        print("FIREBASE_SERVICE_ACCOUNT env var not found. Skipping Firebase Admin SDK initialization.")
except Exception as e:
    print(f"Error initializing Firebase Admin SDK: {e}")

models.Base.metadata.create_all(bind=engine)

app = FastAPI()

# --- Dependencies --- #
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

def get_current_user(x_device_id: Annotated[str, Header()], db: Session = Depends(get_db)):
    user = crud.get_user_by_device_id(db, device_id=x_device_id)
    if not user:
        raise HTTPException(status_code=404, detail="User not found. Please register.")
    if user.isBanned:
        raise HTTPException(status_code=403, detail="User is banned.")
    return user

def get_admin_user(current_user: Annotated[models.User, Depends(get_current_user)]):
    if not current_user.isAdmin:
        raise HTTPException(status_code=403, detail="Admin privileges required.")
    return current_user

# --- User Endpoints --- #
@app.post("/register/", response_model=schemas.User)
def register_user(user: schemas.UserCreate, db: Session = Depends(get_db)):
    db_user = crud.get_user_by_device_id(db, device_id=user.deviceId)
    if db_user:
        if db_user.username != user.username:
            update_data = schemas.UserUpdate(username=user.username)
            return crud.update_user(db, user_id=db_user.id, user_update=update_data)
        return db_user
    return crud.create_user(db=db, user=user)

@app.get("/users/", response_model=List[schemas.User])
def read_users(admin_user: Annotated[models.User, Depends(get_admin_user)], skip: int = 0, limit: int = 100, db: Session = Depends(get_db)):
    return crud.get_users(db, skip=skip, limit=limit)

@app.put("/users/me", response_model=schemas.User)
def update_current_user(user_update: schemas.UserUpdate, current_user: Annotated[models.User, Depends(get_current_user)], db: Session = Depends(get_db)):
    return crud.update_user(db, user_id=current_user.id, user_update=user_update)

@app.put("/users/{user_id}", response_model=schemas.User)
def update_user_permissions(user_id: int, user_update: schemas.UserUpdate, admin_user: Annotated[models.User, Depends(get_admin_user)], db: Session = Depends(get_db)):
    return crud.update_user(db, user_id=user_id, user_update=user_update)

@app.put("/users/me/fcm-token", response_model=schemas.User)
def update_fcm_token(token_update: schemas.FcmTokenUpdate, current_user: Annotated[models.User, Depends(get_current_user)], db: Session = Depends(get_db)):
    return crud.update_fcm_token(db, user_id=current_user.id, token=token_update.fcmToken)

@app.put("/users/me/notification-settings", response_model=schemas.User)
def update_notification_settings(settings_update: schemas.NotificationSettingsUpdate, current_user: Annotated[models.User, Depends(get_current_user)], db: Session = Depends(get_db)):
    return crud.update_notification_settings(db, user_id=current_user.id, settings=settings_update)

# --- Room Endpoints --- #
@app.post("/rooms/", response_model=schemas.Room)
def create_room(room: schemas.RoomCreate, current_user: Annotated[models.User, Depends(get_current_user)], db: Session = Depends(get_db)):
    db_room_check = crud.get_room_by_code(db, room_code=room.roomCode)
    if db_room_check:
        raise HTTPException(status_code=400, detail="このルームコードは既に使用されています")
    if room.isSpecial and not current_user.isAdmin:
        raise HTTPException(status_code=403, detail="Only admins can create special rooms.")
    
    new_room = crud.create_room(db=db, room=room, owner_id=current_user.id)

    # --- Send Notifications --- #
    try:
        users_to_notify = crud.get_users_for_notification(db, new_room)
        tokens = [user.fcmToken for user in users_to_notify if user.fcmToken and user.id != current_user.id]
        if tokens:
            notification_body = f"{new_room.regulation} @{new_room.currentPlayers}/{new_room.maxPlayers}"
            message = messaging.MulticastMessage(
                notification=messaging.Notification(
                    title=f"{new_room.modName} の募集が開始されました",
                    body=notification_body
                ),
                data={"roomId": str(new_room.id)},
                tokens=tokens,
            )
            messaging.send_multicast(message)
            print(f"Sent notification to {len(tokens)} users.")
    except Exception as e:
        print(f"Error sending notification: {e}")

    return new_room

    try:
        users_to_notify = crud.get_users_for_notification(db, new_room)
        tokens = [user.fcmToken for user in users_to_notify if user.fcmToken and user.id != current_user.id]
        if tokens:
            notification_body = f"{new_room.regulation} @{new_room.currentPlayers}/{new_room.maxPlayers}"
            message = messaging.MulticastMessage(
                notification=messaging.Notification(
                    title=f"{new_room.modName} の募集が開始されました",
                    body=notification_body
                ),
                data={"roomId": str(new_room.id)},
                tokens=tokens,
            )
            messaging.send_multicast(message)
            print(f"Sent notification to {len(tokens)} users.")
    except Exception as e:
        print(f"Error sending notification: {e}")

    return new_room

@app.get("/rooms/", response_model=List[schemas.Room])
def read_rooms(skip: int = 0, limit: int = 100, db: Session = Depends(get_db)):
    return crud.get_rooms(db, skip=skip, limit=limit)

@app.get("/rooms/{room_id}", response_model=schemas.Room)
def read_room(room_id: int, db: Session = Depends(get_db)):
    db_room = crud.get_room(db, room_id=room_id)
    if db_room is None:
        raise HTTPException(status_code=404, detail="Room not found")
    return db_room

@app.put("/rooms/{room_id}", response_model=schemas.Room)
def update_room(room_id: int, room: schemas.RoomUpdate, current_user: Annotated[models.User, Depends(get_current_user)], db: Session = Depends(get_db)):
    db_room = crud.get_room(db, room_id=room_id)
    if db_room is None:
        raise HTTPException(status_code=404, detail="Room not found")
    if db_room.ownerId != current_user.id and not current_user.isAdmin:
        raise HTTPException(status_code=403, detail="Not authorized to update this room")
    if room.isSpecial and not current_user.isAdmin:
        raise HTTPException(status_code=403, detail="Only admins can make a room special")
    return crud.update_room(db, room_id=room_id, room=room)

@app.put("/rooms/{room_id}/status", response_model=schemas.Room)
def update_room_status(room_id: int, status_update: schemas.RoomStatusUpdate, current_user: Annotated[models.User, Depends(get_current_user)], db: Session = Depends(get_db)):
    db_room = crud.get_room(db, room_id=room_id)
    if db_room is None:
        raise HTTPException(status_code=404, detail="Room not found")
    if db_room.ownerId != current_user.id and not current_user.isAdmin:
        raise HTTPException(status_code=403, detail="Not authorized to update this room status")
    return crud.update_room_status(db, room_id=room_id, status=status_update.status)

@app.delete("/rooms/{room_id}")
def delete_room(room_id: int, current_user: Annotated[models.User, Depends(get_current_user)], db: Session = Depends(get_db)):
    db_room = crud.get_room(db, room_id=room_id)
    if db_room is None:
        raise HTTPException(status_code=404, detail="Room not found")
    if db_room.ownerId != current_user.id and not current_user.isAdmin:
        raise HTTPException(status_code=403, detail="Not authorized to delete this room")
    crud.delete_room(db, room_id=room_id)
    return {"detail": "Room deleted successfully"}

# --- Announcement Endpoints --- #
@app.get("/announcements/", response_model=List[schemas.Announcement])
def read_announcements(skip: int = 0, limit: int = 100, db: Session = Depends(get_db)):
    return crud.get_announcements(db, skip=skip, limit=limit)

@app.post("/announcements/", response_model=schemas.Announcement)
def create_announcement(announcement: schemas.AnnouncementCreate, admin_user: Annotated[models.User, Depends(get_admin_user)], db: Session = Depends(get_db)):
    return crud.create_announcement(db=db, announcement=announcement)
