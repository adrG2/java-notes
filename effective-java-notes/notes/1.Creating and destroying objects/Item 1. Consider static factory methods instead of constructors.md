# Consider static factory methods instead of constructors

La forma tradicional en la que una clase permite a un cliente obtener una instancia es proveerle de un constructor público. Hay otra forma de hacerlo que todo programador debería conocer: **static factory method**. No es más que un método estático que returns una instancia de la clase.

    static factory method != Factory Method pattern

Una clase puede proveer a sus clientes con static factory methods en lugar de, o además de constructores públicos. 


## Ventajas de usar static factory methods

--- 

### 1. Los Factory methods tienen un nombre

Si los parámetros de un constructor no describen por sí mismos el objeto que devuelve, un static factory con un buen nombre es más fácil de usar y el código del cliente que lo usa queda más fácil de leer.

```Java
    new BigInteger(int, int, Random);
    BigInteger.probablePrime(); //static factory method
```

Una clase sólo puede tener un constructor con la misma firma(número de parámetros + tipos). Los programadores se saltan esta restricción cambiando el orden de los tipos de parámetros, lo cuál es una muy mala idea ya que quién use nuestra API nunca recordará qué constructor es cuál y se confundirá entre los mismos. Tendrá que recurrir a la documentación para poder usar nuestra clase.

En los casos en los que una clase parezca requerir de múltiples constructores con la misma firma, reemplazamos los constructores por static factory methods y escogeremos cuidadosamente sus nombres para resaltar sus diferencias. 

---

## 2. Los Factory methods no nos obligan a crear un nuevo objeto cada vez que sean invocados

Esto nos permite que las clases inmutables utilicen instancias preconstruidas, o almacenen en caché las instancias conforme se vayan construyendo, y las distribuyen repetidamente para evitar crear objetos duplicados innecesariamente. 

Por ejemplo, el siguiente método nunca crea un objecto: 
```
Boolean.valueOf(boolean) 
```

Esta técnica es similar al **Flyweight pattern**. Se podría mejorar enormemente el rendimiento si se solicitan objectos equivalentes con frecuencia, especialmente si su creación es costosa.


La capacidad de los static factory methods para devolver el mismo objeto desde diversas invocaciones repetidas permite a las clases mantener un control estricto sobre qué instancias existen en todo momento. Las clases que hacen esto son llamadas *instance-controlled*. El control de instancia permite a una clase garantizar que es un singleton o no instanciable. Además, permite que una clase de valor inmutable garantice que no existan dos instancias iguales. Los tipos *Enum* proveen esta garantía.

Esto sería la base del patrón *Flyweight*. 

--- 

## 3. Pueden devolver un objeto de cualquier subtipo de su tipo de retorno

Esta flexibilidad permite que una API pueda devolver objetos sin hacer sus clases públicas. Esta técnica se presta a **interface-based frameworks**, dónde las interfaces proporcionan tipos de retorno naturales para los static factory methods. 

Por convención, los static factory methods para una interface llamada *Type* se colocan en una clase complementaria no instanciable llamada *Types*. 

El Java Collections Framework tiene 55 implementaciones de sus interfaces, proporcionando, colecciones inmodificables, colecciones sincronizadas(synchronized), y similares. Casi todas esas implementaciones son exportadas por static factory methods en una clase no instanciable: **java.util.Collections**. Las clases de los objetos devueltos no son públicas.

La API de Collections Framework es mucho más pequeña de lo que hubiera sido si hubiera exportado 45 clases públicas, una por cada  implementación. No sólo se reduce la mayor parte del tamaño de la API sino que también se reduce el **conceptual weight**, la cantidad y dificultad de los conceptos que los programadores necesitan dominar para usar la API. El programador sabe que el objeto devuelto está especificado por su interface, por lo que no necesita leer información adicional de la documentación de la clase para implementarla. Además, utilizar un static factory method requiere que el cliente haga referencia a la interface del objeto devuelto en vez de por su clase de implementación, lo cuál generalmente es una buena práctica.

A partir de Java 8 se eleminó la restricción de que las interfaces no pudieran contener métodos estáticos, por lo que hay pocas razones para proporcionar una clase complementaria no intanciable para una interface. En cambio, muchos miembros estáticos que habrían estado en una clase de este tipo deberían colocarse en la interface. 

Sin embargo, debido a que en Java 8 todos los miembros estáticos de una interface tienen que ser públicos, ten en cuenta que a veces puede ser necesario colocar la mayor parte de la implementación detrás de estos métodos estáticos en una clase privada. En Java 9 se permiten los métodos estáticos privados, pero lo demás aún debe ser público. 

--- 

## 4. La clase del objeto devuelto puede variar de una llamada a otra en función de los parámetros de entrada

Se permite cualquier subtipo del tipo declarado como valor de retorno(return). La clase del objeto devuelto puede también variar de una release a otra.

La clase **EnumSet** no tiene constructores públicos, sólo static factories. En su implementación en el OpenJDK puede devolver una instancia de una de sus dos subclases, dependiendo del tamaño del *enum* subyacente. Si tiene 64 o menos elementos, la static factory devuelve una instancia de *RegularEnumSet*(long). Si tiene 65 o más elementos la factoría devuelve una instancia de *JumboEnumSet*(long array).

Las implementaciones de estas clases es invisible a los clientes. Si *RegularEnumSet* deja de ofrecer ventajas de rendimiento para los tipos enum pequeños, podría ser eliminado en una futura release sin efectos nocivos. De forma similar, una futura release podría añadir una tercera o cuarta implementación de *EnumSet*. 

    Los clientes no saben ni se preocupan por la clase del objeto que obtienen de la fábrica; sólo les importa que sea una subclase de EnumSet. 

---

## 5. La clase del objeto que se va a devolver no necesita existir cuando se escribe la clase que contiene el método

Esa flexibilidad de los static factory methods es la base de los **service provider frameworks**, como por ejemplo el JDBC. Un service provider framework es un sistema en el que los providers implementan un servicio, y el sistema pone las implementaciones a disposición de los clientes, desacoplando a los clientes de las implementaciones. 

Hay 3 componentes esenciales en un service provider framework: 

* **Service interface**. Representa una implementación
* **Provider registration**. La usan los providers para registrar las implementaciones.
* **Service access API**. La usan los clientes para obtener instancias del servicio.

La **service access API** permite a los clientes especificar criterios para elegir una implementación. En ausencia de tales criterios, la API devuelve una instancia de una implementación por defecto, o permite al cliente recorrer todas las implementaciones disponibles. Esta API es una static factory flexible que forma la base del service provider framework. 

Un cuarto componente opcional de un service provider framework es una **service provider interface**, la cuál define una objecto factoría que produce instancias de la service interface. En ausencia de una service provider interface, las implementaciones deben ser instanciadas con reflection. 

En el caso de JDBC:

```
    //Service interface
    Connection

    //Provider registration API
    DriverManager.registerDriver()
    
    //Service Access API
    DriverManager.getConnection()

    //Service provider interface
    Driver
```

Hay muchas variantes del patrón Service provider. Por ejemplo, la service access API puede devolver una service interface más rica para los clientes que la proporcionada por los providers(Esto es un **Bridge pattern**). 

El **Dependency Injection Framework** puede ser visto como un poderoso service provider framework. La plataforma java incluye un service provider framework de propósito general, `java.util.ServiceLoader`. JDBC no lo usa ya que es anterior a la inclusión de ServiceLoader. 


--- 

## Desventajas o limitaciones de static factory methods


### Las clases sin constructores públicos o protected no están abiertas a herencia. 

La principal limitación de sólo proporcionar static factory methods es que las clases sin constructores públicos o protected no pueden están abiertas a herencia. Por ejemplo, no se puede hacer herencia en ninguna de las clases de implementación de **Collections**. Podría decirse que esta "limitación" es una bendición disfrazada ya que alienta a los programadores a usar la composición sobre la herencia, y se requiere para los tipos inmutables. 



### Static factory methods son difíciles de encontrar para los programadores

No se destacan en la documentación de la API de la forma en que lo hacen los constructores, así que puede ser complicado darse cuenta de cómo instanciar una clase que provee static factory methods en vez de constructores. 

Se puede reducir este problema dirigiendo la atención a las static factories en la documentación de la clase o interface a la vez que se emplea la convención de naming.


--- 


## Naming static factories 


* **from**. Un *type-conversion method* que toma un único parámetro y devuelve una instancia correspondiente de su tipo:

    ```Java
    Date d = Date.from(instant);
    ```

* **of**. Un *aggregation method* que toma varios parámetros y devuelve una instancia de ese tipo que los incorpora:

    ```Java
    Set<Rank> faceCards = EnumSet.of(JACK, QUEEN, KING);
    ```

* **valueOf**. Una más específica alternativa a **from** y **of**.

    ```Java
    BigInteger prime = BigInteger.valueOf(Integer.MAX_VALUE);
    ```

* **instance** o **getInstace**. Devuelve una instancia que es descrita por sus parámetros(si los hay) pero que no puede decir que tiene el mismo valor. 

    ```Java
    StackWalker luke = StackWalker.getInstance(options);
    ```

* **create** o **newInstance**. Es como *instance* o *getInstance*, excepto que el método garantiza que en cada llamada devolverá una nueva instancia. 

    ```Java
    Object newArray = Array.newInstance(classObject, arrayLenght);
    ```

* **getType**. Es como *getInstance* pero se usa cuando el factory method está en una clase diferente. *Type* es el tipo del objeto devuelto por el factory method:

    ```Java
    FileStore fs = Files.getFileStore(path);
    ```

* **newType**. Es como *newInstance*, pero se usa si el factory method está en una clase diferente. *Type* es el tipo del objeco devuelto por el factory method: 

    ```Java
    BufferedReader br = Files.newBufferedReader(path);
    ```


* **type**. Una alternativa para *getType* y *newType*:

    ```Java
        List<Complaint> litany = Collections.list(legacyLitany);
    ```


