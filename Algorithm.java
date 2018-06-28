// Created by Ross Taylor, rt345

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Random;
import java.text.DecimalFormat;

public class Algorithm
{
    // global variables
    int numberOfRowsT = 1500;
    int numberOfColumnsT = 9;
    int numberOfRowsR = 27;
    int numberOfColumnsR = 8;
    int numberOfTestRows = 30;
    DecimalFormat decimal = new DecimalFormat("#.###");
    double[][] training = new double[numberOfRowsT - numberOfTestRows][numberOfColumnsT];
    double[][] trainingOriginal = new double[numberOfRowsT - numberOfTestRows][numberOfColumnsT];
    double[][] testData = new double[numberOfTestRows][numberOfColumnsT];
    double[][] testDataResult = new double[numberOfTestRows][4];
    double[][] realData = new double[numberOfRowsR][numberOfColumnsR];
    double[][] results = new double [numberOfRowsR][1];
    double[] factor = new double[numberOfRowsT - numberOfTestRows];
    int[] trainingModification = new int [8];
    Random random = new Random();
    double trainingMax;
    double trainingMin;
    double trainingAverage;
    double trainingStandardDeviation;
    double fitness = 100;
    double fitnessNew;
    double accuracy;
    double percentageAverage;
    double percentageMaximum = -100;
    double percentageMinimum = 100;
    double percentageStandardDeviation;

    /**
     * main method
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception{
    	System.out.println("Coursework by Ross Taylor, rt345");
        Algorithm program = new Algorithm();
        program.importData();
        program.trainAlgorithm();
        program.calculateTestResults();
        program.importRealData();
        program.predictResults();
        program.printOutput();
    }

    /**
     * Method which imports training data into an array which can be accessed by the algorithm
     */
    public void importData() throws Exception{
        String name = "assignement528_1.txt";
        BufferedReader file = new BufferedReader(new FileReader(new File(name)));
        String line = file.readLine();
        double[][] trainingTemp = new double[numberOfRowsT][numberOfColumnsT];
        int[] randomRows = new int[numberOfTestRows];

        line = file.readLine();
        boolean notReplaced = true;
        boolean duplicate = false;
        int a = 0; //x axis on array
        int b = 0; //y axis on array

        int i = 0;
        int j = 0;

        int c = 0;
        while(line != null){
            String[] array = line.split("\t");
            if(!line.trim().isEmpty()){
                for(String s : array){
                    if(!s.trim().isEmpty()){
                        double d = Double.parseDouble(s);
                        trainingTemp[a][b] = d;
                        b++;
                    }
                }
                a++;
                b = 0;
            }
            line = file.readLine();
        }
        a = 0;
        for(a = 0; a <8; a++){
            trainingModification[a] = 100;
        }
        a = 0;
        for(c = 0; c < numberOfTestRows; c++){
            int rand = random.nextInt(1500);
            for(a = 0; a < numberOfTestRows; a++){
                if(randomRows[a] == (rand)){
                    c--;
                    duplicate = true;
                }
            }
            if(duplicate == false){
                randomRows[c] = rand;
            }
            duplicate = false;
        }
        a = 0;
        b = 0;
        c = 0;
        // places training data without gaps into new array.
        for(a = 0; a < numberOfRowsT; a++){
            for(c = 0; c < numberOfTestRows; c++){
                if(randomRows[c] == a){
                    for(b = 0; b < numberOfColumnsT; b++){
                        testData[i][b] = trainingTemp[a][b];
                    }
                    b = 0;
                    i++;
                    notReplaced = false;
                }
            }
            if(notReplaced == true){
                for(b = 0; b < numberOfColumnsT; b++){
                    training[j][b] = trainingTemp[a][b];
                }
                j++;
                b = 0;
            }
            notReplaced = true;
            c = 0;
        }
        a = 0;
        b = 0;

        a = 0;
        b = 0;
        for(a = 0; a < numberOfRowsT - numberOfTestRows; a++){
            for(b = 0; b < numberOfColumnsT; b++){
                trainingOriginal[a][b] = training[a][b];
            }
            b = 0;
        }
    }

    /**
     *  This is the method that trains the algorithm. This will produce an average result for all these values, which
     *  is the overall fitness of the dataset. These results will allow the program to calculate an average result for
     *  a new data set, along with an accuracy percentage.
     */
    public void trainAlgorithm(){
        // machine learning step.
        int a = 0;
        for(int c = 0; c < 100000 ; c++){
            int ran = random.nextInt(8);
            int ran2 = random.nextInt(5)-2;
            for(a = 0; a < numberOfRowsT - numberOfTestRows; a++){
                training[a][ran] = trainingOriginal[a][ran] * (trainingModification[ran] + ran2);
            }
            fitnessFunction();
            if(fitnessNew < fitness){
                fitness = fitnessNew;
                trainingModification[ran] = trainingModification[ran] + ran2;
            }
            else{
                for(a = 0; a < numberOfRowsT - numberOfTestRows; a++){
                    training[a][ran] = trainingOriginal[a][ran] * trainingModification[ran];
                }
            }
        }
        trainingMax = 0;
        trainingMin = 10000000;
        for(int x = 0; x < numberOfRowsT - numberOfTestRows; x++){
            if(trainingMax < factor[x]){
                trainingMax = factor[x];
            }
        }
        for(int y = 0; y < numberOfRowsT - numberOfTestRows; y++){
            if(trainingMin > factor[y]){
                trainingMin = factor[y];
            }
        }
    }

    /**
     * Calculates the current fitness of the training array. Also calculates the maximum number, minimum number,
     * average and standard deviation of the training array.
     */
    private void fitnessFunction(){
        double[] totalZeroToSeven = new double[numberOfRowsT - numberOfTestRows];
        double[] variance = new double[numberOfRowsT - numberOfTestRows];
        double varianceTotal = 0;
        trainingAverage = 0;
        int a = 0;
        // loop fills totalZeroToSeven table.
        for(a = 0; a < numberOfRowsT - numberOfTestRows; a++){
            totalZeroToSeven[a] = training[a][0] + training[a][1] + training[a][2] + training[a][3] + training[a][4] + training[a][5] + training[a][6] + training[a][7];
        }
        a = 0;
        // a factor is generated for each row by dividing the result of each column with the 1-8 columns added together.
        for(a = 0; a < numberOfRowsT - numberOfTestRows; a++){
            factor[a] = training[a][8] / totalZeroToSeven[a];
        }
        a = 0;
        // store the average factor value.
        for(a = 0; a <numberOfRowsT - numberOfTestRows; a++){
            trainingAverage += factor[a];
        }
        trainingAverage = trainingAverage / (numberOfRowsT - numberOfTestRows);
        // calculate the standard deviation.
        a = 0;
        for(a = 0; a <numberOfRowsT - numberOfTestRows; a++){
            double c = factor[a];
            variance[a] =Math.pow(c - trainingAverage, 2);
        }
        a = 0;
        for(a = 0; a <numberOfRowsT - numberOfTestRows; a++){
            varianceTotal = varianceTotal + variance[a];
        }
        varianceTotal = varianceTotal / (numberOfRowsT -1);
        trainingStandardDeviation = Math.sqrt(varianceTotal);

        // calculates the fitness of the training data.
        fitnessNew = trainingStandardDeviation / trainingAverage;
    }

    /**
     * Method which imports real data into an array which can be accessed by the algorithm
     */
    public void importRealData() throws Exception{
        String name = ("assignement528_2.txt");
        BufferedReader file = new BufferedReader(new FileReader(new File(name)));
        String line = file.readLine();
        line = file.readLine();
        int a = 0; //x axis on array
        int b = 0; //y axis on array
        while(line != null){
            String[] array = line.split("\t");
            if(!line.trim().isEmpty()){
                for(String s : array){
                    if(!s.trim().isEmpty()){
                        double d = Double.parseDouble(s);
                        realData[a][b] = d;
                        b++;
                    }
                }
                a++;
                b = 0;
            }
            line = file.readLine();
        }
    }

    /**
     * A method which takes 30 results from the training set, and then tests the algorithm to see what value is given.
     * The first value is the generated result. The second value is the original result. The third result is the accuracy predicted.
     */
    public void calculateTestResults(){
        double[] totalT = new double[numberOfTestRows];
        double[] percentage = new double[numberOfTestRows];
        double[] variance = new double[numberOfTestRows];
        double varianceTotal = 0;
        //calculates output of algorithm.
        for(int a = 0; a < numberOfTestRows; a++){
            totalT[a] = (testData[a][0] * trainingModification[0]) + (testData[a][1]  * trainingModification[1]) + (testData[a][2]  * trainingModification[2]) + (testData[a][3]  * trainingModification[3]) + (testData[a][4]  * trainingModification[4]) + (testData[a][5]  * trainingModification[5]) + (testData[a][6]  * trainingModification[6]) + (testData[a][7]  * trainingModification[7]);
            testDataResult[a][0] = totalT[a] * trainingAverage;
        }

        for(int a = 0; a < numberOfTestRows; a++){
            testDataResult[a][1] = testData[a][8];
        }
        
        // calculating percentage accuracy of results.
        
        for(int a = 0; a < numberOfTestRows; a++){
            percentage[a] = testDataResult[a][1] - testDataResult[a][0];
            testDataResult[a][2] = percentage[a]; 
            percentage[a] = (percentage[a] / testDataResult[a][0]) * 100;
            testDataResult[a][3] = percentage[a];
        }
        
        for(int a = 0; a < numberOfTestRows; a++){
            percentageAverage += percentage[a];
            if(percentageMaximum < percentage[a]){
                percentageMaximum = percentage[a];
            }
            if(percentageMinimum > percentage[a]){
                percentageMinimum = percentage[a];
            }
        }
        percentageAverage = percentageAverage / numberOfTestRows;
        // standard deviation percentage
        for(int a = 0; a <numberOfTestRows; a++){
            variance[a] =Math.pow(percentageAverage, 2);
        }
        
        for(int a = 0; a <numberOfTestRows; a++){
            varianceTotal = varianceTotal + variance[a];
        }
        varianceTotal = varianceTotal / (numberOfRowsT -1);
        percentageStandardDeviation = Math.sqrt(varianceTotal);
    }
    
    

    /**
     * Method which predicts the value of each row of data. Results will be placed into an array.
     * Accuracy of these results will be placed into the 2nd column of this array.
     */
    public void predictResults(){
        double[] totalR = new double[numberOfRowsR];
        // result prediction
        for(int a = 0; a <numberOfRowsR; a++){
            totalR[a] = (realData[a][0] * trainingModification[0]) + (realData[a][1]  * trainingModification[1]) + (realData[a][2]  * trainingModification[2]) + (realData[a][3]  * trainingModification[3]) + (realData[a][4]  * trainingModification[4]) + (realData[a][5]  * trainingModification[5]) + (realData[a][6]  * trainingModification[6]) + (realData[a][7]  * trainingModification[7]);
        }
        int a = 0;
        for(a = 0; a < numberOfRowsR; a++){
            results[a][0] = totalR[a] * trainingAverage;
        }
    }

    /**
     * method to print out the final output.
     */
    public void printOutput(){
        int a = 0;
        String output = "";
        System.out.println("## Training Results: ##");
        System.out.println("");
        System.out.println("Optimal multiplications to subject original data columns to achieve smallest fitness function:");
        output = "";
        for(int modification : trainingModification){
            output += modification + "\t";
        }
        System.out.println("");
        System.out.println("A\tB\tC\tD\tE\tF\tG\tH");
        System.out.println(output);
        System.out.println("");
        System.out.println("Optimal Fitness Value = " + fitness);
        System.out.println("Optimal Average Factor = " + decimal.format(trainingAverage));
        System.out.println("Optimal Largest Factor = " + decimal.format(trainingMax));
        System.out.println("Optimal Smallest Factor = " + decimal.format(trainingMin));
        System.out.println("Optimal Standard Deviation = " + decimal.format(trainingStandardDeviation));
        System.out.println("");
        System.out.println("## Test Data ##");
        System.out.println("");
        System.out.println("Prediction\tOriginal\tDifference\tPercentage Difference(%)");  
        for(a = 0; a < numberOfRowsR; a++){
            output = "";
            for(double test : testDataResult[a]){
                output += decimal.format(test) + "\t" ;
            }
            System.out.println(output);
        }
        System.out.println("");
        System.out.println("Average Percentage difference = " + decimal.format(percentageAverage));
        System.out.println("Maximum Percentage difference = "+ decimal.format(percentageMaximum));
        System.out.println("Minimum Percentage difference = " + decimal.format(percentageMinimum));
        System.out.println("Standard Deviation percentage = " + decimal.format(percentageStandardDeviation));
        System.out.println("");
        System.out.println("## Real Data Output ##");
        System.out.println("");
        System.out.println("A\tB\tC\tD\tE\tF\tG\tH\tResult");
        for(a = 0; a < numberOfRowsR; a++){
            output = "";
            for(double rd : realData[a]){
                output += decimal.format(rd) + "\t" ;
            }
            for(double result : results[a]){
                output +=  decimal.format(result) + "\t" ;
            }
            System.out.println(output);
        }
    }
}