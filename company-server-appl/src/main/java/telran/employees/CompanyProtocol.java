package telran.employees;
import org.json.JSONArray;

import org.json.JSONObject;
import telran.net.*;
import static telran.employees.CompanyConfigProperties.*;
import static telran.net.TcpConfigurationProperties.*;

import java.util.Arrays;
import java.util.stream.Collectors;

public class CompanyProtocol implements Protocol {

    private final CompanyImpl compImpl;

    public CompanyProtocol(CompanyImpl compImpl) {
        this.compImpl = compImpl;
        compImpl.restoreFromFile(FILE_NAME);
    }

    @Override
    public Response getResponse(Request request) {
        String type = request.requestType();
        String data = request.requestData();
        Response response;

        try {
            response = switch (type) {
                case "addEmployee" -> addEmployee(data);
                case "getEmployee" -> getEmployee(data);
                case "removeEmployee" -> removeEmployee(data);
                case "getDepartmentBudget" -> getDepartmentBudget(data);
                case "getDepartments" -> getDepartments();
                case "getManagersWithMostFactor" -> getManagersWithMostFactor();
                default -> new Response(ResponseCode.WRONG_TYPE, type + " is an unknown request type");
            };
        } catch (Exception e) {
            response = new Response(ResponseCode.WRONG_DATA, e.getMessage());
        }

        return response;
    }

    private Response addEmployee(String data) {
        Employee employee = Employee.getEmployeeFromJSON(data);
        compImpl.addEmployee(employee);
        compImpl.saveToFile(FILE_NAME);
        return new Response(ResponseCode.OK, "Employee added successfully");
    }

    private Response getEmployee(String data) {
        long id = Long.parseLong(data);
        Employee employee = compImpl.getEmployee(id);
        return employee != null ? new Response(ResponseCode.OK, employee.toString())
                : new Response(ResponseCode.WRONG_DATA, "Employee not found");
    }

    private Response removeEmployee(String data) {
        Response response;
        long id = Long.parseLong(data);
        Employee removedEmployee = compImpl.removeEmployee(id);
        if (removedEmployee != null) {
            compImpl.saveToFile(FILE_NAME);
            response = new Response(ResponseCode.OK, removedEmployee.toString());
        } else {
            response = new Response(ResponseCode.WRONG_DATA, "Employee not found");
        }
        return response;
    }

    private Response getDepartmentBudget(String department) {
        int budget = compImpl.getDepartmentBudget(department);
        return new Response(ResponseCode.OK, String.valueOf(budget));
    }

    private Response getDepartments() {
        String[] departments = compImpl.getDepartments();
        return new Response(ResponseCode.OK, new JSONArray(Arrays.asList(departments)).toString());
    }

    private Response getManagersWithMostFactor() {
        Manager[] managers = compImpl.getManagersWithMostFactor();
        String managersList = Arrays.stream(managers)
                .map(Manager::toString)
                .collect(Collectors.joining("\n"));
        return new Response(ResponseCode.OK, managersList);
    }

    @Override
    public String getResponseWithJSON(String requestJSON) {
        JSONObject jsonObj = new JSONObject(requestJSON);
        String requestType = jsonObj.getString(REQUEST_TYPE_FIELD);
        String requestData = jsonObj.getString(REQUEST_DATA_FIELD);
        Request request = new Request(requestType, requestData);
        return getResponse(request).toString();
    }
}