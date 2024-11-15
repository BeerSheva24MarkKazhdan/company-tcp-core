package telran.employees;
import telran.io.Persistable;

public class AutoSave extends Thread {
    private final Persistable persistable;
    private final String fileName;
    private final int saveIntervalMs;
    private volatile boolean running = true;

    public AutoSave(Persistable persistable, String fileName, int saveIntervalMillis) {
        this.persistable = persistable;
        this.fileName = fileName;
        this.saveIntervalMs = saveIntervalMillis;
    }

    @Override
    public void run() {
        while (running) {
            try {
                persistable.saveToFile(fileName);
                System.out.println("Employee data saved to " + fileName);
                Thread.sleep(saveIntervalMs);
            } catch (InterruptedException e) {
                System.err.println("AutoSave interrupted");
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void stopSaving() {
        running = false;
        this.interrupt();
    }
}