package com.unstoppapoenguyen.mvttt;

public class Player {
    int player_id;
    String player_name;
    String player_matchesUpdateToken;
    int player_wins;
    int player_losses;
    public Player(int id, String name, int wins, int losses){
        player_id = id;
        player_name = name;
        player_wins = wins;
        player_losses = losses;
    }
}
