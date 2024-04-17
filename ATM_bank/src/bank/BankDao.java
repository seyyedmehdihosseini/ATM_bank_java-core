package bank;

import basicalClass.BaseDao;
import database.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankDao implements BaseDao<Bank> {

    private Connection connection = null;

    public BankDao() {
        this.connection = DataSource.getConnection();
    }

    @Override
    public Boolean save(Bank bank) {
        String query = "INSERT INTO TBL_BANK(CREATE_DATE,BANK_CODE,BANK_NAME) VALUES (?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDate(1, new Date(new java.util.Date().getTime()));
            preparedStatement.setInt(2, bank.getBankCode());
            preparedStatement.setString(3, bank.getBankName());
            preparedStatement.executeUpdate();
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    @Override
    public Bank getById(Long id) {
        String query = "select * from TBL_BANK where id = ?";
        List<Bank> bankList = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            bankList = catchFromTableByResultSet(resultSet);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        if (!bankList.isEmpty())
            return bankList.get(0);
        return null;
    }

    @Override
    public Boolean deleteById(Long id) {
        String query = "delete from TBL_BANK where ID=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1,id);
            preparedStatement.executeUpdate();

            return true;
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public List<Bank> getAll() {
        String query = "select * from TBL_BANK";
        try {
            ResultSet resultSet = connection.prepareStatement(query).executeQuery();

            return catchFromTableByResultSet(resultSet);
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }

    private static List<Bank> catchFromTableByResultSet(ResultSet resultSet) throws SQLException {
        List<Bank> bankList = new ArrayList<>();
        while (resultSet.next()) {
            Bank bank = new Bank();
            bank.setId(resultSet.getLong("ID"));
            bank.setCreateDate(resultSet.getDate("CREATE_DATE"));
            bank.setBankCode(resultSet.getInt("BANK_CODE"));
            bank.setBankName(resultSet.getString("BANK_NAME"));
            bankList.add(bank);
        }
        return bankList;
    }

}
