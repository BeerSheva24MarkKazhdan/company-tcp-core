package telran.employees;

import telran.io.Persistable;
import telran.io.PersistableSaverThread;
import telran.net.TcpServer;

public class Main {
    private static final String FILE_NAME = "employees.data";
    private static final int PORT = 4000;
    private static final long TIME_INTERVAL = 60000;

    public static void main(String[] args) {
        Company company = new CompanyImpl();
        if (company instanceof Persistable persistable) {
            persistable.restoreFromFile(FILE_NAME);
            PersistableSaverThread saverThread = new PersistableSaverThread(persistable, FILE_NAME, TIME_INTERVAL);
            saverThread.start();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> persistable.saveToFile(FILE_NAME)));
        }

        TcpServer tcpServer = new TcpServer(new CompanyProtocol(company), PORT);
        tcpServer.run();
    }
}