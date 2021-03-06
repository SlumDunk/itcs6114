package com.uncc;

import java.io.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zerongliu
 * @Author: Shane Polefko
 * @Author: Zhang Zhang
 * @Date: 10/30/18 12:50
 * @Description: Description of Program: The program is designed to take a given input file that has DNA strands,
 * read the strands in, find the longest common subsequence between every two pairs, and print the
 * results and performance out into a new file "answer.txt".
 * Key Functions: The reads in a file of DNA strands first. The file must not be empty, and it must
 * have at least 2 DNA strands in it. The DNA strands must be separated by new lines, and they must be
 * input in pairs (i.e. DNA strands 1 and 2 will be compared, strands 3 and 4 will be compared, etc). If any
 * strand is at the end without a paired strand, it will be dropped.
 * After they are read in, the program will find the longest common subsequence using the algorithm
 * described within Introduction to Algorithms 3rd Edition. It uses two 2d arrays; one that stores the counts
 * for calculating the LCS, and one that as a pointer to rebuild the optimal subsequence.
 * Once the LCS is calculated for each pair of strands that was read in, the program will write out
 * a new file that contains the DNA strand pairs, the LCS for the pairs, and the performance metrics.
 * Compiler used: Eclipse, IntelliJ
 * Platform: Windows 10, Mac
 * Compiler: JDK 1.8
 * What works: For our scope, the program efficiently sorts through DNA strands given to produce correct
 * output of the LCS. We designed the program with good practices in mind, managed common exceptions that
 * could occur, and utilized variable naming in a way that easily tells a reader what the program is doing.
 * What fails: We could potentially make the program more efficient. For example, in printing the lcs,
 * we could have removed the flags array and created logic to instead manage the printing based on the values
 * within the c table. This would provide us with more efficient use of memory if the program was ever used
 * for much longer DNA strands.
 * Data Structure Description: Our program mostly utilized lists and 2d arrays. Lists were used because
 * our program needed sequentially access and ordered sets of strings. 2d arrays were used as a table or
 * matrix to store the results for the LCS as if strand1 was the row header and strand 2 was the column
 * header.
 */
public class LCS {
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
                        List<String> strandList = new ArrayList<String>();
                        String tmpString;
                        StringBuilder resultBuffer = new StringBuilder();

                        try {
                            // read all lines inside an input file and store them into a list
                            while ((tmpString = reader.readLine()) != null && tmpString.trim().length() > 0) {
                                strandList.add(tmpString);
                            }
                            if (strandList.size() < 2) {
                                System.out.println("Not enough strands in file to provide a comparison.");
                                System.out.println("Please restart program with a file with at least 2 strands");
                                return;
                            }
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
     * use dynamic programming idea to find the longest common subsequence of two strands.
     * flags are used as a pointer to the optimal solution found within the c table. If flags
     * is looked as a 2d table, 1 is an arrow pointing northwest, 2 is an arrow pointing north,
     * and 3 is an arrow pointing west.
     *
     * @param strand1 the first strand
     * @param strand2 the second strand
     * @return the 2d int array that points to the optimal solution of LCS
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
     * @param resultBuffer holds the found optimal LCS
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
