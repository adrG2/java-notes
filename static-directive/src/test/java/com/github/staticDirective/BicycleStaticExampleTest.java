package com.github.staticDirective;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.github.staticDirective.lib.Bicycle;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class BicycleStaticExampleTest {

    @Test
    public void checkIfChangeIdStaticField() {
        assertEquals(0, Bicycle.getNumberOfBicycles());
        Bicycle bike = new Bicycle(5, 2, 12);
        assertNotEquals(0, Bicycle.getNumberOfBicycles());
        assertEquals(1, Bicycle.getNumberOfBicycles());
        assertEquals(1, bike.getID());
        Bicycle secondBike = new Bicycle(5, 2, 12);
        assertNotEquals(1, secondBike.getID());
        // Esto es así porque el id es una variable de instancia de clase. Se inicializa
        // a 0 para cada instancia.
        assertEquals(1, bike.getID());
        assertEquals(2, Bicycle.getNumberOfBicycles());

    }

}