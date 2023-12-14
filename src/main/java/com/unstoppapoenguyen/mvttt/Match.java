package com.unstoppapoenguyen.mvttt;

public class Match {
    int match_id;
    int player_x_id;
    int player_o_id;
    int player_x_score;
    int player_o_score;
    int match_status;
    int match_winner;
    String match_exp;
    int match_updateToken;
    int match_turn;
    int match_lastMoveGame;
    int match_type;
    public Match(){}
    public Match(int id, int xId, int oId, int xScore, int oScore, int status, int win, String exp, int updateToken, int turn, int lastMoveGame, int type){
        match_id = id;
        player_x_id = xId;
        player_o_id = oId;
        player_x_score = xScore;
        player_o_score = oScore;
        match_status = status;
        match_winner = win;
        match_exp = exp;
        match_updateToken = updateToken;
        match_turn = turn;
        match_lastMoveGame = lastMoveGame;
        match_type = type;
    }
}
