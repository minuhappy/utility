package com.utility.print;

import com.utility.print.service.JavaPrintService;

public class AapplicationMain {
    public static void main(String[] args) throws Exception {
        String printName = "Samsung SCX-3400 Series (SEC001599E13F05)";
        String command = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

        new JavaPrintService().doStreamPrint(printName, command);
    }
}
