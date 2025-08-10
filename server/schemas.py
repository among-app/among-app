
from pydantic import BaseModel
from datetime import datetime
from typing import Optional, List

# User Schemas
class UserBase(BaseModel):
    username: str

class UserCreate(UserBase):
    device_id: str

class User(UserBase):
    id: int
    device_id: str
    is_admin: bool
    is_banned: bool
    is_verified: bool

    class Config:
        orm_mode = True

class UserUpdate(BaseModel):
    is_admin: Optional[bool] = None
    is_banned: Optional[bool] = None
    is_verified: Optional[bool] = None

# Room Schemas
class RoomBase(BaseModel):
    room_code: str
    current_players: int
    max_players: int
    regulation: str
    mod_name: str
    server_name: str
    description: Optional[str] = None

class RoomCreate(RoomBase):
    is_special: Optional[bool] = False

class RoomUpdate(BaseModel):
    current_players: Optional[int] = None
    max_players: Optional[int] = None
    regulation: Optional[str] = None
    mod_name: Optional[str] = None
    server_name: Optional[str] = None
    description: Optional[str] = None
    is_special: Optional[bool] = None

class Room(RoomBase):
    id: int
    created_at: datetime
    is_special: bool
    owner: User

    class Config:
        orm_mode = True

# Announcement Schemas
class AnnouncementBase(BaseModel):
    title: str
    content: str

class AnnouncementCreate(AnnouncementBase):
    pass

class Announcement(AnnouncementBase):
    id: int
    created_at: datetime

    class Config:
        orm_mode = True
