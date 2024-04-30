package employee;

import account.Account;
import account.AccountService;
import account.RolePerson;
import bank.BankService;
import basicalClass.BasicInformationPerson;
import input.Input;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EmployeeService {

    private static final EmployeeDao employeeDao = new EmployeeDao();

    public static void checkEmployee(Account account){

    }

    public static void menuEmployee(String roleEmployee) {
        if (roleEmployee.equalsIgnoreCase(EmployeeRole.BOSS.name()))
            menuBoss();

    }


    private static void menuBoss() {
        boolean check = true;

        while (true) {
            System.out.println('\n' + "select one operation (enter just number) ");
            System.out.println("1.Bank Menu" + '\t' + "2.Employee Menu" + '\t' + "3.back");
            int selectOperation = Input.getScanner().nextInt();
            if (selectOperation == 1) {
                BankService.menuBank();
            } else if (selectOperation == 2) {
                menuEmployeesForBoss();
            } else if (selectOperation == 3) {
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
            System.out.println("1.Create New Employee" + '\t' + "2.Update Account Employee"+ '\t' + "3.Update Information Employee"+ '\t'
                    + "4.Delete Employee" + '\t' + "5.Load All Employee" + '\t' + "6.Load Employee By Employee Code" + '\t' + "7.back");
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
                    break;
                case 6:
                    break;
                case 7:
                    check=false;
                    break;
                default:
                    break;
            }
        }
    }

    public static void createAccountEmployee(){
        String uniqueCodeForUsernameAndEmployeeCode = createUniqueCode();
        Account newAccountForEmployee = AccountService.createAccountForPerson(RolePerson.EMPLOYEE, uniqueCodeForUsernameAndEmployeeCode);
        BasicInformationPerson<Employee> employeeBasicInformation = new BasicInformationPerson<>();
        Employee employee = new Employee();
        employee.setAccount(newAccountForEmployee);
        employeeBasicInformation.setE(employee);
        employee = employeeBasicInformation.getE();
        System.out.println('\n' + "select one role for employee (enter just number) ");
        System.out.println("1.BANK_MANAGER"+'\t'+"2.BANKER"+'\t'+"3.BANK_CLERK"+'\t'+"4.FINANCIAL_MANAGER"+'\t'
                +"5.SECURITY_OFFICER"+'\t'+"6.CASHIER");
        int selectOperation = Input.getScanner().nextInt();
        switch (selectOperation){
            case 1:
                employee.setEmployeeRole("BANK_MANAGER");
                break;
                case 2:
                employee.setEmployeeRole("BANKER");
                break;
                case 3:
                employee.setEmployeeRole("BANK_CLERK");
                break;
                case 4:
                employee.setEmployeeRole("FINANCIAL_MANAGER");
                break;
                case 5:
                employee.setEmployeeRole("SECURITY_OFFICER");
                break;
            case 6:
                employee.setEmployeeRole("CASHIER");
                break;
            default:
                break;
        }

        employee.setEmployeeCode(uniqueCodeForUsernameAndEmployeeCode);

        employeeDao.save(employee);

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
