package io.github.miroslavpokorny.blog.testutil;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileReader {
    public static String readTextFile(String path) {
        try {
            return new Scanner(new File(path)).useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
