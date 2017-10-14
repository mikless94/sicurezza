package FrequencyAnalysis;

public class Digram implements Comparable<Digram> {
	
	private String firstCharacter;
	private String secondCharacter;
	private int frequency;
	
	
	public Digram(String firstCharacter, String secondaCharacter, int frequency) {
		this.firstCharacter = firstCharacter;
		this.secondCharacter = secondaCharacter;
		this.frequency = frequency;
	}
	public String getFirstCharacter() {
		return firstCharacter;
	}
	public void setFirstCharacter(String firstCharacter) {
		this.firstCharacter = firstCharacter;
	}
	public String getSecondCharacter() {
		return secondCharacter;
	}
	public void setSecondCharacter(String secondaCharacter) {
		this.secondCharacter = secondaCharacter;
	}
	public int getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	
	public String printDigram() {
		String s = "";
		s += this.getFirstCharacter() + '\t' + this.getSecondCharacter() + '\t' 
				+ Integer.toString(this.getFrequency());
		return s;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + frequency;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Digram other = (Digram) obj;
		if (frequency != other.frequency)
			return false;
		return true;
	}
	@Override
	public int compareTo(Digram o) {
		int freq = this.frequency - o.frequency;
	if(freq > 0) 
			return 1;
	else if (freq < 0) 
		return -1;
	else
		return 0;
	}
	
}
