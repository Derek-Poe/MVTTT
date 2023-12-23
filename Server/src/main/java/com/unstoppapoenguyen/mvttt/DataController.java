package com.unstoppapoenguyen.mvttt;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import java.sql.ResultSet;

public class DataController {
    public static Connection dbConn;
    public static void connectDatabase() throws SQLException,ClassNotFoundException,InstantiationException,IllegalAccessException,InvocationTargetException {
        Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructors()[0].newInstance();
        dbConn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mvttt", "mvtttdb", "MVTTTapp1234!@#$");
    }
    public static Boolean checkUsername(String username) throws SQLException {
        ArrayList<String> players = new ArrayList<String>();
        PreparedStatement tmpl = dbConn.prepareStatement("SELECT * FROM players WHERE player_name = ?");
        tmpl.setString(1, username);
        ResultSet rs = tmpl.executeQuery();
        while(rs.next()){
            players.add(username);
        }
        if(players.size() > 0) return true;
        else return false;
    }
    public static void createPlayer(String username, byte[] salt, byte[] hash) throws SQLException {
        PreparedStatement tmpl = dbConn.prepareStatement("INSERT INTO players (player_name, player_salt, player_hash) VALUES (?, ?, ?)");
        tmpl.setString(1, username);
        tmpl.setBytes(2, salt);
        tmpl.setBytes(3, hash);
        tmpl.executeUpdate();
    }
    public static PlayerHash getPlayerHash(String username) throws SQLException {
        PreparedStatement tmpl = dbConn.prepareStatement("SELECT player_salt, player_hash FROM players WHERE player_name = ?");
        tmpl.setString(1, username);
        ResultSet rs = tmpl.executeQuery();
        rs.next();
        return new PlayerHash(rs.getBytes("player_salt"), rs.getBytes("player_hash")); 
    }
    public static void updatePlayerSession(String username, String session) throws SQLException {
        PreparedStatement tmpl = dbConn.prepareStatement("UPDATE players SET player_session = ? WHERE player_name = ?");
        tmpl.setString(1, session);
        tmpl.setString(2, username);
        tmpl.executeUpdate();
    }
    public static int getPlayerID(String username) throws SQLException {
        PreparedStatement tmpl = dbConn.prepareStatement("SELECT player_id FROM players WHERE player_name = ?");
        tmpl.setString(1, username);
        ResultSet rs = tmpl.executeQuery();
        rs.next();
        return rs.getInt("player_id"); 
    }
    public static ArrayList<Player> getPlayers(int[] players_ids) throws SQLException {
        PreparedStatement tmpl = dbConn.prepareStatement("SELECT * FROM players WHERE player_id = ? OR player_id = ?");
        tmpl.setInt(1, players_ids[0]);
        tmpl.setInt(2, players_ids[1]);
        ResultSet rs = tmpl.executeQuery();
        ArrayList<Player> players = new ArrayList<Player>();
        while(rs.next()){
            players.add(new Player(rs.getInt("player_id"), rs.getString("player_name")));
        }
        return players; 
    }
    public static ArrayList<Match> getMatches(int player_id) throws SQLException {
        PreparedStatement tmpl = dbConn.prepareStatement("SELECT T1.match_id, T1.player_x_id, T1.player_o_id, T1.player_x_score, T1.player_o_score, T1.player_x_name, T2.player_o_name, T1.match_status, T1.match_winner, T1.match_exp, T1.match_updateToken, T1.match_turn, T1.match_lastMoveGame, T1.match_type FROM (SELECT matches.match_id, players.player_name AS player_x_name, matches.player_x_id, matches.player_o_id, matches.player_x_score, matches.player_o_score, matches.match_status, matches.match_winner, matches.match_exp, matches.match_updateToken, matches.match_turn, matches.match_lastMoveGame, matches.match_type FROM matches INNER JOIN players ON matches.player_x_id = players.player_id) AS T1 INNER JOIN (SELECT matches.match_id AS t2_match_id, players.player_name AS player_o_name FROM matches INNER JOIN players ON matches.player_o_id = players.player_id) AS T2 ON T1.match_id = T2.t2_match_id WHERE player_x_id = ? OR player_o_id = ?");
        tmpl.setInt(1, player_id);
        tmpl.setInt(2, player_id);
        ResultSet rs = tmpl.executeQuery();
        ArrayList<Match> matches = new ArrayList<Match>();
        while(rs.next()){
            matches.add(new Match(rs.getInt("match_id"), rs.getInt("player_x_id"), rs.getInt("player_o_id"), rs.getInt("player_x_score"), rs.getInt("player_o_score"), rs.getString("player_x_name"), rs.getString("player_o_name"), rs.getInt("match_status"), rs.getInt("match_winner"), rs.getString("match_exp"), rs.getInt("match_updateToken"), rs.getInt("match_turn"), rs.getInt("match_lastMoveGame"), rs.getInt("match_type")));
        }
        System.out.println(matches.size());
        if(matches.size() == 0) matches.add(new Match(-1, 0, 0, 0, 0, "", "", 0, 0, "", 0, 0, 0, 0));
        return matches;
    }
    public static Match getMatch(int match_id) throws SQLException {
        PreparedStatement tmpl = dbConn.prepareStatement("SELECT T1.match_id, T1.player_x_id, T1.player_o_id, T1.player_x_score, T1.player_o_score, T1.player_x_name, T2.player_o_name, T1.match_status, T1.match_winner, T1.match_exp, T1.match_updateToken, T1.match_turn, T1.match_lastMoveGame, T1.match_type FROM (SELECT matches.match_id, players.player_name AS player_x_name, matches.player_x_id, matches.player_o_id, matches.player_x_score, matches.player_o_score, matches.match_status, matches.match_winner, matches.match_exp, matches.match_updateToken, matches.match_turn, matches.match_lastMoveGame, matches.match_type FROM matches INNER JOIN players ON matches.player_x_id = players.player_id) AS T1 INNER JOIN (SELECT matches.match_id AS t2_match_id, players.player_name AS player_o_name FROM matches INNER JOIN players ON matches.player_o_id = players.player_id) AS T2 ON T1.match_id = T2.t2_match_id WHERE T1.match_id = ?");
        tmpl.setInt(1, match_id);
        ResultSet rs = tmpl.executeQuery();
        rs.next();
        return new Match(rs.getInt("match_id"), rs.getInt("player_x_id"), rs.getInt("player_o_id"), rs.getInt("player_x_score"), rs.getInt("player_o_score"), rs.getString("player_x_name"), rs.getString("player_o_name"), rs.getInt("match_status"), rs.getInt("match_winner"), rs.getString("match_exp"), rs.getInt("match_updateToken"), rs.getInt("match_turn"), rs.getInt("match_lastMoveGame"), rs.getInt("match_type"));
    }
    public static void updateMatch(Match match) throws SQLException {
        PreparedStatement tmpl = dbConn.prepareStatement("UPDATE matches SET player_x_score = ?, player_o_score = ?, match_status = ?, match_winner = ?, match_exp = ?, match_updateToken = ? WHERE match_id = ?");
        tmpl.setInt(1, match.player_x_score);
        tmpl.setInt(2, match.player_o_score);
        tmpl.setInt(3, match.match_status);
        tmpl.setInt(4, match.match_winner);
        tmpl.setString(5, match.match_exp);
        tmpl.setInt(6, (new Random().nextInt(9000) + 1000));
        tmpl.setInt(7, match.match_id);
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
