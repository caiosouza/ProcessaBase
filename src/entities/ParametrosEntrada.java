package entities;

import util.Util;

public class ParametrosEntrada {

	//parametros de entrada
	private String nomeArquivoOriginal;
	private Boolean stemme;
	private Boolean stopWord;
	private String stopListFileName;
	private Boolean podeNumero;
	private Integer minLocalFreq;
	private Integer minGlobalFreq;
	private Integer minSizeWord;
	private Integer maxSizeWord;
	private static final String PROCESSA_BASE_PARAMETERS_PROPERTIES = "parameters.properties";
	
	public ParametrosEntrada() {
		String nomeArquivoOriginal = Util.readFromProperties("inputBaseFileName", PROCESSA_BASE_PARAMETERS_PROPERTIES);
		Boolean stemme = "true".equals(Util.readFromProperties("stemme", PROCESSA_BASE_PARAMETERS_PROPERTIES));
		Boolean stopWord = "true".equals(Util.readFromProperties("stopWord", PROCESSA_BASE_PARAMETERS_PROPERTIES));
		Boolean podeNumero = "true".equals(Util.readFromProperties("podeNumero", PROCESSA_BASE_PARAMETERS_PROPERTIES));
		String stopListFileName = Util.readFromProperties("stopList", PROCESSA_BASE_PARAMETERS_PROPERTIES);
		Integer minLocalFreq = Integer.parseInt(Util.readFromProperties("minLocalFreq", PROCESSA_BASE_PARAMETERS_PROPERTIES));
		Integer minGlobalFreq = Integer.parseInt(Util.readFromProperties("minGlobalFreq", PROCESSA_BASE_PARAMETERS_PROPERTIES));
		Integer minSizeWord = Integer.parseInt(Util.readFromProperties("minSizeWord", PROCESSA_BASE_PARAMETERS_PROPERTIES));
		Integer maxSizeWord = Integer.parseInt(Util.readFromProperties("maxSizeWord", PROCESSA_BASE_PARAMETERS_PROPERTIES));
		
		this.nomeArquivoOriginal = nomeArquivoOriginal;
		this.stemme = stemme;
		this.stopWord = stopWord;
		this.stopListFileName = stopListFileName;
		this.podeNumero = podeNumero;
		this.minLocalFreq = minLocalFreq;
		this.minGlobalFreq = minGlobalFreq;
		this.minSizeWord = minSizeWord;
		this.maxSizeWord = maxSizeWord;
		
	}
	/**
	 * @return the nomeArquivoOriginal
	 */
	public String getNomeArquivoOriginal() {
		return nomeArquivoOriginal;
	}
	/**
	 * @param nomeArquivoOriginal the nomeArquivoOriginal to set
	 */
	public void setNomeArquivoOriginal(String nomeArquivoOriginal) {
		this.nomeArquivoOriginal = nomeArquivoOriginal;
	}
	/**
	 * @return the stemme
	 */
	public Boolean getStemme() {
		return stemme;
	}
	/**
	 * @param steme the stemme to set
	 */
	public void setStemme(Boolean stemme) {
		this.stemme = stemme;
	}
	/**
	 * @return the stopWord
	 */
	public Boolean getStopWord() {
		return stopWord;
	}
	/**
	 * @param stopWord the stopWord to set
	 */
	public void setStopWord(Boolean stopWord) {
		this.stopWord = stopWord;
	}
	/**
	 * @return the stopListFileName
	 */
	public String getStopListFileName() {
		return stopListFileName;
	}
	/**
	 * @param stopListFileName the stopListFileName to set
	 */
	public void setStopListFileName(String stopListFileName) {
		this.stopListFileName = stopListFileName;
	}
	/**
	 * @return the podeNumero
	 */
	public Boolean getPodeNumero() {
		return podeNumero;
	}
	/**
	 * @param podeNumero the podeNumero to set
	 */
	public void setPodeNumero(Boolean podeNumero) {
		this.podeNumero = podeNumero;
	}
	/**
	 * @return the minLocalFreq
	 */
	public Integer getMinLocalFreq() {
		return minLocalFreq;
	}
	/**
	 * @param minLocalFreq the minLocalFreq to set
	 */
	public void setMinLocalFreq(Integer minLocalFreq) {
		this.minLocalFreq = minLocalFreq;
	}
	/**
	 * @return the minGlobalFreq
	 */
	public Integer getMinGlobalFreq() {
		return minGlobalFreq;
	}
	/**
	 * @param minGlobalFreq the minGlobalFreq to set
	 */
	public void setMinGlobalFreq(Integer minGlobalFreq) {
		this.minGlobalFreq = minGlobalFreq;
	}
	/**
	 * @return the minSizeWord
	 */
	public Integer getMinSizeWord() {
		return minSizeWord;
	}
	/**
	 * @param minSizeWord the minSizeWord to set
	 */
	public void setMinSizeWord(Integer minSizeWord) {
		this.minSizeWord = minSizeWord;
	}
	/**
	 * @return the maxSizeWord
	 */
	public Integer getMaxSizeWord() {
		return maxSizeWord;
	}
	/**
	 * @param maxSizeWord the maxSizeWord to set
	 */
	public void setMaxSizeWord(Integer maxSizeWord) {
		this.maxSizeWord = maxSizeWord;
	}

}
