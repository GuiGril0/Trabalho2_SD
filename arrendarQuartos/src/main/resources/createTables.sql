/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  gui
 * Created: Dec 21, 2022
 */

CREATE TABLE advertisement (
  advertiser VARCHAR(50), 
  typead VARCHAR(15), 
  statead VARCHAR(15),
  price INTEGER,
  gender VARCHAR(15),
  localad VARCHAR(100),
  typology VARCHAR(10),
  date DATE,
  description VARCHAR(1000),
  aid SERIAL PRIMARY KEY
);

CREATE TABLE messages (
  sender VARCHAR(50),
  content VARCHAR(1000),
  date DATE,
  aid INTEGER,
  FOREIGN KEY (aid) REFERENCES advertisement(aid)
);