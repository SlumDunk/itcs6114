package com.uncc;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zerongliu
 * @Date: 10/2/18 11:52
 * @Description: implement quick algorithm, sort the numbers of input files and output the answer
 */
public class Qsort {
    /**
     * seperator of each number of each line
     */
    private static final String SEPERATOR = ";";

    /**
     * the name of output File
     */
    private static final String OUTPUT_FILE_NAME = "answer.txt";

    public static void main(String[] args) {
        String rootPath = System.getProperty("user.dir");
        String outputFilePath = rootPath + File.separator + OUTPUT_FILE_NAME;
        if (args.length == 0) {//path of input file must not be empty
            System.out.println("path of input file must not be empty!");
            return;
        } else {
            Qsort qSort = new Qsort();
            //the output file
            File outputFile = new File(outputFilePath);
            if (outputFile.exists()) {
                outputFile.delete();
            }
            //a stringbuffer that store the final output result
            StringBuffer finalResult = new StringBuffer();
            //a stringbutffer that store the performance result
            StringBuffer performanceResult = new StringBuffer();
            performanceResult.append("Performance");
            //a stringbuffer that store the sorted result
            StringBuffer sortedResult = new StringBuffer();
            sortedResult.append("Sorting result:");
            //loop read the input file
            for (int i = 0; i < args.length; i++) {
                String inputFileName = args[i];
                File inputFile = null;
                try {
                    //read an input file
                    String inputFilePath = rootPath + File.separator + inputFileName;
                    inputFile = ITCSUtils.readInputFile(inputFilePath);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("input file does not exist!");
                }
                BufferedReader reader = null;
                try {
                    if (inputFile != null) {
                        long startTime = System.currentTimeMillis();
                        reader = new BufferedReader(new FileReader(inputFile));
                        int[] tmpOriginArray;
                        List<Integer> numberList = new ArrayList<Integer>();
                        String tmpString;
                        try {
                            // read the numbers inside an input file and store them into a list
                            while ((tmpString = reader.readLine()) != null && tmpString.trim().length() > 0) {
                                numberList.addAll(ITCSUtils.splitNumbers(tmpString));
                            }
                            // sort the numbers in order
                            qSort.sort(numberList, 0, numberList.size() - 1);
                            long makespan = System.currentTimeMillis() - startTime;
                            //append the partial sorting result to the sortedResult
                            sortedResult.append(System.getProperty("line.separator"));
                            sortedResult.append(inputFileName + " sorted:");
                            sortedResult.append(ITCSUtils.transList2Str(numberList));

                            //append the partial performance result to the performanceResult
                            performanceResult.append(System.getProperty("line.separator"));
                            performanceResult.append("analysis:" + inputFileName);
                            performanceResult.append("   ");
                            performanceResult.append("Sorting Time:" + makespan);
                            performanceResult.append(System.getProperty("line.separator"));
                            performanceResult.append("Size " + inputFileName + " " + numberList.size());

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
            //append the sortedResult and performanceResult to the finalResult
            finalResult.append(sortedResult);
            finalResult.append(System.getProperty("line.separator"));
            finalResult.append(System.getProperty("line.separator"));
            finalResult.append(performanceResult);
            //output the finalResult to the output file
            ITCSUtils.writeOutputFile(outputFilePath, finalResult);

        }
    }


    /**
     * sort the numbers in array nums into order with algorithm named quick sort
     *
     * @param nums the array be sorted
     * @param low  the low index
     * @param high the high index
     */
    public void sort(List<Integer> nums, int low, int high) {
        if (low > high) {//if low > high, the list of numbers is in order
            return;
        }
        int pivot = nums.get(low);//set the pivot
        int i = low;
        int j = high;
        while (j > i) {//if j>i, continue the loop
            while (j > i && nums.get(j) >= pivot) {// from right to left, find the index of the number that smaller than pivot
                j--;
            }
            while (j > i && nums.get(i) <= pivot) {// from left to right, find the index of the number that bigger than pivot
                i++;
            }
            if (i < j) {//exchange nums[i] and nums[j]
                int tmp = nums.get(i);
                nums.set(i, nums.get(j));
                nums.set(j, tmp);
            }
        }
        //exchange num[i] and num[low], now the numbers in left of nums[i] are smaller than pivot, and the numbers in right of nums[i] are bigger than the pivot
        nums.set(low, nums.get(i));
        nums.set(i, pivot);
        //sort the numbers in the left part of pivot recursively
        sort(nums, low, i - 1);
        //sort the numbers in the right part of pivot recursively
        sort(nums, i + 1, high);
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
