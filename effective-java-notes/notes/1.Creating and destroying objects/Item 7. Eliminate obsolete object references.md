# Item 7. Eliminate obsolete object references

Puede fácilmente dar la impresión de que no tienes que pensar en la gestión de la memoria, pero eso no es del todo cierto.

```Java
public class Stack {
    private Object[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public Stack() {
        elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(Object e) {
        ensureCapacity();
        elements[size++] = e;
    }

    public Object pop() {
        if (size == 0) {
            throw new EmptyStackException();
        }
        return elements[--size];
    }

    private void ensureCapacity() {
        if (elements.length == size) {
            elements = Arrays.copyOf(elements, 2 * size + 1);
        }
    }
}
```
Este código tiene un **memory leak**, el cuál puede manifestarse silenciosamente en forma de reducción del rendimiento debido al aumento de la actividad del recolector de basura o al aumento de la footprint de la memoria. En casos extremos, estos memory leaks pueden provocas paginación de disco e incluso fallo del programa con un **OutOfMemoryError**. 

¿Dónde está el memory leak? Si un Stack crece y luego se "encoge", los objetos que se quitaron(pop) del Stack no serán eliminados por el recolector de basura, incluso si el programa que usa el Stack no tiene más referencias de esos objetos. Una **referencia obsoleta** es simplemente una referencia que nunca será desreferenciada de nuevo. En este caso, cualquier referencia fuera de la parte "activa" del array será obsoleta. La parte activa del array son los elementos cuyo índice es menor que el size.

Si se conserva involuntariamente una referencia a un objeto, no sólo se excluye ese objeto del recolector de basura, sino que también se excluye cualquier objeto al que se haga referencia, y así sucesivamente. Aunque sólo se retengan involuntariamente unas pocas referencias de objetos, se puede evitar que muchos, muchos, objetos vayan al recolector de basura, lo que puede tener grandes efectos en el rendimiento.

El arreglo para este problema es simple: **poner las referencias nulas una vez que estén obsoletas**. En este ejemplo, la referencia a un item se convierte en obsoleta tan pronto como es "popped"(sacada) del stack. 

```Java
public Object pop() {
    if (size == 0) {
        throw new EmptyStackException();
    }
    Object result = elements[--size];
    elements[size] = null;
    return result;
}
```

Un beneficio adicional de esto es que si las referencias son desreferenciadas por error, el programa lanzará un NullPointerException. Siempre es beneficioso detectar los errores tan pronto como sea posible.

Cuando los programadores tiene este problema por primera vez, pueden tratar de sobrecompensarlo anulando cada referencia tan pronto como el programa termine de usarla. Esto es innecesario. 

**Hacer nula la referencia de un objeto debería ser la excepción más que la norma**. La mejor manera de elminar una referencia obsoleta es dejar que la variable que contiene la referencia quede fuera de scope. Esto se logra definiendo cada variable en el scope más estrecho posible. 

¿Cuando deberías poner las referencias a null? ¿Qué aspecto de la clase Stack la hace suceptible de memory leaks? En pocas palabras, maneja su propia memoria. El *storage pool* consiste de los elementos de los elementos del *array*(las referencias del objeto, no los objetos en sí). Los elementos de la parte activa del array están asignados, y los del resto están libres. El recolector de basura no tiene manera de saberlo; para el recolector de basura, todas las referencias en los elementos del array son igual de válidas. Solamente el programador sabe que las referencias de los elementos de la parte inactiva no son relevantes. El programador tiene que decírselo al recolector de forma manual poniendo a null las referencias de los elementos del array conforme van pasando a la parte inactiva(método pop). 

**Siempre que una clase gestiona su propia memoria, el programador debe estar alerta de las memory leaks(fugas de memoria)**. 

**Otra fuente común de memory leaks es la Caché**. Una vez que pones una referencia dentro de una caché, es fácil olvidarte de que está ahí y es posible que cuando ya sea irrelevante siga estando dentro de la caché. Si tienes la suerte de implementar una caché para la 
