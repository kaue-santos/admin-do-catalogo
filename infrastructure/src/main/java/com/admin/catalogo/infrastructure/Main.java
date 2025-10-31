package com.admin.catalogo.infrastructure;

import com.admin.catalogo.application.UseCase;

public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.printf("Hello and welcome!");
        System.out.println(new UseCase().execute());
    }
}