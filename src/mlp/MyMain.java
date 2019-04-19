package mlp;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

public class MyMain {

    public static void main(String[] args)
    {
        String txt1;
        txt1 = JOptionPane.showInputDialog("Enter learning rate");
        double learningRate = Double.parseDouble(txt1);

        String txt2;
        txt2 = JOptionPane.showInputDialog("Enter momentum");
        double momentum = Double.parseDouble(txt2);

        String txt3;
        txt3 = JOptionPane.showInputDialog("Enter number of hidden neurons from 1 up to 20");
        int nrOfNeuronsInHidden = Integer.parseInt(txt3);

        System.setProperty("true", "true");
        System.setProperty("false", "false");
        String txt4;
        txt4 = JOptionPane.showInputDialog("Do you want bias? (true/false)");
        boolean bias = Boolean.getBoolean(txt4);
        //System.out.println(bias);

        double[][] tab = readFile("approximation_train_1.txt");

        Layer hidden = new Layer(nrOfNeuronsInHidden, 1);
        hidden.setBias(bias);
        hidden.setRateAndMomentum(learningRate, momentum);
        hidden.isLastLayer(false);

        Layer output = new Layer(1, nrOfNeuronsInHidden);
        output.setBias(bias);
        output.setRateAndMomentum(learningRate, momentum);
        output.isLastLayer(true);
        hidden.setNextLayer(output);

        Network network = new Network();
        network.addLayer(hidden);
        network.addLayer(output);
        network.setWeights();

        int numberOfEpochs = 2000;

        ArrayList<Double> meanSqrtError = new ArrayList<>();
        ArrayList<Integer> randoms = new ArrayList<>();
        for(int t = 0; t < tab.length; t++)
        {
            randoms.add(t);
        }
        double[] inputs = new double[1];
        double[] expected = new double[1];
        double[] outputFromNet;
        double error;
        for(int i = 0; /*error > 0.01*/ i < numberOfEpochs; i++)
        {
            Collections.shuffle(randoms);
            error = 0.0;
            for(int j = 0; j < tab.length; j++)
            {
                inputs[0] = tab[randoms.get(j)][0]; //tak poniewaz jest jedno wejscie i wyjscie
                network.setInputs(inputs);
                expected[0] = tab[randoms.get(j)][1];
                network.setExpected(expected);

                network.getOutput();
                network.trainNetwork();
                error += error(expected, network.getOutput());
            }
            error /= tab.length;
            meanSqrtError.add(i, error);
            System.out.println("Error: " + error);
            //data[i][0] = i;
            //data[i][1] = meanSqrtError.get(i);
        }

        //double[][] trainData = readFile("approximation_train_1.txt");
        // -- to jednak jest bez sensu, bo w tab[][] jest to samo
        double[][] appData = new double[tab.length][2];

        for(int i = 0; i < appData.length; i++)
        {
            double[] outputFromHidden;
            double[] input = new double[1];
            input[0] = tab[i][0];
            hidden.setInputs(input);
            outputFromHidden = hidden.getOutputs();
            output.setInputs(outputFromHidden);
            appData[i][0] = input[0];
            appData[i][1] = network.getOutput()[0];
        }

        double[][] testData = readFile("approximation_test.txt");
        double[][] appTest = new double[testData.length][2];

        for(int i = 0; i < testData.length; i++)
        {
            double[] outputFromHidden;
            double[] in = new double[1];
            in[0] = testData[i][0];
            hidden.setInputs(in);
            outputFromHidden = hidden.getOutputs();
            output.setInputs(outputFromHidden);
            appTest[i][0] = in[0];
            appTest[i][1] = network.getOutput()[0];
        }

        saveErrorToFile(meanSqrtError, "meanSquaredError.txt");

        double squaredError = 0.0;
        for(int i = 0; i < testData.length; i++)
        {
            double[] input = new double[1];
            input[0] = testData[i][0];
            network.setInputs(input);
            double[] expectedOut = new double[1];
            expectedOut[0] = testData[i][1];
            network.setExpected(expectedOut);
            double[] out = network.getOutput();
            network.trainNetwork();
            squaredError += Math.pow(out[0] - network.getOutput()[0], 2);

        }
        double data[][] = new double[meanSqrtError.size()][2];
        for(int w = 0; w < meanSqrtError.size(); w++) {

            data[w][1] = meanSqrtError.get(w);
            data[w][0] = w;
        }

        //System.out.println("Błąd: " + squaredError / 2);

        //ploting
        Plot1 demo1 = new Plot1("Error", data);
        demo1.setSize(800, 800);
        demo1.setLocationRelativeTo(null);   //ustawia w centrum ekranu
        demo1.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //demo.setBackground(new Color(255,228,196));
        demo1.setBackground(Color.WHITE);
        demo1.setVisible(true);

        Plot2 demo2 = new Plot2("Training data", tab, appData);
        demo2.setSize(800, 800);
        demo2.setLocationRelativeTo(null);   //ustawia w centrum ekranu
        demo2.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //demo.setBackground(new Color(255,228,196));
        demo2.setBackground(Color.WHITE);
        demo2.setVisible(true);

        Plot2 demo3 = new Plot2("Test data", testData, appTest);
        demo3.setSize(800, 800);
        demo3.setLocationRelativeTo(null);   //ustawia w centrum ekranu
        demo3.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //demo.setBackground(new Color(255,228,196));
        demo3.setBackground(Color.WHITE);
        demo3.setVisible(true);

        /*
        Plot1 demo1 = new Plot1("JFreeChart: Plot1.java", data;
        demo.setSize(800, 800);
        demo.setLocationRelativeTo(null);   //ustawia w centrum ekranu
        demo.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //demo.setBackground(new Color(255,228,196));
        demo.setBackground(Color.WHITE);
        demo.setVisible(true);*/
    }


    public static double error(double[] expected, double[] guess)
    {
        if(expected.length != guess.length)
        {
            return 0;
        }
        double error = 0.0;
        double difference;
        for(int i = 0; i < expected.length; i++)
        {
            difference = expected[i] - guess[i];
            error += difference * difference;
        }
        //error /= expected.length;
        return error;
    }

    public static double[][] readFile(String fileName)
    {
        Path filePath = Paths.get(fileName);
        ArrayList<String> read = new ArrayList<>();
        try {
            read = (ArrayList) Files.readAllLines(filePath);
        } catch (IOException ex) {
            System.out.println("No such a file!");
        }
        double[][] readTab = new double[read.size()][];
        int lineNr = 0;
        for(String line : read) {
            String[] lineDataString = line.split(" ");
            double[] lineDouble = new double[lineDataString.length];
            for (int i = 0; i < lineDouble.length; i++)
            {
                lineDouble[i] = Double.parseDouble(lineDataString[i]);
            }
            readTab[lineNr] = lineDouble;
            lineNr++;
        }
        return readTab;
    }

    static public void saveErrorToFile(ArrayList<Double> tab, String fileName)
    {
        try
        {
            FileWriter file = new FileWriter(fileName);
            BufferedWriter buffWrite = new BufferedWriter(file);

            for(int i = 0; i < tab.size(); i++)
            {

                buffWrite.write(Double.toString(tab.get(i)));
                buffWrite.newLine();
            }
            buffWrite.close();
        }

        catch (IOException io)
        {System.out.println(io.getMessage());}

        catch (Exception se)
        {System.err.println("Error!");}
    }
}
