# PRÁCTICA UD2

## 1. Objetivo

Desarrollar una aplicación Java de consola que simule una central de procesamiento de pedidos utilizando los mecanismos de concurrencia trabajados en clase.

Se valorará que el programa funcione, que puedas explicar sus decisiones y que demuestres mediante pruebas cómo se comportan sus hilos.

## 2. Condiciones de realización

El diseño, la programación, las pruebas y la documentación deben realizarse individualmente. Puedes consultar los ejemplos de clase y la documentación de Java, indicando las fuentes utilizadas.

El uso de IA queda limitado a aclarar conceptos y ayudar a interpretar mensajes de error. No se permite generar con ella la solución completa, clases o métodos de la práctica, ni la memoria o las respuestas de la defensa.

Si utilizas IA dentro de estos límites, registra en `USO_IA.md`:

- La consulta realizada.
- Qué información te resultó útil.
- Cómo la comprobaste y aplicaste.

Si no la utilizas, indícalo en ese mismo archivo. El uso declarado dentro de los límites permitidos no penaliza.

Debes comprender y poder modificar todo el código entregado.

## 3. Personalización

Calcula tu parámetro personal:

**P = número de letras de tu primer apellido, sin contar espacios ni guiones.**

Incluye en el `README.md` el valor obtenido. Utilizarás:

- Número de pedidos: **N = 20 + P**.
- Número de consumidores: **C = 2 + (P % 3)**.
- Capacidad de la cola: **3**.

Los pedidos estarán identificados con los números del 1 al N. Estos parámetros personalizan la ejecución; la evaluación también tendrá en cuenta tu proceso y la defensa.

## 4. Trabajo que debes desarrollar

### Parte A. Reparto de trabajo entre hilos

Distribuye los identificadores del 1 al N entre C hilos para calcular su suma.

**Requisitos:**

- Cada hilo debe tener nombre y un rango propio.
- Los rangos deben cubrir todos los identificadores, sin repetir ni omitir ninguno, aunque N no sea divisible entre C.
- Cada trabajador guardará su resultado en una posición independiente de un array.
- Inicia todos los hilos antes de esperar mediante `join()`.
- El hilo principal sumará los resultados parciales y los comparará con N × (N + 1) / 2.
- Muestra el nombre, PID, rango y resultado parcial de cada trabajador.

Explica por qué los trabajadores comparten PID y por qué el resultado final se consulta después de los `join()`.

### Parte B. Pausa y cancelación cooperativa

Implementa un trabajador que simule la revisión de pedidos y permita:

1. Comenzar pausado.
2. Reanudar su actividad.
3. Recibir una nueva solicitud de pausa.
4. Reanudarse otra vez.
5. Finalizar mediante `interrupt()`.

**Requisitos:**

- Utiliza un monitor, `synchronized`, `wait()` y `notifyAll()`.
- Comprueba la condición de pausa mediante un `while`.
- Protege con el mismo monitor todas las lecturas y escrituras de la variable de pausa.
- Usa un `CountDownLatch` para señalar que el trabajador ha llegado a la puerta de pausa inicial.
- Trata correctamente `InterruptedException`.
- Espera su finalización con `join()`.

Puedes utilizar `sleep()` para separar visualmente las fases, pero no como garantía de sincronización. La pausa es cooperativa: puede completarse una operación que ya estaba en curso.

Registra los estados observados y explica por qué una consulta a `getState()` es una observación puntual.

### Parte C. Condición de carrera y corrección

Crea una demostración independiente con dos hilos que intenten incrementar un mismo contador 1.000 + P veces cada uno.

Entrega dos versiones:

- **Insegura:** separa la lectura y la escritura y utiliza `CyclicBarrier`, como en clase, para provocar actualizaciones perdidas de forma reproducible.
- **Segura:** protege el incremento mediante `synchronized`.

Muestra el resultado obtenido y el esperado en ambas versiones.

Explica con un ejemplo de dos incrementos cómo se pierde una actualización y por qué `volatile` no convierte `contador++` en una operación atómica.

### Parte D. Productor y consumidores

Implementa la central de pedidos con:

- Un hilo productor.
- C consumidores.
- Una `ArrayBlockingQueue<Integer>` de capacidad 3.
- Contadores compartidos seguros para la cantidad procesada y la suma de identificadores.

El productor introducirá los pedidos del 1 al N. Los consumidores los retirarán y registrarán qué hilo procesa cada identificador.

**Modo normal:**

- Usa `put()` y `take()`.
- Cada pedido debe procesarse exactamente una vez.
- Utiliza `-1` como centinela, fuera del conjunto de datos válidos.
- Introduce un centinela por consumidor después de todos los pedidos.
- Comprueba que la cantidad final sea N y la suma sea N × (N + 1) / 2.
- Verifica además que ningún identificador se haya omitido o repetido. Puedes registrar las frecuencias en un array protegido mediante `synchronized`; la cantidad y la suma por sí solas no bastan para demostrarlo.

**Modo error:**

- Provoca una excepción en el productor justo antes de introducir el pedido 5.
- Registra el fallo mediante `UncaughtExceptionHandler`.
- Utiliza esperas con límite para detectar trabajadores que no terminan.
- Interrumpe los hilos que sigan activos y espera su finalización.
- Muestra que la ejecución ha fallado, sin presentar los resultados parciales como un procesamiento completo.

No debe quedar ningún hilo bloqueado indefinidamente. Recuerda que `join(tiempo)` limita la espera, pero no termina el hilo.

## 5. Restricciones técnicas

Utiliza los mecanismos trabajados en clase.

No se permite sustituir el desarrollo por `ExecutorService`, `CompletableFuture`, streams paralelos o bibliotecas externas de concurrencia.

Tampoco se permite:

- Usar `Thread.stop()`, `suspend()` o `resume()`.
- Hacer espera activa para consultar continuamente si un hilo puede avanzar.
- Capturar excepciones y ocultarlas sin explicar ni gestionar lo ocurrido.
- Suponer un orden fijo de ejecución de los consumidores.

La pausa de la parte B puede realizarse como demostración independiente; no es obligatorio integrarla en la central de pedidos.

## 6. Pruebas y análisis

Incluye en `PRUEBAS.md` los comandos utilizados, resultados reales y una explicación breve de estas comprobaciones:

| Prueba | Qué debes comprobar |
|---|---|
| Reparto de rangos | No hay huecos ni solapamientos y la suma coincide con el control. |
| Pausa y reanudación | El trabajador espera, vuelve a avanzar y termina al interrumpirlo. |
| Contador inseguro | La demostración provoca actualizaciones perdidas. |
| Contador seguro | El resultado coincide con el número total de incrementos. |
| Central en modo normal | Todos los identificadores se procesan exactamente una vez. |
| Central en modo error | Se registra el fallo y finalizan todos los trabajadores. |
| Tres ejecuciones normales | Los resultados finales son correctos aunque pueda variar el reparto entre consumidores. |

Las salidas deben proceder de tu programa. Si observas un comportamiento inesperado, documéntalo y explica cómo lo investigaste.

Responde también:

1. ¿Qué ocurriría si ejecutaras `run()` directamente en lugar de `start()`?
2. ¿Qué efecto tendría hacer `start()` y `join()` de cada trabajador antes de iniciar el siguiente?
3. ¿Por qué `wait()` debe comprobar su condición dentro de un `while`?
4. ¿Por qué se necesita un centinela por consumidor?
5. ¿Qué puede pasar si el productor falla antes de introducir los centinelas?
6. ¿Por qué el consumidor que procesa más pedidos puede cambiar entre ejecuciones?

Relaciona cada respuesta con tu implementación.

## 7. Entrega mediante GitHub

La entrega consiste en pegar en la plataforma el enlace al repositorio de GitHub que contiene tu práctica.

El repositorio debe incluir:

- Código fuente completo.
- `README.md`: autor, valor de P, versión de Java utilizada, estructura y comandos exactos para compilar y ejecutar cada parte y ambos modos.
- `PRUEBAS.md`: evidencias y respuestas razonadas.
- `USO_IA.md`: declaración del uso o no uso de IA.
- `.gitignore` para excluir archivos compilados y archivos temporales del entorno.

Realiza commits durante el desarrollo con mensajes que describan avances reales. El historial se utilizará como apoyo para revisar el proceso; el número de commits no determina por sí solo la autoría ni la calificación.
