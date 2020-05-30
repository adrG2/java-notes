# Item 2. Consider a builder when faced with constructor parameters

Las factorias estáticas y los constructores comparten una limitación: no escalan bien cuando hay un número importante de parámetros opcionales. 

¿Cómo gestionar este tipo de clases que tienen varios parámetros opciones?

Tradicionalmente se ha uso el conocido patrón *telescoping constructor*. Este patrón consiste en tener un constructor con los parámetros obligatorios, y para ir añadiendo los parámetros opcionales se usa la sobrecarga de constructor. 

![car class](https://github.com/adrG2/java-notes-spanish/blob/master/effective-java-notes/code/1.Creating%20and%20destroying%20objects/Item2/car.png)