package account;

import basicalClass.BaseDaoImpl;

import java.sql.PreparedStatement;
import java.util.List;

public class AccountDao extends BaseDaoImpl<Account> {
    public AccountDao(Class<Account> entityClass) {
        super(entityClass);
    }
    public AccountDao(){
        super(Account.class);
    }


    public Account getAccountByUsername(String username){
        String query = "select * from TBL_ACCOUNT where USER_NAME =? ";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,username);
            List<Account> listAccountFromResultSet = super.setValueFromDatabaseIntoCurrentObject(preparedStatement.executeQuery());
            if (listAccountFromResultSet.isEmpty())
                throw new RuntimeException("user name not found .... ");

            return listAccountFromResultSet.get(0);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public Account getAccountByUsernameNew(String username){
        try {
            List<Account> accounts = getByProperty("username", username);
            if (accounts.isEmpty())
                throw new RuntimeException("account is not valid");
            return accounts.get(0);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }


}
