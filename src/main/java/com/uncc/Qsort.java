package com.uncc;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zerongliu
 * @Date: 10/2/18 11:52
 * @Description: implement quick algorithm, sort the numbers of input files and output the answer
 * <p>
 * Author:
 * Zerong Liu
 * <p>
 * Description of my program design:
 * Class ITCSUtils provide several public funtions for the program
 * 1. The function of method 'readInputFile(String inputFilePath)' is to read the input File.
 * 2. The function of method 'splitNumbers(String strNumbers)' is to split a string of numbers and transfer it into an array.
 * 3. The function of method 'transList2Str(List<Integer> numberList)' is to transfer an array of numbers into a string.
 * 4. The function of method 'writeOutputFile(String outputFilePath, StringBuffer content)' is to ouput the result to a file.
 * <p>
 * Class Qsort provide method that implements algorithm of quick sort and the entrance of program
 * 1. The function of method 'sort(List<Integer> nums, int low, int high)' is to sort numbers with quick sort algorithm.
 * <p>
 * so the progress is that:
 * 1. read the paths of input files
 * 2. loop reading each file
 * loop reading each line of the file until there is no new line
 * transfer string of each line into a sub-list of numbers, and add them to the numberList
 * sort the numbers in the numberList with algorithm of quick sort
 * append the sub sorting result to the global sortingResult
 * append the sub performance result to the global performanceResult
 * 3. append sortingResult and performanceResult to the finalResult
 * 4. write the final result to the ouput file.
 * <p>
 * <p>
 * breakdown of my algorithm:
 * if (low > high) {//if low > high, the list of numbers is in order
 * return;
 * }
 * int pivot = nums.get(low);//set the pivot
 * int i = low;
 * int j = high;
 * while (j > i) {//if j>i, continue the loop
 * while (j > i && nums.get(j) >= pivot) {// from right to left, find the index of the number that smaller than pivot
 * j--;
 * }
 * while (j > i && nums.get(i) <= pivot) {// from left to right, find the index of the number that bigger than pivot
 * i++;
 * }
 * if (i < j) {//exchange nums[i] and nums[j]
 * int tmp = nums.get(i);
 * nums.set(i, nums.get(j));
 * nums.set(j, tmp);
 * }
 * }
 * //exchange num[i] and num[low], now the numbers in left of nums[i] are smaller than pivot, and the numbers in right of nums[i] are bigger than the pivot
 * nums.set(low, nums.get(i));
 * nums.set(i, pivot);
 * //sort the numbers in the left part of pivot recursively
 * sort(nums, low, i - 1);
 * //sort the numbers in the right part of pivot recursively
 * sort(nums, i + 1, high);
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
 * 4. implement the algorithm of quick sort correctly.
 * 5. write the ouput file successfully.
 * <p>
 * data structure design:
 * 1. transfer each line of the input file into an list of integer.
 * 2. transfer the sorted numbers in the list into a String.
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
    public static final String ROOT_PATH = System.getProperty("user.dir");
    public static final String outputFilePath = ROOT_PATH + File.separator + OUTPUT_FILE_NAME;

    public static void main(String[] args) {

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
                inputFile = ITCSUtils.getFileFromPath(ROOT_PATH, inputFileName);
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
}
