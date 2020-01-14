# La directiva Static en Java

## ¿Qué es static en Java?

Es una keyword reservada que sirve para declarar una clase, método o campo como estático, el cuál puede ser accedido o invocado sin la necesidad de tener que intanciar un objeto de la clase. 

--- 

## Campo estático

Los campos de una clase declarados como estáticos son inicializados en el momento en que se carga la clase en memoria(ClassLoader), respetando el orden de la declaración. Los campos estáticos no pueden ser accedidos desde un contexto no estático, lo cuál provocaría un error en tiempo de ejecución. 

Constante de clase:

    ```Java
    public class Book {
        private static final string MATERIAL = "paper";
    }
    ```

Patrón Singleton usando campo estático para guardar una referencia de la instancia:

    ```Java
    public class SingletonTest {
 
        private static SingletonTest instance = null;
    
        protected SingletonTest() {
        }
    
        public static SingletonTest getInstance() {
            if (instance == null) {
                instance = new SingletonTest();
            }
    
            return instance;
        }
    }
    ```

Los campos estáticos no son serializados, por tanto, durante el proceso de deserialización, tomarán su valor por defecto(null para objetos, 0.0 para float, etc).

--- 

## Métodos estáticos

Debido a que los métodos estáticos son enlazados en tiempo de compilación usando la información del tipo mediante static binding, no es posible realizar override de métodos.

Otras de las consecuencias del static binding es que los métodos estáticos no pueden ser declarados como abstract, por el mismo motivo que la imposibilidad de la sobrecarga de métodos.

Uno de las características que produce más dolores de cabeza al tratar con métodos y variables estáticas es que, debido a su naturaleza, no son seguras para la programación con hilos. 
Para tratar más información acerca de esto: https://www.programcreek.com/2014/02/how-to-make-a-method-thread-safe-in-java/

--- 


## Clases y bloques estáticos

Las clases internas pueden ser declaradas estáticas, de tal manera que aumenten la cohesión de la clase que las engloba. Hay que tener en cuenta que, como el resto de clases internas, el compilador crea un fichero .class por cada una de estas clases. 


### Ejemplo clase interna estática

```Java
public class Objeto {
 
    private int campo1;
    private String campo2;
 
    public Objeto(int campo1, String campo2) {
        this.campo1 = campo1;
        this.campo2 = campo2;
    }
 
    private static class ObjetoComparator implements Comparator<Objeto> {
 
        @Override
        public int compare(Objeto o1, Objeto o2) {
            return o2.campo1 - o1.campo1;
        }
 
    }
}
```

### Static initializer block

Son bloques de código que son ejecutados cuando se carga la clase(ClassLoader). Si no se declara un bloque de este tipo de forma explícita, el compilador Just-in-Time combina todos los campos estáticos en un bloque y los ejecuta durante la carga de clases. 

```Java
public class Objeto {

    private static int campo1;

    static {
        campo1 = 10;
    }
}

public class Objeto {

    private static int campo1 = inicializaCampo();

    private static int inicializaCampo() {
        return 10;
    }

}
```

Esto nos permite inicializar variables de clase estáticas sin necesidad de incluirlo en su constructor. 


--- 


## Imports estáticos

Una de las características incluidas en Java 5 fué la capacidad de importar los métodos y variables estáticas de un módulo y acceder a ellos como si hubieran sido declarados en la propia clase. Es especialmente útil, y mejora la legibilidad, cuando se están definiendo test unitarios, ya que la mayoría de los métodos de aserción de JUnit son estáticos.


---  

## Bibliografía
https://www.adictosaltrabajo.com/2015/09/17/la-directiva-static-en-java/