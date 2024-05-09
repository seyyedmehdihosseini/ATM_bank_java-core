package database;

import basicalClass.BaseEntity;
import input.Input;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.stream.Collectors;

public final class DataSource extends BaseDataEntity {
    private static DataSource dataSource = null;

    private DataSource() {
        super();
    }

    private static String databaseType = "", url = "", username = "", password = "";

    private static Connection connection = null;
    private PreparedStatement preparedStatement;

    public Connection getConnection(String typeDatabase, String user, String pass, String database) {
        try {
            url = getInstance().getUrlByTypeDatabase(typeDatabase);
            username = user;
            password = pass;
            connectionDatabaseByEntryValues(database);
            if (connection != null) {
                List<Class<?>> allDeclaredClass = getAllDeclaredClass();
                for (Class<?> clazz : allDeclaredClass) {
                    createTableIfNotExist(createEntity(clazz));
                }
                return connection;
            } else
                throw new RuntimeException("values for connection database not valid ...");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public void dropAllTables() {
        String queryDeactivateForeignKey = "SET FOREIGN_KEY_CHECKS = 0";
        String queryChangeConstrain = "SELECT CONCAT('ALTER TABLE ', table_name, ' DROP FOREIGN KEY ', constraint_name, ';')\t" +
                "FROM information_schema.table_constraints\t" +
                "WHERE constraint_type = 'FOREIGN KEY'";
        String queryDropAllTablesInDatabase = "SELECT CONCAT('DROP TABLE IF EXISTS ', table_name, ';')\t" +
                "FROM information_schema.tables\t" +
                "WHERE table_schema = 'bank';";
        try {
            connection.prepareStatement(queryDeactivateForeignKey).execute();
            connection.prepareStatement(queryChangeConstrain).execute();
            ResultSet resultSet = connection.prepareStatement(queryDropAllTablesInDatabase).executeQuery();
            while (resultSet.next()) {
                String queryDrop = resultSet.getString(1);
                connection.prepareStatement(queryDrop).execute();
            }

            System.out.println("All tables and foreign key constraints have been dropped successfully.");
            connection.prepareStatement("SET FOREIGN_KEY_CHECKS = 1").execute();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                getInstance().getDataForConnectionDataBase();
//                List<Class<?>> allDeclaredClass = getAllDeclaredClass();
//                for (Class<?> clazz : allDeclaredClass) {
//                    createTableIfNotExist(createEntity(clazz));
//                }
                return connection;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }

    public static DataSource getInstance() {
        if (dataSource == null) {
            dataSource = new DataSource();
//            connection = getConnection();
            return dataSource;
        }
        return dataSource;
    }

    private void getDataForConnectionDataBase() {
        System.out.println("specify the type of database. (enter only one number)  : " + '\n' + "1.MYSQL " + '\t' + "2.ORACLE" + '\t' + "3.POSTGRESQL");
        url = getUrlByTypeDatabase(Input.getScanner().next());

        System.out.println("enter username database :");
        username = Input.getScanner().next();

        System.out.println("enter password database :");
        password = Input.getScanner().next();

        System.out.println("enter database name or SID : ");
        connectionDatabaseByEntryValues(Input.getScanner().next());

    }

    private String getUrlByTypeDatabase(String typeDataBase) {
        if (typeDataBase.equals("1") || typeDataBase.equalsIgnoreCase("MYSQL")) {
            databaseType = "MYSQL";
            return "jdbc:mysql://localhost:3306/";
        }
        if (typeDataBase.equals("2") || typeDataBase.equalsIgnoreCase("ORACLE")) {
            databaseType = "ORACLE";
            return "jdbc:oracle:thin:@localhost:1521/";
        }
        if (typeDataBase.equals("3") || typeDataBase.equalsIgnoreCase("POSTGRESQL")) {
            databaseType = "POSTGRESQL";
            return "jdbc:postgresql://localhost:5432/";
        } else throw new RuntimeException("type database is not valid .... ");
    }

    private void connectionDatabaseByEntryValues(String databaseNameOrSID) {
        try {
            switch (databaseType) {
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

    private void createConnectionToDataBase() {
        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("connect database " + databaseType + " ...");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void createDatabaseIfNotExistsForMysqlAndPostgresql(String databaseName) {
        boolean checkIsPresentDatabase = false;

        createConnectionToDataBase();

        if (databaseType.equalsIgnoreCase("MYSQL")) {
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
        } else if (databaseType.equalsIgnoreCase("POSTGRESQL")) {
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

    private void checkCreateOrExistDatabaseInMySqlAndPostgresql(boolean checkIsPresentDatabase, String databaseName) {
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

    private static <E extends BaseEntity> E createEntity(Class<?> entityClass) {
        try {
            return (E) entityClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private static <E extends BaseEntity> void createTableIfNotExist(E entity) {
        List<Field> allFieldsNotIdAndCreateDate = entity.getListDeclaredFieldsInThisClassWithId().stream().filter(field -> !field.getName().equalsIgnoreCase("ID") && !field.getName().equalsIgnoreCase("createDate"))
                .collect(Collectors.toList());
        List<Field> fieldClass = allFieldsNotIdAndCreateDate.stream().filter(instance -> BaseEntity.class.isAssignableFrom(instance.getType())).collect(Collectors.toList());
        if (!fieldClass.isEmpty()) {
            for (Field field : fieldClass) {
                createTableIfNotExist(createEntity(field.getType()));
            }
        }
        String queryCreateTable = getTypeFiledForCreateTable(entity, allFieldsNotIdAndCreateDate);
//        System.out.println(queryCreateTable);
        try {
            connection.prepareStatement(queryCreateTable + ")").executeUpdate();
            System.out.println("create table " + getNameTableFromClass(entity.getClass()));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

}
