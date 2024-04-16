package input;

import java.util.Scanner;

public class Input {
    private static final Scanner scanner = new Scanner(System.in);
    private Input(){}

    public static Scanner getScanner(){
        return scanner;
    }

}
