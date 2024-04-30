package basicalClass;

import input.Input;

import java.util.Date;

public class BasicInformationPerson<E extends Person> {
    private E e;

    public BasicInformationPerson(E e) {
        this.e = e;
    }

    public BasicInformationPerson() {
    }

    public E getE() {
        return e;
    }

    public void setE(E informationPerson) {
        informationPerson.setCreateDate(new Date());
        System.out.println("enter first name :");
        informationPerson.setFirstname(Input.getScanner().next());
        System.out.println("enter last name :");
        informationPerson.setLastname(Input.getScanner().next());
        System.out.println("enter father name :");
        informationPerson.setFatherName(Input.getScanner().next());
        System.out.println("enter national code :");
        informationPerson.setNationalCode(Input.getScanner().next());
        System.out.println("enter Mobile Number :");
        informationPerson.setMobileNumber(Input.getScanner().next());

        this.e = informationPerson;
    }



}
