package com.unstoppapoenguyen.mvttt;

public class PlayerHash {
    byte[] salt;
    byte[] hash;
    public PlayerHash(byte[] s, byte[] h){
        salt = s;
        hash = h;
    }
}
