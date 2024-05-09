package employee;

import account.Account;
import basicalClass.BaseDaoImpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class EmployeeDao extends BaseDaoImpl<Employee> {

    protected EmployeeDao(Class<Employee> entityClass) {
        super(entityClass);
    }
    public EmployeeDao() {
        super(Employee.class);
    }

    public Employee getEmployeeByAccount(Account account){
        String query = "select * from TBL_EMPLOYEE where ACCOUNT_ID = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1,account.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Employee> employees = setValueFromDatabaseIntoCurrentObject(resultSet);
            return employees.get(0);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }

}
