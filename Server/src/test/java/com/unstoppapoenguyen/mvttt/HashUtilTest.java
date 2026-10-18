package com.unstoppapoenguyen.mvttt;

import java.util.Arrays;
import junit.framework.TestCase;

/** Regression coverage for the historical format, not a password-security claim. */
public class HashUtilTest extends TestCase {
    public void testRoundTripAndWrongPassword() throws Exception {
        PlayerHash hash = HashUtil.getHashSet("synthetic-test-password");
        assertEquals(16, hash.salt.length);
        assertEquals(64, hash.hash.length);
        assertTrue(HashUtil.checkHashSet("synthetic-test-password", hash.salt, hash.hash));
        assertFalse(HashUtil.checkHashSet("wrong", hash.salt, hash.hash));
    }

    public void testNewHashesUseIndependentSalts() throws Exception {
        PlayerHash first = HashUtil.getHashSet("synthetic");
        PlayerHash second = HashUtil.getHashSet("synthetic");
        assertFalse(Arrays.equals(first.salt, second.salt));
        assertFalse(Arrays.equals(first.hash, second.hash));
    }
}
