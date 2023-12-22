package com.unstoppapoenguyen.mvttt;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class HashUtil {

    public static PlayerHash getHashSet(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        SecureRandom secRan = new SecureRandom();
        byte[] salt = new byte[16];
        secRan.nextBytes(salt);
        md.update(salt);
        byte[] hPass = md.digest(password.getBytes(StandardCharsets.UTF_8));
        return new PlayerHash(salt, hPass);
    }

    public static Boolean checkHashSet(String password, byte[] salt, byte[] hash) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(salt);
        byte[] compPass = md.digest(password.getBytes(StandardCharsets.UTF_8));
        if(Arrays.equals(hash, compPass)) return true;
        else return false;
    }

}