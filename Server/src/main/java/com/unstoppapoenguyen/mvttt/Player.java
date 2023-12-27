package com.unstoppapoenguyen.mvttt;

public class Player {
    int player_id;
    String player_name;
    String player_matchesUpdateToken;
    String player_wins;
    String player_losses;
    public Player(int id, String name, String wins, String losses){
        player_id = id;
        player_name = name;
        player_wins = wins;
        player_losses = losses;
    }
}
