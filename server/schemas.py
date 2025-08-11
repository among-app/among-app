from pydantic import BaseModel, ConfigDict
from datetime import datetime
from typing import Optional, List

orm_config = ConfigDict(from_attributes=True)

# --- User Schemas ---
class User(BaseModel):
    model_config = orm_config
    id: int
    deviceId: str
    username: str
    isAdmin: bool
    isBanned: bool
    isVerified: bool

class UserCreate(BaseModel):
    deviceId: str
    username: str

class UserUpdate(BaseModel):
    isAdmin: Optional[bool] = None
    isBanned: Optional[bool] = None
    isVerified: Optional[bool] = None

# --- Room Schemas ---
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
    owner: User

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

# --- Announcement Schemas ---
class Announcement(BaseModel):
    model_config = orm_config
    id: int
    title: str
    content: str
    createdAt: datetime

class AnnouncementCreate(BaseModel):
    title: str
    content: str
