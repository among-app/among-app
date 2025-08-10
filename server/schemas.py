from pydantic import BaseModel, ConfigDict
from datetime import datetime
from typing import Optional, List

# Base Pydantic model for request bodies (uses default snake_case)
class RequestBaseModel(BaseModel):
    pass

# Base Pydantic model for responses (generates camelCase aliases)
class ResponseBaseModel(BaseModel):
    model_config = ConfigDict(
        alias_generator=lambda string: ''.join(word.capitalize() if i != 0 else word for i, word in enumerate(string.split('_'))),
        from_attributes=True,
        populate_by_name=True # Allows both alias and field name population
    )

# --- User Schemas ---
class UserBase(ResponseBaseModel):
    username: str

class UserCreate(RequestBaseModel): # For request body
    device_id: str
    username: str

class User(UserBase): # For response
    id: int
    device_id: str
    is_admin: bool
    is_banned: bool
    is_verified: bool

class UserUpdate(RequestBaseModel): # For request body
    is_admin: Optional[bool] = None
    is_banned: Optional[bool] = None
    is_verified: Optional[bool] = None

# --- Room Schemas ---
class RoomBase(ResponseBaseModel):
    room_code: str
    current_players: int
    max_players: int
    regulation: str
    mod_name: str
    server_name: str
    description: Optional[str] = None

class RoomCreate(RequestBaseModel): # For request body
    room_code: str
    current_players: int
    max_players: int
    regulation: List[str]
    mod_name: str
    server_name: str
    description: Optional[str] = None
    is_special: Optional[bool] = False

class RoomUpdate(RequestBaseModel): # For request body
    current_players: Optional[int] = None
    max_players: Optional[int] = None
    regulation: Optional[str] = None
    mod_name: Optional[str] = None
    server_name: Optional[str] = None
    description: Optional[str] = None
    is_special: Optional[bool] = None

class Room(RoomBase): # For response
    id: int
    created_at: datetime
    is_special: bool
    owner: User

# --- Announcement Schemas ---
class AnnouncementBase(ResponseBaseModel):
    title: str
    content: str

class AnnouncementCreate(RequestBaseModel): # For request body
    title: str
    content: str

class Announcement(AnnouncementBase): # For response
    id: int
    created_at: datetime
