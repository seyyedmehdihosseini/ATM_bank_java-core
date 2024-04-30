package account;

import basicalClass.BaseEntity;

import java.util.List;

public class Account extends BaseEntity {

    private String username;
    private String password;
    private Boolean isActive;
    private RolePerson rolePerson;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public RolePerson getRolePerson() {
        return rolePerson;
    }

    public void setRolePerson(String rolePerson) {
        if (rolePerson.equalsIgnoreCase(RolePerson.CUSTOMER.name()))
            this.rolePerson = RolePerson.CUSTOMER;
        else if (rolePerson.equalsIgnoreCase(RolePerson.EMPLOYEE.name()))
            this.rolePerson = RolePerson.EMPLOYEE;
        else
            throw new RuntimeException("role is not valid for person ... ");
    }

    @Override
    public String getValueNameField() {
        return super.getValueNameField()+",USER_NAME,PASSWORD,IS_ACTIVE,ROLE_PERSON";
    }

    @Override
    public String toString() {
        return "Account{" + super.toString() +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", isActive=" + isActive +
                ", rolePerson=" + rolePerson +
                '}';
    }

}
