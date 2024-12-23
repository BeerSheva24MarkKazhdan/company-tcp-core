package telran.employees;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.NoSuchElementException;

import org.json.JSONArray;

import telran.net.Protocol;
import telran.net.Request;
import telran.net.Response;
import telran.net.ResponseCode;

public class CompanyProtocol implements Protocol {
    Company company;

    public CompanyProtocol(Company company) {
        this.company = company;
    }

    @Override
    public Response getResponse(Request request) {
        String requestType = request.requestType();
        String requestData = request.requestData();
        Response response = null;
        try {
            Method method = CompanyProtocol.class.getDeclaredMethod(requestType, String.class);
            method.setAccessible(true);
            response = (Response) method.invoke(this, requestData);
        } catch (NoSuchMethodException e) {
            response = new Response(ResponseCode.WRONG_TYPE, requestType + " Wrong type");

        } catch (InvocationTargetException e) {
            Throwable causeExc = e.getCause();
            String message = causeExc == null ? e.getMessage() : causeExc.getMessage();
            response = new Response(ResponseCode.WRONG_DATA, message);
        } catch (Exception e){
            //only for finishing Server and printing out Exception full stack
            throw new RuntimeException(e);
        }

        return response;
    }

    Response getOkResponse(String responseData) {
        return new Response(ResponseCode.OK, responseData);
    }

    Response addEmployee(String requestData) {
        Employee empl = Employee.getEmployeeFromJSON(requestData);
        company.addEmployee(empl);
        return getOkResponse("");
    }

    Response getEmployee(String requestData) {
        long id = Long.parseLong(requestData);
        Employee empl = company.getEmployee(id);
        if (empl == null) {
            throw new NoSuchElementException(String.format("Employee %d not found", id));
        }
        return getOkResponse(empl.toString());
    }

    Response removeEmployee(String requestData) {
        long id = Long.parseLong(requestData);
        Employee empl = company.removeEmployee(id);
        return getOkResponse(empl.toString());
    }

    Response getDepartmentBudget(String requestData) {
        int budget = company.getDepartmentBudget(requestData);
        return getOkResponse(budget + "");
    }

    Response getDepartments(String requestData) {
        String[] departments = company.getDepartments();
        JSONArray jsonArray = new JSONArray(departments);
        return getOkResponse(jsonArray.toString());
    }

    Response getManagersWithMostFactor(String requestData) {
        Manager[] managers = company.getManagersWithMostFactor();
        JSONArray jsonArray = new JSONArray(Arrays.stream(managers).map(Manager::toString).toList());
        return getOkResponse(jsonArray.toString());
    }

}