package com.github.generics;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class LessonTwoGenericMethods {

    Logger logger = LoggerFactory.getLogger(LessonTwoGenericMethods.class);

    @Test
    public void methodGenericCompare() {
        PairKLessonTwo<Integer, String> p1 = new PairKLessonTwo<>(1, "apple");
        PairKLessonTwo<Integer, String> p2 = new PairKLessonTwo<>(2, "pear");
        boolean same = Util.<Integer, String>compare(p1, p2);
        assertFalse(same);
    }

    @Test
    public void methodGenericCompareInferTypesCompiler() {
        PairKLessonTwo<Integer, String> p1 = new PairKLessonTwo<>(1, "apple");
        PairKLessonTwo<Integer, String> p2 = new PairKLessonTwo<>(2, "pear");

        // El compilador infiere el tipo que se necesita
        boolean same = Util.compare(p1, p2);
        assertFalse(same);
    }

}

class Util {
    public static <K, V> boolean compare(PairKLessonTwo<K, V> p1, PairKLessonTwo<K, V> p2) {
        return p1.getKey().equals(p2.getKey()) && p1.getValue().equals(p2.getValue());
    }
}

class PairKLessonTwo<K, V> {

    private K key;
    private V value;

    public PairKLessonTwo(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}