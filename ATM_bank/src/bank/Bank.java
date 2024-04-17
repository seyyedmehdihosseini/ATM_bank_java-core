package bank;

import basicalClass.BaseEntity;

public class Bank extends BaseEntity {

    private Integer bankCode;
    private String bankName;


    public Integer getBankCode() {
        return bankCode;
    }

    public void setBankCode(Integer bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String nameBank) {
        this.bankName = nameBank;
    }

    @Override
    public String toString() {
        return "Bank{" +super.toString()+
                "bankCode=" + bankCode +
                ", bankName='" + bankName + '\'' +
                '}';
    }
}
