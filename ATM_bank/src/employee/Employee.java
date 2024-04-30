package employee;

import account.Account;
import basicalClass.Person;

public class Employee extends Person {

    private String employeeCode;
    private EmployeeRole employeeRole;
    private Account account;


    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public EmployeeRole getEmployeeRole() {
        return employeeRole;
    }

    public void setEmployeeRole(String employeeRole) {
        if (employeeRole.equalsIgnoreCase(EmployeeRole.BOSS.name()))
            this.employeeRole = EmployeeRole.BOSS;
        else if (employeeRole.equalsIgnoreCase(EmployeeRole.BANK_MANAGER.name()))
            this.employeeRole = EmployeeRole.BANK_MANAGER;
        else if (employeeRole.equalsIgnoreCase(EmployeeRole.BANK_CLERK.name()))
            this.employeeRole = EmployeeRole.BANK_CLERK;
        else if (employeeRole.equalsIgnoreCase(EmployeeRole.CASHEIR.name()))
            this.employeeRole = EmployeeRole.CASHEIR;
        else if (employeeRole.equalsIgnoreCase(EmployeeRole.BANKER.name()))
            this.employeeRole = EmployeeRole.BANKER;
        else if (employeeRole.equalsIgnoreCase(EmployeeRole.FINANCIAL_MANAGER.name()))
            this.employeeRole = EmployeeRole.FINANCIAL_MANAGER;
        else if (employeeRole.equalsIgnoreCase(EmployeeRole.SECURITY_OFFICER.name()))
            this.employeeRole = EmployeeRole.SECURITY_OFFICER;
        else
            throw new RuntimeException("role employee is not valid ....");
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String getValueNameField() {
        return super.getValueNameField() + ",EMPLOYEE_CODE,EMPLOYEE_ROLE";
    }

    @Override
    public String toString() {
        return "Employee{" + super.toString() +
                "employeeCode='" + employeeCode + '\'' +
                ", employeeRole='" + employeeRole + '\'' +
                ", account='" + account + '\'' +
                '}';
    }

}
