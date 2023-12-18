package com.unstoppapoenguyen.mvttt;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.commons.io.IOUtils;
import com.google.gson.Gson;
import java.util.ArrayList;

import jakarta.servlet.*;             
import jakarta.servlet.http.*;        
import jakarta.servlet.annotation.*;

import java.sql.SQLException;

@WebServlet(name = "WebController", urlPatterns = {"/"})
public class WebController extends HttpServlet {
    public void init(){
        try {
            DataController.connectDatabase();
        }
        catch (SQLException|ClassNotFoundException|InstantiationException|IllegalAccessException|InvocationTargetException e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        switch(req.getRequestURI()){
            case "/MVTTT":
            case "/MVTTT/":
                res.setContentType("text/html;charset=UTF-8");
                res.getWriter().print(Files.readString(Paths.get("MVTT_WD\\welcome.html")));
                break;
            case "/MVTTT/testPage":
                res.setContentType("text/html;charset=UTF-8");
                res.getWriter().print(Files.readString(Paths.get("MVTT_WD\\testPage.html")));
                break;
            default:
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String success;
        switch(req.getRequestURI()){
            case "/MVTTT/getPlayers":
                ArrayList<Player> players = null;
                String pStr = IOUtils.toString(req.getReader());
                pStr = pStr.replace("\"", "");
                int[] pIDs = {Integer.parseInt(pStr.split(",", 0)[0]),Integer.parseInt(pStr.split(",", 0)[1])};
                try {
                    players = DataController.getPlayers(pIDs);
                }
                catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                res.getWriter().print(new Gson().toJson(players));
                break;
            case "/MVTTT/getMatch":
                Match match = null;
                try {
                    match = DataController.getMatch(Integer.parseInt(IOUtils.toString(req.getReader())));
                }
                catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                res.getWriter().print(new Gson().toJson(match));
                break;
            case "/MVTTT/getMatches":
                ArrayList<Match> matches = null;
                try {
                    matches = DataController.getMatches(Integer.parseInt(IOUtils.toString(req.getReader())));
                }
                catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                res.getWriter().print(new Gson().toJson(matches));
                break;
            case "/MVTTT/updateMatch":
                success = "";
                try {
                    DataController.updateMatch(new Gson().fromJson(IOUtils.toString(req.getReader()), Match.class));
                    success = "true";
                }
                catch (SQLException e) {
                    System.out.println(e.getMessage());
                    success = "false";
                }
                res.getWriter().print("{\"success\":"+success+"}");
                break;
            case "/MVTTT/getGame":
                Game game = null;
                try {
                    game = DataController.getGame(Integer.parseInt(IOUtils.toString(req.getReader())));
                }
                catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                res.getWriter().print(new Gson().toJson(game));
                break;
            case "/MVTTT/getGames":
                ArrayList<Game> games = null;
                try {
                    games = DataController.getGames(Integer.parseInt(IOUtils.toString(req.getReader())));
                }
                catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                res.getWriter().print(new Gson().toJson(games));
                break;
            case "/MVTTT/updateGame":
                success = "";
                try {
                    DataController.updateGame(new Gson().fromJson(IOUtils.toString(req.getReader()), Game.class));
                    success = "true";
                }
                catch (SQLException e) {
                    System.out.println(e.getMessage());
                    success = "false";
                }
                res.getWriter().print("{\"success\":"+success+"}");
                break;
            default:
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}