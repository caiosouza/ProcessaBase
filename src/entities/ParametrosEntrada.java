package entities;

import util.Util;

public class ParametrosEntrada {

	//parametros de entrada
	private String nomeArquivoBaseTxt;
	private String nomeArquivoCategoriasDocs;
	private Boolean stemme;
	private Boolean stopWord;
	private String stopListFileName;
	private Boolean podeNumero;
	private Integer minLocalFreq;
	private Integer minGlobalFreq;
	private Integer minSizeWord;
	private Integer maxSizeWord;
	private String nomeArquivoLog;
	private static final String PROCESSA_BASE_PARAMETERS_PROPERTIES = "parameters.properties";
	
	public ParametrosEntrada() {
		this.nomeArquivoBaseTxt = Util.readFromProperties("nomeArquivoBaseTxt", PROCESSA_BASE_PARAMETERS_PROPERTIES);
		this.nomeArquivoCategoriasDocs = Util.readFromProperties("nomeArquivoCategoriasDocs", PROCESSA_BASE_PARAMETERS_PROPERTIES);
		this.stemme = "true".equals(Util.readFromProperties("stemme", PROCESSA_BASE_PARAMETERS_PROPERTIES));
		this.stopWord = "true".equals(Util.readFromProperties("stopWord", PROCESSA_BASE_PARAMETERS_PROPERTIES));
		this.podeNumero = "true".equals(Util.readFromProperties("podeNumero", PROCESSA_BASE_PARAMETERS_PROPERTIES));
		this.stopListFileName = Util.readFromProperties("stopList", PROCESSA_BASE_PARAMETERS_PROPERTIES);
		this.minLocalFreq = Integer.parseInt(Util.readFromProperties("minLocalFreq", PROCESSA_BASE_PARAMETERS_PROPERTIES));
		this.minGlobalFreq = Integer.parseInt(Util.readFromProperties("minGlobalFreq", PROCESSA_BASE_PARAMETERS_PROPERTIES));
		this.minSizeWord = Integer.parseInt(Util.readFromProperties("minSizeWord", PROCESSA_BASE_PARAMETERS_PROPERTIES));
		this.maxSizeWord = Integer.parseInt(Util.readFromProperties("maxSizeWord", PROCESSA_BASE_PARAMETERS_PROPERTIES));
		this.nomeArquivoLog = Util.readFromProperties("nomeArquivoLog", PROCESSA_BASE_PARAMETERS_PROPERTIES);
	}
	/**
	 * @return the nomeArquivoBaseTxt
	 */
	public String getNomeArquivoBaseTxt() {
		return nomeArquivoBaseTxt;
	}
	/**
	 * @param nomeArquivoBaseTxt the nomeArquivoBaseTxt to set
	 */
	public void setNomeArquivoBaseTxt(String nomeArquivoBaseTxt) {
		this.nomeArquivoBaseTxt = nomeArquivoBaseTxt;
	}
	/**
	 * @return the nomeArquivoCategoriasDocs
	 */
	public String getNomeArquivoCategoriasDocs() {
		return nomeArquivoCategoriasDocs;
	}
	/**
	 * @param nomeArquivoCategoriasDocs the nomeArquivoCategoriasDocs to set
	 */
	public void setNomeArquivoCategoriasDocs(String nomeArquivoCategoriasDocs) {
		this.nomeArquivoCategoriasDocs = nomeArquivoCategoriasDocs;
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
	public String getNomeProperties() {
		return PROCESSA_BASE_PARAMETERS_PROPERTIES;
	}
	/**
	 * @return the nomeArquivoLog
	 */
	public String getNomeArquivoLog() {
		return nomeArquivoLog;
	}
	/**
	 * @param nomeArquivoLog the nomeArquivoLog to set
	 */
	public void setNomeArquivoLog(String nomeArquivoLog) {
		this.nomeArquivoLog = nomeArquivoLog;
	}

}
