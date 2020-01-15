# Clases

## Declaring classes

Esto es una declaración de clase:

```Java
class MyClass {
    //fields
    //constructor
    //method declaration
}
```
El body de la clase contiene todo el código que proporciona el ciclo de vida de los objetos creados a partir de la clase: los contructores para inicializar nuevos objetos, declaraciones de los campos que proveen el estado de la clases y de los objetos, y métodos que implementan el comportamiento de la clase y los objetos. 

    1. Modificadores de visibilidad como public y private, los cuáles determinan qué clases pueden acceder a tu clase. 
    2. Nombre de clase con la primera letra en mayúscula.
    3. El nombre de la clase padre(superclass) precedida por keyword extends. Una clase sólo puede extends un padre(superclass).
    4. Interfaces separadas por coma con keyword implements. 
    5. El body de la calse entre {}.

Ejemplo: class MyClass extends MySuperClass implements YourInterface {}

MyClass sería una subclase de MySuperClass e implementa la interfaz YourInterface.

--- 

## Declaring member variables

Tipos de variables:

    1. Fields. Variables miembro de clase.
    2. Local Variables. Variables en un método o bloque de código. 
    3. Parameters. Variables en declaraciones de método.

Semántica:

    1. Cero o más modificadores de acceso como public o privado.
    2. El tipo del campo.
    3. El nombre del campo.


### Access modifiers

Para seguir el principio de encapsulación los fields se ponen a private. Lo cuál implica que sólo pueden ser accedidos directamente desde la clase dónde se declaran. Si queremos acceder a estos valores lo haremos con un método accesor(getter). 

### Types

Toda variable tiene que tener un tipo. Pueden ser tipos primitivos(int, float, boolean) o referencias de tipo(string, array, objects).

### Variables Names

Se usará convención camelcase. 

Ejemplo: 
    
    private String redHouse;


--- 

## Defining methods

Ejemplo de declaración de método:



## Providing constructors for your classes

## Passing information to a Method or a Constructor

--- 

# Objects

## Creating objects

## Using objects

--- 

# More on Classes

## Returning a value from a Method

## Using te THIS keyword

## Controlling access to members of a class

## Understanding class members

## Initializing fields

## Summary of creating and using classes and objects

--- 


# Nested classes

Java permite definir una clase dentro de otra clase, clases anidadas(nested class).

```Java
class OuterClass {
    ...
    class NestedClass {
        ...
    }
}
```

Las clases anidadas puedes ser estáticas(static) y no estáticas(clases internas/inner classes). 

```Java
class OuterClass {
    ...
    static class StaticNestedClass {
        ...
    }
    class InnerClass {
        ...
    }
}
```

Una clase anidada es miembro de la clase que la envuelve. Las clases internas(non-static) tienen acceso a otros miembros de la clase que la envuelve, incluso si son privados. Las clases anidadas estáticas no tienen acceso a otro miembros de la clase que la envuelve. Una clase anidada, al ser un miembro de una clase que la envuelve, puede ser declarada private, public, protected o package private. En cambio, recuerda que una clase normal sólo puede ser declarada pública o package private(sin public).

### Why Use Nested Classes?

    1. Es una forma de agrupar por lógica clases que sólo se usan en un lugar. Si una clase que solamente se usa en una clase, es lógico que esté embebida en esa clase y mantener a ambas juntas. Clases como las "helper classes" hacen los paquetes más agiles. 
    2. Aumenta encapsulación. Supongamos que tenemos dos clases, A y B, donde B necesita acceder a miembros de A. Al esconder a la clase B dentro de A, los miembros de A pueden ser declarados privados y B puede acceder a ellos. A su vez, B estaría oculto de cara al exterior. 
    3. Puede llevar a un código más fácil de leer y mantener. Anidar clases pequeñas dentro de clases de nivel superior ayuda a encapsular código a dónde se usa. 


### Static Nested Classes

Está asociada con su clase externa que la envuelve. Y como los métodos estáticos de clase, una clase anidada estática no puede referirse directamente a variables de instancia o métodos definidos en la clase que la envuelve(puede usarlos solo a través de una referencia de objeto). 

Una clase anidada estática interactúa con los miembros de la instancia de su clase externa (y otras clases) como cualquier otra clase de nivel superior. Una clase anidada estática es conductualmente una clase de nivel superior que se ha anidado en otra clase de nivel superior por conveniencia de empaquetado.

```Java
OuterClass.StaticNestedClass nestedObject =
     new OuterClass.StaticNestedClass();
```

### Inner Classes

Como con los métodos y variables de instancia, una clase interna está asociada con una instancia de la clase que la envuelve y tiene acceso directo a los métodos y campos de ese objeto. Además, dado que una clase interna está asociada con una instancia, no puede definir ningún miembro estático. 

```Java
class OuterClass {
    ...
    class InnerClass {
        ...
    }
}
```

Una instancia de InnerClass sólo puede existir dentro de OuterClass y tiene acceso directo a los métodos y campos de la instancia que lo envuelve. 

Para instanciar una clase interna, primero debes instanciar la clase externa. Entonces, crea el objeto interno dentro del objeto externo:

```Java
OuterClass outerObject = new OuterClass();
OuterClass.InnerClass innerObject = outerObject.new InnerClass();
```

Hay dos tipos especiales de clases internas: clases locales y anónimas.


### Shadowing

Si una declaración de un tipo en un particular scope tiene el mismo nombre que otra declaración en el alcance que lo envuelve, entonces la declaración shadows la declaración del ámbito que lo envuelve. 

En el ejemplo se entiende mejor:

```Java
public class ShadowTest {

    public int x = 0;

    class FirstLevel {

        public int x = 1;

        void methodInFirstLevel(int x) {
            System.out.println("x = " + x);
            System.out.println("this.x = " + this.x);
            System.out.println("ShadowTest.this.x = " + ShadowTest.this.x);
        }
    }

    public static void main(String... args) {
        ShadowTest st = new ShadowTest();
        ShadowTest.FirstLevel fl = st.new FirstLevel();
        fl.methodInFirstLevel(23);
    }
}```

Output:

```Java
x = 23
this.x = 1
ShadowTest.this.x = 0
```


### Serialization

La serialización de clases internas(local y anónimas incluidas) está fuertemente desaconsejado. Cuando el compilador compila ciertas construcciones, tales como clases internas, crea synthetic constructs; hay clases, métodos, campos y otras construcciones que no tiene una construcción correspondiente en el código fuente. Las synthetic constructs habilitan al compilador de Java para implementar nuevas features en el lenguaje sin cambiar la JVM. Sin embargo, las construcciones sintéticas pueden variar entre diferentes implementaciones del compilador de Java, lo que significa que los archivos .class también pueden variar entre diferentes implementaciones.

Por ello, puede tener problemas de compatibilidad si serializas una clase interna y la deserializas con una implementación diferente del JRE. 


--- 

## Inner Class Example


```Java
public class DataStructure {
    
    // Create an array
    private final static int SIZE = 15;
    private int[] arrayOfInts = new int[SIZE];
    
    public DataStructure() {
        // fill the array with ascending integer values
        for (int i = 0; i < SIZE; i++) {
            arrayOfInts[i] = i;
        }
    }
    
    public void printEven() {
        
        // Print out values of even indices of the array
        DataStructureIterator iterator = this.new EvenIterator();
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " ");
        }
        System.out.println();
    }
    
    interface DataStructureIterator extends java.util.Iterator<Integer> { } 

    // Inner class implements the DataStructureIterator interface,
    // which extends the Iterator<Integer> interface
    
    private class EvenIterator implements DataStructureIterator {
        
        // Start stepping through the array from the beginning
        private int nextIndex = 0;
        
        public boolean hasNext() {
            
            // Check if the current element is the last in the array
            return (nextIndex <= SIZE - 1);
        }        
        
        public Integer next() {
            
            // Record a value of an even index of the array
            Integer retValue = Integer.valueOf(arrayOfInts[nextIndex]);
            
            // Get the next even element
            nextIndex += 2;
            return retValue;
        }
    }
    
    public static void main(String s[]) {
        
        // Fill the array with integer values and print out only
        // values of even indices
        DataStructure ds = new DataStructure();
        ds.printEven();
    }
}
```

El output es:

    0 2 4 6 8 10 12 14 


Date cuenta de que la clase EvenIterator se refiere directamente a la variable de instancia arrayOfInts del objeto DataStructure.

Puedes usar clases internas para implementar helper classes como las que se ven en este ejemplo. 


## Local Classes

Son clases que se definen en un bloque, dentro de un grupo de cero o más declaraciones entre {}. Normalmente se encuentran en el cuerpo de un método. 


### Declaring local classes

Puedes hacerlo en cualquier bloque. Por ejemplo, puedes definir una clase local en el body de un método, una bucle for o en un claúsula if. 

Ejemplo:

```Java
public class LocalClassExample {
  
    static String regularExpression = "[^0-9]";
  
    public static void validatePhoneNumber(
        String phoneNumber1, String phoneNumber2) {
      
        final int numberLength = 10;
        
        // Valid in JDK 8 and later:
       
        // int numberLength = 10;
       
        class PhoneNumber {
            
            String formattedPhoneNumber = null;

            PhoneNumber(String phoneNumber){
                // numberLength = 7;
                String currentNumber = phoneNumber.replaceAll(
                  regularExpression, "");
                if (currentNumber.length() == numberLength)
                    formattedPhoneNumber = currentNumber;
                else
                    formattedPhoneNumber = null;
            }

            public String getNumber() {
                return formattedPhoneNumber;
            }
            
            // Valid in JDK 8 and later:

//            public void printOriginalNumbers() {
//                System.out.println("Original numbers are " + phoneNumber1 +
//                    " and " + phoneNumber2);
//            }
        }

        PhoneNumber myNumber1 = new PhoneNumber(phoneNumber1);
        PhoneNumber myNumber2 = new PhoneNumber(phoneNumber2);
        
        // Valid in JDK 8 and later:

//        myNumber1.printOriginalNumbers();

        if (myNumber1.getNumber() == null) 
            System.out.println("First number is invalid");
        else
            System.out.println("First number is " + myNumber1.getNumber());
        if (myNumber2.getNumber() == null)
            System.out.println("Second number is invalid");
        else
            System.out.println("Second number is " + myNumber2.getNumber());

    }

    public static void main(String... args) {
        validatePhoneNumber("123-456-7890", "456-7890");
    }
}
```


### Accessing Members of an Enclosing Class

Una clase local tiene acceso a los miembros de la clase que la envuelve. 

Una clase local tiene acceso a variables locales. Sin embargo, una clase local sólo puede acceder a variables locales que sean declaradas como final. Cuando una clase local accede a una variable local o parámetro del bloque que la envuelve, captura esa variable o parámetro. 

Sin embargo, en Java 8, una clase local puede acceder a variables locales y parámetros del bloque que la envuelve ya sean final o un "final efectivo". Una variable o parámetro cuyo valor nunca cambia tras ser inicializado es "final efectivo".

Declaraciones de un tipo(como una variable) en una clase local sombrea(shadow) las declaraciones en el scope(ámbito) que lo envuelve cuando tienen el mismo nombre. 


### Local Classes are similar to inner classes

Las clases locales son similares a las clases internas en que no pueden definir o declarar ningún miembro estático. Las clases locales en métodos estáticos, pueden unicamente referirse a miembros estáticos de la clase que lo envuelve(enclosing class). 

Las clases locales no son estáticas porque ella tienen acceso a miembros de instancia del bloque que lo envuelve. Por tanto, no pueden contener la mayoría de los tipos de declaraciones estáticas. 

No se puede declarar una interface dentro de un block; las interfaces son inherentemente estáticas. 

Tampoco puedes declarar inicializadores estáticos o miembros de interfaces en una clase local. Por ejemplo, esto no compilaría: 

```Java
public void sayGoodbyeInEnglish() {
        class EnglishGoodbye {
            public static void sayGoodbye() {
                System.out.println("Bye bye");
            }
        }
        EnglishGoodbye.sayGoodbye();
    }
```

Una clase local puede tener miembros estáticos siempre que sean variables constantes.
Ejemplo: `private static final int PI;`


--- 

## Anonymous Classes

Las clases anónimas te permiten hacer tu código más conciso. Le permiten declarar e instanciar una clase al mismo tiempo. Son como las clases locales, excepto que no tienen un nombres. Se usan cuando necesitas una clase local sólo una vez.


### Declaring anonymous classes

En el siguiente ejemplo, la clase HelloWorldAnonymousClasses usa claúsulas anónima en las declaraciones de inicialización de las variables locales `frenchGreeting` y `spanishGreeting`, pero usa una clase local para la inicialización de la variable `englishGreeting`.


```Java
public class HelloWorldAnonymousClasses {
  
    interface HelloWorld {
        public void greet();
        public void greetSomeone(String someone);
    }
  
    public void sayHello() {
        
        class EnglishGreeting implements HelloWorld {
            String name = "world";
            public void greet() {
                greetSomeone("world");
            }
            public void greetSomeone(String someone) {
                name = someone;
                System.out.println("Hello " + name);
            }
        }
      
        HelloWorld englishGreeting = new EnglishGreeting();
        
        HelloWorld frenchGreeting = new HelloWorld() {
            String name = "tout le monde";
            public void greet() {
                greetSomeone("tout le monde");
            }
            public void greetSomeone(String someone) {
                name = someone;
                System.out.println("Salut " + name);
            }
        };
        
        HelloWorld spanishGreeting = new HelloWorld() {
            String name = "mundo";
            public void greet() {
                greetSomeone("mundo");
            }
            public void greetSomeone(String someone) {
                name = someone;
                System.out.println("Hola, " + name);
            }
        };
        englishGreeting.greet();
        frenchGreeting.greetSomeone("Fred");
        spanishGreeting.greet();
    }

    public static void main(String... args) {
        HelloWorldAnonymousClasses myApp =
            new HelloWorldAnonymousClasses();
        myApp.sayHello();
    }            
}
```

### Syntax of anonymous classes

La sintaxis de la expresión de una clase anónima es como la invocación de un constructor, excepto que hay una definición de clase contenida en un bloque de código.

```Java
HelloWorld frenchGreeting = new HelloWorld() {
    String name = "tout le monde";
    public void greet() {
        greetSomeone("tout le monde");
    }
    public void greetSomeone(String someone) {
        name = someone;
        System.out.println("Salut " + name);
    }
};
```

La expresión de una clase anónima consisten en:

    1. El operador new
    2. El nombre de una interface que implemente o una clase que extend. En el ejemplo de arriba la clase anónima implementa la interface HelloWorld.
    3. Los paréntesis que contienen los argumentos para un constructor, al igual que una expresión de creación de instancia de clase normal(Cuando implementas una interface no hay constructor, así que usas un par de paréntesis vacíos como en el ejemplo).
    4. Un body, el cuál es una body de declaración de clase. En el body, las declaraciones de métodos están permitidos pero las statements no. 

A causa de que la definición de una clase anónima es una expresión, debe ser parte de un statement. En este ejemplo, la expresión de clase anónima es parte del statement que instancia el frenchGreeting object(Fíjate en el ';' del final). 


### Accessing Local Variables of the Enclosing Scope, and Declaring and Accessing Members of the Anonymous Class

Las clases anónimas pueden capturar variables; tienen el mismo acceso a las variables locales del enclosing scope: 

    * Una clase anónima tiene acceso a los miembros de la clase envolvente.
    * Una clase anónima no puede acceder a variables locales en su ámbito de inclusión(enclosing scope) que no se declaran como final.
    * Al igual que en las clases anidadas, una declaración de un tipo en una clase anónima sombrea(shadow) cualquier otras declaraciones en el enclosing scope que tengan el mismo nombre. 

Restricciones de las clases anónimas:

    * No puedes declarar inicializadores estáticos o miembros de interfaces.
    * Puede tener miembros estáticos en forma de variables constantes.

Puedes declarar lo siguiente en una clase anónima:

    * Fields
    * Extra methods(incluso si no implementan métodos del superType)
    * Inicializadores de instancia
    * Clases locales

Sin embargo, no puedes declarar constructores en una clase anónima.


### Examples of anonymous classes

```Java
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
 
public class HelloWorld extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hello World!");
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });
        
        StackPane root = new StackPane();
        root.getChildren().add(btn);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }
}
```

Ya que la interface EventHandler<ActionEvent> tiene un único método, puedes usar una expresión lambda en vez de una clase anónima. 

Las clases anónimas son ideales para implementar interfaces con dos o más métodos.  


```Java
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class CustomTextFieldSample extends Application {
    
    final static Label label = new Label();
 
    @Override
    public void start(Stage stage) {
        Group root = new Group();
        Scene scene = new Scene(root, 300, 150);
        stage.setScene(scene);
        stage.setTitle("Text Field Sample");
 
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);
 
        scene.setRoot(grid);
        final Label dollar = new Label("$");
        GridPane.setConstraints(dollar, 0, 0);
        grid.getChildren().add(dollar);
        
        final TextField sum = new TextField() {
            @Override
            public void replaceText(int start, int end, String text) {
                if (!text.matches("[a-z, A-Z]")) {
                    super.replaceText(start, end, text);                     
                }
                label.setText("Enter a numeric value");
            }
 
            @Override
            public void replaceSelection(String text) {
                if (!text.matches("[a-z, A-Z]")) {
                    super.replaceSelection(text);
                }
            }
        };
 
        sum.setPromptText("Enter the total");
        sum.setPrefColumnCount(10);
        GridPane.setConstraints(sum, 1, 0);
        grid.getChildren().add(sum);
        
        Button submit = new Button("Submit");
        GridPane.setConstraints(submit, 2, 0);
        grid.getChildren().add(submit);
        
        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                label.setText(null);
            }
        });
        
        GridPane.setConstraints(label, 0, 1);
        GridPane.setColumnSpan(label, 3);
        grid.getChildren().add(label);
        
        scene.setRoot(grid);
        stage.show();
    }
 
    public static void main(String[] args) {
        launch(args);
    }
}
```

--- 

## Lambda Expressions

Un problema con las clases anónimas es que si la implementación de su clase anónima es muy simple, como una interface que contiene un sólo método, la sintaxis de las clases anónimas puede parecer difícil de manejar y poco clara. En estos casos, generalmente estás intentando pasar la funcionalidad como argumento a otro método, como por ejemplo, qué acción se debe tomar cuando alguien hace clic en un botón. Las expresiones lambda te permiten hacer esto, tratar la funcionalidad como argumento de método o el código como datos.


### Ideal Use Case for Lambda Expressions





# Bibliography

https://docs.oracle.com/javase/tutorial/java/javaOO/index.html