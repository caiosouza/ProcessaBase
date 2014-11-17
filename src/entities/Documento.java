package entities;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import preProcessaTexto.NGrama;
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
	private Map<String, Integer> nGramaFrequencia;
	private Map<String, Integer> nGramaPresenca;
	
	public Documento(String conteudoOriginal, Integer idCategoria) {
		/*reduzir os setups iniciais e passar isso para os getters and setters*/
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
	
	/**
	 * @return the nGramaFrequencia
	 */
	public Map<String, Integer> getnGramaFrequencia() {
		if (nGramaFrequencia == null){
			loadNgramaFrequencia();
		}
		return nGramaFrequencia;
	}

	private void loadNgramaFrequencia() {
		
		nGramaFrequencia = new HashMap<String, Integer>();
		String conteudo = getConteudoProcessado();
		String[] tokens = conteudo.split(" ");
		
		for (int n = 1; n <= tokens.length; n++) {
    		//System.out.println(n);
    		for (String ngram : NGrama.ngrams(n, conteudo))
    			Util.incrementaFrequencia(nGramaFrequencia, ngram);
        }
	}

	/**
	 * @param nGramaFrequencia the nGramaFrequencia to set
	 */
	public void setnGramaFrequencia(Map<String, Integer> nGramaFrequencia) {
		this.nGramaFrequencia = nGramaFrequencia;
	}

	/**
	 * @return the nGramaPresenca
	 */
	public Map<String, Integer> getnGramaPresenca() {
		if (nGramaPresenca == null){
			loadNgramaPresenca();
		}
		return nGramaPresenca;
	}

	private void loadNgramaPresenca() {
		
		nGramaPresenca = new HashMap<String, Integer>();
		String conteudo = getConteudoProcessado();
		String[] tokens = conteudo.split(" ");
		
		for (int n = 1; n <= tokens.length; n++) {
    		//System.out.println(n);
    		for (String ngram : NGrama.ngrams(n, conteudo))
    			Util.incrementaPresenca(nGramaPresenca, ngram);
        }
	}

	/**
	 * @param nGramaPresenca the nGramaPresenca to set
	 */
	public void setnGramaPresenca(Map<String, Integer> nGramaPresenca) {
		this.nGramaPresenca = nGramaPresenca;
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
