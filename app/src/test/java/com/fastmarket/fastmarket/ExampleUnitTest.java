package com.fastmarket.fastmarket;

import com.journeyapps.barcodescanner.Util;

import org.hamcrest.core.StringContains;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testSHA() throws Exception {
        String n = "50557098069";
        int a = 39;
        for (int i = 0; i < 10; i++) {
            System.out.println(Utils.calculateRFC2104HMAC(n + String.valueOf(a), "Cc10Q5b3u5Ll0Vu6"));
            a++;
        }
       // assertEquals("4dC42WyPvICPBbix1QX5ANKWmwo=", Utils.calculateRFC2104HMAC("5055709806939", "Cc10Q5b3u5Ll0Vu6"));
    }
}