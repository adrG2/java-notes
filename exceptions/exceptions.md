# ¿Qué es una Exception?

Una excepción es un evento que sucede durante la ejecución de un programa que interrumpe el flujo normal de las intrucciones de un programa.

Cuando se produce un error dentro de un método, el método crea un objeto(Exception) y lo entrega al sistema de ejecución. Ese objeto contiene información sobre el error, incluido su tipo y el estado del programa cuando ocurrió el error. Esto sería lo conocido como *lanzar una excepción*. 

Cuando un método lanza una excepción, el sistema de ejecución intenta encontrar algo para manejarla. Ese "algo" es una lista ordenada de métodos que han sido llamados para llegar al método en el que se produjo el error. Esta lista de métodos se conoce como *call stack*.

El sistema de ejecución va buscando por el call stack un método que contenga un bloque de código que maneje la excepción. Este bloque de código se conoce como *exception handler*. La búsqueda comienza con el método dónde ocurrió el error y va subiendo por el call stack en orden inverso al orden en el que se ejecutaron los métodos. Cuando encuentra el sistema de ejecución encuentra un handler apropiado para la excepción se la pasa. Se considera que un exception handler es apropieado si el tipo de excepción lanzada coincide con el tipo que puede manejar ese handler. 

El exception handler ese el que captura la excepción. Si el sistema de ejecución no encuentra un handler adecuado, el sistema en tiempo de ejecución y por tanto el propio programa finaliza.
