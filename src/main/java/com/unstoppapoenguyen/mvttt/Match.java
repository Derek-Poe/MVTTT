package com.unstoppapoenguyen.mvttt;

public class Match {
    int match_id;
    int player_x_id;
    int player_o_id;
    int player_x_score;
    int player_o_score;
    String match_status;
    int match_winner;
    String match_exp;
    public Match(){}
    public Match(int id, int xId, int oId, int xScore, int oScore, String status, int win, String exp){
        match_id = id;
        player_x_id = xId;
        player_o_id = oId;
        player_x_score = xScore;
        player_o_score = oScore;
        match_status = status;
        match_winner = win;
        match_exp = exp;
    }
}
