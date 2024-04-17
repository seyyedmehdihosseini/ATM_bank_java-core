package bank;

import input.Input;

import java.util.List;
import java.util.Scanner;

public class BankService {
    private static final BankDao bankDao = new BankDao();

    public static void menuBank() {
        boolean checkBack = true;

        while (checkBack) {
            System.out.println('\n' + "select one operation (enter just number) ");
            System.out.println("1.create bank" + '\t' + "2.get bank by id" + '\t' + "3.get all Bank" + '\t' + "4.delete bank By id" + "\t\t" + "5.back");
            int selectOperation = Input.getScanner().nextInt();
            switch (selectOperation) {
                case 1:
                    Bank createBank = new Bank();
                    System.out.println("enter bank code :");
                    createBank.setBankCode(Input.getScanner().nextInt());
                    System.out.println("enter bank name :");
                    createBank.setBankName(Input.getScanner().next());
                    Boolean checkSave = bankDao.save(createBank);
                    if (checkSave)
                        System.out.println("create new Bank ....");
                    break;
                case 2:
                    System.out.println("enter id Bank :");
                    Bank getBankById = bankDao.getById(Input.getScanner().nextLong());
                    System.out.println(getBankById);
                    break;
                case 3:
                    List<Bank> listBank = bankDao.getAll();
                    System.out.println(listBank);
                    break;
                case 4:
                    System.out.println("enter id Bank :");
                    long idBankForDelete = Input.getScanner().nextLong();
                    Boolean checkDelete = bankDao.deleteById(idBankForDelete);
                    if (checkDelete)
                        System.out.println("bank with id " + idBankForDelete + " was deleted .");
                    else
                        System.out.println("delete with id " + idBankForDelete + "failed ...");
                    break;
                case 5:
                    checkBack = false;
                    break;
                default:
                    System.out.println("number is not valid ... " + "\t\t\t" + "try again ... \n");
                    break;
            }
        }

    }

}
