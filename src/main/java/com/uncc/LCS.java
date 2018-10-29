package com.uncc;

import java.io.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zerongliu
 * @Date: 10/28/18 12:50
 * @Description:
 */
public class LCS {
    /**
     * seperator of each number of each line
     */
    private static final String SEPERATOR = ";";

    /**
     * the name of output File
     */
    private static final String OUTPUT_FILE_NAME = "answer.txt";
    /**
     * line break
     */
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    /**
     * tab key
     */
    public static final String TAB = "\t";
    /**
     * symbol to separate partial result
     */
    public static final String PARTIAL_RESULT_SEPARATOR = "--------------------------------------------------------";
    /**
     * template for partial result
     */
    public static final String TEMPLATE = "The DNA strands:" + LINE_SEPARATOR + TAB + "{0}" + LINE_SEPARATOR + TAB + "{1}"
            + LINE_SEPARATOR + "LCS is " + "{2}" + LINE_SEPARATOR + "LCS length is " + "{3}" + LINE_SEPARATOR;
    /**
     * template for performance result
     */
    public static final String PERFORMANCE_TEMPLATE = "Running time: {0} Seconds" + LINE_SEPARATOR;
    /**
     * path of current file
     */
    public static final String ROOT_PATH = System.getProperty("user.dir");
    /**
     * path of output file
     */
    public static final String OUTPUT_FILE_PATH = ROOT_PATH + File.separator + OUTPUT_FILE_NAME;

    public static void main(String[] args) {

        if (args.length == 0) {//path of input file must not be empty
            System.out.println("path of input file must not be empty!");
            return;
        } else {
            LCS lcs = new LCS();
            //the output file
            File outputFile = new File(OUTPUT_FILE_PATH);
            if (outputFile.exists()) {
                outputFile.delete();
            }
            //a stringbuffer that store the final output result
            StringBuffer finalResult = new StringBuffer();
            //a stringbutffer that store the performance result
            StringBuffer performanceResult = new StringBuffer();
            long startTime = System.currentTimeMillis();
            //loop read the input file
            for (int i = 0; i < args.length; i++) {
                String inputFileName = args[i];
                File inputFile = null;
                inputFile = ITCSUtils.getFileFromPath(ROOT_PATH, inputFileName);
                BufferedReader reader = null;
                try {
                    if (inputFile != null) {
                        reader = new BufferedReader(new FileReader(inputFile));
                        int[] tmpOriginArray;
                        List<String> strandList = new ArrayList<String>();
                        String tmpString;
                        try {
                            // read all lines inside an input file and store them into a list
                            while ((tmpString = reader.readLine()) != null && tmpString.trim().length() > 0) {
                                strandList.add(tmpString);
                            }
                            StringBuilder resultBuffer = null;
                            String strand1 = null;
                            String strand2 = null;
                            int[][] flags = null;
                            for (int j = 0; j < strandList.size() - 1; j += 2) {
                                finalResult.append(PARTIAL_RESULT_SEPARATOR);
                                finalResult.append(LINE_SEPARATOR);
                                resultBuffer = resultBuffer.delete(0, resultBuffer.length());
                                strand1 = strandList.get(j);
                                strand2 = strandList.get(j + 1);
                                flags = lcs.findLcsLength(strand1, strand2);
                                lcs.getLcs(flags, strand1, strand1.length(), strand2.length(), resultBuffer);
                                finalResult.append(MessageFormat.format(TEMPLATE, strand1, strand2, resultBuffer.toString(), resultBuffer.length()));
                                finalResult.append(PARTIAL_RESULT_SEPARATOR);
                                finalResult.append(LINE_SEPARATOR);
                            }
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
            long endTime = System.currentTimeMillis();
            finalResult.append(MessageFormat.format(PERFORMANCE_TEMPLATE, (endTime - startTime) / 1000));
            finalResult.append(PARTIAL_RESULT_SEPARATOR);
            //output the finalResult to the output file
            ITCSUtils.writeOutputFile(OUTPUT_FILE_PATH, finalResult);

        }
    }

    /**
     * use dynamic programming idea to find the longest common subsequence of two strands
     *
     * @param strand1 the first strand
     * @param strand2 the second strand
     * @return
     */
    public int[][] findLcsLength(String strand1, String strand2) {
        int m = strand1.length();
        int n = strand2.length();
        int i, j;
        int[][] c = new int[m + 1][n + 1];
        int[][] flags = new int[m + 1][n + 1];
        c[0][0] = 0;
        for (i = 1; i <= m; i++)
            c[i][0] = 0;
        for (j = 0; j <= n; j++)
            c[0][j] = 0;
        for (i = 1; i <= m; i++)
            for (j = 1; j <= n; j++) {
                if (strand1.charAt(i - 1) == strand2.charAt(j - 1)) {
                    c[i][j] = c[i - 1][j - 1] + 1;
                    flags[i][j] = 1;
                } else if (c[i - 1][j] >= c[i][j - 1]) {
                    c[i][j] = c[i - 1][j];
                    flags[i][j] = 2;
                } else {
                    c[i][j] = c[i][j - 1];
                    flags[i][j] = 3;
                }
            }
        return flags;
    }

    /**
     * base on the array flags, find the longest common subsequence
     *
     * @param flags
     * @param strand       original string
     * @param m            length of first strand
     * @param n            length of second strand
     * @param resultBuffer result buffer
     */
    public void getLcs(int[][] flags, String strand, int m, int n, StringBuilder resultBuffer) {
        if (m == 0 || n == 0)
            return;
        if (flags[m][n] == 1) {
            getLcs(flags, strand, m - 1, n - 1, resultBuffer);
            resultBuffer.append(strand.charAt(m - 1));
        } else if (flags[m][n] == 2)
            getLcs(flags, strand, m - 1, n, resultBuffer);
        else
            getLcs(flags, strand, m, n - 1, resultBuffer);
    }
}
