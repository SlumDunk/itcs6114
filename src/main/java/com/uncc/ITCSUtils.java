package com.uncc;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zerongliu
 * @Date: 10/2/18 11:59
 * @Description:
 */
public class ITCSUtils {
    /**
     * seperator of each number of each line
     */
    private static final String SEPERATOR = ";";


    /**
     * read the inputFile from path of the file, if the path is invalid, return null
     *
     * @param inputFilePath path of the input file
     * @return return the input file
     */
    public static File readInputFile(String inputFilePath) throws Exception {
        File inputFile = new File(inputFilePath);
        if (inputFile.length() != 0) {
            return inputFile;
        } else {
            throw new Exception("the input file does not exist!");
        }
    }


    /**
     * split the string composed with numbers and semicolon by semicolon,
     * and transfer the it into array of numbers
     *
     * @param strNumbers a string composed with numbers
     * @return an array of numbers
     */
    public static List<Integer> splitNumbers(String strNumbers) {
        if (strNumbers == null || strNumbers.length() == 0) {
            System.out.println("the string of numbers is empty!");
            return null;
        } else {
            String[] arrStr = strNumbers.split(SEPERATOR);
            List<Integer> subNumberList = new ArrayList<Integer>();
            for (int i = 0; i < arrStr.length; i++) {
                subNumberList.add(Integer.parseInt(arrStr[i].trim()));
            }
            return subNumberList;
        }
    }


    /**
     * transfer an array of integer to a String
     *
     * @param numberList an array of integer
     */
    public static String transList2Str(List<Integer> numberList) throws Exception {
        if (numberList != null && numberList.size() >= 1) {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < numberList.size(); i++) {
                result.append(numberList.get(i));
                if (i != numberList.size() - 1) {
                    result.append(SEPERATOR);
                }
            }
            return result.toString();
        } else {
            throw new Exception("the input array could not be empty!");
        }
    }

    /**
     * write the result into the path of the output file
     *
     * @param outputFilePath the path of the output file
     * @param content        the content to output
     */
    public static void writeOutputFile(String outputFilePath, StringBuffer content) {
        FileWriter writer = null;
        try {
            File outputFile = new File(outputFilePath);
            if (outputFile.exists()) {
                outputFile.delete();
            }
            writer = new FileWriter(outputFilePath);
            writer.write(content.toString());
            writer.flush();
            System.out.println("successful!");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("write answer to output file error!");
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("close output file stream error!");
                }
            }
        }
    }


    public static void closeFile(BufferedReader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("close input file reader error!");
            }
        }
    }

    public static File getFileFromPath(String rootPath, String inputFileName) {
        File inputFile = null;
        try {
            //read an input file
            String inputFilePath = rootPath + File.separator + inputFileName;
            inputFile = ITCSUtils.readInputFile(inputFilePath);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("input file does not exist!");
        }
        return inputFile;
    }

}

