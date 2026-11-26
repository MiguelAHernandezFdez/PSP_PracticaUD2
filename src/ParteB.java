import java.util.concurrent.CountDownLatch;

public class ParteB {
    static class Trabajador implements Runnable{
        // El mismo monitor protege todas las lecturas y escrituras de la variable 'pausado'
        private final Object monitor = new Object();

        // El trabajador empieza pausado
        private boolean pausado = true;

        // Pestillo de un solo uso. Sirve para que el main sepa que el trabajador ya ha llegado a su
        // primera pausa, sin tener que adivinarlo con un sleep().
        private final CountDownLatch puertaInicial = new CountDownLatch(1);

        // El main se queda aquí bloqueado hasta que el trabajador haga 'countDown()'
        void puertaPausaInicial() throws InterruptedException {
            puertaInicial.await();
        }

        void reanudar() {
            // Adquiero la variable que es el monitor que protege el pausado
            synchronized (monitor) {
                // La condicion permite continuar
                pausado = false;

                // Despierto al trabajador, sin este aviso el trabajador se quedaría dormido para siempre
                // aunque 'pausado' sea false.
                monitor.notifyAll();
            }
        }

        void pausar() {
            synchronized (monitor) {
                // Aquí no hace falta 'notifyAll()', ya que el trabajador no está dormido
                // El synchronized sigue siendo necesario para que el hilo vea el valor actualizado.
                pausado = true;
            }
        }

        @Override
        public void run() {
            int pedido = 0;

            try {
                // Continúa mientras no esté interrumpido
                while (!Thread.currentThread().isInterrupted()){
                    // Adquiero el monitor para mirar el 'pausado'
                    synchronized (monitor){
                        // Usamos un while para comprobar la condición después del despertar
                        while (pausado) {
                            // Aviso al main de que he llegado a la pausa inicial (solo cuenta la primera vez).
                            // Como sigo dentro del synchronized, el main no puede reanudarme hasta que
                            // wait() suelte el monitor, así que no se pierde el aviso.
                            puertaInicial.countDown();

                            // Se pausa y liberamos el monitor, si no no se podría reanudar después.
                            monitor.wait();
                        }
                    }
                    pedido++;
                    System.out.println("Pedido " + pedido + " del " + Thread.currentThread().getName());
                    // Pausar para una mejor ejecución y de tiempo a observar los estados.
                    // Si 'sleep()' estuviera dentro del synchronized, pausar() y reanudar() se quedarían bloqueados
                    // esperando el monitor mientras el trabajador duerme, porque sleep no lo suelta.
                    Thread.sleep(200);
                }
            } catch (InterruptedException e){
                // Realizo la interrupción del hilo
                Thread.currentThread().interrupt();
                System.out.println("Trabajador interrumpido.");
            } finally {
                // Pase lo que pase siempre se imprime esto al salir
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
            // Esperamos a que esté en la primera pausa
            tarea.puertaPausaInicial();

            System.out.println("Tras llegar a la pausa inicial: " + trabajador.getState());

            System.out.println("REANUDAR.");

            tarea.reanudar();

            // Dejo trabajando el hilo un segundo.
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
            // Esto va en el finally para que el trabajador se pare siempre, incluso si algo falla arriba.
            // Si no, se quedaría vivo en segundo plano y el programa podría no terminar nunca.
            // interrupt() lo despierta tanto si está en sleep() como en wait().
            trabajador.interrupt();
            // Espero a que termine de verdad antes de mirar su estado final
            trabajador.join();

            System.out.println("Estado final: " + trabajador.getState());
        }


    }
}

// Explica por qué una consulta a `getState()` es una observación puntual.
/*
Porque `getState()` te dice como estaba el hilo justo en ese momento, pero después de la consulta el hilo sigue avanzando
y puede cambiar sin avisar. Como en este caso que tras solicitar la pausa el hilo estaba `TIMED_WAITING`, pero justo
después de 700 ms el estado pasa a `WAITING`.
Por eso, el estado sirve para observar y registrar pero no para tomar decisiones de sincronización.
 */
