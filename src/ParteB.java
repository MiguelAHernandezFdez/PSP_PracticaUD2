import java.util.concurrent.CountDownLatch;

public class ParteB {
    static class Trabajador implements Runnable{
        private final Object monitor = new Object();

        // El trabajador empieza pausado
        private boolean pausado = true;

        private final CountDownLatch puertaInicial = new CountDownLatch(1);

        void puertaPausaInicial() throws InterruptedException {
            puertaInicial.await();
        }

        void reanudar() {
            // Adquiero la variable que es el monitor que protege el pausado
            synchronized (monitor) {
                // La condicion permite continuar
                pausado = false;

                // Avisamos a quien esperan este monitor
                monitor.notifyAll();
            }
        }

        void pausar() {
            synchronized (monitor) {
                pausado = true;
            }
        }

        @Override
        public void run() {
            int pedido = 0;

            try {
                // Continuamos mientras no este interrumpido
                while (!Thread.currentThread().isInterrupted()){
                    // Adquiero el monitor
                    synchronized (monitor){
                        // Usamos un while para comprobar la condicion despues del despertar
                        while (pausado) {
                            // Señalo la primera llegada
                            puertaInicial.countDown();

                            // Liberamos el monitor
                            monitor.wait();
                        }
                    }
                    pedido++;
                    System.out.println("Pedido " + pedido + " del " + Thread.currentThread().getName());
                    // Pausar para una mejor ejecucion
                    Thread.sleep(200);
                }
            } catch (InterruptedException e){
                // Realizo la interrupcion del hilo
                Thread.currentThread().interrupt();
                System.out.println("Trabajador interrumpido.");
            } finally {
                System.out.println("Trabajador finalizado.");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // Creamos el hilo que ejecuta la clase Trabajador
        Trabajador tarea = new Trabajador();
        Thread trabajador = new Thread(tarea, "Revisor-1");

        System.out.println("Antes de arrancar: " + trabajador.getState());

        // Arranco el trabajador
        trabajador.start();

        try {
            // Esperamos a que este en la primera pausa
            tarea.puertaPausaInicial();

            System.out.println("Tras llegar a la pausa inicial: " + trabajador.getState());

            System.out.println("REANUDAR.");

            tarea.reanudar();

            Thread.sleep(1000);

            System.out.println("Después de reanudar: " + trabajador.getState());

            System.out.println("PAUSAR:");

            tarea.pausar();

            System.out.println("Tras solicitar la pausa: " + trabajador.getState());

            Thread.sleep(700);

            System.out.println("Después de volver a pausar: " +trabajador.getState());

            System.out.println("REANUDAR:");

            tarea.reanudar();

            Thread.sleep(600);

        } finally {
            trabajador.interrupt();
            trabajador.join();

            System.out.println("Estado final: " + trabajador.getState());
        }


    }
}

// Explica por qué una consulta a `getState()` es una observación puntual.
/*
Porque `getState()` te dice como estaba el hilo justo en ese momento, pero después del mensaje el hilo sigue avanzando
y puede cambiar sin avisar. Como en este caso que tras solicitar la pausa el hilo estaba `TIMED_WAITING`, pero justo
después de 700 ms el estado pasa a `WAITING`.
 */
