package model;

import algorithm.ba.crossover.MultiCrossoverType;
import algorithm.ga.crossover.CrossoverType;
import algorithm.ga.selection.SelectionType;

/**
 * Application & algorithm parameters.
 * 
 * @author acco
 * 
 *         Jun 26, 2016 10:22:09 AM
 *
 */
public class AppSettings {

	public static AppSettings settings;

	static {
		settings = new AppSettings();
	}

	public int threads;
	public int runs;
	public int gaIterations;
	public int gaPopulation;
	public double gaCrossoverProbability;
	public boolean gaElitism;
	public CrossoverType gaCrossoverMethod;
	public double gaMutationProbability;
	public int antsIterations;
	public int antsPopulation;
	public double antsAlpha;
	public double antsBeta;
	public double antsRho;
	public SelectionType gaSelectionMethod;
	public int baIterations;
	public int baPopulation;
	public double baMutationProbability;
	public MultiCrossoverType baCrossoverMethod;
	public int baMaxInclusionFrequency;
	public double baSimilarityConstant;
	public boolean debug;
	public boolean antsMinMax;

	private AppSettings() {
		/*
		 * General
		 */
		this.threads = Runtime.getRuntime().availableProcessors() + 1;
		this.runs = 1;
		this.debug = true;
		/*
		 * GA
		 */
		this.gaIterations = 5000;
		this.gaPopulation = 200;
		this.gaCrossoverProbability = 0.9;
		this.gaElitism = true;
		this.gaSelectionMethod = SelectionType.MONTECARLO;
		this.gaCrossoverMethod = CrossoverType.DOUBLE_POINT;
		this.gaMutationProbability = 0.1;
		/*
		 * ANTS
		 */
		this.antsIterations = 5000;
		this.antsPopulation = 200;
		this.antsAlpha = 0.6;
		this.antsBeta = 0.4;
		this.antsRho = 0.4;
		this.antsMinMax = true;

		/*
		 * Bionomic
		 */
		this.baIterations = 5000;
		this.baPopulation = 200;
		this.baMaxInclusionFrequency = 5;
		this.baSimilarityConstant = 0.7;
		this.baMutationProbability = 0.1;
		this.baCrossoverMethod = MultiCrossoverType.STANDARD;
	}

	public static void resetDefault() {
		settings = new AppSettings();
	}

	public static AppSettings get() {
		return settings;
	}

}
