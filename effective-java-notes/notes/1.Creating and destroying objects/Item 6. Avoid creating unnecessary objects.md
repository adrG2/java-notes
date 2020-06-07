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

Si la clase que contienen la versión mejorar de *isRomanNumeral* se inicializa pero el método nunca es invocado, el campo **ROMAN** será inicializado innecesariamente. Sería posible eliminar la inicialización con *lazily initializing* 
