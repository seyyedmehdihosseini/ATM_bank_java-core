import datasource.DataSource;

public class Main {
    public static void main(String[] args) {
        System.out.println("welcome to Atm Bank" + '\n' + '\n');
        DataSource dataSource = DataSource.getInstance();
        dataSource.getConnection("mysql", "root", "13311376", "bank");
        dataSource.createAllTables();
        dataSource.dropAllTables();

    }
}