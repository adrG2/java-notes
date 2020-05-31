# Item 3. Enforce the singleton property with a private constructor or an anum type

Un *singleton* es una clase que se instancia una única vez. Los Singleton suelen representar un objeto sin estado, como una función o un componente del sistema que es intrínsecamente único. Hacer que una clase sea un singleton puede dificultar el testing de sus clientes, porque es imposible sustituir una implementación mockeada de un singleton a menos que implemente una interfaz que sirva como su tipo.

Hay 2 maneras comunes de implementar un singleton. Ambos se basan en mantener el constructor privado y exportar un miembro estático de clase para dar acceso a la instancia.

## Singleton con constante estática pública

```Java
public class Home {
    public static final Home INSTANCE = new Home();

    private Home() {}
}
```

El constructor privado es llamado una única vez. Al hacer el constructor privado y mantener la instancia en una constante sólo existirá dicha instancia en todo el sisitema. Un cliente sólo podría instanciar otra instancia mediante reflection(*AccesibleObject.setAccessible*). Si necesitas prohibier esto último puedes modificar el constructor privado lanzando una excepción cuando te soliciten una segunda instancia.

## Singleton con factory static method

```Java
public class Home {
    private static final Home INSTANCE = new Home();

    private Home() {}
    
    public static Home getInstance() { return INSTANCE; }
}
```
Una llamada a Home.getInstance() devolverá la misma referencia, y no se creará ninguna otra instancia de Home.

Ventajas del uso de la constante estática pública: 
* Es que la API deja claro que la clase es un singleton: el campo estático público es *final*, así que siempre contendrá la misma referencia del objeto. 
* Es más simple

Ventajas del uso de static factory method:
* Flexibilidad para cambiar de opinición sobre si la clase debe seguir siendo singleton sin tener que cambiar su API. En un principio un static factory method estaría devolviendo una única instancia, pero podría ser modificado para devolver, por ejemplo, una instancia para cada hilo que lo invoque. Esto no afectaría de cara al exterior, es decir, al cliente.
* Puedes escribir una *generic singleton factory* si tu aplicación lo necesita.
* Puedes usar un *method reference* como supplier. Homer::instance es un Supplier<Home>. 

A menos que una de estas ventajas sea relevante, **es preferible usar un campo público**.

Para hacer serializable una clase singleton que use cualquier de estos enfoques, no es suficiente con añadir "implements Serializable" a su declaración. Para mantener la garantía singleton, hay que declarar todos los campos de instancia con la keyword *transient*(se usa para indicar que el campo no debería formar parte de la serialización) e implementar un método *readResolve*. De lo contrario, cada vez que la instancia serializada se deserialize se creará una nueva instancia. 

```Java 
// readResolve() para preservar la propiedad Singleton
private Object readResolve() {
    // Devuelve la verdadera Home y deja al recolector de basura que se encargue del imitador de Home
    return INSTANCE;
}
```

## Singleton enum type

Este enfoque es parecido al de campo público, pero es más conciso, provee la maquinaria de serialización gratis, y proporciona una garantía férrea contra la instanciación múltiple, incluso frente a sofisticados ataques de serialización o de reflexión. **Un single-element enum type suele ser la mejor forma de implementar un singleton**. No puedes usar este enfoque si tu singleton debe heredar de una superclase que no sea Enum(aunque se puede declarar un Enum para implementar las interfaces). 
