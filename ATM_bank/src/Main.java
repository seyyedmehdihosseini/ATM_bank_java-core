import database.DataSource;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("welcome to Atm Bank" + '\n' + '\n');
        DataSource instance = DataSource.getInstance();
        instance.getConnection("mysql", "root", "13311376", "bank");

    }
}