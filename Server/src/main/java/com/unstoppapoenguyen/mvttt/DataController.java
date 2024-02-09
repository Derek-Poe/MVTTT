package com.unstoppapoenguyen.mvttt;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

import java.sql.ResultSet;

public class DataController {
    public static Connection dbConn;

    public static void connectDatabase() throws SQLException, ClassNotFoundException, InstantiationException,
            IllegalAccessException, InvocationTargetException {
        Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructors()[0].newInstance();
        dbConn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mvttt?connectTimeout=0&socketTimeout=0&autoReconnect=true", "mvtttdb", "MVTTTapp1234!@#$");
    }

    public static Boolean checkUsername(String username) throws SQLException {
        ArrayList<String> players = new ArrayList<String>();
        PreparedStatement tmpl = dbConn.prepareStatement("SELECT * FROM players WHERE LOWER(player_name) = ?");
        tmpl.setString(1, username.toLowerCase());
        ResultSet rs = tmpl.executeQuery();
        while (rs.next()) {
            players.add(username);
        }
        if (players.size() > 0)
            return true;
        else
            return false;
    }

    public static void createPlayer(String username, byte[] salt, byte[] hash, String email) throws SQLException {
        PreparedStatement tmpl = dbConn
                .prepareStatement(
                        "INSERT INTO players (player_name, player_salt, player_hash, player_email, player_matchesUpdateToken, player_wins, player_losses, player_lastLogon) VALUES (?, ?, ?, ?, 0, 0, 0, ?)");
        tmpl.setString(1, username);
        tmpl.setBytes(2, salt);
        tmpl.setBytes(3, hash);
        tmpl.setString(4, email);
        tmpl.setString(5, LocalDateTime.now().plus(Period.ofDays(7)).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        tmpl.executeUpdate();
    }

    public static PlayerHash getPlayerHash(String username) throws SQLException {
        PreparedStatement tmpl = dbConn
                .prepareStatement("SELECT player_salt, player_hash FROM players WHERE player_name = ?");
        tmpl.setString(1, username);
        ResultSet rs = tmpl.executeQuery();
        rs.next();
        return new PlayerHash(rs.getBytes("player_salt"), rs.getBytes("player_hash"));
    }

    public static void updatePlayerSession(String username, String session) throws SQLException {
        PreparedStatement tmpl = dbConn.prepareStatement("UPDATE players SET player_session = ?, player_lastLogon = ? WHERE player_name = ?");
        tmpl.setString(1, session);
        tmpl.setString(2, LocalDateTime.now().plus(Period.ofDays(7)).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        tmpl.setString(3, username);
        tmpl.executeUpdate();
    }

    public static void clearPlayerSession(String session) throws SQLException {
        PreparedStatement tmpl = dbConn.prepareStatement("UPDATE players SET player_session = ? WHERE player_session = ?");
        tmpl.setNull(1, Types.VARCHAR);
        tmpl.setString(2, session);
        tmpl.executeUpdate();
    }

    public static int getPlayerID(String username) throws SQLException {
        PreparedStatement tmpl = dbConn.prepareStatement("SELECT player_id FROM players WHERE player_name = ?");
        tmpl.setString(1, username);
        ResultSet rs = tmpl.executeQuery();
        rs.next();
        return rs.getInt("player_id");
    }

    public static ArrayList<Player> getPlayers() throws SQLException {
        PreparedStatement tmpl = dbConn.prepareStatement("SELECT * FROM players ORDER BY player_name");
        ResultSet rs = tmpl.executeQuery();
        ArrayList<Player> players = new ArrayList<Player>();
        while (rs.next()) {
            players.add(new Player(rs.getInt("player_id"), rs.getString("player_name"), rs.getInt("player_wins"),
                    rs.getInt("player_losses")));
        }
        return players;
    }

    public static void updatePlayerMatceshUpdateToken(int player_id) throws SQLException {
        PreparedStatement tmpl = dbConn
                .prepareStatement("UPDATE players SET player_matchesUpdateToken = ? WHERE player_id = ?");
        tmpl.setInt(1, (new Random().nextInt(9000) + 1000));
        tmpl.setInt(2, player_id);
        tmpl.executeUpdate();
    }

    public static int getPlayerMatceshUpdateToken(int player_id) throws SQLException {
        PreparedStatement tmpl = dbConn.prepareStatement("SELECT player_matchesUpdateToken FROM players WHERE player_id = ?");
        tmpl.setInt(1, player_id);
        ResultSet rs = tmpl.executeQuery();
        rs.next();
        return rs.getInt("player_matchesUpdateToken");
    }

    public static void updatePlayerRecords(int winnerID, int unwinnerID) throws SQLException {
        PreparedStatement tmpl = dbConn.prepareStatement("SELECT * FROM players WHERE player_id = ? OR player_id = ? ORDER BY player_name");
        tmpl.setInt(1, winnerID);
        tmpl.setInt(2, unwinnerID);
        ResultSet rs = tmpl.executeQuery();
        ArrayList<Player> players = new ArrayList<Player>();
        while (rs.next()) {
            players.add(new Player(rs.getInt("player_id"), rs.getString("player_name"), rs.getInt("player_wins"),
                    rs.getInt("player_losses")));
        }
        Player winner = players.stream().filter(player ->  player.player_id == winnerID).findFirst().get();
        Player unwinner = players.stream().filter(player ->  player.player_id == winnerID).findFirst().get();
        tmpl = dbConn
                .prepareStatement("UPDATE players SET player_wins = ? WHERE player_id = ?");
        tmpl.setInt(1, winner.player_wins + 1);
        tmpl.setInt(2, winner.player_id);
        tmpl.executeUpdate();
        tmpl = dbConn
                .prepareStatement("UPDATE players SET player_losses = ? WHERE player_id = ?");
        tmpl.setInt(1, unwinner.player_losses + 1);
        tmpl.setInt(2, unwinner.player_id);
        tmpl.executeUpdate();
    }

    public static int createMatch(int xId, int oId, int turn, int type, int scoreGoal) throws SQLException {
        int boardLimit = 9;
        PreparedStatement tmpl = dbConn.prepareStatement(
                "INSERT INTO matches ( player_x_id, player_o_id, player_x_score, player_o_score, match_status, match_winner, match_exp, match_updateToken, match_turn, match_lastMoveGame, match_type, match_boardLimit, match_scoreGoal) VALUES ( ?, ?, 0, 0, 1, 0, ?, 0, ?, 0, ?, ?, ?)");
        tmpl.setInt(1, xId);
        tmpl.setInt(2, oId);
        tmpl.setString(3, LocalDateTime.now().plus(Period.ofDays(7)).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        tmpl.setInt(4, turn);
        tmpl.setInt(5, type);
        tmpl.setInt(6, 9);
        tmpl.setInt(7, scoreGoal);
        tmpl.executeUpdate();
        updatePlayerMatceshUpdateToken(xId);
        updatePlayerMatceshUpdateToken(oId);
        int newMatchID = getLatestMatch(xId);
        int gameCount = 0;
        if(type == 1) gameCount = 1;
        else if(type == 2) gameCount = boardLimit;
        for(int i = 0; i < gameCount; i++){
            createGame(new Game(-1, newMatchID, "---------", "---------", 0, 0, 0));
        }
        return newMatchID;
    }

    public static ArrayList<Match> getMatches(int player_id) throws SQLException {
        PreparedStatement tmpl = dbConn.prepareStatement(
                "SELECT match_id, player_x_id, player_o_id, player_x_score, player_o_score, (SELECT player_name FROM players p WHERE p.player_id = m.player_x_id LIMIT 0,1) player_x_name, (SELECT player_name FROM players p WHERE p.player_id = m.player_o_id LIMIT 0,1) player_o_name, match_status, match_winner, match_exp, match_updateToken, match_turn, match_lastMoveGame, match_type, match_boardLimit, match_scoreGoal FROM matches m WHERE player_x_id = ? OR player_o_id = ?");
        tmpl.setInt(1, player_id);
        tmpl.setInt(2, player_id);
        ResultSet rs = tmpl.executeQuery();
        ArrayList<Match> matches = new ArrayList<Match>();
        while (rs.next()) {
            matches.add(new Match(rs.getInt("match_id"), rs.getInt("player_x_id"), rs.getInt("player_o_id"),
                    rs.getInt("player_x_score"), rs.getInt("player_o_score"), rs.getString("player_x_name"),
                    rs.getString("player_o_name"), rs.getInt("match_status"), rs.getInt("match_winner"),
                    rs.getString("match_exp"), rs.getInt("match_updateToken"), rs.getInt("match_turn"),
                    rs.getInt("match_lastMoveGame"), rs.getInt("match_type"), rs.getInt("match_boardLimit"),
                    rs.getInt("match_scoreGoal")));
        }
        if (matches.size() == 0)
            matches.add(new Match(-1, 0, 0, 0, 0, "", "", 0, 0, "", 0, 0, 0, 0, 0, 0));
        return matches;
    }

    public static Match getMatch(int match_id) throws SQLException {
        PreparedStatement tmpl = dbConn.prepareStatement(
                "SELECT match_id, player_x_id, player_o_id, player_x_score, player_o_score, (SELECT player_name FROM players p WHERE p.player_id = m.player_x_id LIMIT 0,1) player_x_name, (SELECT player_name FROM players p WHERE p.player_id = m.player_o_id LIMIT 0,1) player_o_name, match_status, match_winner, match_exp, match_updateToken, match_turn, match_lastMoveGame, match_type, match_boardLimit, match_scoreGoal FROM matches m WHERE match_id = ?");
        tmpl.setInt(1, match_id);
        ResultSet rs = tmpl.executeQuery();
        rs.next();
        return new Match(rs.getInt("match_id"), rs.getInt("player_x_id"), rs.getInt("player_o_id"),
                rs.getInt("player_x_score"), rs.getInt("player_o_score"), rs.getString("player_x_name"),
                rs.getString("player_o_name"), rs.getInt("match_status"), rs.getInt("match_winner"),
                rs.getString("match_exp"), rs.getInt("match_updateToken"), rs.getInt("match_turn"),
                rs.getInt("match_lastMoveGame"), rs.getInt("match_type"), rs.getInt("match_boardLimit"),
                rs.getInt("match_scoreGoal"));
    }

    public static int getLatestMatch(int player_id) throws SQLException {
        PreparedStatement tmpl = dbConn.prepareStatement(
                "SELECT match_id FROM matches WHERE player_x_id = ? OR player_o_id = ? ORDER BY match_id DESC LIMIT 0,1");
        tmpl.setInt(1, player_id);
        tmpl.setInt(2, player_id);
        ResultSet rs = tmpl.executeQuery();
        rs.next();
        return rs.getInt("match_id");
    }

    public static void updateMatch(Match match) throws SQLException {
        PreparedStatement tmpl = dbConn.prepareStatement(
                "UPDATE matches SET player_x_score = ?, player_o_score = ?, match_status = ?, match_winner = ?, match_exp = ?, match_updateToken = ?, match_turn = ?, match_lastMoveGame = ?, match_type = ? WHERE match_id = ?");
        tmpl.setInt(1, match.player_x_score);
        tmpl.setInt(2, match.player_o_score);
        tmpl.setInt(3, match.match_status);
        tmpl.setInt(4, match.match_winner);
        tmpl.setString(5, match.match_exp);
        tmpl.setInt(6, (new Random().nextInt(9000) + 1000));
        tmpl.setInt(7, match.match_turn);
        tmpl.setInt(8, match.match_lastMoveGame);
        tmpl.setInt(9, match.match_type);
        tmpl.setInt(10, match.match_id);
        tmpl.executeUpdate();
    }

    public static int getMatchUpdateToken(int match_id) throws SQLException {
        PreparedStatement tmpl = dbConn.prepareStatement("SELECT match_updateToken FROM matches WHERE match_id = ?");
        tmpl.setInt(1, match_id);
        ResultSet rs = tmpl.executeQuery();
        rs.next();
        return rs.getInt("match_updateToken");
    }

    public static void updateMatchUpdateToken(int match_id) throws SQLException {
        PreparedStatement tmpl = dbConn.prepareStatement("UPDATE matches SET match_updateToken = ? WHERE match_id = ?");
        tmpl.setInt(1, (new Random().nextInt(9000) + 1000));
        tmpl.setInt(2, match_id);
        tmpl.executeUpdate();
    }

    public static void cleanupMatches() throws SQLException {
        // String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        // PreparedStatement tmpl = dbConn.prepareStatement("DELETE FROM games WHERE match_id IN (SELECT match_id FROM matches WHERE match_exp < ?) LIMIT 10000");
        // tmpl.setString(1, date);
        // tmpl.executeUpdate();
        // tmpl = dbConn.prepareStatement("DELETE FROM matches WHERE match_exp < ? LIMIT 10000");
        // tmpl.setString(1, date);
        // tmpl.executeUpdate();
        dbConn.prepareCall("{call cleanupMatches()}").execute();
    }

    public static void createGame(Game game) throws SQLException {
        PreparedStatement tmpl = dbConn.prepareStatement(
                "INSERT INTO games (match_id, board_current, board_prev, game_status, game_winner, game_lastPlayer) VALUES (?, ?, ?, ?, ?, ?)");
        tmpl.setInt(1, game.match_id);
        tmpl.setString(2, game.board_current);
        tmpl.setString(3, game.board_prev);
        tmpl.setInt(4, game.game_status);
        tmpl.setInt(5, game.game_winner);
        tmpl.setInt(6, game.game_lastPlayer);
        tmpl.executeUpdate();
        updateMatchUpdateToken(game.match_id);
    }

    public static Game getGame(int game_id) throws SQLException {
        PreparedStatement tmpl = dbConn.prepareStatement("SELECT * FROM games WHERE game_id = ? AND game_status != 2");
        tmpl.setInt(1, game_id);
        ResultSet rs = tmpl.executeQuery();
        rs.next();
        return new Game(rs.getInt("game_id"), rs.getInt("match_id"), rs.getString("board_current"),
                rs.getString("board_prev"), rs.getInt("game_status"), rs.getInt("game_winner"),
                rs.getInt("game_lastPlayer"));
    }

    public static ArrayList<Game> getGames(int match_id) throws SQLException {
        PreparedStatement tmpl = dbConn.prepareStatement("SELECT * FROM games WHERE match_id = ? AND game_status != 2");
        tmpl.setInt(1, match_id);
        ResultSet rs = tmpl.executeQuery();
        ArrayList<Game> games = new ArrayList<Game>();
        while (rs.next()) {
            games.add(new Game(rs.getInt("game_id"), rs.getInt("match_id"), rs.getString("board_current"),
                    rs.getString("board_prev"), rs.getInt("game_status"), rs.getInt("game_winner"),
                    rs.getInt("game_lastPlayer")));
        }
        return games;
    }

    public static void updateGame(Game game) throws SQLException {
        PreparedStatement tmpl = dbConn.prepareStatement(
                "UPDATE games SET board_current = ?, board_prev = ?, game_status = ?, game_winner = ?, game_lastPlayer = ? WHERE game_id = ?");
        tmpl.setString(1, game.board_current);
        tmpl.setString(2, game.board_prev);
        tmpl.setInt(3, game.game_status);
        tmpl.setInt(4, game.game_winner);
        tmpl.setInt(5, game.game_lastPlayer);
        tmpl.setInt(6, game.game_id);
        tmpl.executeUpdate();
        updateMatchUpdateToken(game.match_id);
    }

    public static void ka() throws SQLException {
        PreparedStatement tmpl = dbConn.prepareStatement("SELECT player_id FROM players LIMIT 0,1");
        tmpl.executeQuery();
    }
}
