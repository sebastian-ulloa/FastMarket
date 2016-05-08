package com.fastmarket.fastmarket;

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
        System.out.println(R.string.authDE);
        System.out.println(String.valueOf(R.string.authDE));
        assertEquals("4dC42WyPvICPBbix1QX5ANKWmwo=",Utils.calculateRFC2104HMAC("5055709806939", "Cc10Q5b3u5Ll0Vu6"));
    }
}