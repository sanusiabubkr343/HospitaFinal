package com.example.hospitalwaka.Util;

import java.util.Random;

public class Constants {
    public static int randomInt(int max, int min) {
        return new Random().nextInt(max - min) + min;

    }
}
