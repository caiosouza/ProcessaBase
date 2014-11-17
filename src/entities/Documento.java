package entities;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import preProcessamento.PreProcessaCorpus;
import processamento.ValidaCaracteres;
import util.Util;

public class Documento {

	private String conteudoOriginal;
	private String conteudoProcessado;
	private Set<String> termos;
	private Map<String, Integer> termoFrequencia;
	private Map<String, Integer> biGramaFrequencia;
	private Integer idCategoria;
	private Map< Integer, Map<String, Integer>> nGramaFrequencia;
	private Map< Integer, Map<String, Integer>> nGramaPresenca;
	
	public Documento(String conteudoOriginal, Integer idCategoria) {
		this.conteudoOriginal = conteudoOriginal;
		this.biGramaFrequencia = new HashMap<String, Integer>();
		this.termoFrequencia = new HashMap<String, Integer>();
		this.termos = new HashSet<String>();
		
		if (idCategoria != null){
			this.idCategoria = idCategoria;
		}
	}
	
	public String getConteudoOriginal() {
		return conteudoOriginal;
	}
	public void setConteudoOriginal(String conteudoOriginal) {
		this.conteudoOriginal = conteudoOriginal;
	}
	
	/**
	 * @return the conteudoProcessado
	 */
	public String getConteudoProcessado() {
		return conteudoProcessado;
	}

	/**
	 * @param conteudoProcessado the conteudoProcessado to set
	 */
	public void setConteudoProcessado(String conteudoProcessado) {
		this.conteudoProcessado = conteudoProcessado;
	}

	/**
	 * @return the termos
	 */
	public Set<String> getTermos() {
		return termos;
	}

	/**
	 * @param termos the termos to set
	 */
	public void setTermos(Set<String> termos) {
		this.termos = termos;
	}

	public Map<String, Integer> getTermoFrequencia() {
		return termoFrequencia;
	}
	public void setTermoFrequencia(Map<String, Integer> termoFrequencia) {
		this.termoFrequencia = termoFrequencia;
	}
	public int getIdCategoria() {
		return idCategoria;
	}
	public void setIdCategoria(Integer idCategoria) {
		this.idCategoria = idCategoria;
	}

	public Map<String, Integer> getBiGramaFrequencia() {
		return biGramaFrequencia;
	}

	public void setBiGramaFrequencia(Map<String, Integer> biGramaFrequencia) {
		this.biGramaFrequencia = biGramaFrequencia;
	}

	public void processaDocumento(ParametrosEntrada parametrosEntrada, Set<String> stopList) {
	
		PreProcessaCorpus preProcessaCorpus = new PreProcessaCorpus();
		//remove stop word, reduz stemme e retira palavras cujo tamanho esta fora dos limites
		String conteudoProcessado = preProcessaCorpus.preProcessaLinha(getConteudoOriginal(), parametrosEntrada.getStopWord(), 
				parametrosEntrada.getStemme(), parametrosEntrada.getStopListFileName(), parametrosEntrada.getPodeNumero(), stopList,
				parametrosEntrada.getMinSizeWord(), parametrosEntrada.getMaxSizeWord());
		
		ValidaCaracteres validaCaracteres = new ValidaCaracteres();
		if(validaCaracteres.validaLinhas(conteudoProcessado, parametrosEntrada.getPodeNumero())){
			//conteudoProcessado = "INICIO "+ conteudoProcessado+ " FIM";
			setConteudoProcessado(conteudoProcessado);
			//setTermos(new HashSet<String>(Arrays.asList(conteudoProcessado.split(" "))));
		} 
	}
	
	public void posProcessaDocumento(int freqMinLocal){
		processaTermoFrequencia(freqMinLocal);
		processaBigramaFrequencia(freqMinLocal);
	}
	
	private void processaTermoFrequencia(int freqMinLocal) {
		
		if (getConteudoProcessado() != null){
			for (String termo: getConteudoProcessado().split(" ")){
				if (termo.trim().length() > 0){
					Util.incrementaFrequencia(termoFrequencia, termo);
				}
			}
		}
		Util.removeFreqMin(termoFrequencia, freqMinLocal);
	}

	private void processaBigramaFrequencia(int freqMinLocal) {
		String inicio = "INICIO";
		String fim = "FIM";
		String anterior = inicio;
		String atual;
		String[] termos = null;
		if (getConteudoProcessado() != null){
			termos = getConteudoProcessado().split(" ");
			for (String termo: termos){
				if (termo.trim().length() > 0){
					atual = anterior + " " + termo;
					Util.incrementaFrequencia(biGramaFrequencia, atual);
					anterior = termo;
				}
			}
			//coloca a marcacao de final
			atual = anterior + " " + fim;
			Util.incrementaFrequencia(biGramaFrequencia, atual);
			
			Util.removeFreqMin(termoFrequencia, freqMinLocal);
		}
	}
	
}
