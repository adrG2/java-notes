# Item 4. Enforce noninstantiability with a private constructor

En ocasiones, podrías querer escribir una clase que sólo tiene métodos y campos estáticos. Ese tipo de clases han adquirido una mala reputación porque algunos han hecho un mal uso de ellas para evitar pensar en términos de objetos, pero, sí que tienen usos válidos: 

* Agrupar métodos relacionados por valores primitivos o arrays(Ej: java.util.Arrays). 
* Agrupar métodos estáticos, incluyendo factorías, para objetos que implementan algún tipo de interface como java.util.Collections.
* Agrupar métodos en una clase *final* para que no se puedan usar en alguna subclase.

Las *utiliy classes* no están diseñadas para que puedan instanciarse. En ausencia de constructor, sin embargo, el compilador provee un público y sin parámetros constructor por defecto. Para un usuario, este constructor es indistinguible de cualquier otro. 

**Intentar hacer cumplir la no instaciabilidad haciendo la clase abstracta no funciona**. La clase puede ser heredara, y en la subclase ser instanciada. Además, engaña al usuario haciéndole pensar que la clase fue diseñada para la herencia. **Para hacer una clase no instanciable puedes incluir un constructor privado**.

```Java
public class UtilityClass {
    private UtilityClass() {
        throw new AssertionError();
    }
}
```

Ahora es inaccesible desde fuera de la clase. El AssertionError no es estrictamente necesario pero da la seguridad de que el constructor no va a ser invocado por accidente dentro de la clase. Garantiza que la clase nunca será instanciada. 

Como efecto secundario, esto también previene que la clase sea heredada. Todos los constructores deben invocar un constructor de su superclase, explícitamente o implícitamente, y una subclase no tendría ningún constructor accesible de una superclase para invocar. 