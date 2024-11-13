package telran.employees;
import telran.io.Persistable;
import telran.net.*;

public class Main {
    private static final String FILE_NAME = "employees.data";
    private static final int PORT = 4000;
    
        public static void main(String[] args) {
            Company company = new CompanyImpl();
            if (company instanceof Persistable persistable) {
                persistable.restoreFromFile(FILE_NAME);
        }
        TcpServer tcpServer = new TcpServer(new CompanyProtocol(company), PORT);
        tcpServer.run();
}
}