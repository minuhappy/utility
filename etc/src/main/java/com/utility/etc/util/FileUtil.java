package com.utility.etc.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.StringJoiner;

/**
 * @author Minu.Kim
 */
public class FileUtil {

    private static String CHAR_SET_UTF8 = "UTF-8";

    /**
     * File 생성.
     *
     * @param dirPath
     * @param fileName
     * @param content
     */
    public static void create(String dirPath, String fileName, String content) throws Exception {
        // 1. Directory 체크 후 존재하지 않으면 생성
        File dir = new File(dirPath);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 2. File 구분자 추가.
        if (!dirPath.endsWith(File.separator)) {
            dirPath += File.separator;
        }

        File file = new File(dirPath, fileName);
        try (FileWriter fileWriter = new FileWriter(file); BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(content);
        }
    }

    /**
     * File 내용 추가
     */
    public static void update(String filePath, String value) throws Exception {
        update(new File(filePath), value);
    }

    public static void update(File file, String value) throws Exception {
        String readData = read(file);

        StringJoiner contents = new StringJoiner("\n");
        if (readData != null && !readData.isEmpty()) {
            contents.add(readData);
        }

        contents.add(value);

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            bufferedWriter.write(contents.toString());
        }
    }

    public void delete(String filePath) throws Exception {
        this.delete(new File(filePath));
    }

    public void delete(File file) throws Exception {
        // 위험해서 주석 처리.
        // if (file.isDirectory()) {
        // File[] files = file.listFiles();
        // for (File f : files)
        // this.delete(f);
        // }

        file.delete();
    }

    /**
     * File 읽어서 내용을 String으로 리턴
     */
    public static String read(String filePath) throws Exception {
        return read(new File(filePath));
    }

    public static String read(String dirPath, String fileName) throws Exception {
        return read(new File(dirPath, fileName));
    }

    public static String read(File file) throws Exception {
        if (!file.exists())
            return null;

        return new String(Files.readAllBytes(Paths.get(file.toString())), CHAR_SET_UTF8);
    }

    /**
     * classpath 내의 파일 내용 불러오기.
     *
     * @param path
     * @return
     */
    //	public static String readByClassPath(String path) throws Exception {
    //		if (path == null || path.isEmpty())
    //			return null;
    //
    //		return IOUtils.toString(new ClassPathResource(path.trim()).getInputStream(), CHAR_SET_UTF8);
    //	}
}