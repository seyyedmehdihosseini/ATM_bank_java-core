package database;

import basicalClass.BaseEntity;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

public abstract class BaseDataEntity {

    protected BaseDataEntity() {}

    private static final String CREATE_TABLE_IF_NOT_EXIST = "CREATE TABLE IF NOT EXISTS ";

    protected static <E extends BaseEntity> String getTypeFiledForCreateTable(E entity, List<Field> fieldList) {
        StringJoiner queryCreateTable = new StringJoiner(" , ");
        String queryTable = CREATE_TABLE_IF_NOT_EXIST + getNameTableFromClass(entity.getClass()) + "(ID BIGINT PRIMARY KEY AUTO_INCREMENT , CREATE_DATE DATE ";
        queryCreateTable.add(queryTable);

        StringJoiner relationToOtherTable = new StringJoiner(",");
        for (Field field : fieldList) {
            Class<?> type = field.getType();
            String columnName = entity.convertFieldToNameColumn(field);
            if (type == String.class || type.isEnum())
                queryCreateTable.add(columnName + " VARCHAR(255)");
            else if (type == Integer.class || type == int.class)
                queryCreateTable.add(columnName + " INTEGER");
            else if (type == Long.class || type == long.class)
                queryCreateTable.add(columnName + " BIGINT");
            else if (type == Date.class)
                queryCreateTable.add(columnName + " DATE");
            else if (type == Float.class || type == float.class)
                queryCreateTable.add(columnName + " FLOAT");
            else if (type == Double.class || type == double.class)
                queryCreateTable.add(columnName + " DOUBLE");
            else if (type == Boolean.class || type == boolean.class)
                queryCreateTable.add(columnName + " BOOLEAN");
                //type.getClass()==Class.class
            else if (BaseEntity.class.isAssignableFrom(type)) {
                try {
                    Class<?> aClass = Class.forName(type.getName());
                    String nameTable = getNameTableFromClass(aClass);
                    String join = "FOREIGN KEY (" + columnName + ") REFERENCES " + nameTable + "(ID) ON DELETE CASCADE";
                    relationToOtherTable.add(join);
                    queryCreateTable.add(columnName + " BIGINT");
                } catch (Exception ex) {
                    throw new RuntimeException(ex.getMessage());
                }
            }
        }

        if (!String.valueOf(relationToOtherTable).isEmpty())
            queryCreateTable.add(String.valueOf(relationToOtherTable));

        return String.valueOf(queryCreateTable);
    }

    protected static String getNameTableFromClass(Class<?> clasEntity) {
        return "TBL_" + clasEntity.getSimpleName().toUpperCase();
    }

    protected static List<Class<?>> getAllDeclaredClass() {
        return findClassesInPackage("");
    }

    private static List<Class<?>> findClassesInPackage(String packageName) {
        List<Class<?>> classes = new ArrayList<>();
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String path = packageName.replace('.', '/');
            for (java.net.URL resource : java.util.Collections.list(classLoader.getResources(path))) {
                if (!resource.getFile().contains(".jar")) {
                    // Handle classes in directories
                    String filePath = resource.getFile();
                    List<Class<?>> classesInDirectory = findClassesInDirectory(packageName, filePath);
                    classes.addAll(classesInDirectory);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classes;
    }

    private static List<Class<?>> findClassesInDirectory(String packageName, String directory) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        java.io.File dir = new java.io.File(directory);
        if (!dir.exists()) {
            return classes;
        }
        java.io.File[] files = dir.listFiles();
        for (java.io.File file : files) {
            if (file.isDirectory()) {
                classes.addAll(findClassesInDirectory(packageName + "." + file.getName(), file.getAbsolutePath()));
            } else if (file.getName().endsWith(".class")) {
                String className = packageName.replace(".", "/") + '/' + file.getName().substring(0, file.getName().length() - 6);
                className = className.replaceAll("/", ".").replaceFirst(".", "").trim();
                Class<?> clazz = Class.forName(className);
                if (!BaseEntity.class.getTypeName().equals(className)
                        && BaseEntity.class.isAssignableFrom(clazz)
                        && !Modifier.isAbstract(clazz.getModifiers())) {
                    classes.add(clazz);
                }
            }
        }
        return classes;
    }
}
