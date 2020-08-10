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

