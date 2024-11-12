package telran.employees;
import java.util.function.*;
import java.util.*;
import telran.view.*;
import java.util.stream.*;
import static telran.employees.CompanyConfigProperties.*;
public class CompanyItems {
    static Company company;
    public static Item[] getItems(Company company) {
        CompanyItems.company = company;
        Item[] items = {
                Item.of("add employee", CompanyItems::addEmployee)	,
                Item.of("display employee data", CompanyItems::getEmployee),
                Item.of("fire employee", CompanyItems::removeEmployee),
                Item.of("display department budget", CompanyItems::getDepartmentBudget),
                Item.of("display departments", CompanyItems::getDepartments),
                Item.of("display managers with most factor", CompanyItems::getManagersWithMostFactor),

        };
        return items;

    }

    static void addEmployee(InputOutput io) {
        Menu menu = new Menu("adding Employee of required Type", getAddEmployeeItems());
        menu.perform(io);
        io.writeLine("=".repeat(40));
    }

    private static Item[] getAddEmployeeItems() {
        Item[] items = {
                Item.of("Hire Employee",
                        io -> addEmployeeItem(io, CompanyItems::getEmployee)),
                Item.of("Hire WageEmployee",
                        io -> addEmployeeItem(io, CompanyItems::getWageEmployee)),
                Item.of("Hire SalesPerson", io -> addEmployeeItem(io, CompanyItems::getSalesPerson)),
                Item.of("Hire Manager", io -> addEmployeeItem(io, CompanyItems::getManager)),
                Item.ofExit()
        };
        return items;
    }
    static private void addEmployeeItem(InputOutput io,
                                        BiFunction<Employee, InputOutput, Employee> actualAdding) {
        Employee empl = readEmployee(io);
        Employee result = actualAdding.apply(empl, io);
        company.addEmployee(result);
        io.writeLine("Employee has been added");
        io.writeLine("=".repeat(40));
    }
    private static Employee getEmployee(Employee empl, InputOutput io) {
        return empl;
    }
    private static Employee getSalesPerson(Employee empl, InputOutput io) {
        WageEmployee wageEmployee = (WageEmployee) getWageEmployee(empl, io);
        float percents = io.readNumberRange("Enter percents", "Wrong percents value", MIN_PERCENT, MAX_PERCENT).floatValue();
        long sales = io.readNumberRange("Enter sales", "Wrong sales value", MIN_SALES, MAX_SALES).longValue();
        return new SalesPerson(empl.getId(),empl.getBasicSalary() , empl.getDepartment(),
                wageEmployee.getHours(), wageEmployee.getWage(),
                percents, sales);
    }
    private static Employee getManager(Employee empl, InputOutput io) {

        float factor = io.readNumberRange("Enter factor",
                "Wrong factor value", MIN_FACTOR, MAX_FACTOR).floatValue();
        return new Manager(empl.getId(), empl.getBasicSalary(), empl.getDepartment(), factor );
    }
    private static Employee getWageEmployee(Employee empl, InputOutput io) {

        int hours = io.readNumberRange("Enter working hours",
                "Wrong hours value", MIN_HOURS, MAX_HOURS).intValue();
        int wage = io.readNumberRange("Enter hour wage",
                "Wrong wage value", MIN_WAGE, MAX_WAGE).intValue();;
        return new WageEmployee(empl.getId(), empl.getBasicSalary(), empl.getDepartment(), hours, wage);
    }
    private static Employee readEmployee(InputOutput io) {

        long id = readEmployeeId(io);
        int basicSalary = io.readNumberRange("Enter basic salary", "Wrong basic salary", MIN_BASIC_SALARY,
                MAX_BASIC_SALARY).intValue();
        String department = readDepartment(io);
        return new Employee(id, basicSalary, department);
    }
    private static String readDepartment(InputOutput io) {
        HashSet<String> departments = new HashSet<>(List.of(DEPARTMENTS));
        return io.readStringOptions("Enter department " + departments, "Wrong department", departments);
    }
    private static long readEmployeeId(InputOutput io) {
        return io.readNumberRange("Enter id value", "Wrong id value", MIN_ID, MAX_ID).longValue();
    }
    static void getEmployee(InputOutput io) {
        long id = readEmployeeId(io);
        Employee empl = company.getEmployee(id);
        String line = empl == null ? "no employee with the entered ID"
                : empl.toString();
        io.writeLine(line);
    }
    static void removeEmployee(InputOutput io) {
        long id = readEmployeeId(io);
        Employee empl = company.removeEmployee(id);
        io.writeLine(empl);
        io.writeLine("has been removed from the company\n");
    }
    static void getDepartmentBudget(InputOutput io) {
        String department = readDepartment(io);
        int budget = company.getDepartmentBudget(department);
        String line = budget == 0 ? "no employees working in entered department" :
                "Budget of entered department is " + budget;
        io.writeLine(line);
    }
    static void getDepartments(InputOutput io) {
        String [] departments = company.getDepartments();
        String line = departments.length == 0 ? "no employees" :
                String.join("\n", departments);
        io.writeLine(line);
    }
    static void getManagersWithMostFactor(InputOutput io) {
        Manager[] managers = company.getManagersWithMostFactor();
        String line = managers.length == 0 ? "no managers" :
                Arrays.stream(managers).map(Employee::toString)
                        .collect(Collectors.joining("\n"));
        io.writeLine(line);
    }
}
