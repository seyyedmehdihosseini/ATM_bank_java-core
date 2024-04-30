package basicalClass;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public abstract class BaseEntity {

    private Long id;
    private Date createDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getValueNameField() {
        return "CREATE_DATE";
    }

    public List<Field> getListDeclaredFieldsInThisClassWithId() {
        return new ArrayList<>(Arrays.asList(getAllFields(this.getClass())));
    }

    public List<Field> getListDeclaredFieldsInThisClassNotId() {
        Field[] declaredFields = getAllFields(this.getClass());
        List<Field> fieldList = new ArrayList<>();
        for (Field field : declaredFields) {
            if (!field.getName().equalsIgnoreCase("id"))
                fieldList.add(field);
        }
        return fieldList;
    }

    public String convertListFieldsToNameColumns(List<Field> fieldList) {
        return getAllNameColumnsFromListFields(fieldList);
    }

    public String convertFieldToNameColumn(Field field) {
        return getNameColumnFromNameField(field.getName());
    }

    private String getNameColumnFromNameField(String fieldName) {
        String upperCaseNameFiled = "";
        String[] split = fieldName.split("(?=[A-Z])");
        for (int i = 0; i < split.length; i++) {
            if ((split.length - 1) == i)
                upperCaseNameFiled += split[i];
            else
                upperCaseNameFiled = split[i] + "_";

        }
        return upperCaseNameFiled;
    }

    private String getAllNameColumnsFromListFields(List<Field> fieldList) {
        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < fieldList.size(); i++) {
            String nameField = fieldList.get(i).getName();
            String[] split = nameField.split("(?=[A-Z])");
            if (split.length > 1) {
                String upperCaseNameFiled = "";
                for (int j = 0; j < split.length; j++) {
                    if ((split.length - 1) == j) {
                        upperCaseNameFiled += split[j];
                    } else {
                        upperCaseNameFiled = split[j] + "_";
                    }
                }
                stringBuffer.append(upperCaseNameFiled.toUpperCase());
            } else
                stringBuffer.append(nameField.toUpperCase());

            if (i != fieldList.size() - 1)
                stringBuffer.append(",");

        }
        return String.valueOf(stringBuffer);
    }

    private Field[] getAllFields(Class<?> clazz) {
        Field[] declaredFields = clazz.getDeclaredFields();
        if (clazz.getSuperclass() != null) {
            Field[] fieldsSuperClass = getAllFields(clazz.getSuperclass());
            Field[] allFields = new Field[declaredFields.length + fieldsSuperClass.length];
            System.arraycopy(declaredFields, 0, allFields, 0, declaredFields.length);
            System.arraycopy(fieldsSuperClass, 0, allFields, declaredFields.length, fieldsSuperClass.length);
            return allFields;
        }
        return declaredFields;
    }

    public String getNumOfBindVariableFromListField(List<Field> fieldList) {
//        StringJoiner stringJoiner = new StringJoiner(",");
//        for (Field field : fieldList) {
//            stringJoiner.add("?");
//        }
//        return stringJoiner.toString();
        return String.join(",", Collections.nCopies(fieldList.size(), "?"));
    }

    public Map<String, String> mapFieldNameAndColumnName(List<String> allNameFields) {
        Map<String, String> fieldNameAndUpperCaseFieldName = new HashMap<>();
        for (String nameField : allNameFields) {
            String[] split = nameField.split("(?=[A-Z])");
            if (split.length > 1) {
                String upperCaseNameFiled = "";
                for (int j = 0; j < split.length; j++) {
                    if ((split.length - 1) == j) {
                        upperCaseNameFiled += split[j];
                    } else {
                        upperCaseNameFiled = split[j] + "_";
                    }
                }
                fieldNameAndUpperCaseFieldName.put(nameField, upperCaseNameFiled.toUpperCase());
            } else
                fieldNameAndUpperCaseFieldName.put(nameField, nameField.toUpperCase());
        }
        return fieldNameAndUpperCaseFieldName;
    }

    @Override
    public String toString() {
        return "id=" + id
                + ", createDate=" + createDate + ", ";
    }

}
