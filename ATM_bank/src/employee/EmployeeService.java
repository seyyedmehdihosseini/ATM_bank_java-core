package employee;

import account.Account;
import account.AccountService;
import account.AccountType;
import bank.BankService;
import basicalClass.BasicInformationPerson;
import input.Input;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EmployeeService {
    private static final EmployeeDao employeeDao = new EmployeeDao();

    public static void showMenuByRole(Account account){
        Employee employee = employeeDao.getEmployeeByAccount(account);
        if (employee!=null){
            String employeeRole = employee.getEmployeeRole().name();
            if (employeeRole.equals(EmployeeRole.BOSS.name()) || employeeRole.equals(EmployeeRole.BANK_MANAGER.name()))
                menuBoss();
            else if (employeeRole.equals(EmployeeRole.BANKER.name())){}
            else if (employeeRole.equals(EmployeeRole.SECURITY_OFFICER.name())){}
        }
    }

    private static void menuBoss() {
        boolean check = true;

        while (check) {
            System.out.println('\n' + "select one operation (enter just number) ");
            System.out.println("1.Bank Menu" + '\t' + "2.Employee Menu" + '\t' + "3.back");
            int selectOperation = Input.getScanner().nextInt();
            if (selectOperation == 1) {
                BankService.menuBank();
            } else if (selectOperation == 2) {
                menuEmployeesForBoss();
            } else if (selectOperation == 3) {
                check=false;
                break;
            } else
                System.out.println("number is not valid ... " + "\t\t\t" + "try again ... \n");
        }
    }

    // menu all employees for CRUD by boss
    private static void menuEmployeesForBoss() {
        boolean check = true;

        while (check) {
            System.out.println('\n' + "select one operation (enter just number) ");
            System.out.println("1.Create New Employee" + "2.Delete Employee" + '\t' + "3.Load All Employee" + '\t' + "4.Load Employee By Employee Code" + '\t' + "5.back");
            int selectOperation = Input.getScanner().nextInt();
            switch (selectOperation) {
                case 1:
                    createAccountEmployee();
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    check=false;
                    break;
                default:
                    break;
            }
        }
    }

    private static void createAccountEmployee(){
        String uniqueCodeForUsernameAndEmployeeCode = createUniqueCode();
        Account newAccountForEmployee = AccountService.createAccountForPerson(AccountType.EMPLOYEE, uniqueCodeForUsernameAndEmployeeCode);
        BasicInformationPerson<Employee> employeeBasicInformation = new BasicInformationPerson<>();
        Employee employee = new Employee();
        employee.setAccount(newAccountForEmployee);
        employeeBasicInformation.setValuesIntoEntity(employee);
        employee = employeeBasicInformation.getE();
        boolean checkValid = true;
        while (checkValid){
            System.out.println('\n' + "select one role for employee (enter just number) ");
            System.out.println("1.BANK_MANAGER"+'\t'+"2.BANKER"+'\t'+"3.BANK_CLERK"+'\t'+"4.FINANCIAL_MANAGER"+'\t'
                    +"5.SECURITY_OFFICER"+'\t'+"6.CASHIER");
            int selectOperation = Input.getScanner().nextInt();
            switch (selectOperation){
                case 1:
                    employee.setEmployeeRole("BANK_MANAGER");
                    checkValid = false;
                    break;
                case 2:
                    employee.setEmployeeRole("BANKER");
                    checkValid = false;
                    break;
                case 3:
                    employee.setEmployeeRole("BANK_CLERK");
                    checkValid = false;
                    break;
                case 4:
                    employee.setEmployeeRole("FINANCIAL_MANAGER");
                    checkValid = false;
                    break;
                case 5:
                    employee.setEmployeeRole("SECURITY_OFFICER");
                    checkValid = false;
                    break;
                case 6:
                    employee.setEmployeeRole("CASHIER");
                    checkValid = false;
                    break;
                default:
                    break;
            }
        }

        employee.setEmployeeCode(uniqueCodeForUsernameAndEmployeeCode);

        employeeDao.save(employee);

        employeeDao.getByProperty("employeeCode",uniqueCodeForUsernameAndEmployeeCode).stream().findFirst().orElseThrow(()->new RuntimeException("employee failed to save employee ..." + "when save new employee"));

        System.out.println(employee);
    }

    private static String createUniqueCode(){
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
        return ft.format(dNow);
    }



    private static void menuEmployee() {
        boolean check = true;

        while (check) {
            System.out.println('\n' + "select one operation (enter just number) ");
            System.out.println("1.customer" + '\t' + "2.account" + '\t' + "3.show all account exist into bank " + '\t' + "4.back");
            int selectOperation = Input.getScanner().nextInt();
            if (selectOperation == 1)
                System.out.println("---------------***** customer *****---------------");
            else if (selectOperation == 2)
                System.out.println("---------------***** account *****---------------");
            else if (selectOperation == 3)
                System.out.println("---------------***** show all account *****---------------");
            else if (selectOperation == 4)
                check = false;
            else
                System.out.println("number is not valid ... " + "\t\t\t" + "try again ... \n");

        }
    }


}
