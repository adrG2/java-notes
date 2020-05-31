# Item 5. Prefer Dependency injection to hardwiring resources

Muchas clases dependen de uno o más recursos subyacentes. Por ejemplo un spell checker depdende de un diccionario. No es poco común ver tales clases implementadas como clases estáticas de utilidad(Util) o con singletons.

Ninguna de esos enfoques son satifsfactorios ya que asumen que hay unicamente un diccionario. En la práctica, cada lenguaje tiene su propio diccionario, y diccionarios especiales usados por vocabularios especiales. También, podríamos querer usar un diccinario especial para testear. Parece algo lógico que un único diccionario no será suficiente.

Podrías tratar de hacer que SpellChecker tenga soporte para múltiples diccionarios con un campo llamado 'dictionary' que no sea final y añadiendo un método para cambiar el diccionario de un spell checker existente, pero esto sería algo torpe, propenso a errores, e inviable en un entorno concurrente.

**Las clases estáticas de utilidad y los singleton son inapropieados para las clases cuyo comportamiento está parametrizado por un recurso subyacente**

¿Cómo soportar múltiples instancias de la clase y que cada una de las cuales utilice el recurso deseado por el cliente? **Pasando el recurso dentro del constructor cuando creas una nueva instancia**. Esto es una forma de *dependency injection*. 

```Java
public class SpellCheker {
    private final Lexicon dictionary;

    public SpellChecker(Lexicon dictionary) {
        this.dictionary = Objects.requireNonNull(dictionary);
    }
}
```

El patrón de inyección de dependencias es tan simple que muchos programadores lo han usado durante años sin tan siquiera saber si tiene un nombre. DI funciona con un arbitrario número de recursos y grafos de depedencias. Preserva la **inmutabilidad**, así *múltiples clientes pueden compartir objetos dependientes*(asumiendo que los lcientes desean el mismo recurso subyacente). **DI es igualmente aplicable a constructores, static factories y builders**.

Una variante del patrón es pasar una **factoría de recurso** al constructor. Una factoría es un objeto que puede ser llamado varias veces para crear instancias de un tipo(Facory Method pattern). La interface **Supplier<T>** es perfecta para representar factorías. Los métodos que reciben por parámetro una **Supplier<T>** deberían restingir el type parameter usando un *bounded wildcard type* para permitir que el cliente pase una factoría que cree cualquier subtipo de un tipo específico. 

```Java
Room create(Supplier<? extends Fitment> fitmentFactory) {
    //
}
```

A pesar de que la inyección de dependencias mejora la flexibilidad y la testabilidad, puede desordenar grandes proyectos, los cuales contienen cientos de dependencias. Este desorden puede ser casi eliminado usando un framework de DI, como Dagger, Guice o Spring. 

En resumen, no uses singleton ni clases de utilidad para implementar una clase que dependa de uno o más recursos subyacentes cuyo comportamiento afecta al de la clase, y no hacen que la clase cree estos recursos directamente. En vez de eso, pasa los recursos, o factorías para crearlos, dentro del constructor(o static factory o builder). 

Usar DI mejorará enormemente:
* Flexibilidad
* Reutilización
* Testabilidad

