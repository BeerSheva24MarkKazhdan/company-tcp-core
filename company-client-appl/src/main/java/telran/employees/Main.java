package telran.employees;
import telran.view.*;

import java.io.IOException;
import java.util.Arrays;

import telran.net.*;
public class Main {
    private static final String HOST = "localhost";
    private static final int PORT = 4000;

    public static void main(String[] args) {
        InputOutput io = new StandardInputOutput();
        TcpClient tcpClient = new TcpClient(HOST, PORT);
        Company company = new CompanyTcpProxy(tcpClient);
        Item[] items = CompanyItems.getItems(company);
        items = addExitItem(items, tcpClient);
        Menu menu = new Menu("Company Network Application", items);
        menu.perform(io);
        io.writeLine("Application is finished");
    }

    private static Item[] addExitItem(Item[] items, TcpClient tcpClient) {
       Item[] res = Arrays.copyOf(items, items.length + 1);
       res[items.length] = Item.of("Exit", io -> {
        try {
            tcpClient.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }, true);
    return res;
    }
}