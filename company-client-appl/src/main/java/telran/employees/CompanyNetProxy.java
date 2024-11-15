package telran.employees;
import java.util.Iterator;

import org.json.JSONArray;

import telran.net.*;
public class CompanyNetProxy implements Company{
    NetworkClient netClient;
    public CompanyNetProxy (NetworkClient netClient) {
        this.netClient = netClient;
    }
    @Override
    public Iterator<Employee> iterator() {
        throw new UnsupportedOperationException("Unimplemented method 'iterator'");
    }

    @Override
    public void addEmployee(Employee empl) {
        netClient.sendAndReceive("addEmployee", empl.toString());
    }

    @Override
    public int getDepartmentBudget(String department) {
        String responseData = netClient.sendAndReceive("getDepartmentBudget", department);
        return Integer.parseInt(responseData);
    }

    @Override
    public String[] getDepartments() {
        String jsonStr = netClient.sendAndReceive("getDepartments", "");
        JSONArray jsonArray = new JSONArray(jsonStr) ;
        String[]res = jsonArray.toList().toArray(String[]::new);
        return res;
    }

    @Override
    public Employee getEmployee(long id) {
        String responseData = netClient.sendAndReceive("getEmployee", id + "");
        return Employee.getEmployeeFromJSON(responseData);
    }

    @Override
    public Manager[] getManagersWithMostFactor() {
        String responseData = netClient.sendAndReceive("getManagersWithMostFactor",  "");
        Manager[] res = new JSONArray(responseData).toList().stream().map(Object::toString)
                .map(Employee::getEmployeeFromJSON).toArray(Manager[]::new);
        return res;
    }

    @Override
    public Employee removeEmployee(long id) {
        String responseData = netClient.sendAndReceive("removeEmployee",  "" + id);
        return Employee.getEmployeeFromJSON(responseData);
    }

}