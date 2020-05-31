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

El patrón de inyección de dependencias es tan simple que muchos programadores lo han usado durante años sin tan siquiera saber si tiene un nombre. DI funciona con un arbitrario número de recursos y grafos de depedencias. Preserva la inmutabilidad, así múltiples clientes pueden compartir objetos dependientes(asumiendo que los lcientes desean el mismo recurso subyacente). DI es igualmente aplicable a constructores, static factories y builders.

