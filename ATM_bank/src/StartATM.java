import account.Account;
import input.Input;

public class StartATM {

    public static void startAtm(){
        boolean checkContinue = true;

        while (checkContinue){
            System.out.println("select one operation (enter just number)");
            System.out.println("1. login Employee" + '\t' + "2. login Customer"+ '\t' + "3. Exit");
            int selectOperation = Input.getScanner().nextInt();
            if (selectOperation == 1) {
            } else if (selectOperation == 2) {

            } else if (selectOperation == 3) {
                checkContinue = false;
            } else {
                System.out.println("number is not valid ... " + "\t\t\t" + "try again ... \n");
            }
        }
    }

}
