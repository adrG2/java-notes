# Genéricos en Java

# Tabla de contenidos

- [Genéricos en Java](#gen%c3%a9ricos-en-java)
- [Tabla de contenidos](#tabla-de-contenidos)
  - [Type Parameter Naming Conventions](#type-parameter-naming-conventions)
  - [Type parameter y Type Argument no son lo mismo](#type-parameter-y-type-argument-no-son-lo-mismo)
  - [Multiple type parameters](#multiple-type-parameters)
  - [Parameterized types](#parameterized-types)
  - [Unchecked Error Messages](#unchecked-error-messages)
  - [Generic Methods](#generic-methods)
  - [Bounded type parameters](#bounded-type-parameters)
  - [Multiple Bounds](#multiple-bounds)
  - [Generics, inheritance and subtypes](#generics-inheritance-and-subtypes)
  - [Type Inference](#type-inference)
    - [Type Inference and Generic Methods](#type-inference-and-generic-methods)
    - [Type Inference and Instantiation of Generic Classes](#type-inference-and-instantiation-of-generic-classes)
    - [Type Inference and Generic Constructors of Generic and Non-Generic Classes](#type-inference-and-generic-constructors-of-generic-and-non-generic-classes)
    - [Target Types](#target-types)
  - [Wildcards](#wildcards)
    - [Upper Bounded Wildcards](#upper-bounded-wildcards)
    - [Unbounded Wildcards](#unbounded-wildcards)
    - [Upper Bounded Wildcards](#upper-bounded-wildcards-1)
    - [Wildcards and Subtyping](#wildcards-and-subtyping)
    - [Wildcard Capture and Helper Methods](#wildcard-capture-and-helper-methods)
      - [¿Cómo solucinar este problema?](#%c2%bfc%c3%b3mo-solucinar-este-problema)
    - [Guidelines for Wildcard Use](#guidelines-for-wildcard-use)
        - [WildCard Guidelines](#wildcard-guidelines)
  - [Type Erasure](#type-erasure)
    - [Erasure of generic types](#erasure-of-generic-types)
        - [Ejemplo unbounded](#ejemplo-unbounded)
        - [Ejemplo bounded](#ejemplo-bounded)
    - [Erasure of Generic Methods](#erasure-of-generic-methods)
    - [Effects of Type Erasure and Bridge Methods](#effects-of-type-erasure-and-bridge-methods)
    - [Bridge Methods](#bridge-methods)
  - [Non-Reifiable Types](#non-reifiable-types)
    - [Non-Reifiable Types](#non-reifiable-types-1)
    - [Heap Pollution](#heap-pollution)
    - [Potential Vulnerabilities of Varargs Methods with Non-Reifiable Formal Parameters](#potential-vulnerabilities-of-varargs-methods-with-non-reifiable-formal-parameters)
    - [Prevent Warnings from Varargs Methods with Non-Reifiable Formal Parameters](#prevent-warnings-from-varargs-methods-with-non-reifiable-formal-parameters)
  - [Restrictions on Generics](#restrictions-on-generics)
    - [Cannot instatiate Generic Types with Primitve types](#cannot-instatiate-generic-types-with-primitve-types)
    - [Cannot Create Instances of Type Parameters](#cannot-create-instances-of-type-parameters)
    - [Cannot Declare Static Fields Whose Types are Type Parameters](#cannot-declare-static-fields-whose-types-are-type-parameters)
    - [Cannot Use Casts or instanceof with Parameterized Types](#cannot-use-casts-or-instanceof-with-parameterized-types)
    - [Cannot Create Arrays of Parameterized Types](#cannot-create-arrays-of-parameterized-types)
    - [Cannot Create, Catch, or Throw Objects of Parameterized Types](#cannot-create-catch-or-throw-objects-of-parameterized-types)
    - [Cannot Overload a Method Where the Formal Parameter Types of Each Overload Erase to the Same Raw Type](#cannot-overload-a-method-where-the-formal-parameter-types-of-each-overload-erase-to-the-same-raw-type)
  - [Bibliografía](#bibliograf%c3%ada)
    
    
    


## Type Parameter Naming Conventions 

    1. E - Element
    2. K - Key
    3. N - Number
    4. T - Type
    5. V - Value
    6. S, U, V - types

## Type parameter y Type Argument no son lo mismo 

Cuando codificamos, proporcionamos argumentos de tipo para crear un tipo parametrizado.
Además, la T en Foo<T> es un type parameter y el String en Foo<String> es un type argument.

Box<Integer> -> Esto simplemente declara que integerBox tendrá una referencia a un "Box de Integer".
Una invocación de un tipo genérico es generalmente conocida como un parameterized type.

## Multiple type parameters 

```Java
Pair<String, String>  p2 = new OrderedPair<String, String>("hello", "world");
```

## Parameterized types 

```Java
OrderedPair<String, Box<Integer>> p = new OrderedPair<>("primes", new Box<Integer>(...));
```

## Unchecked Error Messages 

Suele aparecer al usar API antigua con raw types.

El término "unchecked" significa que el compilador no tiene suficiente de tipo para realizar todas las
verificaciones de tipo necesarias para garantizar la seguridad de tipo.

Suprimir advertencias no verificadas:


@SuppressWarnings("unchecked")

## Generic Methods 

Son métodos que introducen sus propios type parameters. Es parecido a declarar un tipo genérico pero
el ámbito de los type parameters está limitado al método dónde es declarado.

Se permiten los métodos genéricos estáticos y no estáticos, así cómo constructores de clase genéricos.

## Bounded type parameters 

Es posible restringir los tipos que se pueden usar como type arguments en un parameterized type.

Ejemplos:

```
public <U extends Number> void inspect(U u)
```

```
class Box<U extends Factory>
```

## Multiple Bounds 

```
<T extends B1 & B2 & B3>
```

Una variable de tipo con múltiples bounds es un subtipo de todos los tipos en el bound. Si uno de los bounds es una clase, debe ir la primera:

```
Class A {}
interface B {}
interface C {}

class D <T extends A & B & C>{} //compile-time error

```

## Generics, inheritance and subtypes 

Error muy común trabajando con genéricos es el siguiente:

Aunque Integer sí que sea subtipo de Number, no quiere decir que Box<Integer> sea un subtipo de Box<Number>.

![Jerarquía de herencia para el ejemplo](https://docs.oracle.com/javase/tutorial/figures/java/generics-subtypeRelationship.gif)

Dados dos clases concretas como Number e Integer, Box<Number> no tiene relación con Box<Integer>, a pesar de que Number e Integer tengan relación. El padre común de ambos es Object.

Puedes hacer un tipo subtipo de otro heredando de una clase(extends) o implementando una interface(implements).

![Herencia genéricos ejemplo](https://docs.oracle.com/javase/tutorial/figures/java/generics-sampleHierarchy.gif)

```
interface PayloadList<E, P> extends List<E> {
    void setPayload(int index, P val);
}
```

![Herencia genéricos ejemplo interface List](https://docs.oracle.com/javase/tutorial/figures/java/generics-payloadListHierarchy.gif)

## Type Inference 

La inferencia de tipos es la capacidad del compilador de Java para inspeccionar la invocación de cada método y la declaración correspondiente para determinar el argumento de tipo.

El algoritmo de inferencia determina los tipos de argumentos y, si están disponibles, el tipo al que se asigna o devuelve el resultado. Y por último intenta encontrar el tipo más específico que funciona con todos los argumentos.

### Type Inference and Generic Methods 

```Java
BoxDemo.<Integer>addBox(Integer.valueOf(10), listOfIntegerBoxes);
```

Podemos omitir el tipo ya que el algoritmo de inferiencia cogerá el tipo de dato que le pasamos al método: _Integer_

```Java
BoxDemo.addBox(Integer.valueOf(20), listOfIntegerBoxes);
```

Implmentación addBox()

```Java
public static <U> void addBox(U u,
    java.util.List<Box<U>> boxes) {
    Box<U> box = new Box<>();
    box.set(u);
    boxes.add(box);
}
```

### Type Inference and Instantiation of Generic Classes 

Se puede reemplazar los argumentos requeridos para invocar el constructor de una clase genérica con diamond("<>") siempre que el compilador pueda inferir los argumentos de tipo del contexto.

```Java
Map<String, List<String>> myMap = new HashMap<String, List<String>>();
```

```Java
Map<String, List<String>> myMap = new HashMap<>();
```

El compilador genera un warning por "unchecked conversion" al no haber verificado el tipo. Esto se debe a que estamos usando el raw type de Hashmap.

```Java
Map<String, List<String>> myMap = new HashMap(); // unchecked conversion warning
```

### Type Inference and Generic Constructors of Generic and Non-Generic Classes 

Hay clases genéricas que tienen su propio formal type parameter.

```Java
class MyClass<X> {
  <T> MyClass(T t) {
    // ...
  }
}

// Example
new MyClass<Integer>("")
```

Esta sentencia crea una instancia del parameterized type `MyClass<Integer>`; la cuál explícitamente especifica el tipo Integer para el formal type parameter, X, de la clase genérica `MyClass<X>`.

Fíjate que el constructor de esta clase genérica contiene un formal type parameter, T.

El compilador infiere el tipo String para el formal type parameter, T, del constructor, ya que el actual parameter de este constructor es un String.

```Java
MyClass<Integer> myObject = new MyClass<>("");
```

El compilador infiere el tipo Integer para el formal type parameter, X de `MyClass<X>`. También infiere el String del formal type parameter, T, del constructor de esta clase genérica.


    El algoritmo de inferencia usa solo argumentos de invocación, target types, y posiblemente el tipo de retorno esperado para inferir el tipo. El algoritmo no usa resultados de otra parte del programa o de más tarde. 


### Target Types 

El target type de una expresión es el tipo de dato que el compilador espera dependiendo de dónde aparezca la expresión. 

```Java
List<String> listOne = Collections.emptyList();
```

El Target type de esta expresión es `List<String>`. El método `emptyList()` devuelve `List<T>`, el compilador infiere que el type argument T debe ser un String. 

```Java
void processStringList(List<String> stringList) {
    // process stringList
}

//Java SE7 : not compile
processStringList(Collections.emptyList());
//Java SE7 : compile
processStringList(Collections.<String>emptyList());
```

Java7 da error como: `List<Object> cannot be converted to List<String>`


En Java 8 el compilador infiere el tipo de `List<T>` que devuelve `Collections.emptyList()`, a `List<String>. 


## Wildcards 

El wildcard, '?', nunca se usa como type argument en la invocación de un método genérico, la instanciación de una clase genérica, o un supertype.


### Upper Bounded Wildcards 

```Java
public static double sumOfList(List<? extends Number> list) {
    double s = 0.0;
    for (Number n : list)
        s += n.doubleValue();
    return s;
}
```

En este método, se le puede pasar por parámetro una lista de Number o de cualquier subtipo de Number como Double, Integer, etc. 

### Unbounded Wildcards 

El unbounded wildcard type es cuando usamos como tipo el wildcard, por ejemplo, `List<?>`. Esto sería una lista de tipo desconocido. 

Hay 2 escenarios dónde un unbounded wildcard es útil:

    1. Si estás escribiendo un método que puede ser implementado usando funcionalidad de la clase Object
    2. Cuando el código usa métodos en la clase genérica que no dependen del type parameter.

Por ejemplo, la clase Class<?> se usa con tanta frecuencia porque la mayoría de los métodos de la Clase <T> no dependen de T. 

```Java
public static void printList(List<Object> list) {
    for (Object elem : list)
        System.out.println(elem + " ");
    System.out.println();
}
```

Esto sólo imprime una lista de instancias de Object. No puede imprimir un List<Integer>.


Es importante tener en cuenta que List<Object> y List<?> no es lo mismo. Puedes insertar un Object o cualquier subtipo de Object dentro de un List<Object>, **pero lo único que puede insertar en List<?> es null**.


### Upper Bounded Wildcards 

Restringe el tipo desconocido('?') a ser un tipo específico de un supertipo de ese tipo.

    Puedes especificar upper bound(extends) o un lower bound(super) pero no ambos.

Digamos que quieres hacer un método que ponga objectos de tipo Integer dentro de una lista. Para obtener una mejor flexibilidad te gustaría que el método funcionase con List<Integer>, List<Number> y List<Object>.  

``` Java
public static void addNumbers(List<? super Integer> list) {
    for (int i = 1; i <= 10; i++) {
        list.add(i);
    }
}

```

El término `List<Integer>` es más restrictivo que `List<? super Integer>`. El segundo término coincide con una lista de cualquier tipo que sea un supertipo de Integer.


### Wildcards and Subtyping 

Las clases o interfaces genéricas no están relacionados simplemente porque haya una relación entre sus tipos. Sin embargo, podemos usar wildcard para crear una relación entre clases genéricas o interfaces. 

```Java
class A { /* ... */ }
class B extends A { /* ... */ }

B b = new B();
A a = b;
```

La herencia de clases regulares sigue esta regla de subtyping. La clase B es un subtipo de A si B extends A. Esta regla no es igual para clases genéricas.

```Java
List<B> lb = new ArrayList<>();
List<A> la = lb;   // compile-time error
```

![Herencia genéricos](https://docs.oracle.com/javase/tutorial/figures/java/generics-listParent.gif)


Aunque Integer sea un subtipo de Number, `List<Integer>` no es subtipo de `List<Number>` y, de hecho, son tipos totalmente diferentes. Sólo tienen en común un padre, `List<?>`.

Para crear una relación entre `List<Integer>` y `List<Number>` usaremos un upper bounded wildcard:

```Java
List<? extends Integer> intList = new ArrayList<>();
List<? extends Number>  numList = intList;  // OK. List<? extends Integer> is a subtype of List<? extends Number>
```

![Herencia genéricos](https://docs.oracle.com/javase/tutorial/figures/java/generics-wildcardSubtyping.gif)


### Wildcard Capture and Helper Methods 

En algunos casos, el compilador infiere el tipo de un wildcard. Por ejemplo, una lista puede ser definida como `List<?>` pero, cuando evalua una expresión, el compilador infiere un particular tipo del código. Este escenario es conocido como "wildcard capture".

No necesitas preocuparte por esto salvo cuando vemos el mensaje de error con "capture of". 

Capture error:

```Java
void foo(List<?> i) {
    i.set(0, i.get(0));
}
```

En este ejemplo, el compilador procesa el input i como si fuera un Object. Cuando este método invoca el método `List.set(int E)`, el compilador no puede confirmar el tipo de objeto que está siendo insertado en la lista y lanza un error. 

El compilador cree que estás insertando un tipo erróneo a la variable. Los genéricos se incorporaron en Java por esta razón, para obligar el cumplimiento de la seguridad de tipos en tiempo de compilación.

#### ¿Cómo solucinar este problema?

Podemos crear un helper:

```Java
public class WildcardFixed {

    void foo(List<?> i) {
        fooHelper(i);
    }


    // Helper method created so that the wildcard can be captured
    // through type inference.
    private <T> void fooHelper(List<T> l) {
        l.set(0, l.get(0));
    }

}
```

El compilador usa inferencia para determinar que T es CAP#1, la variable de captura, en la invocación.

Por convención, los helpers son llamados: `originalMethodNameHelper`

Otro ejemplo más comlejo de una operación insegura:
```Java
import java.util.List;

public class WildcardErrorBad {

    void swapFirst(List<? extends Number> l1, List<? extends Number> l2) {
      Number temp = l1.get(0);
      l1.set(0, l2.get(0)); // expected a CAP#1 extends Number,
                            // got a CAP#2 extends Number;
                            // same bound, but different types
      l2.set(0, temp);	    // expected a CAP#1 extends Number,
                            // got a Number
    }
}
```

```Java
List<Integer> li = Arrays.asList(1, 2, 3);
List<Double>  ld = Arrays.asList(10.10, 20.20, 30.30);
swapFirst(li, ld);
```

 `List<Integer>` y `List<Double>` cumplen el criterio de `List<? extends Number>`, esto es claramente incorrecto ya que no podemos coger un Integer y meterlo en una lista de Doubles.

 Para este caso no funciona ningún helper ya que el code es erróneo. 


### Guidelines for Wildcard Use 

Uno de los aspectos más confusos cuando aprendes a programar con genéricos es cuando usar upper bounded wildcard y cuando usar lowe bounded wildcard.

Es útil pensar que las variables proporcionan una de estas dos funciones:

    1. Una variable "In". Sirve datos al código. Por ejemplo un método `copy(src, dest)`. El argumetno "src" contiene el dato que van a ser copiados, así que src es un parámetro "in".
    2. Una variable "Out". Contiene datos para usar en otro lugar. En el ejemplo anterior, `copy(src, dest)`, el argumento "dest" acepta datos, así que "dest" es un parámetro "out".


##### WildCard Guidelines 

    * Una variable "in" es definida con un upper *bounded wildcard(extends)*.
    * Una variable "out" es definida con *lower bounded wildcard(super)*.
    * En los casos en el que la variable "in" puede ser accedida usando métodos definidos en la clase Object, usa *unbounded wildcard*.
    * En los casos donde se necesita acceder a la variable tanto como "in" como "out", no uses wildcard.

Esta guía no aplica para los tipos de return en los métodos. Usar un wildcard como return type debe ser evitado porque estás forzando a los programadores a usar el código lidiando con wildcards.

Una lista definida con `List<? extends ...>` puede considerarse inforlmente como "read-only", pero eso no es ninguna garantía estricta.

```Java
class NaturalNumber {

    private int i;

    public NaturalNumber(int i) { this.i = i; }
    // ...
}

class EvenNumber extends NaturalNumber {

    public EvenNumber(int i) { super(i); }
    // ...
}
```

```Java
List<EvenNumber> le = new ArrayList<>();
List<? extends NaturalNumber> ln = le;
ln.add(new NaturalNumber(35));  // compile-time error
```

Puedes asignar le a ln porque `List<EvenNumber>` es un subtipo de `List<? extends NaturalNumber>`, pero no puedes usar ln para añadir un NaturalNumber a la lista de EvenNumber. En este caso, sólo son posibles las siguientes operaciones:

    * Añadir null
    * Llamar a clear
    * Get Iterator y llamar a remove
    * Capturar el wildcard y escribir los elementos que has leído de la lista.

Se puede ver que la lista definida por `List<? extends NatualNumber>` no es sólo "read-only" en el sentido más estricto de la palabra, pero puede pensar de esa manera porque no puedes almacenar un nuevo elemento o cambiar un elemento existente en la lista.



## Type Erasure 

Para implementar los genéricos, el compilador de java aplica "type erasure"(borrado de tipos) para:

    * Remplazar todos los parámetros de tipo(type parameter) en los tipos genéricos por sus bounds u por Object en caso de que los type parameter sean unbounded. El bytecode sólo contiene clases ordinarias, interfaces y métodos.
    * Inserta cast de tipos de ser necesario para preservar el tipado seguro.
    * Genera métodos bridge para preservar el polimorfismo en tipos genéricos heredados(extend)

El type erasure(borrado de tipos) asegura que no se creen nuevas clases para tipos parametrizados; en consecuencia, los genéricos no incurren en gastos generales de tiempo de ejecución.

### Erasure of generic types 

Durante el proceso de borrado de tipos, el compilador borra todos los type parameters y los reemplaza con el primer bound si el type parameter es bounded, si es unbounded lo reemplaza por Object.

##### Ejemplo unbounded 
```Java
public class Node<T> {

    private T data;
    private Node<T> next;

    public Node(T data, Node<T> next) {
        this.data = data;
        this.next = next;
    }

    public T getData() { return data; }
    // ...
}
```
El type parameter T es unbounded, el compilador lo reemplaza con Object: 

```Java
public class Node {

    private Object data;
    private Node next;

    public Node(Object data, Node next) {
        this.data = data;
        this.next = next;
    }

    public Object getData() { return data; }
    // ...
}
```

##### Ejemplo bounded 

```Java
public class Node<T extends Comparable<T>> {

    private T data;
    private Node<T> next;

    public Node(T data, Node<T> next) {
        this.data = data;
        this.next = next;
    }

    public T getData() { return data; }
    // ...
}
```

El compilador reemplaza el bounded type parameter T con la primera bound class, Comparable:

```Java
public class Node {

    private Comparable data;
    private Node next;

    public Node(Comparable data, Node next) {
        this.data = data;
        this.next = next;
    }

    public Comparable getData() { return data; }
    // ...
}
```

### Erasure of Generic Methods 

El borrado de tipos también actúa en métodos genéricos:

```Java
// Counts the number of occurrences of elem in anArray.
//
public static <T> int count(T[] anArray, T elem) {
    int cnt = 0;
    for (T e : anArray)
        if (e.equals(elem))
            ++cnt;
        return cnt;
}
```

Como T es unbounded, el compilador lo reemplaza por Object:

```Java
public static int count(Object[] anArray, Object elem) {
    int cnt = 0;
    for (Object e : anArray)
        if (e.equals(elem))
            ++cnt;
        return cnt;
}
```

Ejemplo con type parameter bounded en un método genérico:
```Java
public static <T extends Shape> void draw(T shape) { /* ... */ }
```

El compilador reemplaza T con Shape:

    public static void draw(Shape shape) { /* ... */ }



### Effects of Type Erasure and Bridge Methods 

Algunas veces el compilador crea un método sintético, llamado bridge method, durante el proceso de borrado de tipos.

Dadas las siguientes dos clases:

    public class Node<T> {

        public T data;

        public Node(T data) { this.data = data; }

        public void setData(T data) {
            System.out.println("Node.setData");
            this.data = data;
        }
    }

    public class MyNode extends Node<Integer> {
        public MyNode(Integer data) { super(data); }

        public void setData(Integer data) {
            System.out.println("MyNode.setData");
            super.setData(data);
        }
    }

Observemos el siguiente ejemplo de código: 

    MyNode mn = new MyNode(5);
    Node n = mn;            // A raw type - compiler throws an unchecked warning
    n.setData("Hello"); 
    Integer x = mn.data;    // Causes a ClassCastException to be thrown.
 
 Tras el type erasure, este código queda así:
    MyNode mn = new MyNode(5);
    Node n = (MyNode)mn;         // A raw type - compiler throws an unchecked warning
    n.setData("Hello");
    Integer x = (String)mn.data; // Causes a ClassCastException to be thrown.
    
    
Esto es lo que pasa cuando se ejecuta el código:

    * n.setData("Hello"); Esto provoca que el método setData(Object) se ejecute en el objeto
    de la clase MyNode. La clase MyNode heredó de setData(Object) de Node.
    * En el cuerpo de setData(Object), el campo data del objeto referenciado por n es asignado
    a un String.
    * El campo data del mismo objeto, referenciado por mn, puede ser accedido y se espera
    que sea un número entero(en mn es un MyNode el cuál es un Node<Integer>).
    * Intentar asignar un String a un Integer provoca una ClassCastException del cast de la asignación.
    
### Bridge Methods 

Cuando el compilador está compilando una clase o interface que extends de una clase parametrizada 
o que implementa una interface parametrizada, el compilador puede necesitar crear un método sintético
conocido como bridge method como parte del proceso de borrado de tipos. Normalmente no necesitas
preocuparte por los bridge methods, pero es posible que te desconciertes si aparece uno en el stack
trace.

Antes type erasure:

    public class Node<T> {
        public T data;

        public Node(T data) { this.data = data; }

        public void setData(T data) {
            System.out.println("Node.setData");
            this.data = data;
        }
    }

Después type erasure:

    public class Node {
    
        public Object data;
    
        public Node(Object data) { this.data = data; }
    
        public void setData(Object data) {
            System.out.println("Node.setData");
            this.data = data;
        }
    }
    
Antes de type erasure:

    public class MyNode extends Node<Integer> {
        public MyNode(Integer data) { super(data); }

        public void setData(Integer data) {
            System.out.println("MyNode.setData");
            super.setData(data);
        }
    }
    
Después de type erasure:

    public class MyNode extends Node {
    
        public MyNode(Integer data) { super(data); }
    
        public void setData(Integer data) {
            System.out.println("MyNode.setData");
            super.setData(data);
        }
    }

Tras la eliminación de tipos, las definiciones de los métodos no coinciden. 
El método Node se convierte en setData (Object) y el método MyNode se convierte en setData (Integer).
Por lo tanto, el método MyNode setData no sobrescribe el método Node setData.

Para arreglar este problema y preservar el polimorfismo de los tipos genéricos tras el borrado de tipos,
el compilador genera un bridge method para garantizar que el subtyping funcione como se espera.

Para la clase MyNode, el compilador genera el siguiente bridge method:

    class MyNode extends Node {

        // Bridge method generated by the compiler
        //
        public void setData(Object data) {
            setData((Integer) data);
        }
    
        public void setData(Integer data) {
            System.out.println("MyNode.setData");
            super.setData(data);
        }
    
        // ...
    }
    
Como se puede apreciar el bridge method setData en MyNode tiene la misma definición, MyNode.setData(Object)
que el de la clase Node, Node.setData(Object), tras el borrado de tipos. 
El bridge method delega al original setData method.

## Non-Reifiable Types 

El type erasure tiene consecuencias relacionadas con los métodos de argumentos variables(varags) cuyo parámetro 
formal varargs tiene un tipo non-reifiable.

### Non-Reifiable Types

A reifiable type es un tipo cuya información de tipo está completamente disponible en tiempo de ejecución.
Esto incluye a los tipos primitivos, los tipos no genéricos, los raw types y las invocaciones de unbound 
wildcards.

Los tipos non-reifiable son tipos dónde la información ha sido borrado en tiempo de compilación por el borrado
de tipos(invocaciones de tipos genéricos que no son definidos como ubounded wildcards(List<?>). Un tipo non-reifiable
no tiene toda su información disponible en tiempo de ejecución. 

Ejemplos de tipos non-reifiable son List<String> y List<Number>; la JVM no puede decir la diferencia entre esos tipos
en tiempo de ejecución. Hay ciertas situaciones dónde los tipos non-reifiable no pueden ser usados: en un instanceof, 
por ejemplo, o como un elemento en un array.

### Heap Pollution 

Esto ocurre cuando una variable de un tipo parametrizado se refiere a un objeto que no es de tipo parametrizado.
Esta situación ocurre si el programa realizó alguna operación que da lugar a un unchecked warning en tiempo de
compilación.

Un unchecked warning es generado si, ya sea en compile-time o run-time, no se puede verificar la exactitud de una 
operación que involucra un tipo parametrizado(por ejemplo, un cast o llamada de método). Por ejemplo, heap pollution
ocurre cuando se mezclan raw types y tipos paremetrizados, o cuando se realizan casts unchecked.

En situaciones normales, cuando todo el código se compila al mismo tiempo, el compilador emite un warning 
unchecked para llamar la atención del posible heap pollution. Si compilas secciones de tu código por separado
es complicado detectar el riesgo potencial de heap pollution. Si te aseguras que tu código compila sin warnings, no 
puede producirse un heap pollution.

### Potential Vulnerabilities of Varargs Methods with Non-Reifiable Formal Parameters 

Los métodos genéricos que incluyen parámetros de entrada(input) vararg(main(String... args)), pueden causar heap pollution.

    public class ArrayBuilder {
    
      public static <T> void addToList (List<T> listArg, T... elements) {
        for (T x : elements) {
          listArg.add(x);
        }
      }
    
      public static void faultyMethod(List<String>... l) {
        Object[] objectArray = l;     // Valid
        objectArray[0] = Arrays.asList(42);
        String s = l[0].get(0);       // ClassCastException thrown here
      }
    
    }
    
Ejemplo de uso:

public class HeapPollutionExample {

    public static void main(String[] args) {
        List<String> stringListA = new ArrayList<String>();
        List<String> stringListB = new ArrayList<String>();
    
        ArrayBuilder.addToList(stringListA, "Seven", "Eight", "Nine");
        ArrayBuilder.addToList(stringListB, "Ten", "Eleven", "Twelve");
        List<List<String>> listOfStringLists =
          new ArrayList<List<String>>();
        ArrayBuilder.addToList(listOfStringLists,
          stringListA, stringListB);
    
        ArrayBuilder.faultyMethod(Arrays.asList("Hello!"), Arrays.asList("World!"));
      }
    }

Cuando compila, la definición del método ArrayBuilder.addToList provoca el siguiente warning:
    warning: [varargs] Possible heap pollution from parameterized vararg type T

Cuando el compilador encuentra un método vararg, mete los varargs dentro de un array. Sin embargo,
java no permite la creación de arrays de tipos parametrizados. En el método ArrayBuilder.addToList,
el compilador traduce el parámetro formal `T... elements` al parámetro formal `T[] elements`. Además,
debido al borrado de tipos, el compilador convierte el parámetro formal varargs a `Object[] elements`.
Por lo que habría un posible heap pollution.


Esto puede potencialmente provocar una heap pollution:

    List<String>... l;
    Object[] objectArray = l;
   
El compilador no genera una warning unchecked para esta expresión. El compilador ya ha generado una warning
cuando tradujo el parámetro formal `List<String>... l` a `List[] l`. Esto es válido, la variable l tiene el 
tipo List[], el cuál es un subtipo de Object[].

Por ello, el compilador no lanza un warning o error si asignas un objeto List de cualquier tipo a cualquier
array de objectArray:

    objectArray[0] = Arrays.asList(42);
    
Esto asigna al primer elemento del array objectArray con un objecto List que contiene un objeto de tipo Integer.

Supón que invocas ArrayBuilder.faultyMethod:
    
     ArrayBuilder.faultyMethod(Arrays.asList("Hello!"), Arrays.asList("World!"));

En tiempo de ejecución, la JVM lanza un ClassCastException en la siguiente sentencia:

    // ClassCastException thrown here
    String s = l[0].get(0);

El objeto almacenado en el primer elemento del array de la variable l tiene el tipo List<Integer>, pero 
esta sentencia está esperando un objeto de tipo List<String>.
 

 ### Prevent Warnings from Varargs Methods with Non-Reifiable Formal Parameters 

 Si declaras un método con varargs que tiene parametros con un tipo parametrizado, y te aseguras de que la 
 implementación del método no lanza una ClassCastException u otra similar a causa de un mal manejo de los 
 varargs parameters, puedes prevenir el warning que el compilador genera para este tipo de métodos varargs 
 agregando la siguiente anotación en las declaraciones de métodos estáticos y métodos que no sean constructores.
 
    @SafeVarargs
    
Esta anotación es una parte documentada del contrato del método. Esta anotación asegura que la implementación
del método no manejará erróneamente los varargs.

También es posible, pero menos adecuado, suprimir esos warnings con la siguiente anotación en la declaración
del método:

    @SuppressWarnings({"unchecked", "varargs"})


_ _ _
## Restrictions on Generics

### Cannot instatiate Generic Types with Primitve types

    class Pair<K, V> {
    
        private K key;
        private V value;
    
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }
    
        // ...
    }
    
Cuando vayas a crear un Pair Object no puedes sustituir un tipo primitivo
para el type parameter K o V:

    Pair<int, char> p = new Pair<>(8, 'a');  // compile-time error

Sólo puedes usar tipos no primitivos para los type parameters K y V:

    Pair<Integer, Character> p = new Pair<>(8, 'a');
    
El compilador autoboxea 8 a Integer.valueOf(8) y 'a' a Character('a'):

    Pair<Integer, Character> p = new Pair<>(Integer.valueOf(8), new Character('a'));



### Cannot Create Instances of Type Parameters

    public static <E> void append(List<E> list) {
        E elem = new E();  // compile-time error
        list.add(elem);
    }

Se podría crear mediante reflection:

    public static <E> void append(List<E> list, Class<E> cls) throws Exception {
        E elem = cls.newInstance();   // OK
        list.add(elem);
    }

Para invocarlo:

    List<String> ls = new ArrayList<>();
    append(ls, String.class);



### Cannot Declare Static Fields Whose Types are Type Parameters

El campo estático de clase es una variable de nivel de clase compartida por todos los objetos 
no estáticos de la clase. Por lo tanto no se permiten campos estáticos de type parameters.

    public class MobileDevice<T> {
        private static T os;
    
        // ...
    }

Si fueran permitidos pasaría lo siguiente:

    MobileDevice<Smartphone> phone = new MobileDevice<>();
    MobileDevice<Pager> pager = new MobileDevice<>();
    MobileDevice<TabletPC> pc = new MobileDevice<>();
    
Si el campo estático T os fuera compartido para phone, pager y pc.. ¿Cuál sería el tipo actual de os?
No puede ser Smartphone, Pager y TabletPC a la misma vez. 



### Cannot Use Casts or instanceof with Parameterized Types

Debido a que el compilador borra todos los type parameters en el código genérico, no puedes verificar 
qué tipo parametrizado de un tipo genérico está siendo usado en tiempo de ejecución. 

     public static <E> void rtti(List<E> list) {
        if (list instanceof ArrayList<Integer>) {  // compile-time error
            // ...
        }
     }

En tiempo de ejecución no se hace seguimiento de los type parameters, así que no se puede saber la diferencia 
entre un ArrayList<Integer> y un ArrayList<String>. La mejor que puedes hacer es usar un unbounded wildcard 
para verificar que la lista es un ArrayList:

    public static void rtti(List<?> list) {
        if (list instanceof ArrayList<?>) {  // OK; instanceof requires a reifiable type
            // ...
        }
    }

No puedes hacer cast a un tipo parametrizado a menos que esté parametrizado por un unbounded wildcard:

    List<Integer> li = new ArrayList<>();
    List<Number>  ln = (List<Number>) li;  // compile-time error
    
Sin embargo, en algunos casos el compilador conoce qué type parameter es siempre válido y permite el cast:

    List<String> l1 = ...;
    ArrayList<String> l2 = (ArrayList<String>)l1;  // OK
    
  
    
### Cannot Create Arrays of Parameterized Types

    List<Integer>[] arrayOfLists = new List<Integer>[2];  // compile-time error
    
El siguiente código muestra lo que ocurre cuando diferentes tipos son insertados en un array:

    Object[] strings = new String[2];
    strings[0] = "hi";   // OK
    strings[1] = 100;    // An ArrayStoreException is thrown.
    
En el caso de ser posible, si hicieramos lo mismo con una lista genérica, habría un problema:

    Object[] stringLists = new List<String>[];  // compiler error, but pretend it's allowed
    stringLists[0] = new ArrayList<String>();   // OK
    stringLists[1] = new ArrayList<Integer>();  // An ArrayStoreException should be thrown,
                                                // but the runtime can't detect it.
                                                
Si los arrays de listas parametrizadas estuvieran permitidos, el código anterior no lanzaría un ArrayStoreException, ya que 
tanto ArrayList<String> como ArrayList<Integer> serían el mismo tipo para el compilador.                                                                                          
            
            
### Cannot Create, Catch, or Throw Objects of Parameterized Types

Una clase genérica no puede extends de la clase Throwable directamente o indirectamente. Por ejemplo, las siguientes clases
no compilarían:

    // Extends Throwable indirectly
    class MathException<T> extends Exception { /* ... */ }    // compile-time error
    
    // Extends Throwable directly
    class QueueFullException<T> extends Throwable { /* ... */ // compile-time error
   
Un método no puede capturar una instancia de un type parameter:

    public static <T extends Exception, J> void execute(List<J> jobs) {
        try {
            for (J job : jobs)
                // ...
        } catch (T e) {   // compile-time error
            // ...
        }
    }    
    
Puedes, sin embargo, usar un type parameter en un claúsula throws:

    class Parser<T extends Exception> {
        public void parse(File file) throws T {     // OK
            // ...
        }
    }
    
### Cannot Overload a Method Where the Formal Parameter Types of Each Overload Erase to the Same Raw Type

Una clase no puede tener dos métodos sobrecargados que tendrán la misma firma tras el borrado de tipos:

    public class Example {
        public void print(Set<String> strSet) { }
        public void print(Set<Integer> intSet) { }
    }    
    
Todas las sobrecargas compartirían la misma representación de archivo de clase y generarán un error en tiempo de compilación.         



## Bibliografía

[Tutorial Java Generics](https://docs.oracle.com/javase/tutorial/java/generics/index.html)






                                 