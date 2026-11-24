# AUTOR: Miguel Angel Hernández Fernández

## Valores

**P = número de letras de tu primer apellido**
* P = `9 (Hernández)`

**Número de pedidos**
* N = 20 + P
* N = 20 + 9 = `29 pedidos`

**Número de consumidores**
* C = 2 + (P % 3)
* C = 2 + (9 % 3) = `2 consumidores`

**Capacidad de la cola**
* Capacidad = 3

## Version de Java:
* openjdk 17.0.21.1 2026-11-17

## Estructura

* PSP_PracticaUD2/
  * src/
    * ParteA.java -> Reparto de rangos entre hilos
    * ParteB.java -> Pausa y cancelación cooperativa
    * ParteC.java -> Contador inseguro y seguro
    * ParteD.java -> Central de pedidos (modo normal y error)
  * EJERCICIO.md -> Enunciado de la práctica
  * README.md  
  * PRUEBAS.md -> Evidencias y respuestas razonadas
  * USO_IA.md  -> Declaración de uso o no uso de IA
  * .gitignore

## Comandos

* Parte A: 
  * javac -d out src/ParteA.java
  * java -cp out ParteA
  * Para realizar pruebas del funcionamiento solamente se debe cambiar los valores de las constantes `NUMERO_PEDIDOS` y `NUMERO_HILOS` y darle al Play o usar los comandos anteriormente citados.

* Parte B:
  * javac -d out src/ParteB.java 
  * java -cp out ParteB

* Parte C:




