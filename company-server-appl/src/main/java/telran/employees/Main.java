package telran.employees;
import telran.net.*;

public class Main {
    private static final int PORT = 4000;

    public static void main(String[] args) {
        CompanyImpl companyImpl = new CompanyImpl();
        Protocol protocol = new CompanyProtocol(companyImpl);
        TcpServer server = new TcpServer(protocol, PORT);
        new Thread(server).start();
}
}