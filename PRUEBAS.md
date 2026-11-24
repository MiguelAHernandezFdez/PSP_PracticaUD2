# Parte A

He realizado unos pruebas cambiando los valores de N (NUMERO_PEDIDOS) y C (NUMERO_HILOS) para comprobar si se reparten bien los rangos y que el total coincida, he usado los siguientes datos:

1) N=23 y C=4
   * HILO-4 PID: 54273 con rango: 19-23 y resultado = 105 
   * HILO-3 PID: 54273 con rango: 13-18 y resultado = 93 
   * HILO-2 PID: 54273 con rango: 7-12 y resultado = 57 
   * HILO-1 PID: 54273 con rango: 1-6 y resultado = 21 
   * El total 276 es igual al resultado de la formula 276

2) N=29 y C=3
   * HILO-2 PID: 54554 con rango: 11-20 y resultado = 155 
   * HILO-3 PID: 54554 con rango: 21-29 y resultado = 225 
   * HILO-1 PID: 54554 con rango: 1-10 y resultado = 55 
   * El total 435 es igual al resultado de la formula 435

Como se puede apreciar en los resultados de las pruebas los rangos se reparten bien y el total coinciden.

# Parte B

En esta Parte no hay ningún valor que cambiar para realizar pruebas, simplemente debería de salir los estados y mensajes según el código:

+ Antes de arrancar: `NEW` 
+ Tras llegar a la pausa inicial: `WAITING` 
+ Pedido 1 del Revisor-1 
+ Pedido 2 del Revisor-1 
+ Pedido 3 del Revisor-1 
+ Pedido 4 del Revisor-1 
+ Pedido 5 del Revisor-1 
+ Después de reanudar: `TIMED_WAITING` 
+ Tras solicitar la pausa: `TIMED_WAITING` 
+ Después de volver a pausar: `WAITING`  
+ Pedido 6 del Revisor-1 
+ Pedido 7 del Revisor-1 
+ Pedido 8 del Revisor-1 
+ Trabajador interrumpido. 
+ Trabajador finalizado. 
+ Estado final: `TERMINATED`

# Parte C