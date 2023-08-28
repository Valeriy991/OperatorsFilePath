package org.valeriygulin.program;

import org.valeriygulin.util.Service;

import java.io.File;
import java.io.IOException;

public class Program {
    public static void main(String[] args) {
        /**
         * 1. Программа получает на вход имя корневой директории, где находится папка "new_data"
         */
        File fileDir = new File("D:\\Operators");
        Service service = new Service(fileDir);
        try {
            service.start();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
