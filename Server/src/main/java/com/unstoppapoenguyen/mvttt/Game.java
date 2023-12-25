package com.unstoppapoenguyen.mvttt;

public class Game {
    int game_id;
    int match_id;
    String board_current;
    String board_prev;
    int game_status;
    int game_winner;
    int game_lastPlayer;
    public Game(int id, int mId, String bCurrent, String bPrev, int status, int win, int lastPlayer){
        game_id = id;
        match_id = mId;
        board_current = bCurrent;
        board_prev = bPrev;
        game_status = status;
        game_winner = win;
        game_lastPlayer = lastPlayer;
    }
}
