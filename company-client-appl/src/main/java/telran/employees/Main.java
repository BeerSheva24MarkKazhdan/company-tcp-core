package telran.employees;
import telran.view.*;
import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;

import telran.net.*;
public class Main {
    private static final String HOST = "localhost";
    private static final int PORT = 4000;

    public static void main(String[] args) {
        InputOutput io = new StandardInputOutput();
        NetworkClient netClient = new TcpClient(HOST, PORT);
        Company company = new CompanyNetProxy(netClient);
        Item[] items = CompanyItems.getItems(company);
        items = addExitItem(items, netClient);
        Menu menu = new Menu("Company Network Application", items);
        menu.perform(io);
        io.writeLine("Application is finished");
    }

    private static Item[] addExitItem(Item[] items, NetworkClient netClient) {
        Item[] res = Arrays.copyOf(items, items.length + 1);
        res[items.length] = Item.of("Exit", io -> {
            try {
                if(netClient instanceof Closeable closeable)
                    closeable.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, true);
        return res;
    }
}