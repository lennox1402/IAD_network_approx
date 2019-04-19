package mlp;

import java.util.ArrayList;

public class Network {

    private ArrayList<Layer> layers = new ArrayList<>();
    private double[] expected;

    public Network() {}

    public void addLayer(Layer layer)
    {
        layers.add(layer);
    }

    public void setExpected(double[] expected)
    {
        this.expected = expected;
    }

    public void setWeights()
    {
        for(Layer layer : layers)
        {
            layer.setWeights();
        }
    }

    public void setInputs(double[] in)
    {
        layers.get(0).setInputs(in);
    }

    public double[] getOutput()
    {
        double[] outLayer;

        for(int i = 0; i < layers.size() - 1; i++)
        {
            outLayer = layers.get(i).getOutputs();
            for(int j = 0; j < layers.get(i + 1).getNeurons().size(); j++)
            {
                layers.get(i + 1).getNeurons().get(j).setInputs(outLayer);
            }
        }
        return layers.get(layers.size() - 1).getOutputs();
    }

    public void trainNetwork()
    {
        double outLayer;
        for(int i = 0; i < layers.get(layers.size() - 1).getNeurons().size(); i++)
        {
            outLayer = layers.get(layers.size() - 1).getNeurons().get(i).getOutput();

                layers.get(layers.size() - 1).getNeurons().get(i).setGradient((layers.get(layers.size() - 1).getNeurons().get(i).function(outLayer) - expected[i]) * layers.get(layers.size() - 1).getNeurons().get(i).linearDerivative(outLayer));

        }
        for(int i = layers.size() - 2; i >= 0; i--)
        {
            layers.get(i).trainLayer();
        }
        for(Layer layer : layers)
        {
            layer.updateWeights();
        }
    }
}
