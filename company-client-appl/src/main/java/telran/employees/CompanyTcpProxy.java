package telran.employees;
import java.util.Iterator;
import org.json.*;
import telran.net.TcpClient;


public class CompanyTcpProxy implements Company {
    TcpClient tcpClient;
    public CompanyTcpProxy (TcpClient tcpClient) {
        this.tcpClient = tcpClient;
    }
    @Override
    public Iterator<Employee> iterator() {
        throw new UnsupportedOperationException("Unimplemented method 'iterator'");
    }

    @Override
    public void addEmployee(Employee empl) {
        tcpClient.sendAndReceive("addEmployee", empl.toString());
    }

    @Override
    public int getDepartmentBudget(String department) {
        String responseData = tcpClient.sendAndReceive("getDepartmentBudget", department);
        return Integer.parseInt(responseData);
    }

    @Override
    public String[] getDepartments() {
        String jsonStr = tcpClient.sendAndReceive("getDepartments", "");
        JSONArray jsonArray = new JSONArray(jsonStr) ;
        String[]res = jsonArray.toList().toArray(String[]::new);
        return res;
    }

    @Override
    public Employee getEmployee(long id) {
       String responseData = tcpClient.sendAndReceive("getEmployee", id + "");
       return Employee.getEmployeeFromJSON(responseData);
    }

    @Override
    public Manager[] getManagersWithMostFactor() {
        String responseData = tcpClient.sendAndReceive("getManagersWithMostFactor",  "");
        Manager[] res = new JSONArray(responseData).toList().stream().map(Object::toString)
        .map(Employee::getEmployeeFromJSON).toArray(Manager[]::new);
        return res;
    }

    @Override
    public Employee removeEmployee(long id) {
        String responseData = tcpClient.sendAndReceive("removeEmployee",  "" + id);
        return Employee.getEmployeeFromJSON(responseData);
    }

}