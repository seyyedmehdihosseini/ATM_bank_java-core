package database;

import input.Input;

import java.sql.*;

public final class DataSource {

    private DataSource() {}

    private static String database = "", url = "", username = "", password = "";

    private static Connection connection = null;
    private static PreparedStatement preparedStatement;


    public static void getDataForConnectionDataBase() {
        System.out.println("specify the type of database. (enter only one number)  : " + '\n' + "1.MYSQL " + '\t' + "2.ORACLE" + '\t' + "3.POSTGRESQL");
        url = getUrlByTypeDatabase(Input.getScanner().next());

        System.out.println("enter username database :");
        username = Input.getScanner().next();

        System.out.println("enter password database :");
        password = Input.getScanner().next();

        System.out.println("enter database name or SID : ");
        connectionDatabaseByEntryValues(Input.getScanner().next());

    }

    private static String getUrlByTypeDatabase(String typeDataBase) {
        if (typeDataBase.equals("1") || typeDataBase.equalsIgnoreCase("MYSQL")) {
            database = "MYSQL";
            return "jdbc:mysql://localhost:3306/";
        }
        if (typeDataBase.equals("2") || typeDataBase.equalsIgnoreCase("ORACLE")) {
            database = "ORACLE";
            return "jdbc:oracle:thin:@localhost:1521/";
        }
        if (typeDataBase.equals("3") || typeDataBase.equalsIgnoreCase("POSTGRESQL")) {
            database = "POSTGRESQL";
            return "jdbc:postgresql://localhost:5432/";
        } else throw new RuntimeException("type database is not valid .... ");
    }

    private static void connectionDatabaseByEntryValues(String databaseNameOrSID) {
        try {
            switch (database) {
                case "MYSQL":
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    createDatabaseIfNotExistsForMysqlAndPostgresql(databaseNameOrSID);
                    break;
                case "ORACLE":
                    url += databaseNameOrSID;
                    Class.forName("oracle.jdbc.OracleDriver");
                    createConnectionToDataBase();
                    break;
                case "POSTGRESQL":
                    Class.forName("org.postgresql.Driver");
                    createDatabaseIfNotExistsForMysqlAndPostgresql(databaseNameOrSID);
                    break;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("entry value for connection database is not valid ... ");
        }
    }

    private static void createConnectionToDataBase() {
        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("connect database " + database + " ...");
        } catch (Exception e) {

            throw new RuntimeException(e.getMessage());
        }
    }

    private static void createDatabaseIfNotExistsForMysqlAndPostgresql(String databaseName) {
        boolean checkIsPresentDatabase = false;

        createConnectionToDataBase();

        if (database.equalsIgnoreCase("MYSQL")) {
            try {
                String query = "SHOW DATABASES";
                preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    if (resultSet.getString(1).equalsIgnoreCase(databaseName)) {
                        checkIsPresentDatabase = true;
                        break;
                    }
                }

                checkCreateOrExistDatabaseInMySqlAndPostgresql(checkIsPresentDatabase, databaseName);

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                System.out.println("database by name ' " + databaseName + " ' in not valid ... ");
            }
        } else if (database.equalsIgnoreCase("POSTGRESQL")) {
            try {
                String query = "SELECT datname FROM pg_catalog.pg_database";
                preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    if (resultSet.getString(1).equalsIgnoreCase(databaseName)) {
                        checkIsPresentDatabase = true;
                        break;
                    }
                }

                checkCreateOrExistDatabaseInMySqlAndPostgresql(checkIsPresentDatabase, databaseName);

            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("database by name ' " + databaseName + " ' in not valid ... ");
            }
        }
    }

    private static void checkCreateOrExistDatabaseInMySqlAndPostgresql(boolean checkIsPresentDatabase, String databaseName) {
        try {
            if (!checkIsPresentDatabase) {
                String createDatabase = "create database " + databaseName;
                preparedStatement = connection.prepareStatement(createDatabase);
                preparedStatement.executeUpdate();
            }
            url += databaseName;
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static Connection getConnection() {
        return connection;
    }


}
