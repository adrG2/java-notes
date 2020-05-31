# Item 2. Consider a builder when faced with constructor parameters

Las factorias estáticas y los constructores comparten una limitación: no escalan bien cuando hay un número importante de parámetros opcionales. 

¿Cómo gestionar este tipo de clases que tienen varios parámetros opcionales?

## Patrón telescoping constructor. 

Este patrón consiste en tener un constructor con los parámetros obligatorios, y para ir añadiendo los parámetros opcionales se usa la sobrecarga de constructor. 

![carTelescoping class](https://github.com/adrG2/java-notes-spanish/blob/master/effective-java-notes/code/1.Creating%20and%20destroying%20objects/Item2/carTelescopingPattern.png)

Al utilizar este patrón, en muchas ocasiones, estaremos obligados a pasar un valor por parámetro que no queremos. En el ejemplo anterior tendremos que setear a **false** extras del coche que no queremos activar, cuando lo más eficiente sería sólo activarlos cuando realmente ese coche vaya a incorporarlos. Con "pocos" parámetros esto no tendría porqué parecer algo horrible pero se te irá de las manos rápidamente en cuanto aumente el número de parámetros.

En conclusión, el patrón *telescoping constructor* funciona, pero se hace complicado tanto usar ese código por el cliente cuando hay varios parámetros como leer el código. El lector tendrá que preguntarse qué significan todos esos valores y para averguarlo tendrá que contar con cuidado el número de parámetros para averiguarlo. Además si son parámetros con el mismo tipo el código será más propenso a errores. Por ejemplo, si al instanciar un objeto cambias el orden de dos strings, el compilador no se quejará pero probablemente tendrás un error en tiempo de ejecución.

## Patrón JavaBean

Con este patrón se llama a un constructor sin parámetros para crear el objeto y luego con setter estableces los valores de los parámetros obligatios y opcionales. 

![carJavaBean class](https://github.com/adrG2/java-notes-spanish/blob/master/effective-java-notes/code/1.Creating%20and%20destroying%20objects/Item2/carJavaBeanPattern.png)

Este patrón no tiene ninguna de las desventajas del patrón *telescoping*. Es fácil crear instancias y leer el código resultante: 

![carJavaBean cretaing](https://github.com/adrG2/java-notes-spanish/blob/master/effective-java-notes/code/1.Creating%20and%20destroying%20objects/Item2/carTelescopingPatternCreating.png)

Importantes desventajas:

* **Estado inconsistente**. Esto se debe a que se construye en llamadas separadas. La clase no puede reforzar su consistencia con solamente validar los parámetros de construcción(setters en este caso). Usar un objeto inconsistente puede causar graves fallos que están ocultos y por tanto, puede convertirse en un infierno hacer debug.

* **No es posible hacer la clase inmutable**. Es necesario un esfuerzo extra para garantizar la seguridad del hilo(**thread safety**).


## Patrón Builder

En lugar de instanciar el obecto deseado directamente, el cliente llama a un constructor (o factoría estática) con todos los parámetros requeridos y obtiene un objeto *builder*. Luego se llaman a métodos "setters" por cada parámetro opcional que queramos establecer. Para finalizar, el cliente llama a un método sin parámetros llamado *build* para generar el objeto, el cuál suele ser *inmutable*. El builder suele ser un miembro estático de clase de la clase que construye.

![carBuilderPattern class](https://github.com/adrG2/java-notes-spanish/blob/master/effective-java-notes/code/1.Creating%20and%20destroying%20objects/Item2/CarBuilderPattern.png)

La clase CarBuilderPattern es inmutable, y el valor por defecto de todos los parámetros está en un único lugar. Los métodos "setters" del builder devuelven al propio builder para que las invocaciones se puedan encadenar. 

``` Java
CarBuilderPattern car = new CarBuilderPattern.Builder(4, motor, 4).leatherSeats(true).build();
```

El código del cliente es fácil de escribir y de leer. **El patrón builder simula los llamados parámetros ocpionales** que se encuentran Python o Scala.

La validación de los parámetros se realiza en el constructor y en los métodos. Si el checkeo falla se lanza una *IllegalArgumentException* cuyo mensaje indica qué parámetros son inválidos.

**El patrón builder se adapta bien a las jerarquías de clase**. Usar una jerarquía paralela de builders, cada uno anidado en la clase correspondiente. Las clases abstractas tienen builders abstractos; las clases concretas tienen builders concretos. 

![pizza abstract](https://github.com/adrG2/java-notes-spanish/blob/master/effective-java-notes/code/1.Creating%20and%20destroying%20objects/Item2/pizzaAbstract.png)

Fíjate en que *Pizza.Builder* es un tipo genérico con un *parámetro de tipo recursivo*. Esto, junto con el método abstracto *self*, permite el encadenamiento de métodos funcione correctamente en las subclases, sin la necesidad de usar *casts*. Esta solución para el hecho de que Java carece de un tipo *self* es conocido como el *tipo self simulado*.

![pizza subclasses](https://github.com/adrG2/java-notes-spanish/blob/master/effective-java-notes/code/1.Creating%20and%20destroying%20objects/Item2/pizza_subclasses.png)

Fíjate que el método *build* en el builder de cada subclase se declara para devolver la subclase correcta. Esta técnica, en la que se declara un método de subclase para devolver un subtipo del tipo de devolución declarado en la superclase, se conoce como *covariant return typing*.

El código del cliente de estos builders jerárquicos no difieren mucho del ejemplo anterior:

```Java
NyPizza pizza = new NyPizza.Builder(SMALL).addTopping(SAUSAGE).addTopping(ONION).build();
Calzone calzone = new Calzone.Builder().addTopping(HAM).sauceInside().build();
```

Una pequeña ventaja de los builders respecto a los contructores es que los builders pueden tener múltiples parámetros *varargs*( Ej: int ...a) ya que cada parámetro se especifica en su propio método. De forma alternativa, los builders pueden agregar los parámetros pasados en múltiples llamadas a un metodo en un sólo campo. 

El patrón Builder es muy flexible. Un único builder puede ser usado repetidamente para construir múltiples objetos. Los parámetros del builder pueden ser ajustados entre las invocaciones del método build para variar los objetos que se crean. Un builder puede rellenar algunos campos automáticamente al crear un objeto, como un número de serie que aumenta cada vez que se crea un objeto. 

El patrón Builder tiene desventajas también. Para crear un objeto, primero debes crear su builder. Aunque es poco probable que el coste de crear este builder se note en la práctica, podría ser un problema en situaciones de rendimiento crítico. Además, el patrón builder es más verboso que el patrón *constructor telescópico*, por lo que debe ser usado **sólo si hay suficientes parámetros para que valga la pena, digamos cuatro o más**. Pero si se empieza con constructores o fábricas estáticas y se cambia a un builder cuando la clase evoluciona hasta el punto de que el número de parámetros se sale de control, los constructores obsoletos o las fábricas estáticas sobresaldrán como un pulgar dolorido. Por lo tanto, a menudo es mejor empezar con un constructor en primer lugar.

En resumen, **el patrón Builder es una buena opción cuando diseñamos clases cuyos constructores o factorías estáticas tendrían más de un puñado de parámetros**, especialmente si muchos de los parámetros son opcionales o del mismo tipo. El código del cliente es más fácil de leer y escribir con builder que con constructores telescópicos, y los builders son más seguros que JavaBeans. 

