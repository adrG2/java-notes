# La directiva Static en Java

## ¿Qué es static en Java?

Es una keyword reservada que sirve para declarar una clase, método o campo como estático, el cuál puede ser accedido o invocado sin la necesidad de tener que intanciar un objeto de la clase. 

--- 

## Campo estático

Los campos de una clase declarados como estáticos son inicializados en el momento en que se carga la clase en memoria(ClassLoader), respetando el orden de la declaración. Los campos estáticos no pueden ser accedidos desde un contexto no estático, lo cuál provocaría un error en tiempo de ejecución. 


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


#### Constante de clase:

El modifier final indica que el valor de este campo no puede cambiar. Las constantes no pueden ser reasignadas, y lanzará un error en tiempo de ejecución si se intenta. 

    ```Java
    public class Book {
        private static final string MATERIAL = "paper";
        static final double PI = 3.141592653589793;
    }
    ```

Si una constante es definida como constante y el valor se conoce en tiempo de compilación, el compilador reemplaza el nombre de la constante por todo el código por ese valor. A esto se le conoce como constante en tiempo de compilación. Si el valor de la constante cambia por cambios externos pues tendrás que recompilar todas las clases que usen esta constante para poder tener el nuevo valor. 

--- 

## Métodos estáticos

Debido a que los métodos estáticos son enlazados en tiempo de compilación usando la información del tipo mediante static binding, no es posible realizar override de métodos.

Otras de las consecuencias del static binding es que los métodos estáticos no pueden ser declarados como abstract, por el mismo motivo que la imposibilidad de la sobrecarga de métodos.

Uno de las características que produce más dolores de cabeza al tratar con métodos y variables estáticas es que, debido a su naturaleza, no son seguras para la programación con hilos. 
Para profundizar acerca de esto: https://www.programcreek.com/2014/02/how-to-make-a-method-thread-safe-in-java/

--- 


## Clases y bloques estáticos

Las clases anidadas estáticas aumentan la cohesión de la clase que las engloba. Hay que tener en cuenta que el compilador crea un fichero .class por cada una de estas clases. 

Tal y cómo pasa con los métodos y variables estáticos, una clase anidada estática está asociada con su clase externa. Y tal y como pasa con los métodos de clase estáticos, una clase anidada estática no puede referirse directamente a variables de instancia o método definidos en su clase adjunta: puede usarlos sólo a través de una referencia de objeto.

Para crear un objeto de una clase anidada estática:
``` Java
OuterClass.StaticNestedClass nestedObject = new OuterClass.StaticNestedClass();
```




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

Podemos inicializar campos con un valor inicial para variables de clase estáticas:
```Java
public class BedAndBreakfast {

    // initialize to 10
    public static int capacity = 10;

    // initialize to false
    private boolean full = false;
}
```

Esto es útil cuando el valor está disponible y la inicialización puede ser puesta en una línea. Sin embargo, esto tiene ciertas limitaciones a causa de su simplicidad. Si la inicialización require de algo de lógica, por ejemplo, manejo de error o un bucle for para rellenar un array, la asignación simple es inadecuada. Las variables de instancia pueden ser inicializadas en el constructor, dónde el manejo de error u otra lógica puede ser usada. Para este mismo fin también podemos usar los "static initialization blocks".  

Los static initialization blocks son bloques de código que son ejecutados cuando se carga la clase(ClassLoader). Si no se declara un bloque de este tipo de forma explícita, el compilador Just-in-Time combina todos los campos estáticos en un bloque y los ejecuta durante la carga de clases. 

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

Una clase puede tener cualquier número de static initialization blocks, y pueden aparecer en cualquier lado del body de la clase. El sistema en runtime garantiza que los static initialization blocks serán llamados en el orden en que aparecen en el código fuente.

Hay una alternativa a los static blocks, un private static method:

```Java
class Whatever {
    public static varType myVar = initializeClassVariable();
        
    private static varType initializeClassVariable() {

        // initialization code goes here
    }
}
```

La ventaja de usar un método privado estático es que puede ser reutilizado más tarde si necesitas reinicializar la variable de clase.

--- 


## Imports estáticos

Una de las características incluidas en Java 5 fué la capacidad de importar los métodos y variables estáticas de un módulo y acceder a ellos como si hubieran sido declarados en la propia clase. Es especialmente útil, y mejora la legibilidad, cuando se están definiendo test unitarios, ya que la mayoría de los métodos de aserción de JUnit son estáticos.


---  

## Bibliografía
https://www.adictosaltrabajo.com/2015/09/17/la-directiva-static-en-java/
https://docs.oracle.com/javase/tutorial/java/javaOO/classvars.html
https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html