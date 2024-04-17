import database.DataSource;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws InterruptedException, SQLException {
        System.out.println("welcome to Atm Bank" +'\n'+'\n');
//        Thread.sleep(2000);
        DataSource.getInstance().getDataForConnectionDataBase();
        DataSource.getConnection().close();
        DataSource.getInstance();

    }

}