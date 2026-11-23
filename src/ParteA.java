public class ParteA {
    private static final int NUMERO_PEDIDOS = 29; // N
    private static final int NUMERO_HILOS = 3; // C

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
