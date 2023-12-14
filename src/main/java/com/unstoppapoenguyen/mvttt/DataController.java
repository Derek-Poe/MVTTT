package com.unstoppapoenguyen.mvttt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;

public class DataController {
    public static Connection dbConn;
    public static void connectDatabase() throws SQLException,ClassNotFoundException,InstantiationException,IllegalAccessException {
        Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        dbConn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mvttt", "mvtttdb", "MVTTTapp1234!@#$");
    }
    public static Match getMatch(int match_id) throws SQLException {
        ResultSet rs = dbConn.createStatement().executeQuery("SELECT * FROM matches WHERE match_id=" + match_id + ";");
        rs.next();
        return new Match(rs.getInt("match_id"), rs.getInt("player_x_id"), rs.getInt("player_o_id"), rs.getInt("player_x_score"), rs.getInt("player_o_score"), rs.getString("match_status"), rs.getInt("match_winner"), rs.getString("match_exp"));
    }
}
