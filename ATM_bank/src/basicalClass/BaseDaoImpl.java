package basicalClass;

import com.sun.xml.internal.ws.util.StringUtils;
import database.OtherDataSource;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BaseDaoImpl<E extends BaseEntity> implements BaseDao<E> {
    private final String INSERT = "INSERT INTO ";
    private final String SELECT_ALL = "SELECT * FROM ";
    private final String VALUES = " VALUES ";
    private final String TBL = "TBL_";

    private final Connection connection = OtherDataSource.getConnection();
    private Class<E> entityClass;

    public BaseDaoImpl(Class<E> entityClass) throws InstantiationException, IllegalAccessException {
        this.entityClass = entityClass;
    }

    public Class<E> getEntityClass() {
        return entityClass;
    }

    private final E entity = getEntityClass().newInstance();

    @Override
    public Boolean save(E e) {
        String tableName = TBL + entity.getClass().getName().toUpperCase().split("\\.")[0].toUpperCase();
          try {
              List<Field> listField = entity.getListDeclaredFieldsInThisClassNotId();
              String queryInsert = INSERT + tableName + '(' + entity.convertListFieldsToNameColumns(listField) + ')' + VALUES + entity.getNumOfBindVariableFromListField(listField);
              PreparedStatement preparedStatement = connection.prepareStatement(queryInsert);
            setValuesIntoPreparedStatementForSave(preparedStatement, listField).executeUpdate();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
        return null;
    }

    @Override
    public E getById(Long id) {
        String tableName = TBL + entity.getClass().getName().toUpperCase().split("\\.")[0].toUpperCase();

        String queryGetByID = SELECT_ALL + tableName + " WHERE " + "ID =?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(queryGetByID);
            ResultSet resultSet = preparedStatement.executeQuery();
            return setValueFromDatabaseIntoCurrentObject(resultSet).get(0);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return null;
        }
    }

    @Override
    public Boolean deleteById(Long id) {
        return null;
    }

    @Override
    public List<E> getAll() {
        String tableName = TBL + entity.getClass().getName().toUpperCase().split("\\.")[0].toUpperCase();
        String querySelectAll = SELECT_ALL + tableName;
        try {
            ResultSet resultSet = connection.prepareStatement(querySelectAll).executeQuery();
            return setValueFromDatabaseIntoCurrentObject(resultSet);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public void getMethodSet() {
        List<Field> fields = entity.getListDeclaredFieldsInThisClassNotId();
        try {
            for (Field field : fields) {
                String methodName = "set" + StringUtils.capitalize(field.getName());
                Method method;
                if (field.getType().isEnum())
                    method = entity.getClass().getMethod(methodName, String.class);
                else
                    method = entity.getClass().getMethod(methodName, field.getType());

                if (field.getType() == String.class) {
                    method.invoke(entity, "mehdi");
                }
                if (field.getType().isEnum()) {
                    method.invoke(entity, "BANK_MANAGER");
                }
                if (field.getType().isLocalClass()) {
                    method.invoke(entity, Class.forName(field.getType().getName()));
                }

            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println(Arrays.toString(ex.getStackTrace()));
        }
    }

    private List<E> setValueFromDatabaseIntoCurrentObject(ResultSet resultSet) throws Exception {
        List<Field> listDeclaredFieldsInThisClassWithId = entity.getListDeclaredFieldsInThisClassWithId();
        List<E> listEntity = new ArrayList<>();
        while (resultSet.next()) {
            E instance = entityClass.newInstance();
            for (Field currentField : listDeclaredFieldsInThisClassWithId) {

                String methodSetter = nameMethodSetterByFiled(currentField);
                Method method = instance.getClass().getMethod(methodSetter, currentField.getType());
                String columnName = instance.convertFieldToNameColumn(currentField);

                if (currentField.getType() == String.class || currentField.getType().isEnum())
                    method.invoke(instance, resultSet.getString(columnName));
                else if (currentField.getType() == Integer.class)
                    method.invoke(instance, resultSet.getInt(columnName));
                else if (currentField.getType() == Long.class)
                    method.invoke(instance, resultSet.getLong(columnName));
                else if (currentField.getType().isLocalClass()) {
                    Class<?> localClass = Class.forName(currentField.getType().getName());
                    Method setIdClass = localClass.getMethod("setId", Long.class);
                    setIdClass.invoke(localClass, resultSet.getLong(columnName));
                    method.invoke(instance, localClass);
                }
            }
            listEntity.add(instance);
        }
        return listEntity;
    }

    private String nameMethodSetterByFiled(Field field) {
        return "set" + StringUtils.capitalize(field.getName());
    }

    private Method setterMethod(Field field, Class<?> parameterType) throws Exception {
        String methodSetter = "set" + StringUtils.capitalize(field.getName());
        return entity.getClass().getMethod(methodSetter, parameterType);
    }

    private PreparedStatement setValuesIntoPreparedStatementForSave(PreparedStatement preparedStatement, List<Field> filedList) throws Exception {
        for (int i = 1; i <= filedList.size(); i++) {
            Field currentField = filedList.get(i);
            Object valueMethod = getValueMethodByFiled(currentField);
            if (currentField.getType() == String.class || currentField.getType().isEnum()) {
                preparedStatement.setString(i, String.valueOf(valueMethod));
            } else if (currentField.getType() == Integer.class) {
                preparedStatement.setInt(i, (Integer) valueMethod);
            } else if (currentField.getType() == Long.class) {
                preparedStatement.setLong(i, (Long) valueMethod);
            } else if (currentField.getType().isLocalClass()) {
                preparedStatement.setObject(i, valueMethod);
            }
        }
        return preparedStatement;
    }

    public Object getValueMethodByFiled(Field field) throws Exception {
        Method[] methods = entity.getClass().getMethods();

        Method methodGetByField = Arrays.stream(methods).filter(method -> isGetter(method) && method.getName().equals(nameMethodGetterByFiled(field))).findFirst().orElse(null);
        if (methodGetByField != null)
            return methodGetByField.invoke(entity);
        return null;
    }

    // تابعی برای بررسی اینکه آیا یک متد getter است یا نه
    private boolean isGetter(Method method) {
        return method.getName().startsWith("get") &&
                method.getParameterCount() == 0 &&
                !void.class.equals(method.getReturnType());
    }

    // نام متد getter بر اساس نام فیلد
    private String nameMethodGetterByFiled(Field field) {
        return "get" + StringUtils.capitalize(field.getName());
    }


}
