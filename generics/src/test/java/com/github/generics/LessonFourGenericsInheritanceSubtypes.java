package com.github.generics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.github.generics.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class LessonFourGenericsInheritanceSubtypes {

    @Test
    public void checkSubtypesInGenerics() {
        // ERROR compile-time
        // BoxTest.check(new BoxGeneric<Double>(Double.valueOf(6.55)));
        // ERROR compile-time
        // BoxTest.check(new BoxGeneric<Integer>(Integer.valueOf(6)));
        PayloadList<String, Integer> pay = new PayloadListImpl<>();
    }

}

class BoxTest {
    public static void check(BoxGeneric<Number> n) {

    }
}

interface PayloadList<E, P> extends List<E> {
    void setPayload(int index, P val);
}

class PayloadListImpl<E, P> implements PayloadList<E, P> {

    public PayloadListImpl() {

    }

    @Override
    public boolean add(E arg0) {

        return false;
    }

    @Override
    public void add(int arg0, E arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean addAll(Collection<? extends E> arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean addAll(int arg0, Collection<? extends E> arg1) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean contains(Object arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public E get(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int indexOf(Object arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isEmpty() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int lastIndexOf(Object arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public ListIterator<E> listIterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ListIterator<E> listIterator(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean remove(Object arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public E remove(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean removeAll(Collection<?> arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public E set(int arg0, E arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int size() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<E> subList(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object[] toArray() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T[] toArray(T[] arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setPayload(int index, P val) {
        // TODO Auto-generated method stub

    }

}