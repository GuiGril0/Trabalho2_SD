/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sd.rest;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author gui
 */
public class ConnectionDB {
    private String PG_HOST;
    private String PG_DB;
    private String USER;
    private String PWD;
    Statement stmt = null; //variável responsável sobretudo por realizar alterações nas tabelas da base de dados SQL
    Connection con = null; //variável responsável por estabelecer a comunicação com a base de dados pretendida

    public ConnectionDB(String PG_HOST, String PG_DB, String USER, String PWD) {
        this.PG_HOST = PG_HOST;
        this.PG_DB = PG_DB;
        this.USER = USER;
        this.PWD = PWD;
    }

    /*
    função responsável por criar a conexão com a base de dados
     */
    public void connectDb() {
        try{
            Class.forName("org.postgresql.Driver");

            con = DriverManager.getConnection("jdbc:postgresql://" + PG_HOST + ":5432/" + PG_DB,
                    USER,
                    PWD);
            stmt = con.createStatement();
        }
        catch (Exception e){
            e.printStackTrace();
            System.err.println("Problems setting the connection");
        }
    }

    /*
    função que encerra a conexão com a base de dados
     */
    public void closeConnection() {
        try {
            stmt.close();
            con.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Statement getStatement() {
        return stmt;
    }
}
