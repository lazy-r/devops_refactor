package top.lazyr.genetic.nsgaii.model.allele;

public class ValueAllele extends AbstractAllele {

	public ValueAllele(double gene) {
		super(gene);
	}

	@Override
	public Double getGene() {
		return (double) this.gene;
	}

	@Override
	public AbstractAllele getCopy() {
		return new ValueAllele((double) this.gene);
	}

	@Override
	public String toString() {
		return String.valueOf((double) this.gene);
	}
}
