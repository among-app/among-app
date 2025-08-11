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
    notificationsEnabled = Column("notifications_enabled", Boolean, default=True)
    fcmToken = Column("fcm_token", String, nullable=True, unique=True)
    rooms = relationship("Room", back_populates="owner")
    notificationFilters = relationship("NotificationFilter", back_populates="user", cascade="all, delete-orphan")

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
    status = Column(String, default="recruiting")
    ownerId = Column("owner_id", Integer, ForeignKey("users.id"))
    owner = relationship("User", back_populates="rooms")

class Announcement(Base):
    __tablename__ = "announcements"
    id = Column(Integer, primary_key=True, index=True)
    title = Column(String)
    content = Column(String)
    createdAt = Column("created_at", DateTime(timezone=True), server_default=func.now())

class NotificationFilter(Base):
    __tablename__ = "notification_filters"
    id = Column(Integer, primary_key=True, index=True)
    filterType = Column("filter_type", String)
    filterValue = Column("filter_value", String)
    userId = Column("user_id", Integer, ForeignKey("users.id"))
    user = relationship("User", back_populates="notificationFilters")