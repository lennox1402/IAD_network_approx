package mlp;

import java.util.ArrayList;

public class Layer {

    private ArrayList<Neuron> neurons = new ArrayList<>();
    private Layer nextLayer;
    private boolean bias;


    public Layer(int numberOfNeurons, int sizeOfInput)
    {
        for(int i = 0; i < numberOfNeurons; i++)
        {
            neurons.add(0, new Neuron(sizeOfInput));
        }
    }

    public void setRateAndMomentum(double learningRate, double momentum)
    {
        for(Neuron neuron : neurons)
        {
            neuron.setLearningRate(learningRate);
            neuron.setMomentum(momentum);
        }
    }

    public void setWeights()
    {
        for(Neuron neuron : neurons)
        {
            neuron.setRandomWeights();
        }
    }

    public void setInputs(double[] inputs)
    {
        for (Neuron neuron : neurons)
        {
            neuron.setInputs(inputs);
        }
    }

    public void setBias(boolean isBias)
    {
        bias = isBias;
        for(Neuron neuron: neurons)
        {
            neuron.setBias(isBias);
        }
    }

    public void isLastLayer(boolean bool)
    {
        for(Neuron neuron: neurons)
        {
            neuron.setLast(bool);
        }
    }

    public ArrayList<Neuron> getNeurons()
    {
        return neurons;
    }

    public double[] getOutputs()
    {
        double[] out = new double[neurons.size()];
        for(int i = 0; i < neurons.size(); i++)
        {
            out[i] = neurons.get(i).sum();
        }
        return out;
    }

    public void setNextLayer(Layer layer)
    {
        this.nextLayer = layer;
    }

    public void trainLayer()
    {
        // backpropagation
        for(int i = 0; i < neurons.size(); i++)
        {
            double delta = 0.0;
            for(int j = 0; j < nextLayer.getNeurons().size(); j++)
            {
                if(bias == true)
                {
                    delta += nextLayer.getNeurons().get(j).getGradient() * nextLayer.getNeurons().get(j).getWeights()[i + 1];
                }
                else
                {
                    delta += nextLayer.getNeurons().get(j).getGradient() * nextLayer.getNeurons().get(j).getWeights()[i];
                }
                    neurons.get(i).setGradient(delta * neurons.get(i).sigmoidDerivative(neurons.get(i).getOutput()));
            }
        }
    }

    public void updateWeights()
    {
        for(Neuron neuron : neurons)
        {
            neuron.updateWeights();
        }
    }
}
