public class ParteA {
    private static final int NUMERO_PEDIDOS = 29; // N
    private static final int NUMERO_HILOS = 2; // C

    public static void main(String[] args) throws InterruptedException {
        int inicioSiguiente = 1;
        int base = NUMERO_PEDIDOS/NUMERO_HILOS;
        int resto = NUMERO_PEDIDOS % NUMERO_HILOS;

        Thread[] hilos = new Thread[NUMERO_HILOS];

        int[] resultados = new int[NUMERO_HILOS];

        // Creamos las tareas
        for (int i=0; i< hilos.length; i++){
            int cantidad = base;
            if (i < resto){
                cantidad++;
            }
            int inicio = inicioSiguiente;
            int fin = inicio + cantidad - 1;
            inicioSiguiente = fin + 1;
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

        for (Thread hilo:hilos){
            hilo.start();
        }

        for (Thread hilo:hilos){
            hilo.join();
        }

        int total = 0;
        for (int valor : resultados){
            total += valor;
        }

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
Porque el 'join()' hace que el hilo que lo llama espere a que termine otro, si lo el resultado lo pusiéramos después
del 'start()' los hilos aún no habrían terminado y leerían ceros.
 */

/* NOTA: el orden de salida de los hilos es aleatorio porque 'start()' no ejecuta el hilo al instante, solo lo deja
listo y el sistema operativo es el que decide quien sale primero pero sin un orden fijo.
 */
