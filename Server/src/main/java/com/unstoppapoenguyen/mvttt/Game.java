package com.unstoppapoenguyen.mvttt;

public class Game {
    int game_id;
    int match_id;
    String board_current;
    String board_prev;
    int game_status;
    int game_winner;
    public Game(){}
    public Game(int id, int mId, String bCurrent, String bPrev, int status, int win){
        game_id = id;
        match_id = mId;
        board_current = bCurrent;
        board_prev = bPrev;
        game_status = status;
        game_winner = win;
    }
}
