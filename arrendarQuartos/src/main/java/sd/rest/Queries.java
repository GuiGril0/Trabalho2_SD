/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sd.rest;

import java.io.*;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

/**
 *
 * @author gui
 */
public class Queries {
    private static ConnectionDB setConnectionToDB() throws Exception{
        String[] props = new String[4];
        InputStream is = new FileInputStream("src/main/resources/configs.properties");
        Properties p = new Properties();
        p.load(is);
        props[0] = p.getProperty("host");
        props[1] = p.getProperty("db");
        props[2] = p.getProperty("user");
        props[3] = p.getProperty("password");

        return new ConnectionDB(props[0], props[1], props[2], props[3]);
    }

    /*
    função que trata de inserir valores na tabela advertisement da base de dados
     */
    public int insertIntoTableAdvertisement(Ad ad) {
        int aid = -1;
        try {
            ConnectionDB db = setConnectionToDB();
            db.connectDb();
            Statement stmt = db.getStatement();

            stmt.executeUpdate("insert into advertisement values ('"
                    +ad.getAdvertiser()+ "', " +
                    "'" +ad.getType()+ "', " +
                    "'" +ad.getState()+ "', "+ ad.getPrice()+
                    ", '" +ad.getGender()+ "', " +
                    "'" +ad.getLocal()+ "', " +
                    "'" +ad.getTypology()+ "', " +
                    "'" +ad.getDate()+ "', " +
                    "'" +ad.getDescription()+ "' );");

            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS number FROM advertisement");

            rs.next();
            aid = rs.getInt("number");

            db.closeConnection();
        } catch(Exception e) {
            e.printStackTrace();
            System.err.println("Problems on insert...");
        }
        return aid;
    }

    /*
    função que serve para consultar os valores na tabela advertisement
     */
    public List<Ad> consultTableAdvertisement(String fields) {
        List<Ad> results = new ArrayList<Ad>();
        Ad ad = null;
        try {
            ConnectionDB db = setConnectionToDB();
            db.connectDb();
            Statement stmt = db.getStatement();

            String[] aux = fields.split("&");
            String response = "";
            for(String i : aux) {
                String[] values = i.split("=");
                if(values[0].equals("aid") || values[0].equals("statead")) {
                    if(values[0].equals("aid"))
                        response += values[0] + "=" + values[1];
                    else
                        response += values[0] + "='" + values[1] + "'";
                }
                else {
                    response += values[0] + " like '%";
                    for (int j = 1; j < values.length; j++)
                        response += values[j];
                    response += "%'";
                }
                response += " AND ";
            }
            response = response.substring(0, response.length() - 4);
            response = response.trim();

            ResultSet rs = stmt.executeQuery("SELECT * FROM advertisement WHERE " + response + " ORDER BY aid;");

            while (rs.next()) {
                String advertiser = rs.getString("advertiser");
                String typeAd = rs.getString("typead");
                String stateAd = rs.getString("statead");
                int price = rs.getInt("price");
                String gender = rs.getString("gender");
                String localAd = rs.getString("localad");
                String typology = rs.getString("typology");
                Date date = rs.getDate("date");
                int aid = rs.getInt("aid");
                String description = rs.getString("description");

                ad = new Ad();
                ad.setAdvertiser(advertiser);
                ad.setType(typeAd);
                ad.setState(stateAd);
                ad.setPrice(price);
                ad.setGender(gender);
                ad.setLocal(localAd);
                ad.setTypology(typology);
                ad.setDate(date);
                ad.setAid(aid);
                ad.setDescription(description);

                results.add(ad);
            }
            db.closeConnection();
            return results;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

     /*
    função que trata de inserir valores na tabela messages da base de dados
     */

    public boolean insertIntoTableMessages(Message msg) {
        try {
            ConnectionDB db = setConnectionToDB();
            db.connectDb();
            Statement stmt = db.getStatement();

            stmt.executeUpdate("insert into messages values ('"
                    +msg.getSender()+ "', " +
                    "'" +msg.getContent()+ "', " +
                    "'" +msg.getDate()+ "', " +
                    msg.getAid()+ ");");

            return true;
        } catch(Exception e) {
            e.printStackTrace();
            System.err.println("Problems on insert...");
            return false;
        }
    }

    /*
    função que serve para consultar os valores na tabela messages
     */
    public List<Message> consultTableMessages(String fields) {
        List<Message> results = new ArrayList<Message>();
        try {
            ConnectionDB db = setConnectionToDB();
            db.connectDb();
            Statement stmt = db.getStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM messages WHERE aid=" + fields + ";");

            while(rs.next()) {
                String sender = rs.getString("sender");
                String content = rs.getString("content");
                Date date = rs.getDate("date");
                int aid = rs.getInt("aid");

                Message msg = new Message();
                msg.setSender(sender);
                msg.setContent(content);
                msg.setDate(date);
                msg.setAid(aid);

                results.add(msg);
            }

            return results;
        } catch(Exception e) {
            e.printStackTrace();
            System.err.println("Problems retrieving data from db...");
            return null;
        }
    }

    /*
    função que serve para alterar valores, nomeadamente o estado, na tabela advertisement
     */
    public boolean alterTableAdvertisement(String state, String aid) {
        try {
            ConnectionDB db = setConnectionToDB();
            db.connectDb();
            Statement stmt = db.getStatement();

            stmt.executeUpdate("UPDATE advertisement SET statead='" + state + "' WHERE aid=" + aid + ";");

            return true;
        } catch(Exception e) {
            e.printStackTrace();
            System.err.println("Problems updating the table...");
            return false;
        }
    }
}
