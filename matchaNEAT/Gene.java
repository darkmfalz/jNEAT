package matchaNEAT;

public class Gene {

	protected int geneNum, from, to;
	protected boolean isLink;
	
	public Gene(int geneNum, int from, int to, boolean isLink){
		
		this.geneNum = geneNum;
		this.from = from;
		this.to = to;
		this.isLink = isLink;
		
	}
	
	//Getters
	public int getGeneNum(){
		
		return geneNum;
		
	}
	
	public int getFrom(){
		
		return from;
		
	}
	
	public int getTo(){
		
		return to;
		
	}
	
	public boolean getIsLink(){
		
		return isLink;
		
	}
	
	//Setters
	public void setGeneNum(int geneNum){
		
		this.geneNum = geneNum;
		
	}
	
}
