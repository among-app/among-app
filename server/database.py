
import os
from sqlalchemy import create_engine
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker

# Check for the Koyeb DATABASE_URL environment variable first
DATABASE_URL = os.getenv("DATABASE_URL")

# If DATABASE_URL is not set, fall back to local SQLite DB
if DATABASE_URL is None:
    print("DATABASE_URL not found, falling back to SQLite.")
    basedir = os.path.abspath(os.path.dirname(__file__))
    db_path = os.path.join(basedir, "among_us.db")
    DATABASE_URL = f"sqlite:///{db_path}"
    engine = create_engine(
        DATABASE_URL, connect_args={"check_same_thread": False}
    )
else:
    print("DATABASE_URL found, connecting to PostgreSQL.")
    # SQLAlchemy's handling of Heroku-style DATABASE_URLs
    if DATABASE_URL.startswith("postgres://"):
        DATABASE_URL = DATABASE_URL.replace("postgres://", "postgresql://", 1)
    engine = create_engine(DATABASE_URL)

SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

Base = declarative_base()
