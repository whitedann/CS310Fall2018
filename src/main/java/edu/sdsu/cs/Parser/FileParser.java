package edu.sdsu.cs.Parser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class FileParser implements Parser {

    private Path currentPath, outputPath;
    private List<Integer> counts, mostOccurringCounts, leastOccurringCounts,
            leastOccurringCountsNoCase, mostOccurringCountsNoCase,
            countsCaseInsensitive;
    private List<String> tokens, uniqueTokens, lines,
            mostOccurring, leastOccurring,
            mostOccurringNoCase, leastOccurringNoCase,
            uniqueTokensCaseInsensitive;
    private Scanner tokenize;
    private int lineLengthAvg, lengthOfLongestLine;

    public FileParser() {
        this.uniqueTokens = new ArrayList<>();
        this.counts = new ArrayList<>();
        this.uniqueTokensCaseInsensitive = new ArrayList<>();
        this.countsCaseInsensitive = new ArrayList<>();
        this.mostOccurring = new ArrayList<>();
        this.leastOccurring = new ArrayList<>();
        this.mostOccurringCounts = new ArrayList<>();
        this.leastOccurringCounts = new ArrayList<>();
        this.leastOccurringNoCase = new ArrayList<>();
        this.mostOccurringNoCase = new ArrayList<>();
        this.mostOccurringCountsNoCase = new ArrayList<>();
        this.leastOccurringCountsNoCase = new ArrayList<>();
    }

    private void readFile() {
        try {
            this.lines = Files.readAllLines(currentPath,
                    Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void tokenizeLinesInFile() {
        /** The means for splitting the lines into tokens was inspired
         *  by this post: http://www.fredosaurus.com/notes-java/data/strings
         *  /96string_examples/example_stringToArray.html */

        this.tokens = new ArrayList<>();
        int lineLengthTotal = 0;
        int numberOfLines = 0;
        lengthOfLongestLine = 0;

        for(String line : this.lines){
            numberOfLines++;
            lineLengthTotal += line.length();
            if(line.length() > lengthOfLongestLine)
                lengthOfLongestLine = line.length();
            this.tokenize = new Scanner(line);
            while(tokenize.hasNext())
                this.tokens.add(tokenize.next());
        }
        this.lineLengthAvg = lineLengthTotal / numberOfLines;

    }

    private void parseTextCaseSensitive(){
        for(String e : this.tokens){
            if(this.uniqueTokens.contains(e)) {
                int indexOfElement = this.uniqueTokens.indexOf(e);
                int newCount = this.counts.get(indexOfElement) + 1;
                this.counts.set(indexOfElement, newCount);
            }
            else {
                this.uniqueTokens.add(e);
                this.counts.add(1);
            }
        }
    }

    private void parseTextCaseInsensitive(){
        for(String e : this.tokens){
            if(this.uniqueTokensCaseInsensitive.contains(e.toLowerCase())) {
                int indexOfElement =
                        this.uniqueTokensCaseInsensitive.indexOf(e.toLowerCase()
                        );
                int newCount =
                        this.countsCaseInsensitive.get(indexOfElement) + 1;
                this.countsCaseInsensitive.set(indexOfElement, newCount);
            }
            else {
                this.uniqueTokensCaseInsensitive.add(e.toLowerCase());
                this.countsCaseInsensitive.add(1);
            }
        }
    }

    private void findMostOccurringTokenInFile(List<String> tokenList,
                                       List<Integer> countsList,
                                       List<String> outputTokens,
                                       List<Integer> outputCounts)
    {
        /** Similar to the method below, we have to remove the discovered
         * element from the list, so that when we run the method again, we
         * are searching for the "next most" occurring.
         */
        int highestCount = 0;
        for(int e : countsList) {
            if (e > highestCount)
                highestCount = e;
        }
        int index = countsList.indexOf(highestCount);
        outputTokens.add(tokenList.get(index));
        outputCounts.add(countsList.get(index));
        removeTokenFromListByIndex(index, tokenList, countsList);
    }

    private void findLeastOccuringToken(List<String> tokensList,
                                        List<Integer> countsList,
                                        List<String> outputTokens,
                                        List<Integer> outputCounts)
    {
        /** Generates list of least occurring tokens. This method will be run
         * 10 times on the same list, so we have to remove the discovered token
         * from the master list at the end of each call. */
        int lowestCount = Integer.MAX_VALUE;
        for(int e : countsList) {
            if (e < lowestCount)
                lowestCount = e;
        }
        int index = countsList.indexOf(lowestCount);
        outputTokens.add(tokensList.get(index));
        outputCounts.add(countsList.get(index));
        removeTokenFromListByIndex(index, tokensList, countsList);
    }

    private void removeTokenFromListByIndex(int index, List<String> uniques,
                                            List<Integer> counts)
    {
        /** Removes target index element from the two input lists **/
        Iterator<String> strIter = uniques.iterator();
        Iterator<Integer> intIter = counts.iterator();
        for(int i = 0; i <= index; i++) {
            strIter.next();
            intIter.next();
        }
        strIter.remove();
        intIter.remove();
    }

    private void findTopTenMostAndLeastOccuring(){
        /** builds a list of less than 10, if fewer than 10 unique tokens
         * exist in the file */
        List<String> uniquesCopy1 = new ArrayList<>(this.uniqueTokens);
        List<String> uniquesCopy2 = new ArrayList<>(this.uniqueTokens);
        List<String> uniquesCopy3 =
                new ArrayList<>(this.uniqueTokensCaseInsensitive);
        List<String> uniquesCopy4 =
                new ArrayList<>(this.uniqueTokensCaseInsensitive);
        List<Integer> countsCopy1 = new ArrayList<>(this.counts);
        List<Integer> countsCopy2 = new ArrayList<>(this.counts);
        List<Integer> countsCopy3 = new ArrayList<>(this.countsCaseInsensitive);
        List<Integer> countsCopy4 = new ArrayList<>(this.countsCaseInsensitive);
        int sizeCaseSensitive = this.uniqueTokens.size();
        int sizeCaseInsensitive = this.uniqueTokensCaseInsensitive.size();
        /** Sets Maximum list size to 10 **/
        if(sizeCaseSensitive > 10)
            sizeCaseSensitive = 10;
        if(sizeCaseInsensitive > 10)
            sizeCaseInsensitive = 10;
        /** Generates list **/
        for(int i = 0; i < sizeCaseSensitive; i++){
            findMostOccurringTokenInFile(uniquesCopy1, countsCopy1,
                    this.mostOccurring, this.mostOccurringCounts);
            findLeastOccuringToken(uniquesCopy2, countsCopy2,
                    this.leastOccurring, this.leastOccurringCounts);
        }
        for(int i = 0; i < sizeCaseInsensitive; i++){
            findMostOccurringTokenInFile(uniquesCopy3, countsCopy3,
                    this.mostOccurringNoCase, this.mostOccurringCountsNoCase);
            findLeastOccuringToken(uniquesCopy4, countsCopy4,
                    this.leastOccurringNoCase, this.leastOccurringCountsNoCase);
        }
    }

    private List<String> formatOutput(){
        /** Adds lines to write to file into a list of Strings**/
        ArrayList<String> formattedOutput = new ArrayList<>();
        formattedOutput.add(String.format("%20s: %d", "Longest Line in File",
                this.lengthOfLongestLine));
        formattedOutput.add(String.format("%19s: %d", "Average line length",
                this.lineLengthAvg));
        formattedOutput.add(String.format("%19s: %d", "Number of unique" +
                " tokens", this.uniqueTokens.size()));
        formattedOutput.add(String.format("%12s: %d", "Total tokens",
                this.tokens.size()));
        formattedOutput.add(String.format("\n%s","Top 10 most occurring " +
                "tokens (case-sensitive)"));
        for(int i = 0; i < this.mostOccurring.size(); i++){
            String entry = String.format("%d.\t%d\t%s", i +1,
                    this.mostOccurringCounts.get(i),
                    this.mostOccurring.get(i));
            formattedOutput.add(entry);
        }
        formattedOutput.add(String.format("\n%s", "Top 10 least occurring " +
                "tokens (case-sensitive)"));
        for(int i = 0; i < this.leastOccurring.size(); i++) {
            String entry = String.format("%d.\t%d\t%s", i + 1,
                    this.leastOccurringCounts.get(i),
                    this.leastOccurring.get(i));
            formattedOutput.add(entry);
        }
        formattedOutput.add(String.format("\n%s","Top 10 most occurring " +
                "tokens (case-insensitive)"));
        for(int i = 0; i < this.mostOccurringNoCase.size(); i++){
            String entry = String.format("%d.\t%d\t%s", i+1,
                    this.mostOccurringCountsNoCase.get(i),
                    this.mostOccurringNoCase.get(i));
            formattedOutput.add(entry);
        }
        formattedOutput.add(String.format("\n%s", "Top 10 least occurring " +
                "tokens (case-insensitive)"));
        for(int i = 0; i < this.leastOccurringNoCase.size(); i++){
            String entry = String.format("%d.\t%d\t%s", i+1,
                    this.leastOccurringCountsNoCase.get(i),
                    this.leastOccurringNoCase.get(i));
            formattedOutput.add(entry);
        }
        return formattedOutput;
    }

    private void writeToFile(List<String> toWrite) {
        try {
            Files.write(this.outputPath, toWrite, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTarget(String path){
        this.currentPath = Paths.get(path);
    }

    public void setOutputPath(String path){
        String finalPath = path + ".stats";
        this.outputPath = Paths.get(finalPath);
    }

    public void parseCurrentTarget(){
        readFile();
        tokenizeLinesInFile();
        parseTextCaseInsensitive();
        parseTextCaseSensitive();
        findTopTenMostAndLeastOccuring();
        writeToFile(formatOutput());
    }

}
