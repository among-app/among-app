from pydantic import BaseModel, ConfigDict
from datetime import datetime
from typing import Optional, List

# Pydantic config to use camelCase for JSON responses
class CamelCaseModel(BaseModel):
    model_config = ConfigDict(alias_generator=lambda string: ''.join(word.capitalize() if i != 0 else word for i, word in enumerate(string.split('_'))), from_attributes=True)

# User Schemas
class UserBase(CamelCaseModel):
    username: str

class UserCreate(UserBase):
    device_id: str

class User(UserBase):
    id: int
    device_id: str
    is_admin: bool
    is_banned: bool
    is_verified: bool

class UserUpdate(BaseModel): # No need for camelCase on update
    is_admin: Optional[bool] = None
    is_banned: Optional[bool] = None
    is_verified: Optional[bool] = None

# Room Schemas
class RoomBase(CamelCaseModel):
    room_code: str
    current_players: int
    max_players: int
    regulation: str
    mod_name: str
    server_name: str
    description: Optional[str] = None

class RoomCreate(RoomBase):
    regulation: List[str]
    is_special: Optional[bool] = False

class RoomUpdate(BaseModel): # No need for camelCase on update
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

# Announcement Schemas
class AnnouncementBase(CamelCaseModel):
    title: str
    content: str

class AnnouncementCreate(AnnouncementBase):
    pass

class Announcement(AnnouncementBase):
    id: int
    created_at: datetime