# Item 6. Avoid creating unnecessary objects

Suele ser apropiado reutilizar un objeto en vez de crear una nuevo objeto con una funcionalidad equivalente cada vez que sea necesario. Reutilizar puede ser más rápido y estiloso. Un objeto siempre pude ser reusado si es inmutable.

```Java
String s = new String("bikini"); //No hagas esto
```

Esta sentencia crea una nueva instancia de String cada vez que se ejecuta, y no son necesarios ninguno de esos objetos creados. El argumento del constructor es en sí mismo otra instancia de String. Si esto ocurriese en un bucle o en un método al que se llama frecuentemente, se crearían millones de instancias de String innecesariamente. 

```Java
String s = "bikini";
```
Esta versión usa una única instancia de String. Esta versión garantiza que el objeto será reutilizado por cualquier otro código que se ejecute en la misma máquina virtual y que contenga la misma cadena literal.

Puedes evitar crear objetos innecsarios usando los *static factory methods* en lugar de constructores. Por ejemplo, el método estático de factoría *Boolean.valueOf(String)* es mejor que el constructor *Boolean(String)*. El constructor debe crear un nuevo objeto cada vez que se llama, mientras que un método de factoria nunca lo debería hacer en la práctica. También puedes reutilizar objetos mutables si sabes que no serán modificados.

La creación algunos objetos es más costosa que la de otros. Si vas a necesitar un objeto "costos" varias veces, puede ser aconsejable cachearlo para reutilizarlo. Desafortunadamente, eso no siempre es tan obvio cuando se crea un objeto así. Supongamos que quieres validar si un string es una número romano. 

``` Java
// La performance puede ser mejorada enormemente
static boolean isRomanNumeral(String s) {
    return s.matches(REGEX_ROMAN_NUMERAL)
}
```
El problema de esta implementación es que se basa en el método String.matches. **Mientras que String.matches es la manera más sencilla para checkear una expresión regular, no es adecuado para un uso repetido en situaciones críticas de performance**. El problema es que internamente crea una instancia de **Pattern** para la expresión regular y la usa una única vez, tras lo cual se convierte en *elegible* para el recolector de basura. Crear una instancia de **Pattern** es caro porque es necesario compilar la expresión regular en una *máquina de estados finitos*. 

Para mejorar la performance, compila la expresión regular en una instancia de **Pattern** como parte de la inicialización de la clase, almacenala en caché y reutiliza la misma instancia para cada invocación del método *isRomanNumeral*:

```Java
// Reutilizar el objeto costoso para mejorar la performance
public class RomanNumerals {
    private static final Pattern ROMAN = Pattern.compile(REGEX);

    static boolean isRomanNumeral(String s) {
        return ROMAN.matcher(s).matches();
    }
}
```
La versión mejorada de *isRomanNumeral* es 6.5 veces más rápida. No sólo se mejora la performance, podría decirse, que también se mejora la claridad. La creación de un campo final estático(static final field) para la instancia de **Pattern**, que de otro modo sería invisible, nos permite darle un nombre, que es mucho más legible que la propia expresión regular.

Si la clase que contienen la versión mejorada de *isRomanNumeral* se inicializa pero el método nunca se invoca, el campo **ROMAN** será inicializado innecesariamente. Sería posible eliminar la inicialización con *lazily initializing* la primera vez que el campo es llamado a través de la invocación de *isRomanNumeral*, pero esto no es recomendable. Como suele pasar con la inicialización lazy, se añade una capa de complicación que no reporta una sustancial mejora de rendimiento.

Cuando un objeto es inmutable, se puede reutilizar de forma segura, pero hay otras situaciones en las que es mucho menos obvio, incluso contraintuitivo. Considera el caso de los *adapters*, también conocidas como *vistas*. Un adapter es un objeto que delega a un objeto de soporte, que proporciona una interface alternativa. Debido a que un adapter no tiene estado más allá del de su objeto de soporte, no hay necesidad de crear más de una instancia de un adapter.

Por ejemplo el método *keySet* de la interface *Map*  devuelve una vista *Set* del objeto *Map*, que consta de todas las keys del mapa. Ingenuamente, parecería que cada llamada a keySet tendría que crear una nueva instancia de *Set*, pero cada llamada a *keySet* para un mismo objeto *Map* debe devolver la misma instancia de *Set*. A pesar de que la instancia de *Set* devuelta es mutable, todos los objetos devueltos son funcionalmente idénticos: cuando uno de los objetos devueltos cambia también lo hacen el resto ya que todo pertenecen a la misma intancia de *Map*. 

Otra manera de crear objetos innecesariamente es con **autoboxing**, el cuál permite al programador mezclar tipos primitivos y primitivos *boxed*, haciendo boxing y unboxing de forma automática según sea necesario. **El autoboxing nubla pero no elimina la distinción entre tipos primitivos y primitivos boxed**. Hay sutiles distinciones semánticas y diferencias de rendimiento no tan sutiles. 

``` Java
private static long sum() {
    Long sum = 0L;
    for (long i = 0; i <= Integer.MAX_VALUE; i++) {
        sum += i;
    }

    return sum;
}
```
Este programa funciona pero es mucho más lento de lo que debería ser, debido a un error tipográfico de un carácter. La variable *sum* es declarada como un **Long** en vez de un **long**, lo que significa que el programa construya unas 2<sup>31</sup> veces innecesariamente la instancia de Long (más o menos una cada vez que el long i se añade al Long sum). Cambiando la declaración de sum de un Long a un long reduces el tiempo de ejecución de 6.3 segundos a 0.59. La lección es clara: **Es preferible usar tipos primitivos a primitivos boxed, y ten cuidado con los autoboxing desintencionados**. 

Este artículo no debe ser malinterpretado para dar a entender que la creación de objetos es costosa y debe ser evitada. Por el contrario, la creación y recuperación de pequeños objetos cuyos constructores hacen poco trabajo explícito es barata, especialmente en las modernas implementaciones de JVM. La creación de objetos adicionales para mejorar la claridad, la simplicidad o la potencia de un programa es generalmente algo bueno.

Por el contrario, evitar la creación de objetos manteniendo su propia pool de objetos es una mala idea, a menos que los objetos de la pool sean extremadamente pesados. El ejemplo clásico de un objeto que sí justifica una pool de objetos es una conexión a una base de datos. El coste de establecer la conexión es lo suficientemente alto como para que tenga sentido reutilizar estos objetos. Sin embargo, en términos generales, mantener tu propia pool de objetos desordena tu código, aumenta la huella en memoria(footprint memory) y perjudica el rendimiento. Las implementaciones modernas de la JVM tienen una alta optimización de los recolectores de basura que superan facilmente a las pools de objetos ligeros. 

El contrapeso a este item es el Item 50 en "defensive copying". El presente item dice, "no crees un nuevo objeto cuando deberías reutilizar uno existente", mientras que el Item 50 dice, "No reutilices un objeto existente cuando deberías crear un nuevo". Obsérvese que la pena por reutilizar un objeto cuando se requiere una copia defensiva es mucho mayor que la pena por crear innecesariamente un objeto duplicado. No hacer copias defensivas cuando se requiere puede llevar a insidiosos errores y agujeros de seguridad; crear objetos innecesariamente sólo afecta al estilo y al rendimiento.
