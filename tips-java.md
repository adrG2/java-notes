# TIPS JAVA

## División por 0

### Divides un double/float por 0

``` Java
    double p = 1;
    System.out.println(p/0);
```

En este caso el output es *Infinity*.

### Divides un entero por 0

``` Java
    int p = 1;
    System.out.println(p/0);
    // output
    // Exception in thread "main" java.lang.ArithmeticException: / by zero

```

## Excepciones checked vs unchecked

### Checked(marcadas)

Son las excepciones que son marcadas en tiempo de compilación. Si algún código lanza una excepción marcada, entonces el método debe manejar la excepción o debe especificar la excepción con la keyword throws.

```Java
import java.io.*;

class Main {
	public static void main(String[] args) {
		FileReader file = new FileReader("C:\\test\\a.txt");
		BufferedReader fileInput = new BufferedReader(file);
		
		for (int counter = 0; counter < 3; counter++)
			System.out.println(fileInput.readLine());
		
		fileInput.close();
	}
}

/*
Exception in thread "main" java.lang.RuntimeException: Uncompilable source code -
unreported exception java.io.FileNotFoundException; must be caught or declared to be
thrown
    at Main.main(Main.java:5)
*/

```

Para arreglar esto hay que usar throws o un bloque try catch.

### Unchecked(sin marcar)

Son excepciones no marcadas en tiempo de ejecución. Depende de los programdores espcificarla o capturarlas. En Java, las excepciones sin marcar son Error y RuntimeException. Todo lo relacionado con Throwable está marcado.

#### Ejemplo

```Java
class Main {
public static void main(String args[]) {
	int x = 0;
	int y = 10;
	int z = y/x;

}

/*
Exception in thread "main" java.lang.ArithmeticException: / by zero
    at Main.main(Main.java:5)
Java Result: 1
*/

```

