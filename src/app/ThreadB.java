package app;


/**
 *
 * @author Yonatha Almeida
 */
public class ThreadB extends Thread {

    int total;

    @Override
    public void run() {
        synchronized (this) {
            for (int i = 0; i < 999999999; i++) {
                total += i;
            }
            notify();
        }
    }
}
