package com.uncc;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zerong liu
 * @Date: 2018/8/27 16:37
 * @Description: a class that implement the insertion sort algorithm
 */
public class Lsort {

    /**
     * the name of output File
     */
    private static final String OUTPUT_FILE_NAME = "answer.txt";

    /**
     * the entrance of the application, a main function
     *
     * @param args the input parameters
     */
    public static void main(String[] args) {
        String rootPath = System.getProperty("user.dir");
        if (args.length == 0) {//path of input file must not be empty
            System.out.println("path of input file must not be empty!");
            return;
        }
        if (args.length > 1) {//path of input file only could be ones
            System.out.println("you only can input one path of input file!");
        } else {
            //read the input file
            String inputFileName = args[0];
            File inputFile = null;
            try {
                inputFile = ITCSUtils.readInputFile(rootPath + File.separator + inputFileName);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("input file does not exist!");
            }

            BufferedReader reader = null;
            try {
                if (inputFile != null) {
                    reader = new BufferedReader(new FileReader(inputFile));
                    StringBuffer result = new StringBuffer();
                    int[] tmpOriginArray;
                    List<Integer> numberList = new ArrayList<Integer>();
                    String tmpString;
                    try {
                        while ((tmpString = reader.readLine()) != null) {
                            numberList.addAll(ITCSUtils.splitNumbers(tmpString));
                        }
                        insertSort(numberList);
                        result.append(ITCSUtils.transList2Str(numberList));
                        ITCSUtils.writeOutputFile(rootPath + File.separator + OUTPUT_FILE_NAME, result);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("read a line of file error!");
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("transfer array of integer to string error!");
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.out.println("could not find the input file");
            } finally {
                ITCSUtils.closeFile(reader);
            }

        }
    }


    /**
     * sort the array of integers with the insertion sort algorithm
     *
     * @param numberList the original numberList, maybe it is disordered
     * @return the array in order
     */
    private static void insertSort(List<Integer> numberList) throws Exception {
        if (numberList == null || numberList.size() == 0) {
            System.out.println("the input number list is empty!");
            throw new Exception("the input number list is empty!");
        } else {
            int len = numberList.size();
            int tmp;
            int j;
            for (int i = 0; i < len; i++) {
                tmp = numberList.get(i);
                for (j = i; j > 0 && tmp < numberList.get(j - 1); j--) {
                    numberList.set(j, numberList.get(j - 1));
                }
                numberList.set(j, tmp);
            }
        }
    }

    /**
     * @Author: zerongliu
     * @Date: 10/2/18 11:59
     * @Description:
     */
    public static class ITCSUtils {
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
    }
}
