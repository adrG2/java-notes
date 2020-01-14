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

Debido a que los métodos estáticos son enlazados en tiempo de compilación mediante static binding usando la información del tipo, no es posible realizar override de métodos.




## Bibliografía
https://www.adictosaltrabajo.com/2015/09/17/la-directiva-static-en-java/