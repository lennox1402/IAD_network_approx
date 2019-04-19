package mlp;

import java.util.Random;

public class Neuron {

    private double learningRate;
    private double momentum;

    private double[] weights;
    private double[] inputs;
    private double gradient;
    private boolean bias;
    private double output;
    private boolean isLast = false;


    public Neuron(int size)
    {
        weights = new double[size];
        inputs = new double[size];
    }

    public static double randomNumbers(int min, int max)
    {
        Random generator = new Random();
        double f = generator.nextDouble() * (max - min) + min;
        return f;
    }

    public void setRandomWeights()
    {
        for(int i = 0; i < weights.length; i++)
        {
            weights[i] = randomNumbers(0, 1);
        }
    }

    public void setLearningRate(double learningRate)
    {
        this.learningRate = learningRate;
    }

    public void setMomentum(double momentum)
    {
        this.momentum = momentum;
    }

    public double[] getWeights()
    {
        return weights;
    }

    public void setBias(boolean isBias)
    {
        if(isBias == true)
        {
            weights = inputs = new double[weights.length + 1];
            inputs[0] = 1.0;
            bias = isBias;
        }
    }

    public double linearDerivative(double x)
    {
        double w = 1.0;
        return w;
    }

    public void setLast(boolean bool)
    {
        this.isLast = bool;
    }

    public double function(double x)
    {
        if(isLast == false)
        {
            return (1.0 / (1.0 + Math.exp(-x)));
        }
        else return x;
    }

    public double sigmoidDerivative(double x)
    {
        return (function(x) * (1.0 - function(x)));
    }

    public double getOutput()
    {
        return output;
    }

    public void setInputs(double[] inputs)
    {
        if(bias == true)
        {
            this.inputs = new double[inputs.length + 1];
            this.inputs[0] = 1.0;
            System.arraycopy(inputs, 0, this.inputs, 1, inputs.length);
        }
        else
        {
            this.inputs = inputs.clone();
        }
    }

    public double sum()
    {
        double result = 0.0;

        for (int i = 0; i < weights.length; i++)
        {
            result += weights[i] * inputs[i];
        }
        this.output = result;
        return function(output);
    }

    public void setGradient(double gradient)
    {
        this.gradient = gradient;
    }

    public double getGradient()
    {
        return gradient;
    }

    public void updateWeights()
    {
        double[] weightsOld = weights.clone();
        for(int i = 0; i < weights.length; i++)
        {
            weights[i] -= gradient * learningRate * inputs[i];
            weights[i] -= momentum * (weights[i] - weightsOld[i]);
        }
    }
}
