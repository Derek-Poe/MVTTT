package com.unstoppapoenguyen.mvttt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import java.sql.ResultSet;

public class DataController {
    public static Connection dbConn;
    public static void connectDatabase() throws SQLException,ClassNotFoundException,InstantiationException,IllegalAccessException {
        Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        dbConn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mvttt", "mvtttdb", "MVTTTapp1234!@#$");
    }
    public static ArrayList<Match> getMatches(int player_id) throws SQLException {
        PreparedStatement tmpl = dbConn.prepareStatement("SELECT * FROM matches WHERE player_x_id = ? OR player_o_id = ?");
        tmpl.setInt(1, player_id);
        tmpl.setInt(2, player_id);
        ResultSet rs = tmpl.executeQuery();
        ArrayList<Match> matches = new ArrayList<Match>();
        while(rs.next()){
            matches.add(new Match(rs.getInt("match_id"), rs.getInt("player_x_id"), rs.getInt("player_o_id"), rs.getInt("player_x_score"), rs.getInt("player_o_score"), rs.getInt("match_status"), rs.getInt("match_winner"), rs.getString("match_exp"), rs.getInt("match_updateToken"), rs.getInt("match_turn"), rs.getInt("match_lastMoveGame"), rs.getInt("match_type")));
        }
        return matches;
    }
    public static Match getMatch(int match_id) throws SQLException {
        PreparedStatement tmpl = dbConn.prepareStatement("SELECT * FROM matches WHERE match_id = ?");
        tmpl.setInt(1, match_id);
        ResultSet rs = tmpl.executeQuery();
        rs.next();
        return new Match(rs.getInt("match_id"), rs.getInt("player_x_id"), rs.getInt("player_o_id"), rs.getInt("player_x_score"), rs.getInt("player_o_score"), rs.getInt("match_status"), rs.getInt("match_winner"), rs.getString("match_exp"), rs.getInt("match_updateToken"), rs.getInt("match_turn"), rs.getInt("match_lastMoveGame"), rs.getInt("match_type"));
    }
    public static void updateMatch(Match match) throws SQLException {
        PreparedStatement tmpl = dbConn.prepareStatement("UPDATE matches SET player_x_id = ?, player_o_id = ?, player_x_score = ?, player_o_score = ?, match_status = ?, match_winner = ?, match_exp = ?, match_updateToken = ? WHERE match_id = ?");
        tmpl.setInt(1, match.player_x_id);
        tmpl.setInt(2, match.player_o_id);
        tmpl.setInt(3, match.player_x_score);
        tmpl.setInt(4, match.player_o_score);
        tmpl.setInt(5, match.match_status);
        tmpl.setInt(6, match.match_winner);
        tmpl.setString(7, match.match_exp);
        tmpl.setInt(8, (new Random().nextInt(9000) + 1000));
        tmpl.setInt(9, match.match_id);
        tmpl.executeUpdate();        
    }
    public static void updateMatchUpdateToken(int match_id) throws SQLException {
        PreparedStatement tmpl = dbConn.prepareStatement("UPDATE matches SET match_updateToken = ? WHERE match_id = ?");
        tmpl.setInt(1, (new Random().nextInt(9000) + 1000));
        tmpl.setInt(2, match_id);
        tmpl.executeUpdate();        
    }
    public static Game getGame(int game_id) throws SQLException {
        PreparedStatement tmpl = dbConn.prepareStatement("SELECT * FROM games WHERE game_id = ?");
        tmpl.setInt(1, game_id);
        ResultSet rs = tmpl.executeQuery();
        rs.next();
        return new Game(rs.getInt("game_id"), rs.getInt("match_id"), rs.getString("board_current"), rs.getString("board_prev"), rs.getInt("game_status"), rs.getInt("game_winner"));
    }
    public static ArrayList<Game> getGames(int match_id) throws SQLException {
        PreparedStatement tmpl = dbConn.prepareStatement("SELECT * FROM games WHERE match_id = ?");
        tmpl.setInt(1, match_id);
        ResultSet rs = tmpl.executeQuery();
        ArrayList<Game> games = new ArrayList<Game>();
        while(rs.next()){
            games.add(new Game(rs.getInt("game_id"), rs.getInt("match_id"), rs.getString("board_current"), rs.getString("board_prev"), rs.getInt("game_status"), rs.getInt("game_winner")));
        }
        return games;
    }
    public static void updateGame(Game game) throws SQLException {
        PreparedStatement tmpl = dbConn.prepareStatement("UPDATE games SET board_current = ?, board_prev = ?, game_status = ?, game_winner = ? WHERE game_id = ?");
        tmpl.setString(1, game.board_current);
        tmpl.setString(2, game.board_prev);
        tmpl.setInt(3, game.game_status);
        tmpl.setInt(4, game.game_winner);
        tmpl.setInt(5, game.game_id);
        tmpl.executeUpdate();
        updateMatchUpdateToken(game.match_id);    
    }
}
