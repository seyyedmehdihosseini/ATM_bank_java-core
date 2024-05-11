package bank;

import input.Input;

import java.util.List;

public class BankService {
    private static final BankDao bankDao = new BankDao();

    public static void menuBank() {
        boolean checkBack = true;

        while (checkBack) {
            System.out.println('\n' + "select one operation (enter just number) ");
            System.out.println("1.create bank" + '\t' + "2.get bank by id" + '\t' + "3.get all Bank" + '\t'
                    + "4.delete bank By id" + "\t\t" +"5.get bank By bank code"+ "\t\t" + "6.back");
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
                    Bank getBankById = bankDao.getById((Long) Input.getInput("enter id Bank :",Long.class));
                    System.out.println(getBankById);
                    break;
                case 3:
                    List<Bank> listBank = bankDao.getAll();
                    System.out.println(listBank);
                    break;
                case 4:
                    long idBankForDelete = (Long) Input.getInput("enter id Bank :",Long.class);
                    Boolean checkDelete = bankDao.deleteById(idBankForDelete);
                    if (checkDelete)
                        System.out.println("bank with id " + idBankForDelete + " was deleted .");
                    else
                        System.out.println("delete with id " + idBankForDelete + "failed ...");
                    break;
                case 5:
                    List<Bank> bankCode = bankDao.getByProperty("bankCode", (Integer) Input.getInput("enter bank code :", Integer.class));
                    if (!bankCode.isEmpty())
                        System.out.println(bankCode.get(0));
                    else
                        System.out.println("bank code is not found ... ");
                    break;
                case 6:
                    checkBack = false;
                    break;
                default:
                    System.out.println("number is not valid ... " + "\t\t\t" + "try again ... \n");
                    break;
            }
        }
    }

}
