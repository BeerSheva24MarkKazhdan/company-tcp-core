package telran.io;

public class PersistableSaverThread extends Thread{
    private Persistable persistable;
    private String fileName;
    private long timeInterval;
    public PersistableSaverThread(Persistable persistable, String fileName, long timeInterval) {
        this.persistable = persistable;
        this.fileName = fileName;
        this.timeInterval = timeInterval;
        setDaemon(true);
    }
    @Override
    public void run(){
        try {
            while(true) {
                persistable.saveToFile(fileName);
                sleep(timeInterval);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}