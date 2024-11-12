package telran.employees;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.json.*;

import telran.net.ResponseCode;
import telran.net.TcpClient;

import static telran.net.TcpConfigurationProperties.*;

public class CompanyTcpProxy implements Company {
    TcpClient tcpClient;

    public CompanyTcpProxy(TcpClient tcpClient) {
        this.tcpClient = tcpClient;
    }

    @Override
    public Iterator<Employee> iterator() {
        String jsonStr = tcpClient.sendAndReceive("getAllEmployees", "");
        JSONArray jsonArray = new JSONArray(jsonStr);

        List<Employee> employees = jsonArray.toList().stream()
                .map(obj -> {
                    JSONObject jsonObject = new JSONObject((Map<?, ?>) obj);
                    long id = jsonObject.getLong("id");
                    int basicSalary = jsonObject.getInt("basicSalary");
                    String department = jsonObject.getString("department");
                    return new Employee(id, basicSalary, department);
                })
                .toList();

        return employees.iterator();
    }


    @Override
    public void addEmployee(Employee empl) {
        String response = tcpClient.sendAndReceive("addEmployee", empl.toString());

        JSONObject jsonResponse = new JSONObject(response);
        String codeString = jsonResponse.optString(RESPONSE_CODE_FIELD, ResponseCode.WRONG_DATA.toString());
        ResponseCode responseCode = ResponseCode.valueOf(codeString);

        if (responseCode != ResponseCode.OK) {
            String data = jsonResponse.optString(RESPONSE_DATA_FIELD, "Failed to add employee");
            throw new RuntimeException("Error from server: " + data);
        }
    }

    @Override
    public int getDepartmentBudget(String department) {
        String response = tcpClient.sendAndReceive("getDepartmentBudget", department);

        JSONObject jsonResponse = new JSONObject(response);

        String codeString = jsonResponse.optString(RESPONSE_CODE_FIELD, ResponseCode.WRONG_DATA.toString());
        ResponseCode responseCode = ResponseCode.valueOf(codeString);

        String data = jsonResponse.optString(RESPONSE_DATA_FIELD, "0");

        if (responseCode == ResponseCode.OK) {
            return Integer.parseInt(data);
        } else {
            throw new RuntimeException("Error from server: " + data);
        }
    }

    @Override
    public String[] getDepartments() {
        String response = tcpClient.sendAndReceive("getDepartments", "");
        JSONObject jsonResponse = new JSONObject(response);
        String codeString = jsonResponse.optString(RESPONSE_CODE_FIELD, ResponseCode.WRONG_DATA.toString());
        ResponseCode responseCode = ResponseCode.valueOf(codeString);

        if (responseCode == ResponseCode.OK) {
            JSONArray data = jsonResponse.optJSONArray(RESPONSE_DATA_FIELD);
            return data != null ? data.toList().toArray(new String[0]) : new String[0];
        } else {
            String errorMessage = jsonResponse.optString(RESPONSE_DATA_FIELD, "Failed to retrieve departments");
            throw new RuntimeException("Error from server: " + errorMessage);
        }
    }

    @Override
    public Employee getEmployee(long ID) {
        String jsonStr = tcpClient.sendAndReceive("getEmployee", String.valueOf(ID));

            JSONObject jsonObject = new JSONObject(jsonStr);
            long employeeId = jsonObject.getLong("id");
            int basicSalary = jsonObject.getInt("basicSalary");
            String department = jsonObject.getString("department");
            return new Employee(employeeId, basicSalary, department);
        }

    @Override
    public Manager[] getManagersWithMostFactor() {
        String response = tcpClient.sendAndReceive("getManagersWithMostFactor", "");
        JSONObject jsonResponse = new JSONObject(response);

        String codeString = jsonResponse.optString(RESPONSE_CODE_FIELD, ResponseCode.WRONG_DATA.toString());
        ResponseCode responseCode = ResponseCode.valueOf(codeString);

        Manager[] result;

        if (responseCode == ResponseCode.OK) {
            JSONArray data = jsonResponse.optJSONArray(RESPONSE_DATA_FIELD);
            result = data != null
                    ? data.toList().stream()
                    .map(obj -> {
                        JSONObject jsonObject = new JSONObject((Map<?, ?>) obj);
                        long id = jsonObject.getLong("id");
                        int basicSalary = jsonObject.getInt("basicSalary");
                        String department = jsonObject.getString("department");
                        float factor = jsonObject.getFloat("factor");
                        return new Manager(id, basicSalary, department, factor);
                    })
                    .toArray(Manager[]::new)
                    : new Manager[0];
        } else {
            String errorMessage = jsonResponse.optString(RESPONSE_DATA_FIELD, "Failed to retrieve managers");
            throw new RuntimeException("Error from server: " + errorMessage);
        }

        return result;
    }

    @Override
    public Employee removeEmployee(long id) {
        String response = tcpClient.sendAndReceive("removeEmployee", Long.toString(id));

        JSONObject jsonResponse = new JSONObject(response);
        String codeString = jsonResponse.optString(RESPONSE_CODE_FIELD, ResponseCode.WRONG_DATA.toString());
        ResponseCode responseCode = ResponseCode.valueOf(codeString);

        if (responseCode == ResponseCode.OK) {
            JSONObject data = jsonResponse.optJSONObject(RESPONSE_DATA_FIELD);
            long employeeId = data.getLong("id");
            int basicSalary = data.getInt("basicSalary");
            String department = data.getString("department");
            return new Employee(employeeId, basicSalary, department);
        } else {
            String errorMessage = jsonResponse.optString(RESPONSE_DATA_FIELD, "Employee not found");
            throw new NoSuchElementException(errorMessage);
        }
    }

}