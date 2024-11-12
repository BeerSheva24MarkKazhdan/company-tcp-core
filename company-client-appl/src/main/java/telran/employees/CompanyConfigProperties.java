package telran.employees;

public interface CompanyConfigProperties {
    int MAX_BASIC_SALARY = 30000;
    int MIN_BASIC_SALARY = 7000;
    float MIN_FACTOR = 1.5f;
    float MAX_FACTOR = 5.0f;
    int MIN_WAGE = 100;
    int MAX_WAGE = 500;
    int MAX_HOURS = 100;
    int MIN_HOURS = 1;
    float MIN_PERCENT = 0.1f;
    float MAX_PERCENT = 10.0f;
    long MIN_SALES = 500;
    long MAX_SALES = Integer.MAX_VALUE;
    long MIN_ID = 1000;
    long MAX_ID = 9999;
    String FILE_NAME = "employees.data";
    String[] DEPARTMENTS = {"QA","Development", "Accounting", "Management"};
}
