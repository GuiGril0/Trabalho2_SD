CREATE TABLE messages (
  sender VARCHAR(50),
  content VARCHAR(1000),
  date DATE,
  aid INTEGER,
  FOREIGN KEY (aid) REFERENCES advertisement(aid)
);