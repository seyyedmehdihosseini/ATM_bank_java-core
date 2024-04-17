import bank.BankDao;
import bank.BankService;
import database.DataSource;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        System.out.println("welcome to Atm Bank" +'\n'+'\n');
//        Thread.sleep(2000);

        DataSource.getInstance();

        DataSource.getConnection().close();

        DataSource.getConnection();

    }

}