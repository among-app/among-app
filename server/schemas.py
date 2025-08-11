from pydantic import BaseModel, ConfigDict
from datetime import datetime
from typing import Optional, List

orm_config = ConfigDict(from_attributes=True)

# --- All schemas now use camelCase exclusively ---

class User(BaseModel):
    model_config = orm_config
    id: int
    deviceId: str
    username: str
    isAdmin: bool
    isBanned: bool
    isVerified: bool
    notificationsEnabled: bool

class Room(BaseModel):
    model_config = orm_config
    id: int
    roomCode: str
    currentPlayers: int
    maxPlayers: int
    regulation: str
    modName: str
    serverName: str
    description: Optional[str] = None
    createdAt: datetime
    isSpecial: bool
    status: str
    owner: User

class Announcement(BaseModel):
    model_config = orm_config
    id: int
    title: str
    content: str
    createdAt: datetime

class NotificationFilter(BaseModel):
    model_config = orm_config
    id: int
    filterType: str
    filterValue: str

class NotificationSettings(BaseModel):
    notificationsEnabled: bool
    filters: List[NotificationFilter]

class UserCreate(BaseModel):
    deviceId: str
    username: str

class UserUpdate(BaseModel):
    username: Optional[str] = None
    isAdmin: Optional[bool] = None
    isBanned: Optional[bool] = None
    isVerified: Optional[bool] = None

class FcmTokenUpdate(BaseModel):
    fcmToken: str

class NotificationFilterCreate(BaseModel):
    filterType: str
    filterValue: str

class NotificationSettingsUpdate(BaseModel):
    notificationsEnabled: Optional[bool] = None
    filters: Optional[List[NotificationFilterCreate]] = None

class RoomCreate(BaseModel):
    roomCode: str
    currentPlayers: int
    maxPlayers: int
    regulation: List[str]
    modName: str
    serverName: str
    description: Optional[str] = None
    isSpecial: Optional[bool] = False

class RoomUpdate(BaseModel):
    currentPlayers: Optional[int] = None
    maxPlayers: Optional[int] = None
    regulation: Optional[str] = None
    modName: Optional[str] = None
    serverName: Optional[str] = None
    description: Optional[str] = None
    isSpecial: Optional[bool] = None

class RoomStatusUpdate(BaseModel):
    status: str

class AnnouncementCreate(BaseModel):
    title: str
    content: str
