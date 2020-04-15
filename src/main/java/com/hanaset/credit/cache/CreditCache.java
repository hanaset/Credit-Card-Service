package com.hanaset.credit.cache;

import com.google.common.collect.Sets;
import lombok.Synchronized;

import java.util.Set;

public class CreditCache {

    private static Set<String> cardNumberSet = Sets.newHashSet();
    private static Set<String> idSet = Sets.newHashSet();

    @Synchronized
    public static boolean idContainsAndSet(String id) {
        if(idSet.contains(id)) {
            return true;
        } else {
            idSet.add(id);
            return false;
        }
    }

    @Synchronized
    public static boolean cardNumberContainsAndSet(String cardNumber){
        if(cardNumberSet.contains(cardNumber)) {
            return true;
        } else {
            cardNumberSet.add(cardNumber);
            return false;
        }
    }

    @Synchronized
    public static void idSetRemove(String id) {
        idSet.remove(id);
    }

    @Synchronized
    public static void cardNumberSetRemove(String cardNumber) {
        cardNumberSet.remove(cardNumber);
    }
}
