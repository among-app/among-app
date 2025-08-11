from sqlalchemy.orm import Session
from . import models, schemas

def get_user_by_device_id(db: Session, device_id: str):
    return db.query(models.User).filter(models.User.deviceId == device_id).first()

def create_user(db: Session, user: schemas.UserCreate):
    is_admin = False
    if user.deviceId == "ADMIN_DEVICE_ID":
        is_admin = True
    db_user = models.User(deviceId=user.deviceId, username=user.username, isAdmin=is_admin)
    db.add(db_user)
    db.commit()
    db.refresh(db_user)
    return db_user

def get_users(db: Session, skip: int = 0, limit: int = 100):
    return db.query(models.User).offset(skip).limit(limit).all()

def update_user(db: Session, user_id: int, user_update: schemas.UserUpdate):
    db_user = db.query(models.User).filter(models.User.id == user_id).first()
    if db_user:
        update_data = user_update.dict(exclude_unset=True)
        for key, value in update_data.items():
            setattr(db_user, key, value)
        db.commit()
        db.refresh(db_user)
    return db_user

def get_room(db: Session, room_id: int):
    return db.query(models.Room).filter(models.Room.id == room_id).first()

def get_room_by_code(db: Session, room_code: str):
    return db.query(models.Room).filter(models.Room.roomCode == room_code).first()

def get_rooms(db: Session, skip: int = 0, limit: int = 100):
    return db.query(models.Room).order_by(models.Room.createdAt.desc()).offset(skip).limit(limit).all()

def create_room(db: Session, room: schemas.RoomCreate, owner_id: int):
    regulation_str = ",".join(room.regulation)
    db_room = models.Room(
        ownerId=owner_id,
        regulation=regulation_str,
        roomCode=room.roomCode,
        currentPlayers=room.currentPlayers,
        maxPlayers=room.maxPlayers,
        modName=room.modName,
        serverName=room.serverName,
        description=room.description,
        isSpecial=room.isSpecial
    )
    db.add(db_room)
    db.commit()
    db.refresh(db_room)
    return db_room

def update_room(db: Session, room_id: int, room: schemas.RoomUpdate):
    db_room = get_room(db, room_id)
    if db_room:
        update_data = room.dict(exclude_unset=True)
        for key, value in update_data.items():
            setattr(db_room, key, value)
        db.commit()
        db.refresh(db_room)
    return db_room

def delete_room(db: Session, room_id: int):
    db_room = get_room(db, room_id)
    if db_room:
        db.delete(db_room)
        db.commit()
    return db_room

def get_announcements(db: Session, skip: int = 0, limit: int = 100):
    return db.query(models.Announcement).order_by(models.Announcement.createdAt.desc()).offset(skip).limit(limit).all()

def create_announcement(db: Session, announcement: schemas.AnnouncementCreate):
    db_announcement = models.Announcement(**announcement.dict())
    db.add(db_announcement)
    db.commit()
    db.refresh(db_announcement)
    return db_announcement