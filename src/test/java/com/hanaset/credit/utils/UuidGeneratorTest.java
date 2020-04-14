package com.hanaset.credit.utils;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UuidGeneratorTest {

    @Test
    public void getUuidTest() {

        List<String> uuidList = Lists.newArrayList();
        for(int i = 0 ; i < 100 ; i++) {
            uuidList.add(UuidGenerator.getUuid());
        }

        assertEquals(uuidList.stream().distinct().count(), 100L);
    }
}
