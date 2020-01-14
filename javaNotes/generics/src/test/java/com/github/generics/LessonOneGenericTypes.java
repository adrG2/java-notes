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

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class LessonOneGenericTypes {

    Logger logger = LoggerFactory.getLogger(LessonOneGenericTypes.class);

    @Test
    public void forceRunTimeError() {
        final Box boxString = Box.createBox("Hola, tengo ");
        final Box boxInteger = Box.createBox(28); // Autoboxing -> int Cast to Integer.class
        final List<Box> boxes = List.of(boxString, boxInteger);
        // Clase Box no es safe type
        boxString.set(LocalDateTime.now()); // set() rompe inmutabilidad
        boxInteger.set(45.3445);

        assertThat(boxInteger, instanceOf(Box.class));
        assertThat(boxString, instanceOf(Box.class));

        try {
            final String s = (String) boxes.get(0).get();
        } catch (final ClassCastException ex) {
            logger.debug(String.format("Box access: %s", boxes.get(0).get()));
            logger.error(String.format("[Error] %s", ex.getMessage()));
        }
    }

    @Test
    public void useGenericTypeBox() {
        BoxGeneric<String> boxString = BoxGeneric.createBoxGeneric("Hola");
        BoxGeneric<Integer> boxInteger = BoxGeneric.createBoxGeneric(2);
        List<BoxGeneric<String>> boxes = List.of(boxString);
        // Compile time error si añadimos el boxInteger
        // boxes.add(boxInteger);

        List<BoxGeneric> boxesRawType = List.of(boxString, boxInteger); // No compile/runtime error
    }

    @Test
    public void multipleTypeParameters() {

        // Autoboxin. Tipo genérico sólo acepta objetos. Al pasarle un entero primitivo
        // hace una conversión a Integer.class
        int intPrimitiveType = 4;
        Pair<String, Integer> p1 = new OrderedPair<>("Even", intPrimitiveType);
        assertThat(p1.getValue(), instanceOf(Integer.class));

        Pair<String, String> p2 = new OrderedPair<>("hello", "world");

    }

    @Test
    public void parameterizedTypes() {
        OrderedPair<String, BoxGeneric<Integer>> p = new OrderedPair<>("primes", BoxGeneric.createBoxGeneric(5));
        assertThat(p.getKey(), is("primes"));
        assertThat(p.getValue(), instanceOf(BoxGeneric.class));
        assertThat(p.getValue().get(), is(5));
    }

    @Test
    public void rawTypeBox() {

        BoxGeneric<Integer> intBox = new BoxGeneric<>(5);
        BoxGeneric raw = new BoxGeneric();

        // Por compatibilidad con versiones con versiones anteriores a tipos genéricos
        BoxGeneric assignGenericTypeToRawType = intBox;

        // Este warning nos dice que no se está comprobando el tipo genérico y no está
        // capturando código inseguro en tiempo de ejecución
        BoxGeneric<Integer> assignRawTypeToGeneric = raw;
    }

    @Test
    public void uncheckedErrorMessages() {

    }

}

class Box {
    private Object object;

    public static Box createBox(final Object object) {
        return new Box(object);
    }

    private Box(final Object object) {
        this.object = object;
    }

    public Object get() {
        return object;
    }

    public void set(final Object o) {
        object = o;
    }
}

class BoxGeneric<T> {
    private T t;

    public static <T> BoxGeneric<T> createBoxGeneric(T t) {
        return new BoxGeneric<>(t);
    }

    public BoxGeneric() {

    }

    public BoxGeneric(T t) {
        this.t = t;
    }

    public void set(final T t) {
        this.t = t;
    }

    public T get() {
        return t;
    }

}

interface Pair<K, V> {
    public K getKey();

    public V getValue();
}

class OrderedPair<K, V> implements Pair<K, V> {

    private K key;
    private V value;

    public OrderedPair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}

class WarningDemo {
    public static void main(String[] args) {
        BoxGeneric<Integer> bi;
        bi = createBox();
    }

    static BoxGeneric createBox() {
        return new BoxGeneric();
    }
}