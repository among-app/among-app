from sqlalchemy import Boolean, Column, Integer, String, DateTime, ForeignKey
from sqlalchemy.orm import relationship
from sqlalchemy.sql import func

from .database import Base

class User(Base):
    __tablename__ = "users"

    id = Column(Integer, primary_key=True, index=True)
    deviceId = Column("device_id", String, unique=True, index=True)
    username = Column(String, index=True)
    isAdmin = Column("is_admin", Boolean, default=False)
    isBanned = Column("is_banned", Boolean, default=False)
    isVerified = Column("is_verified", Boolean, default=False)

    rooms = relationship("Room", back_populates="owner")

class Room(Base):
    __tablename__ = "rooms"

    id = Column(Integer, primary_key=True, index=True)
    roomCode = Column("room_code", String, unique=True, index=True)
    currentPlayers = Column("current_players", Integer)
    maxPlayers = Column("max_players", Integer)
    regulation = Column(String)
    modName = Column("mod_name", String)
    serverName = Column("server_name", String)
    description = Column(String, nullable=True)
    createdAt = Column("created_at", DateTime(timezone=True), server_default=func.now())
    isSpecial = Column("is_special", Boolean, default=False)

    ownerId = Column("owner_id", Integer, ForeignKey("users.id"))
    owner = relationship("User", back_populates="rooms")

class Announcement(Base):
    __tablename__ = "announcements"

    id = Column(Integer, primary_key=True, index=True)
    title = Column(String)
    content = Column(String)
    createdAt = Column("created_at", DateTime(timezone=True), server_default=func.now())