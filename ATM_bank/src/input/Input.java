package input;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Input {
    private static final Scanner scanner = new Scanner(System.in);
    private Input(){}

    public static Scanner getScanner(){
        return scanner;
    }

    private static void print(Object o){
        System.out.println(o);
    }

    public static <T> Object getInput(String massage, T c) {
        print(massage);
        try {
            if (c == int.class || c == Integer.class)
                return scanner.nextInt();
            if (c == String.class)
                return scanner.nextLine();
            if (c == double.class || c == Double.class)
                return scanner.nextDouble();
            if (c == float.class || c == Float.class)
                return scanner.nextFloat();
        } catch (InputMismatchException e) {
            return getInput("Not valid", c);
        }

        return null;
    }

}
