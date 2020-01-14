package com.github.generics;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import lombok.NonNull;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class LessonSixWildcard {

    @Test
    public void sumOfListTest() {
        final List<Integer> li = List.of(1, 2, 3);
        final double sumOfList = sumOfList(li);
        assertEquals(sumOfList, 6);
        assertNotEquals(sumOfList, 5);
    }

    @Test
    public void sumOfListForcePossibleTypeUnsafeCaseUse() {
        final List<Number> li = List.of(1, 2, 3.6);
        final double sumOfList = sumOfList(li);
        assertEquals(sumOfList, 6.6);
    }

    private static double sumOfList(@NonNull List<? extends Number> list) {
        return list.stream().map(Number::doubleValue).reduce(Double::sum).orElse(Double.valueOf(0));
    }

    @Test
    public void printListWildcard() {
        printList(List.of("Hola, ", "soy ", "Voldemort.\n"));
        printList(List.of(3, 5, 6));
    }

    private void printList(@NonNull List<?> list) {
        list.forEach(System.out::print);
    }

    @Test
    public void addNumber_lowerBoundedWildCardsExample() {
        final List<Number> list = new ArrayList<>();
        addNumbers(list);
        assertThat(list, contains(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
    }

    private static void addNumbers(@NonNull List<? super Integer> list) {
        for (int i = 1; i <= 10; i++) {
            list.add(i);
        }
    }

    @Test
    public void upperBoundedWildcardToRelationshipGenericClasses() {
        final List<? extends Integer> integers = List.of(1, 3, 5);
        final List<? extends Number> numbers = integers;
        assertThat(numbers, instanceOf(List.class));
        final Optional<? extends Number> sum = sumNumberOfList(numbers);
        assertThat(sum.get(), is(9.0));
    }

    private Optional<Double> sumNumberOfList(@NonNull final List<? extends Number> listNumbers) {
        return listNumbers.stream().map(Number::doubleValue).reduce(Double::sum);
    }

    @Test
    public void wildCardList_exampleNotOnlyRead_operationsAllowed() {
        final List<EvenNumber> le = new ArrayList<>();
        le.add(new EvenNumber(2));
        final List<? extends NaturalNumber> ln = le;
        ln.add(null); // add null
        // ln.clear(); // clear list
        // ln.iterator().remove(); // iterator and remove
        final List<Object> listNumbers = Arrays.asList(helperMethodCapture(ln));
        assertThat(listNumbers.get(0), instanceOf(EvenNumber.class));
    }

    private <T> T[] helperMethodCapture(@NonNull final List<T> l) {
        return (T[]) Arrays.array(l.stream().toArray());
    }

}

class NaturalNumber {

    private final int i;

    public NaturalNumber(int i) {
        this.i = i;
    }

    protected int get() {
        return this.i;
    }
}

final class EvenNumber extends NaturalNumber {

    public EvenNumber(int i) {
        super(i);
    }
    // ...
}