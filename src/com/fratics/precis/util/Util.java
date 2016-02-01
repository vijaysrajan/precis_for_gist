package com.fratics.precis.util;

public class Util {
    public static String generateRandomId() {
	return Integer.toString((int) (Math.random() * 100000));
    }
}
