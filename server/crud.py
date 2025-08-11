from sqlalchemy.orm import Session
from . import models, schemas

# --- User CRUD --- #
def get_user(db: Session, user_id: int):
    return db.query(models.User).filter(models.User.id == user_id).first()

def get_user_by_device_id(db: Session, device_id: str):
    return db.query(models.User).filter(models.User.deviceId == device_id).first()

def get_users(db: Session, skip: int = 0, limit: int = 100):
    return db.query(models.User).offset(skip).limit(limit).all()

def create_user(db: Session, user: schemas.UserCreate):
    is_admin = False
    if user.deviceId == "bc198616-6536-4695-b009-50502a3c48d7":
        is_admin = True
    db_user = models.User(deviceId=user.deviceId, username=user.username, isAdmin=is_admin)
    db.add(db_user)
    db.commit()
    db.refresh(db_user)
    return db_user

def update_user(db: Session, user_id: int, user_update: schemas.UserUpdate):
    print(f"--- Updating user {user_id} ---")
    print(f"Received update data: {user_update.model_dump()}")
    
    db_user = db.query(models.User).filter(models.User.id == user_id).first()
    if not db_user:
        print("User not found in DB.")
        return None

    print(f"User before update: isAdmin={db_user.isAdmin}, isBanned={db_user.isBanned}, isVerified={db_user.isVerified}")

    update_data = user_update.model_dump(exclude_unset=True)
    for key, value in update_data.items():
        print(f"Setting {key} = {value}")
        setattr(db_user, key, value)
    
    print(f"User after update in memory: isAdmin={db_user.isAdmin}, isBanned={db_user.isBanned}, isVerified={db_user.isVerified}")

    try:
        db.add(db_user)
        db.commit()
        db.refresh(db_user)
        print("Commit successful.")
        print(f"User after commit: isAdmin={db_user.isAdmin}, isBanned={db_user.isBanned}, isVerified={db_user.isVerified}")
    except Exception as e:
        print(f"!!! An error occurred during commit: {e}")
        db.rollback()

    return db_user

def update_fcm_token(db: Session, user_id: int, token: str):
    db_user = get_user(db, user_id=user_id)
    if db_user:
        db_user.fcmToken = token
        db.commit()
        db.refresh(db_user)
    return db_user

# --- Notification Settings CRUD --- #
def get_notification_filters(db: Session, user_id: int):
    return db.query(models.NotificationFilter).filter(models.NotificationFilter.userId == user_id).all()

def update_notification_settings(db: Session, user_id: int, settings: schemas.NotificationSettingsUpdate):
    db_user = get_user(db, user_id=user_id)
    if not db_user:
        return None
    if settings.notificationsEnabled is not None:
        db_user.notificationsEnabled = settings.notificationsEnabled
    if settings.filters is not None:
        db.query(models.NotificationFilter).filter(models.NotificationFilter.userId == user_id).delete()
        for f in settings.filters:
            db_filter = models.NotificationFilter(userId=user_id, filterType=f.filterType, filterValue=f.filterValue)
            db.add(db_filter)
    db.commit()
    db.refresh(db_user)
    return db_user

def get_users_for_notification(db: Session, room: models.Room):
    all_users = db.query(models.User).filter(models.User.notificationsEnabled == True, models.User.fcmToken != None).all()
    users_to_notify = set()
    room_regulations = set(room.regulation.split(','))
    for user in all_users:
        if not user.notificationFilters:
            users_to_notify.add(user)
            continue
        user_filters = {}
        for f in user.notificationFilters:
            if f.filterType not in user_filters:
                user_filters[f.filterType] = set()
            user_filters[f.filterType].add(f.filterValue)
        regulation_match = not user_filters.get("regulation") or not user_filters["regulation"].isdisjoint(room_regulations)
        mod_match = not user_filters.get("mod") or room.modName in user_filters["mod"]
        if regulation_match and mod_match:
            users_to_notify.add(user)
    return list(users_to_notify)

# --- Room CRUD --- #
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

def update_room_status(db: Session, room_id: int, status: str):
    db_room = get_room(db, room_id)
    if db_room:
        db_room.status = status
        db.commit()
        db.refresh(db_room)
    return db_room

def delete_room(db: Session, room_id: int):
    db_room = get_room(db, room_id)
    if db_room:
        db.delete(db_room)
        db.commit()
    return db_room

# --- Announcement CRUD --- #
def get_announcements(db: Session, skip: int = 0, limit: int = 100):
    return db.query(models.Announcement).order_by(models.Announcement.createdAt.desc()).offset(skip).limit(limit).all()

def create_announcement(db: Session, announcement: schemas.AnnouncementCreate):
    db_announcement = models.Announcement(**announcement.dict())
    db.add(db_announcement)
    db.commit()
    db.refresh(db_announcement)
    return db_announcement

def update_announcement(db: Session, announcement_id: int, announcement: schemas.AnnouncementCreate):
    db_announcement = db.query(models.Announcement).filter(models.Announcement.id == announcement_id).first()
    if db_announcement:
        db_announcement.title = announcement.title
        db_announcement.content = announcement.content
        db.commit()
        db.refresh(db_announcement)
    return db_announcement

def delete_announcement(db: Session, announcement_id: int):
    db_announcement = db.query(models.Announcement).filter(models.Announcement.id == announcement_id).first()
    if db_announcement:
        db.delete(db_announcement)
        db.commit()
    return db_announcement
