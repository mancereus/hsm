package de.dkfz.phenopermutation;

public class Phenotype {

	public final int id;
	public String extid;
	public final double value;

	public Phenotype(int id, double value) {
		this.id = id;
		this.value = value;
	}

	public int getId() {
		return id;
	}

	public double getValue() {
		return value;
	}
}
