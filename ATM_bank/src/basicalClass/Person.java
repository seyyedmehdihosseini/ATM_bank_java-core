package basicalClass;

import entity.BaseEntity;

public abstract class Person extends BaseEntity {

    private String firstname;
    private String lastname;
    private String fatherName;
    private String nationalCode;
    //    private Date birthdate;
    private String mobileNumber;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
//        if (firstname.length() < 3)
//            throw new RuntimeException("name is not valid ... ");
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getNationalCode() {
        return nationalCode;
    }

    public void setNationalCode(String nationalCode) {
//        if (!Validation.validationNationalCode(nationalCode))
//            throw new RuntimeException("national code is not valid .... ");
        this.nationalCode = nationalCode;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
//        if (!Validation.validateMobilNumber(mobileNumber))
//            throw new RuntimeException("mobile number is not valid .... ");
        this.mobileNumber = mobileNumber;
    }

    @Override
    public String getValueNameField() {
        return super.getValueNameField()+",FIRST_NAME,LAST_NAME,FATHER_NAME,NATIONAL_CODE,MOBILE_NUMBER";
    }

    @Override
    public String toString() {
        return  super.toString() +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", fatherName='" + fatherName + '\'' +
                ", nationalCode='" + nationalCode + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' + ", ";
    }

}
