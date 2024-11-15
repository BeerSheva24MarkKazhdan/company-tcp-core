package telran.employees;
import telran.io.Persistable;
import telran.net.*;

public class Main {
    private static final String FILE_NAME = "employees.data";
    private static final int PORT = 4000;
    private static final int SAVE_INTERVAL_MS = 60000;
    
        public static void main(String[] args) {
            Company company = new CompanyImpl();
            if (company instanceof Persistable persistable) {
                persistable.restoreFromFile(FILE_NAME);
                AutoSave autoSave = new AutoSave(persistable, FILE_NAME, SAVE_INTERVAL_MS);
                autoSave.start();

                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    System.out.println("Shutting down auto-save...");
                    autoSave.stopSaving();
                }));
        }

        TcpServer tcpServer = new TcpServer(new CompanyProtocol(company), PORT);
        tcpServer.run();
}
}