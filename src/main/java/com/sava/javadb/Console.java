package com.sava.javadb;

import java.util.Scanner;

public class Console {
    private final Scanner sc;

    public Console() {
        sc = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Welcome to JavaDB!");
        System.out.println("Type EXIT to quit.");

        while (true) {
            System.out.print("> ");
            String input = sc.nextLine();

            if (input.equalsIgnoreCase("exit"))
                break;

            System.out.println("You entered: " + input);
        }

        System.out.println("Goodbye!");
    }
}
