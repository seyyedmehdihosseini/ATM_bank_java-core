package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class OtherDataSource {

    private OtherDataSource() {}

    private static final String url = "jdbc:mysql://localhost:3306/bank";
    private static final String username = "root";
    private static final String password = "13311376";

    private static Connection connection = null;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url,username,password);
            System.out.println("connect database mysql ...");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static Connection getConnection(){
        return connection;
    }


    public static void createAllTableIfNotExist(Connection connection){
        String queryCreateTableBank = "CREATE TABLE IF NOT EXISTS TBL_BANK " +
                "(ID BIGINT PRIMARY KEY AUTO_INCREMENT , CREATE_DATE DATE , BANK_CODE INTEGER , BANK_NAME VARCHAR(40))";
        String queryCreateTableEmployee ="CREATE TABLE IF NOT EXISTS TBL_EMPLOYEE " +
                "(ID BIGINT PRIMARY KEY AUTO_INCREMENT , CREATE_DATE DATE , FIRST_NAME VARCHAR(40) ,LAST_NAME VARCHAR(40), " +
                "FATHER_NAME VARCHAR(40),NATIONAL_CODE VARCHAR(11) ,MOBILE_NUMBER VARCHAR(15) , EMPLOYEE_CODE VARCHAR(40),EMPLOYEE_ROLE VARCHAR(50))";

        String queryCreateTableAccount ="CREATE TABLE IF NOT EXISTS TBL_ACCOUNT " +
                "(ID BIGINT PRIMARY KEY AUTO_INCREMENT , CREATE_DATE DATE , USER_NAME VARCHAR(40) ,PASSWORD VARCHAR(40), " +
                "ROLE_PERSON VARCHAR(40),IS_ACTIVE BOOLEAN )";

        try {
            connection.prepareStatement(queryCreateTableBank).executeUpdate();
            connection.prepareStatement(queryCreateTableEmployee).executeUpdate();
            connection.prepareStatement(queryCreateTableAccount).executeUpdate();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}
