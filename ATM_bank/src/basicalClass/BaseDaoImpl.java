package basicalClass;

import com.sun.xml.internal.ws.util.StringUtils;
import database.DataSource;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class BaseDaoImpl<E extends BaseEntity> implements BaseDao<E> {
    private final String INSERT = "INSERT INTO ";
    private final String SELECT_ALL = "SELECT * FROM ";
    private final String DELETE = "DELETE FROM";
    private final String VALUES = "VALUES";

    //.getInstance().getConnection("mysql", "root", "13311376", "bank")
    protected final Connection connection = DataSource.getConnection();
    private Class<E> entityClass;
    private E entity;

    private BaseDaoImpl() {
    }

    protected BaseDaoImpl(Class<E> entityClass) {
        try {
            this.entityClass = entityClass;
            this.entity = createEntity();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public E createEntity() {
        try {
            return this.entityClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Boolean save(E e) {
        try {
            e.setCreateDate(new Date());
            List<Field> listField = e.getListDeclaredFieldsInThisClassNotId();
            StringJoiner queryInsert = new StringJoiner(" ");
            queryInsert.add(INSERT).add("TBL_" + entityClass.getSimpleName().toUpperCase()).add("(").add(e.convertListFieldsToNameColumns(listField)).add(")").add(VALUES).add("(").add(e.getNumOfBindVariableFromListField(listField)).add(")");
            PreparedStatement preparedStatement = connection.prepareStatement(String.valueOf(queryInsert));
            setValuesIntoPreparedStatementForSave(preparedStatement, listField, e).executeUpdate();
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException("save failed .... ");
        }
    }

    @Override
    public E getById(Long id) {
        StringJoiner queryGetByID = new StringJoiner(" ");
        queryGetByID.add(SELECT_ALL).add("TBL_" + entityClass.getSimpleName().toUpperCase()).add("WHERE").add("ID=?");
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(String.valueOf(queryGetByID));
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return setValueFromDatabaseIntoCurrentObject(resultSet).get(0);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return null;
        }
    }

    @Override
    public Boolean deleteById(Long id) {
        StringJoiner queryGetByID = new StringJoiner(" ");
        queryGetByID.add(DELETE).add("TBL_" + entityClass.getSimpleName().toUpperCase()).add("WHERE").add("ID=?");
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(String.valueOf(queryGetByID));
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            return true;
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return false;
        }
    }

    @Override
    public List<E> getAll() {
        String querySelectAll = SELECT_ALL + "TBL_" + entityClass.getSimpleName().toUpperCase();
        try {
            ResultSet resultSet = connection.prepareStatement(querySelectAll).executeQuery();
            return setValueFromDatabaseIntoCurrentObject(resultSet);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    @Override
    public List<E> getByProperty(String propertyName, Object values) {
        List<Field> allFieldsIntoClass = entity.getListDeclaredFieldsInThisClassWithId();
        Field property = allFieldsIntoClass.stream().filter(field -> field.getName().equalsIgnoreCase(propertyName))
                .findFirst().orElseThrow(() -> new RuntimeException("property is not present into class by this " + propertyName));

        String queryGetByProperty = SELECT_ALL + "TBL_" + entityClass.getSimpleName().toUpperCase() + " WHERE " + entity.convertFieldToNameColumn(property) + "=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(queryGetByProperty);
            return setValueFromDatabaseIntoCurrentObject(setValueIntoPreparedStatement(preparedStatement, property, 1, values).executeQuery());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public Boolean update(Long id, E e) {
        E oldObject = getById(id);
        if (oldObject != null) {
            StringJoiner queryUpdate = new StringJoiner(" ");
            queryUpdate.add("UPDATE").add("TBL_" + entityClass.getSimpleName().toUpperCase()).add("SET");
            List<Field> listFields = oldObject.getListDeclaredFieldsInThisClassNotId();
            StringJoiner setColumnAndValue = new StringJoiner(",");

            try {
                for (Field currentField : listFields) {
                    if (!currentField.getName().equalsIgnoreCase("ID") && !currentField.getName().equalsIgnoreCase("createDate")) {
                        Object valueOldFiled = getValueMethodByFiled(currentField, oldObject);
                        Object valueNewField = getValueMethodByFiled(currentField, e);
                        if (!valueOldFiled.equals(valueNewField))
                            setColumnAndValue.add(oldObject.convertFieldToNameColumn(currentField) + "= " + valueNewField);
                    }
                }

                queryUpdate.add(String.valueOf(setColumnAndValue)).add("WHERE ID = ?");

                PreparedStatement preparedStatement = connection.prepareStatement(String.valueOf(queryUpdate));
                preparedStatement.setLong(1, oldObject.getId());
                preparedStatement.executeUpdate();

                return true;
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                return false;
            }
        }
        throw new RuntimeException("id is not valid .... ");
    }

    protected List<E> setValueFromDatabaseIntoCurrentObject(ResultSet resultSet) {
        try {
            List<E> listEntity = new ArrayList<>();
            while (resultSet.next()) {
                E instance = entityClass.newInstance();
                List<Field> listDeclaredFieldsInThisClassWithId = instance.getListDeclaredFieldsInThisClassWithId();
                for (Field currentField : listDeclaredFieldsInThisClassWithId) {

                    String methodSetter = nameMethodSetterByFiled(currentField);
                    Method method = instance.getClass().getMethod(methodSetter, currentField.getType());
                    String columnName = instance.convertFieldToNameColumn(currentField);
                    Class<?> typeCurrentField = currentField.getType();
                    if (typeCurrentField == String.class || typeCurrentField.isEnum())
                        method.invoke(instance, resultSet.getString(columnName));
                    else if (typeCurrentField == Integer.class)
                        method.invoke(instance, resultSet.getInt(columnName));
                    else if (typeCurrentField == Date.class)
                        method.invoke(instance, resultSet.getDate(columnName));
                    else if (typeCurrentField == Long.class)
                        method.invoke(instance, resultSet.getLong(columnName));
                    else if (typeCurrentField == Float.class)
                        method.invoke(instance, resultSet.getFloat(columnName));
                    else if (typeCurrentField == Double.class)
                        method.invoke(instance, resultSet.getDouble(columnName));
                    else if (typeCurrentField == Boolean.class)
                        method.invoke(instance, resultSet.getBoolean(columnName));
                    else if (BaseEntity.class.isAssignableFrom(typeCurrentField)) {
                        Class<?> localClass = Class.forName(typeCurrentField.getName());
                        Method setIdClass = localClass.getMethod("setId", Long.class);
                        setIdClass.invoke(localClass, resultSet.getLong(columnName));
                        method.invoke(instance, localClass);
                    }
                }
                listEntity.add(instance);
            }
            return listEntity;
        } catch (SQLException | ClassNotFoundException | InvocationTargetException | InstantiationException |
                 IllegalAccessException | NoSuchMethodException sqlException) {
            System.out.println(sqlException.getMessage());
            throw new RuntimeException("catch from database failed .... ");
        }
    }

    protected E setObject(E en) {
        try {
            List<Field> listDeclaredFieldsInThisClassWithId = en.getListDeclaredFieldsInThisClassWithId();
            E instance = entityClass.newInstance();
            for (Field currentField : listDeclaredFieldsInThisClassWithId) {
                String methodSetter = nameMethodSetterByFiled(currentField);
                Object getValueMethodByFiledAndInstanceFromEntity = getValueMethodByFiled(currentField, en);
                Class<?> typeCurrentField = currentField.getType();
                if (BaseEntity.class.isAssignableFrom(typeCurrentField)) {
                    Method method = en.getClass().getMethod(methodSetter, typeCurrentField);
                    Class<?> localClass = Class.forName(typeCurrentField.getName());
                    Method setIdMethod = localClass.getMethod("setId", Long.class);
                    setIdMethod.invoke(localClass, (Long) getValueMethodByFiledAndInstanceFromEntity);
                    method.invoke(instance, localClass);
                } else if (typeCurrentField.isEnum()) {
                    Method method = en.getClass().getMethod(methodSetter, String.class);
                    Enum<?> enumValue = getValueEnumClassFromFiledEnumAndValue(currentField, getValueMethodByFiledAndInstanceFromEntity);
                    method.invoke(instance, enumValue.name());
                } else {
                    Method method = en.getClass().getMethod(methodSetter, typeCurrentField);
                    method.invoke(instance, getValueMethodByFiledAndInstanceFromEntity);
                }
            }
            return instance;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException("operation for set into object failed ....");
        }
    }

    private Enum<?> getValueEnumClassFromFiledEnumAndValue(Field field, Object value) {
        Class<?> enumClass = field.getType();
        return Enum.valueOf((Class<? extends Enum>) enumClass, String.valueOf(value));
    }

    private String nameMethodSetterByFiled(Field field) {
        return "set" + StringUtils.capitalize(field.getName());
    }

    private Method setterMethod(Field field, Class<?> parameterType, E e) throws Exception {
        String methodSetter = "set" + StringUtils.capitalize(field.getName());
        return e.getClass().getMethod(methodSetter, parameterType);
    }

    private PreparedStatement setValuesIntoPreparedStatementForSave(PreparedStatement preparedStatement, List<Field> filedList, E e) throws Exception {
        for (int i = 0; i < filedList.size(); i++) {
            int index = i + 1;
            Field currentField = filedList.get(i);
            Object valueMethod = getValueMethodByFiled(currentField, e);

            Class<?> typeCurrentField = currentField.getType();

            if (typeCurrentField == String.class || typeCurrentField.isEnum())
                preparedStatement.setString(index, String.valueOf(valueMethod));
            else if (typeCurrentField == Date.class) {
                Date date = (Date) valueMethod;
                assert date != null;
                preparedStatement.setDate(index, new java.sql.Date(date.getTime()));
            } else if (typeCurrentField == Integer.class)
                preparedStatement.setInt(index, (Integer) valueMethod);
            else if (typeCurrentField == Long.class)
                preparedStatement.setLong(index, (Long) valueMethod);
            else if (typeCurrentField == Boolean.class)
                preparedStatement.setBoolean(index, (Boolean) valueMethod);
            else if (typeCurrentField == Double.class)
                preparedStatement.setDouble(index, (Double) valueMethod);
            else if (typeCurrentField == Float.class)
                preparedStatement.setFloat(index, (Float) valueMethod);
            else if (BaseEntity.class.isAssignableFrom(typeCurrentField))
                preparedStatement.setObject(index, valueMethod);
        }
        return preparedStatement;
    }

    private PreparedStatement setValueIntoPreparedStatement(PreparedStatement preparedStatement, Field currentField, Integer index, Object value) throws Exception {
        try {
            Class<?> typeCurrentField = currentField.getType();

            if (typeCurrentField == String.class || typeCurrentField.isEnum())
                preparedStatement.setString(index, String.valueOf(value));
            else if (typeCurrentField == Date.class) {
                Date date = (Date) value;
                assert date != null;
                preparedStatement.setDate(index, new java.sql.Date(date.getTime()));
            } else if (typeCurrentField == Integer.class)
                preparedStatement.setInt(index, (Integer) value);
            else if (typeCurrentField == Long.class)
                preparedStatement.setLong(index, (Long) value);
            else if (typeCurrentField == Boolean.class)
                preparedStatement.setBoolean(index, (Boolean) value);
            else if (typeCurrentField == Double.class)
                preparedStatement.setDouble(index, (Double) value);
            else if (typeCurrentField == Float.class)
                preparedStatement.setFloat(index, (Float) value);
            else if (BaseEntity.class.isAssignableFrom(typeCurrentField))
                preparedStatement.setObject(index, value);

            return preparedStatement;
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private Object getValueMethodByFiled(Field field, E e) throws Exception {
        Method[] methods = e.getClass().getMethods();
        Method methodGetByField = Arrays.stream(methods).filter(method -> isGetter(method) && method.getName().equals(nameMethodGetterByFiled(field))).findFirst().orElse(null);
        return methodGetByField.invoke(e);
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
