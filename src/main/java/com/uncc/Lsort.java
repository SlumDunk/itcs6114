package com.uncc;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zerong liu
 * @Date: 2018/8/27 16:37
 * @Description: a class that implement the insertion sort algorithm
 * <p>
 * Author:
 * Zerong Liu
 * <p>
 * Description of my program design:
 * 1. The function of method 'readInputFile(String inputFilePath)' is to read the input File.
 * 2. The function of method 'splitNumbers(String strNumbers)' is to split a string of numbers and transfer it into an array.
 * 3. The function of method 'insertSort(List<Integer> numberList)' is to sort an array of integers with algorithm of insertion sort.
 * 4. The function of method 'transList2Str(List<Integer> numberList)' is to transfer an array of numbers into a string.
 * 5. The function of method 'writeOutputFile(String outputFilePath, StringBuffer content)' is to ouput the result to a file.
 * <p>
 * so the progress is that:
 * 1. read the input file
 * 2. loop reading each line of the file until there is no new line
 * 2.1 transfer string of each line into a sub-list of numbers, and add them to the numberList
 * 3.sort the numbers in the numberList with algorithm of insertion
 * 4. write the final result to the ouput file.
 * <p>
 * <p>
 * breakdown of my algorithm:
 * int len = numberList.size();// the length of array
 * int tmp;// variable to store the temporary value
 * int j;
 * for (int i = 0; i < len; i++) {//loop the whole array of numbers
 * tmp = numberList.get(i);// assigns the current number to tmp
 * for (j = i; j > 0 && tmp < numberList.get(j - 1); j--) {// move the number to the back position if it is bigger than tmp
 * numberList.set(j, numberList.get(j - 1));
 * }
 * //move current number to the right place
 * numberList.set(j, tmp);
 * }
 * <p>
 * <p>
 * compiler: 1.8.0_111
 * <p>
 * platform: MacOS
 * <p>
 * summary of key factors:
 * 1. read file successfully.
 * 2. the number must be integer
 * 3. the content of the input file must be correct format
 * 4. implement the algorithm of insertion sort correctly.
 * 5. write the ouput file successfully.
 * <p>
 * data structure design:
 * 1. transfer each line of the input file into an list of integer.
 * 2. transfer the sorted numbers in the list into a String.
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
}
