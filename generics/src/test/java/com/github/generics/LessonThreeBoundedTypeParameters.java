package com.github.generics;

import java.util.Objects;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class LessonThreeBoundedTypeParameters {

    Logger logger = LoggerFactory.getLogger(LessonOneGenericTypes.class);

    @Test
    public void boundedTypeParameter() {
        BoxBounded<Integer> integerBox = new BoxBounded<Integer>();
        integerBox.set(Integer.valueOf(10));
        // integerBox.inspect("String permitido"); // error: this is still String!

        assertThat(integerBox.get(), is(10));
    }

    @Test
    public void personBoundedType() {
        Person teen = new Teenager("Anton", 15);
        Worker<Person> personWorker = new Worker<>(teen);
        assertThat(teen, instanceOf(Teenager.class));
        assertThat(teen, instanceOf(Person.class));
        assertFalse(personWorker.isAdult(teen));

        Person adult = new Adult("Miguel", 30);
        assertTrue(personWorker.isAdult(adult));
    }

    @Test
    public void countGreaterThan() {
        int one = BoxBounded.countGreaterThan(Arrays.array(1, 3, 5), 3);
        assertThat(one, is(1));
        int all = BoxBounded.countGreaterThan(Arrays.array(1, 3, 5), -1);
        assertThat(all, is(3));
    }

    @Test
    public void comparableMethodPerson() {

        Person antonio = new Adult("Antonio", 30);
        Person miguel = new Adult("Miguel", 36);
        Person juan = new Teenager("Juan", 16);
        Person[] persons = Arrays.array(antonio, juan);

        // Esto sirve porque <S extends Comparable<S>>
        // Lo cual está diciendo que acepta cualquier tipo que implemente la interfaz.
        // Person implements Comparable<Person>
        assertThat(BoxBounded.countGreaterThan(persons, miguel), is(1));
    }

}

class BoxBounded<T> {

    private T t;

    public void set(T t) {
        this.t = t;
    }

    public T get() {
        return t;
    }

    public static <S extends Comparable<S>> int countGreaterThan(S[] anArray, S elem) {

        int count = 0;
        for (S e : anArray)
            if (e.compareTo(elem) > 0)
                ++count;
        return count;

    }

    public <U extends Number> void inspect(U u) {
        System.out.println("T: " + t.getClass().getName());
        System.out.println("U: " + u.getClass().getName());
    }
}

class Worker<T extends Person> {
    private T p;

    public Worker(T t) {
        this.p = t;
    }

    public boolean isAdult(T p) {
        return p instanceof Adult;
    }

    public Person getTypePerson() {
        return p;
    }
}

class Adult extends Person {

    public Adult(String name, int age) {
        super(name, age);
    }

}

class Teenager extends Person {

    public Teenager(String name, int age) {
        super(name, age);
    }

}

abstract class Person implements Comparable<Person> {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Person name(String name) {
        this.name = name;
        return this;
    }

    public Person age(int age) {
        this.age = age;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Person)) {
            return false;
        }
        Person person = (Person) o;
        return Objects.equals(name, person.name) && age == person.age;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }

    @Override
    public String toString() {
        return "{" + " name='" + getName() + "'" + ", age='" + getAge() + "'" + "}";
    }

    @Override
    public int compareTo(Person p) {
        return p.getName().equals(this.getName()) ? 0 : 1;
    }

}