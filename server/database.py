from sqlalchemy import create_engine
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker

# !!!セキュリティ上の警告!!! データベースの接続情報をコードに直接書き込むことは非常に危険です。
# このコードが公開されると、第三者がデータベースにアクセスできてしまいます。
SQLALCHEMY_DATABASE_URL = "postgres://koyeb-adm:npg_MQgIqjY59PLH@ep-delicate-queen-a4u1iney.us-east-1.pg.koyeb.app/and-among"

# SQLAlchemyがPostgreSQLを正しく認識できるようにURLを調整
if SQLALCHEMY_DATABASE_URL.startswith("postgres://"):
    SQLALCHEMY_DATABASE_URL = SQLALCHEMY_DATABASE_URL.replace("postgres://", "postgresql://", 1)

engine = create_engine(SQLALCHEMY_DATABASE_URL)

SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

Base = declarative_base()