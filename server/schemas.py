from pydantic import BaseModel, ConfigDict
from datetime import datetime
from typing import Optional, List

orm_config = ConfigDict(from_attributes=True)

# --- Schemas defined in snake_case, matching Python conventions ---

class User(BaseModel):
    model_config = orm_config
    id: int
    device_id: str
    username: str
    is_admin: bool
    is_banned: bool
    is_verified: bool

class Room(BaseModel):
    model_config = orm_config
    id: int
    room_code: str
    current_players: int
    max_players: int
    regulation: str
    mod_name: str
    server_name: str
    description: Optional[str] = None
    created_at: datetime
    is_special: bool
    owner: User

class Announcement(BaseModel):
    model_config = orm_config
    id: int
    title: str
    content: str
    created_at: datetime

# --- Schemas for Request/Response Bodies ---
class UserCreate(BaseModel):
    device_id: str
    username: str

class UserUpdate(BaseModel):
    is_admin: Optional[bool] = None
    is_banned: Optional[bool] = None
    is_verified: Optional[bool] = None

class RoomCreate(BaseModel):
    room_code: str
    current_players: int
    max_players: int
    regulation: List[str]
    mod_name: str
    server_name: str
    description: Optional[str] = None
    is_special: Optional[bool] = False

class RoomUpdate(BaseModel):
    current_players: Optional[int] = None
    max_players: Optional[int] = None
    regulation: Optional[str] = None
    mod_name: Optional[str] = None
    server_name: Optional[str] = None
    description: Optional[str] = None
    is_special: Optional[bool] = None

class AnnouncementCreate(BaseModel):
    title: str
    content: str