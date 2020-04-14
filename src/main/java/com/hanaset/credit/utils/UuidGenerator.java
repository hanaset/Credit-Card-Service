package com.hanaset.credit.utils;

import net.bytebuddy.utility.RandomString;

public class UuidGenerator {

    public static String getUuid() {
       return RandomString.make(20);
    }
}
