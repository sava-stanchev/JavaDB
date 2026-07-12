package com.sava.javadb;

import java.util.Scanner;

public class Console {
    private final Scanner sc;

    public Console() {
        sc = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Welcome to JavaDB!");
        String input = sc.nextLine();
        System.out.println("You entered: " + input);
    }
}
