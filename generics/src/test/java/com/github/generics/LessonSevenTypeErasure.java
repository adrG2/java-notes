package com.github.generics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class LessonSevenTypeErasure {

    @Test
    public void checkTypeAfterErasure_whenIsTypeUnboundedTypeParameter() {
        NodeUnBounded<String> nUnBounded = new NodeUnBounded<>("unbounded", new NodeUnBounded<>());
        assertThat(nUnBounded.getData(), is("unbounded"));
        assertThat(nUnBounded.getData(), instanceOf(String.class));
    }

    @Test
    public void checkTypeAfterErasure_whenIsTypeMultipleBoundedTypeParameters() {
        NodeBounded<ExampleConcrete> bounded = new NodeBounded<>(new ExampleConcrete(), new NodeBounded<>());
        assertThat(bounded.getData(), instanceOf(ExampleConcrete.class));
        assertEquals(bounded.getData().compareTo(bounded.getData()), 0);
    }

    @Test
    public void heapPollutionVarArgsExample() {
        List<String> stringListA = new ArrayList<String>();
        List<String> stringListB = new ArrayList<String>();

        ArrayBuilder.addToList(stringListA, "Seven", "Eight", "Nine");
        ArrayBuilder.addToList(stringListB, "Ten", "Eleven", "Twelve");
        List<List<String>> listOfStringLists = new ArrayList<List<String>>();
        ArrayBuilder.addToList(listOfStringLists, stringListA, stringListB);

        // ArrayBuilder.faultyMethod(Arrays.asList("Hello!"), Arrays.asList("World!"));
    }
}

class NodeUnBounded<T> {

    private T data;
    private NodeUnBounded<T> next;

    public NodeUnBounded(T data, NodeUnBounded<T> next) {
        this.data = data;
        this.next = next;
    }

    public NodeUnBounded() {
    }

    public T getData() {
        return data;
    }
}

class NodeBounded<T extends Comparable<T> & Serializable> {

    private T data;
    private NodeBounded<T> next;

    public NodeBounded(T data, NodeBounded<T> next) {
        this.data = data;
        this.next = next;
    }

    public NodeBounded() {
    }

    public T getData() {
        return data;
    }
}

class ExampleConcrete implements Comparable<ExampleConcrete>, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public int compareTo(ExampleConcrete arg0) {
        return 0;
    }

}

class NodeTypeErasure<T> {

    public T data;

    public NodeTypeErasure(T data) {
        this.data = data;
    }

    public void setData(T data) {
        System.out.println("Node.setData");
        this.data = data;
    }
}

class MyNodeTypeErasure extends NodeTypeErasure<Integer> {
    public MyNodeTypeErasure(Integer data) {
        super(data);
    }

    public void setData(Integer data) {
        System.out.println("MyNode.setData");
        super.setData(data);
    }
}

class ArrayBuilder {

    public static <T> void addToList(List<T> listArg, T... elements) {
        for (T x : elements) {
            listArg.add(x);
        }
    }

    public static void faultyMethod(List<String>... l) {
        Object[] objectArray = l; // Valid
        objectArray[0] = Arrays.asList(42);
        String s = l[0].get(0); // ClassCastException thrown here
        // Si le quita la declaración de String, no hay ClassCastException
    }

}