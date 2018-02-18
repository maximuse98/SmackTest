package dao;

import connections.Connect;
import connections.ConnectDb;
import models.Msg;
import models.UserNote;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.List;

public class UserDao {
    private String driverName;
    private String connectionString;
    private String login;
    private String password;

    public UserDao(ConnectDb connection) throws FileNotFoundException {
        this.connectionString = connection.getConnectionString();
        this.driverName = connection.getDriverName();
        this.login = connection.getLogin();
        this.password = connection.getPassword();
    }

    public ResultSet get(Connect selectedUser){
        try {
            Class.forName(driverName);
            Connection con = DriverManager.getConnection(connectionString, login, password);
            String sql = "SELECT *" +
                    "FROM USER " +
                    "WHERE login = "+"'"+selectedUser.getUsername()+"'";
            Statement statement = con.createStatement();
            return statement.executeQuery(sql);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet getNote(Connect selectedUser, String action){
        try {
            Class.forName(driverName);
            Connection con = DriverManager.getConnection(connectionString, login, password);
            String sql = "SELECT *" +
                    "FROM USER " +
                    "WHERE login = '"+selectedUser.getUsername()+"' && ACTION LIKE '%"+action+"%'";
            Statement statement = con.createStatement();
            return statement.executeQuery(sql);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void add(UserNote note) throws IOException {
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            System.out.println("Can't get class. No driver found");
            e.printStackTrace();
            return;
        }

        Connection connection;
        try {
            connection = DriverManager.getConnection(connectionString, login, password);
        } catch (SQLException e) {
            System.out.println("Can't get connection. Incorrect URL");
            e.printStackTrace();
            return;
        }

        try {
            String sql = "INSERT INTO `user`"
                    + "(`login`, `action`, `time`)"
                    + "VALUES ('" + note.getLogin() + "','" + note.getAction() + "','" + note.getTime() + "')";
            connection.prepareStatement(sql);
            connection.prepareStatement(sql).execute();
        } catch (SQLException e) {
            System.out.println("Can't add user");
            e.printStackTrace();
            return;
        }

        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("Can't close connection");
            e.printStackTrace();
            return;
        }
    }

    public void remove(UserNote note) throws IOException {

        try {
            Class.forName(driverName);
            Connection con = DriverManager.getConnection(connectionString, login, password);
            String sql = "DELETE FROM `user`"
                    +"WHERE LOGIN='"+note.getLogin()+"' && ACTION='"+note.getAction()+"' && TIME = '"+note.getTime()+"';";
            con.prepareStatement(sql);
            con.prepareStatement(sql).execute();
            con.close();
        } catch (Exception e) {
            System.out.println("Can't add user");
            e.printStackTrace();
            return;
        }
    }
}
