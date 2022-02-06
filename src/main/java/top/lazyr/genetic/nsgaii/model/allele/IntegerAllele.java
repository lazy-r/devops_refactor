package top.lazyr.genetic.nsgaii.model.allele;

public class IntegerAllele extends AbstractAllele {

	public IntegerAllele(int gene) {
		super(gene);
	}

	@Override
	public Integer getGene() {
		return (int) this.gene;
	}

	@Override
	public AbstractAllele getCopy() {
		return new IntegerAllele((int) this.gene);
	}

	@Override
	public String toString() {
		return String.valueOf((int) this.gene);
	}
}
