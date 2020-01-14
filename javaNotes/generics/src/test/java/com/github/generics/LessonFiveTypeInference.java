package com.github.generics;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class LessonFiveTypeInference {

    @Test
    public void serializableExample() {
        assertThat(SerializableExample.pick("d", new ArrayList<>()), instanceOf(List.class));

        Serializable s = SerializableExample.pick("d", new ArrayList<>());
        assertThat(s, instanceOf(Serializable.class));
        assertThat(s, instanceOf(List.class));
    }

    @Test
    public void boxDemo() {
        java.util.ArrayList<BoxGeneric<Integer>> listOfIntegerBoxes = new java.util.ArrayList<>();

        // Si quitamos el <Integer> el algoritmo de inferencia toma el tipo de los
        // argumentos del método.
        BoxDemo.<Integer>addBox(Integer.valueOf(10), listOfIntegerBoxes);

        BoxDemo.addBox(Integer.valueOf(20), listOfIntegerBoxes);
        BoxDemo.addBox(Integer.valueOf(30), listOfIntegerBoxes);
        BoxDemo.outputBoxes(listOfIntegerBoxes);
    }

    @Test
    public void genericConstructorOfGenericClass() {
        MyClass<Integer> myObject = new MyClass<>("");
        System.out.println(myObject.getClass());
        // Class<?> genericType = MyClass.findSuperClassParameterType(myObject,
        // MyClass.class, 0);
        // assert genericType == String.class;
    }

}

class SerializableExample {
    static <T> T pick(T a1, T a2) {
        return a2;
    }
}

class BoxDemo {

    public static <U> void addBox(U u, java.util.List<BoxGeneric<U>> boxes) {
        BoxGeneric<U> box = new BoxGeneric<>();
        box.set(u);
        boxes.add(box);
    }

    public static <U> void outputBoxes(java.util.List<BoxGeneric<U>> boxes) {
        int counter = 0;
        for (BoxGeneric<U> box : boxes) {
            U boxContents = box.get();
            System.out.println("Box #" + counter + " contains [" + boxContents.toString() + "]");
            counter++;
        }
    }
}

class MyClass<X> {

    <T> MyClass(T t) {
        System.out.println(t.getClass());
    }

    public static Class<?> findSuperClassParameterType(Object instance, Class<?> classOfInterest, int parameterIndex) {
        Class<?> subClass = instance.getClass();
        ParameterizedType parameterizedType = (ParameterizedType) subClass.getGenericSuperclass();
        return (Class<?>) parameterizedType.getActualTypeArguments()[parameterIndex];
    }

}