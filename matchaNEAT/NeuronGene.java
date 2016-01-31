package matchaNEAT;

public class NeuronGene extends Gene {

	public static enum NeuronType {
		
		INPUT, HIDDEN, BIAS, OUTPUT, NONE;
		
	}
	
	private int id;
	private NeuronType type; //This is the type of neuron
	private double tier, p; //p in 1/(1 + e^(- a / p)), the logistic sigmoid activation function
	private boolean recurrent; //Is this a recurrent neuron
	
	public NeuronGene(int geneNum, int from, int to, int id, NeuronType type, double tier, double p, boolean recurrent) {
		
		super(geneNum, from, to, false);
		this.id = id;
		this.type = type;
		this.tier = tier;
		this.p = p;
		this.recurrent = recurrent;
		
	}

	//Getters
	public int getID(){
		
		return id;
		
	}
	
	public NeuronType getType(){
		
		return type;
		
	}
	
	public double getTier(){
		
		return tier;
		
	}
	
	public double getP(){
		
		return p;
		
	}
	
	public boolean getRecurrent(){
		
		return recurrent;
		
	}

	//Setters
	public void setP(double p){
		
		this.p = p;
		
	}
	
	public void setRecurrent(boolean recurrent){
		
		this.recurrent = recurrent;
		
	}
	
	//Cloners
	public NeuronGene clone(){
		
		return new NeuronGene(geneNum, from, to, id, type, tier, p, recurrent);
		
	}
	
}
