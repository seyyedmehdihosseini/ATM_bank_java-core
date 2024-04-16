import database.DataSource;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("welcome to Atm Bank" +'\n'+'\n');
//        Thread.sleep(2000);
        DataSource.getDataForConnectionDataBase();
        DataSource.getConnection();
    }

}