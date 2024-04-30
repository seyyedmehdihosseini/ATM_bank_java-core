package employee;

import account.Account;
import bank.Bank;
import basicalClass.BaseDao;
import database.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDao implements BaseDao<Employee> {

    private final Connection connection =  DataSource.getConnection();

    @Override
    public Boolean save(Employee employee) {
        String query = "insert into TBL_EMPLOYEE("+employee.getValueNameField()+") VALUES (?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDate(1, new Date(new java.util.Date().getTime()));
            preparedStatement.setString(2,employee.getFirstname());
            preparedStatement.setString(3,employee.getLastname());
            preparedStatement.setString(4,employee.getFatherName());
            preparedStatement.setString(5,employee.getNationalCode());
            preparedStatement.setString(6,employee.getMobileNumber());
            preparedStatement.setString(7,employee.getEmployeeCode());
            preparedStatement.setString(8,employee.getEmployeeRole().name());
            preparedStatement.executeUpdate();

            System.out.println("employee with employee code " + employee.getEmployeeCode() +" created .");

            return true;
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            System.out.println("save employee failed .... ");
            return false;
        }
    }

    @Override
    public Employee getById(Long id) {
        String query = "select * from TBL_EMPLOYEE where id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1,id);
            List<Employee> employees = catchFromTableByResultSet(preparedStatement.executeQuery());
            if (employees.isEmpty())
                throw new RuntimeException("employee by id not found ");
            return employees.get(0);
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
    public List<Employee> getAll() {
        String query ="select * from TBL_EMPLOYEE";
        try {
            return catchFromTableByResultSet(connection.prepareStatement(query).executeQuery());
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public Employee getEmployeeByAccountId(Account account){
        String query = "select * from TBL_EMPLOYEE where ACCOUNT_ID = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1,account.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Employee> employees = catchFromTableByResultSet(resultSet);
            return employees.get(0);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }

    private static List<Employee> catchFromTableByResultSet(ResultSet resultSet) throws SQLException {
        List<Employee> employeeList = new ArrayList<>();
        while (resultSet.next()) {
            Employee employee = new Employee();
            employee.setFirstname(resultSet.getString("FIRST_NAME"));
            employee.setLastname(resultSet.getString("LAST_NAME"));
            employee.setFatherName(resultSet.getString("FATHER_NAME"));
            employee.setNationalCode(resultSet.getString("NATIONAL_CODE"));
            employee.setMobileNumber(resultSet.getString("MOBILE_NUMBER"));
            employee.setEmployeeCode(resultSet.getString("EMPLOYEE_CODE"));
            employee.setEmployeeRole(resultSet.getString("EMPLOYEE_ROLE"));
            employee.setAccount(resultSet.getObject("ACCOUNT_ID",Account.class));
            employeeList.add(employee);
        }
        return employeeList;
    }

}
