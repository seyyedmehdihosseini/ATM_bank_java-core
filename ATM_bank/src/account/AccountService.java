package account;

import input.Input;

import java.util.Date;

public class AccountService {

    private static final AccountDao accountDao = new AccountDao();


    public static Account createAccountForPerson(RolePerson rolePerson , String uniqueCodeOrNationalCode ){
        Account account = new Account();

        account.setUsername(uniqueCodeOrNationalCode);
        System.out.println("enter password :");
        account.setPassword(Input.getScanner().next());
        account.setActive(true);
        account.setCreateDate(new Date());
        account.setRolePerson(rolePerson.name());

        if (accountDao.save(account))
            return accountDao.getAccountByUsername(account.getUsername());

        return null;
    }



    private static void printLogin(){
        boolean checkLogin = true;
        while (checkLogin){
            System.out.println("enter username : ");
            String username = Input.getScanner().next();
            Account accountPerson = accountDao.getAccountByUsername(username);
            if (accountPerson!=null){
                System.out.println("enter password : ");
                String password = Input.getScanner().next();
                if (accountPerson.getPassword().equals(password)){
                    if (accountPerson.getRolePerson().equals(RolePerson.EMPLOYEE)){

                    }else {

                    }
                }else
                    throw new RuntimeException("password is not valid. \t\t try again ...");
            }else
                throw new RuntimeException("user name is not valid. \t\t try again ...");

        }

    }

    public static void menuAccount(){
        System.out.println('\n' + "select one operation (enter just number) ");
        System.out.println("1.Bank Menu" + '\t' + "2.Employee Menu" + '\t' + "3.back");
        int selectOperation = Input.getScanner().nextInt();

    }
}
