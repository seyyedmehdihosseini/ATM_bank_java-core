package account;

import basicalClass.BaseDao;
import database.OtherDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDao implements BaseDao<Account> {

    private final Connection connection = OtherDataSource.getConnection();

    public Boolean save(Account account){
        String query = "insert into TBL_ACCOUNT("+account.getValueNameField()+") values (?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDate(1, (Date) account.getCreateDate());
            preparedStatement.setString(2,account.getUsername());
            preparedStatement.setString(3,account.getPassword());
            preparedStatement.setBoolean(4,account.getActive());
            preparedStatement.setString(5,account.getRolePerson().name());

            preparedStatement.executeUpdate();

            System.out.println("account with username " + account.getUsername() +" created ." );

            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public Account getById(Long id) {
        String query = "select * from TBL_ACCOUNT where ID =?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1,id);
            List<Account> listAccountFromResultSet = getListAccountFromResultSet(preparedStatement.executeQuery());

            if (listAccountFromResultSet.isEmpty())
                throw new RuntimeException("account by id ' "+ id +" ' not found .... ");

            return listAccountFromResultSet.get(0);

        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }

    @Override
    public Boolean deleteById(Long id) {
        return null;
    }

    @Override
    public List<Account> getAll() {
        String query = "select * from TBL_ACCOUNT";
        try {
            return getListAccountFromResultSet(connection.prepareStatement(query).executeQuery());
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public Account getAccountByUsername(String username){
        String query = "select * from TBL_ACCOUNT where USER_NAME =? ";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,username);

            List<Account> listAccountFromResultSet = getListAccountFromResultSet(preparedStatement.executeQuery());

            if (listAccountFromResultSet.isEmpty())
                throw new RuntimeException("user name not found .... ");

            return listAccountFromResultSet.get(0);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }



    private List<Account> getListAccountFromResultSet(ResultSet resultSet) throws SQLException {
        List<Account> accountList = new ArrayList<>();

        while (resultSet.next()){
            Account account = new Account();
            account.setId(resultSet.getLong("ID"));
            account.setCreateDate(resultSet.getDate("CREATE_DATE"));
            account.setUsername(resultSet.getString("USER_NAME"));
            account.setPassword(resultSet.getString("PASSWORD"));
            account.setActive(resultSet.getBoolean("IS_ACTIVE"));
            account.setRolePerson(resultSet.getString("ROLE_PERSON"));

            accountList.add(account);
        }

        return accountList;
    }


}
