package com.unstoppapoenguyen.mvttt;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.io.IOUtils;
import com.google.gson.Gson;
import java.util.ArrayList;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.sql.SQLException;

@WebServlet(name = "WebController", urlPatterns = { "/" })
public class WebController extends HttpServlet {
    public void init() {
        try {
            DataController.connectDatabase();
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException
                | InvocationTargetException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // System.out.println(req.getRequestURI());
        switch (req.getRequestURI()) {
            case "/MVTTT":
            case "/MVTTT/":
                res.setContentType("text/html;charset=UTF-8");
                res.getWriter().print(Files.readString(Paths.get("MVTT_WD\\welcome.html")));
                break;
            case "/MVTTT/testPage":
                res.setContentType("text/html;charset=UTF-8");
                res.getWriter().print(Files.readString(Paths.get("MVTT_WD\\testPage.html")));
                break;
            case "/MVTTT/webMobile":
                res.setContentType("text/html;charset=UTF-8");
                res.getWriter().print(Files.readString(Paths.get("MVTT_WD\\web\\web_mobile\\index.html")));
                break;
            case "/MVTTT/mvttt_web.css":
                res.setContentType("text/css;charset=UTF-8");
                res.getWriter().print(Files.readString(Paths.get("MVTT_WD\\web\\web_mobile\\mvttt_web.css")));
                break;
            case "/MVTTT/mvttt_web.js":
                res.setContentType("text/js;charset=UTF-8");
                res.getWriter().print(Files.readString(Paths.get("MVTT_WD\\web\\web_mobile\\mvttt_web.js")));
                break;
            default:
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Match match;
        String success;
        String bodyStr;
        String[] bStrs;
        int updateToken;
        switch (req.getRequestURI()) {
            case "/MVTTT/login":
                try {
                    CredSet creds = new Gson().fromJson(IOUtils.toString(req.getReader()), CredSet.class);
                    if (!DataController.checkUsername(creds.username)) {
                        res.getWriter().print("{\"success\":false,\"reason\":\"usernameAndPassword\"}");
                        break;
                    }
                    PlayerHash hashSet = DataController.getPlayerHash(creds.username);
                    if (HashUtil.checkHashSet(creds.password, hashSet.salt, hashSet.hash)) {
                        try {
                            DataController.updatePlayerSession(creds.username, req.getHeader("session"));
                            res.getWriter()
                                    .print("{\"success\":true, \"id\": " + DataController.getPlayerID(creds.username)
                                            + ", \"username\": \"" + creds.username + "\"}");
                        } catch (SQLException e) {
                            res.getWriter().print("{\"success\":false,\"reason\":\"sessionUpdateError\"}");
                        }
                    } else
                        res.getWriter().print("{\"success\":false,\"reason\":\"usernameAndPassword\"}");
                } catch (SQLException | NoSuchAlgorithmException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "/MVTTT/logout":
                bodyStr = (new Gson().fromJson(IOUtils.toString(req.getReader()), ReqBodyStr.class)).str;
                try {
                    if(bodyStr.equals(req.getHeader("session"))) DataController.clearPlayerSession(bodyStr);
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "/MVTTT/createPlayer":
                try {
                    CredSet creds = new Gson().fromJson(IOUtils.toString(req.getReader()), CredSet.class);
                    if (DataController.checkUsername(creds.username)) {
                        res.getWriter().print("{\"success\":false,\"reason\":\"username\"}");
                        break;
                    }
                    try {
                        PlayerHash hashSet = HashUtil.getHashSet(creds.password);
                        DataController.createPlayer(creds.username, hashSet.salt, hashSet.hash, creds.email);
                        DataController.updatePlayerSession(creds.username, req.getHeader("session"));
                        res.getWriter().print("{\"success\":true, \"id\": " + DataController.getPlayerID(creds.username) + "}");
                    } catch (SQLException e) {
                        res.getWriter().print("{\"success\":false,\"reason\":\"playerCreationError\"}");
                        System.out.println(e.getMessage());
                    }
                } catch (SQLException | NoSuchAlgorithmException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "/MVTTT/getPlayers":
                ArrayList<Player> players = null;
                try {
                    players = DataController.getPlayers();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                res.getWriter().print(new Gson().toJson(players));
                break;
            case "/MVTTT/createMatch":
                bStrs = (new Gson().fromJson(IOUtils.toString(req.getReader()), ReqBodyStr.class)).str.split(",");
                int newMatchID = -1;
                try {
                    newMatchID = DataController.createMatch(Integer.parseInt(bStrs[0]), Integer.parseInt(bStrs[1]),
                            Integer.parseInt(bStrs[2]), Integer.parseInt(bStrs[3]), Integer.parseInt(bStrs[4]));
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                res.getWriter().print("{\"match_id\":" + newMatchID + "}");
                break;
            case "/MVTTT/getMatch":
                match = null;
                bodyStr = (new Gson().fromJson(IOUtils.toString(req.getReader()), ReqBodyStr.class)).str;
                try {
                    match = DataController.getMatch(Integer.parseInt(bodyStr));
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                res.getWriter().print(new Gson().toJson(match));
                break;
            case "/MVTTT/getMatches":
                ArrayList<Match> matches = null;
                bodyStr = (new Gson().fromJson(IOUtils.toString(req.getReader()), ReqBodyStr.class)).str;
                try {
                    matches = DataController.getMatches(Integer.parseInt(bodyStr));
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                res.getWriter().print(new Gson().toJson(matches));
                break;
            case "/MVTTT/getMatchUpdateToken":
                bodyStr = (new Gson().fromJson(IOUtils.toString(req.getReader()), ReqBodyStr.class)).str;
                updateToken = -1;
                try {
                    updateToken = DataController.getMatchUpdateToken(Integer.parseInt(bodyStr));
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                res.getWriter().print("{\"token\":" + updateToken + "}");
                break;
            case "/MVTTT/getPlayerMatceshUpdateToken":
                bodyStr = (new Gson().fromJson(IOUtils.toString(req.getReader()), ReqBodyStr.class)).str;
                updateToken = -1;
                try {
                    updateToken = DataController.getPlayerMatceshUpdateToken(Integer.parseInt(bodyStr));
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                res.getWriter().print("{\"token\":" + updateToken + "}");
                break;
            case "/MVTTT/updateMatch":
                match = new Gson().fromJson(IOUtils.toString(req.getReader()), Match.class);
                Match newMatch = null;
                try {
                    DataController.updateMatch(match);
                    newMatch = DataController.getMatch(match.match_id);
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                res.getWriter().print(new Gson().toJson(newMatch));
                break;
            case "/MVTTT/getGame":
                Game game = null;
                bodyStr = (new Gson().fromJson(IOUtils.toString(req.getReader()), ReqBodyStr.class)).str;
                try {
                    game = DataController.getGame(Integer.parseInt(bodyStr));
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                res.getWriter().print(new Gson().toJson(game));
                break;
            case "/MVTTT/getGames":
                ArrayList<Game> games = null;
                bodyStr = (new Gson().fromJson(IOUtils.toString(req.getReader()), ReqBodyStr.class)).str;
                try {
                    games = DataController.getGames(Integer.parseInt(bodyStr));
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                res.getWriter().print(new Gson().toJson(games));
                break;
            case "/MVTTT/updateGame":
                success = "";
                try {
                    DataController.updateGame(new Gson().fromJson(IOUtils.toString(req.getReader()), Game.class));
                    success = "true";
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                    success = "false";
                }
                res.getWriter().print("{\"success\":" + success + "}");
                break;
            case "/MVTTT/turnUpdate":
                bodyStr = (new Gson().fromJson(IOUtils.toString(req.getReader()), ReqBodyStr.class)).str;
                bStrs = bodyStr.split("<~>");
                Match matchReturn = null;
                if (bStrs[0].equals("normal")) {
                    match = new Gson().fromJson(bStrs[1], Match.class);
                    try {
                        DataController.updateGame(new Gson().fromJson(bStrs[2], Game.class));
                        DataController.updateMatch(match);
                        matchReturn = DataController.getMatch(match.match_id);
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                } else if (bStrs[0].equals("creation")) {
                    match = new Gson().fromJson(bStrs[1], Match.class);
                    try {
                        DataController.updateGame(new Gson().fromJson(bStrs[2], Game.class));
                        DataController.createGame(new Gson().fromJson(bStrs[3], Game.class));
                        DataController.updateMatch(match);
                        matchReturn = DataController.getMatch(match.match_id);
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                }
                res.getWriter().print(new Gson().toJson(matchReturn));
                break;
            default:
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}