public class ParteA {
    private static final int NUMERO_PEDIDOS = 29; // N
    private static final int NUMERO_HILOS = 2; // C

    public static void main(String[] args) throws InterruptedException {
        // Primer pedido que le toca al siguiente hilo
        int inicioSiguiente = 1;

        // 'base' es lo que le toca como mínimo a cada hilo.
        // 'resto' es cuántos pedidos sobran, se reparten de uno en uno entre los primeros hilos
        int base = NUMERO_PEDIDOS/NUMERO_HILOS;
        int resto = NUMERO_PEDIDOS % NUMERO_HILOS;

        Thread[] hilos = new Thread[NUMERO_HILOS];

        // Cada hilo apunta su resultado en SU hueco del array y en ninguno más.
        // Como nadie pisa el hueco de otro, no hay carrera y no hace falta synchronized.
        int[] resultados = new int[NUMERO_HILOS];

        // Creamos las tareas
        for (int i=0; i< hilos.length; i++){
            int cantidad = base;
            if (i < resto){
                cantidad++; /// Este hilo es de los que se llevan uno de los pedidos sobrantes
            }
            int inicio = inicioSiguiente;
            int fin = inicio + cantidad - 1;
            inicioSiguiente = fin + 1; // el siguiente hilo empieza justo donde acaba este

            // Copias para la lambda: solo puede usar variables que no se vuelven a modificar.
            // 'i' cambia en cada vuelta, así que guardo su valor en 'posicion'.
            // 'inicio' y 'fin' los creo dentro del bucle para que cada hilo tenga los suyos.
            int posicion = i;

            hilos[i] = new Thread(() -> {
                int suma = 0;
                for (int pedido = inicio; pedido <= fin; pedido++){
                    suma += pedido;
                }
                resultados[posicion] = suma;
                System.out.println(Thread.currentThread().getName() + " PID: " + ProcessHandle.current().pid()
                        + " con rango: " + inicio + "-" + fin + " y resultado = " + suma);
            }, "HILO-" + (i + 1));
        }

        // Bucles separados: uno para arrancar con 'start()' y otro para esperar con 'join()', asi los hilos
        // trabajan a la vez.
        for (Thread hilo:hilos){
            hilo.start();
        }

        for (Thread hilo:hilos){
            hilo.join();
        }

        // Ahora leemos el Array, cuando todos los hilos ya han acabado, y el 'join()' nos garantiza que el main ve
        // lo que escribieron.
        int total = 0;
        for (int valor : resultados){
            total += valor;
        }

        // Verificación
        int formula = NUMERO_PEDIDOS * (NUMERO_PEDIDOS + 1)/2;

        if(total == formula){
            System.out.println("El total " + total + " es igual al resultado de la formula " + formula);
        }else {
            System.out.println("El total " + total + " no es igual al resultado de la formula " + formula);
        }
    }
}

// Explica por qué los trabajadores comparten PID y por qué el resultado final se consulta después de los 'join()'.
/*
Comparten PID porque los hilos son 'trabajadores' dentro del mismo proceso, por eso tienen el mismo PID que el proceso.
Si fueran varios procesos entonces cada proceso tendría su propio PID.
Porque el 'join()' hace que el hilo que lo llama espere a que termine otro, si el resultado lo pusiéramos después
del 'start()' los hilos aún no habrían terminado y leerían ceros. También el 'join()' garantiza que el main ve lo que el
hilo escribió en el array.
Importante saber que cada hilo se diferencia uno de otro por su nombre, no por su PID.
 */

/* NOTA: el orden de salida de los hilos es aleatorio porque 'start()' no ejecuta el hilo al instante, solo lo deja
listo y el sistema operativo es el que decide quien sale primero pero sin un orden fijo.
 */
