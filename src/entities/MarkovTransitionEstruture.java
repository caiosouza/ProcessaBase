package entities;

import java.util.Map;
import java.util.Map.Entry;

public class MarkovTransitionEstruture {

	private int[][] markovTransitionMatrix;
	private int[][] sparseMarkovTransitionMatrix;
	
	/**
	 * @param markovTransitionMatrix
	 */
	public MarkovTransitionEstruture(BaseEstruturada baseEstruturada) {
		
		Map<String, Integer> biGramasFrequencia = baseEstruturada.getBiGramasFrequenciasGlobal();
		Map<String, Integer> dicionarioBiGramas = baseEstruturada.getDicionarioBiGrama();
		
		loadMarkovTransitionMatrix(biGramasFrequencia, dicionarioBiGramas);
		loadSparseMarkovTransitionMatrix(biGramasFrequencia, dicionarioBiGramas);
	}

	/**
	 * @return the markovTransitionMatrix
	 */
	public int[][] getMarkovTransitionMatrix() {
		return markovTransitionMatrix;
	}

	/**
	 * @param markovTransitionMatrix the markovTransitionMatrix to set
	 */
	public void setMarkovTransitionMatrix(int[][] markovTransitionMatrix) {
		this.markovTransitionMatrix = markovTransitionMatrix;
	}

	/**
	 * @return the sparseMarkovTransitionMatrix
	 */
	public int[][] getSparseMarkovTransitionMatrix() {
		return sparseMarkovTransitionMatrix;
	}

	/**
	 * @param sparseMarkovTransitionMatrix the sparseMarkovTransitionMatrix to set
	 */
	public void setSparseMarkovTransitionMatrix(int[][] sparseMarkovTransitionMatrix) {
		this.sparseMarkovTransitionMatrix = sparseMarkovTransitionMatrix;
	}

	public void putMarkovTransitionMatrix(Integer termoI, Integer termoJ, Integer frequencia){
		this.markovTransitionMatrix[termoI][termoJ] = frequencia;
	}
	
	private void addMarkovTransitionMatrix(Integer termoI, Integer termoJ, Integer frequencia){
		int freqAtual = this.markovTransitionMatrix[termoI][termoJ];
		this.markovTransitionMatrix[termoI][termoJ] = frequencia+freqAtual;
	}
	
	private void loadMarkovTransitionMatrix(Map<String, Integer> biGramasFrequencia, Map<String, Integer> dicionarioBiGramas){
		
		this.markovTransitionMatrix = new int[dicionarioBiGramas.size()][dicionarioBiGramas.size()];
		
		//processa os bigrama frequencia e montar a tabela
		String[] termos;
		Integer frequencia;
		int idPrimeiroTermo;
		int idSegundoTermo;
		
		for (Entry<String, Integer> biGramaFrequencia : biGramasFrequencia.entrySet()) {
			termos = biGramaFrequencia.getKey().split(" ");
			frequencia = biGramaFrequencia.getValue();
			idPrimeiroTermo = dicionarioBiGramas.get(termos[0]);
			idSegundoTermo = dicionarioBiGramas.get(termos[1]);
			this.addMarkovTransitionMatrix(idPrimeiroTermo, idSegundoTermo, frequencia);
		}
		
	}

	private void loadSparseMarkovTransitionMatrix(Map<String, Integer> biGramasFrequencia, Map<String, Integer> dicionarioBiGramas) {

		this.sparseMarkovTransitionMatrix = new int[biGramasFrequencia.size()][3];
		
		//processa os bigrama frequencia e montar a tabela
		String[] termos;
		int i = 0;
		for (Entry<String, Integer> biGramaFrequencia : biGramasFrequencia.entrySet()) {
			termos = biGramaFrequencia.getKey().split(" ");
			
			int linha[] = new int[3];
			linha[0] = dicionarioBiGramas.get(termos[0]);
			linha[1] = dicionarioBiGramas.get(termos[1]);
			linha[2] = biGramaFrequencia.getValue();
			
			this.sparseMarkovTransitionMatrix[i] = linha;
			i++;
		}
	}
	
}
