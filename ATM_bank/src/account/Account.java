package account;

import basicalClass.BaseEntity;

public class Account extends BaseEntity {

    private String username;
    private String password;
    private Boolean isActive;
    private AccountType accountType;


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

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(String rolePerson) {
        if (rolePerson.equalsIgnoreCase(AccountType.CUSTOMER.name()))
            this.accountType = AccountType.CUSTOMER;
        else if (rolePerson.equalsIgnoreCase(AccountType.EMPLOYEE.name()))
            this.accountType = AccountType.EMPLOYEE;
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
                ", rolePerson=" + accountType +
                '}';
    }

}
