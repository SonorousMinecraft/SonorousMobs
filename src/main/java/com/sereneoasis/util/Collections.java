package com.sereneoasis.util;

import java.util.Collection;
import java.util.Optional;

public class Collections {

    public static <E> E getRandom (Collection<E> e) {

        return e.stream()
                .skip((int) (e.size() * Math.random()))
                .findFirst().get();
    }
}
