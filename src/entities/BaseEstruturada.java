package entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import preProcessaTexto.StopWords;
import util.Arquivo;
import util.Util;

public class BaseEstruturada {

	private Set<String> termosUniGrama;
	private Set<String> termosBiGrama;
	private Set<Documento> documentos;
	private Set<String> stopList;
	private Map<String, Integer> termosFrequenciasGlobal;
	private Map<String, Integer> biGramasFrequenciasGlobal;
	private LinkedHashMap<String, Integer> dicionarioUniGrama;
	private LinkedHashMap<String, Integer> dicionarioBiGrama;
	private Map<String, Integer> nGramaFrequenciaGlobal;
	private Map<String, Integer> nGramaPresencaGlobal;
	
	
	public BaseEstruturada(ParametrosEntrada parametrosEntrada) {
		/*reduzir os setups iniciais e passar isso para os getters and setters*/
		this.termosUniGrama = new LinkedHashSet<String>();
		this.termosBiGrama = new LinkedHashSet<String>();
		this.documentos = new LinkedHashSet<Documento>();
		this.stopList = new LinkedHashSet<String>();
		this.termosFrequenciasGlobal = new HashMap<String, Integer>();
		this.biGramasFrequenciasGlobal = new HashMap<String, Integer>();
		this.dicionarioUniGrama = new LinkedHashMap<String, Integer>();
		this.dicionarioBiGrama = new LinkedHashMap<String, Integer>();
		
		loadStopList(parametrosEntrada.getStopListFileName());
		loadDocumentos(parametrosEntrada.getNomeArquivoOriginal());
		processaDocumentos(parametrosEntrada);
		//loadDicionarios(parametrosEntrada);

	}
	
	/**
	 * Recebe um lista de linhas e a cada linha nao vazia associa um documento.
	 * @param linhas
	 */
	public void loadDocumentos(List<String> linhas) {
		
		//cria o set de documentos para cada linha nao branca do arquivo
		for (String linha : linhas) {
			if (linha.trim().length() > 0){
				documentos.add(new Documento(linha, null));
			}	
		}
		System.out.println(getDocumentos().size()+ " Documentos carregados para a base.");
	}

	private void loadDocumentos(String nomeArquivoOriginal) {
		
		List<String> linhas = Arquivo.abreArquivo(nomeArquivoOriginal);
		loadDocumentos(linhas);
	}

	@SuppressWarnings("unused")
	private void loadDicionarios(ParametrosEntrada parametrosEntrada) {
		
		for (Documento documento : documentos) {
			termosFrequenciasGlobal = Util.consolidaNgramaFrequencia(termosFrequenciasGlobal, documento.getTermoFrequencia());
			biGramasFrequenciasGlobal = Util.consolidaNgramaFrequencia(biGramasFrequenciasGlobal, documento.getBiGramaFrequencia());
		}
		
		List<String> termosRemovidos = Util.removeFreqMin(termosFrequenciasGlobal, parametrosEntrada.getMinGlobalFreq());
		List<String> biGramasRemovidos = Util.removeFreqMin(biGramasFrequenciasGlobal, parametrosEntrada.getMinGlobalFreq());
		
		for (Documento documento : documentos) {
			for (String termoRemovido : termosRemovidos) {
				documento.getTermoFrequencia().remove(termoRemovido);
			}
			for (String biGramaRemovido : biGramasRemovidos) {
				documento.getBiGramaFrequencia().remove(biGramaRemovido);
			}
		}
		
		for (String termoRemovido : termosRemovidos){
			termosFrequenciasGlobal.remove(termoRemovido);
		}
		
		for (String biGramaRemovido : biGramasRemovidos){
			biGramasFrequenciasGlobal.remove(biGramaRemovido);
		}
		
		setTermosUniGrama(termosFrequenciasGlobal.keySet());
		for (String biGrama : biGramasFrequenciasGlobal.keySet()){
			String [] tokens = biGrama.split(" ");
			addAllTermosBiGrama(new ArrayList<String>(Arrays.asList(tokens)));
		}
		setTermosBiGrama(new TreeSet<String>(getTermosBiGrama()));
		
		dicionarioUniGrama = montaDicionario(termosUniGrama);
		dicionarioBiGrama = montaDicionario(termosBiGrama);		
		validaInicioFimDocumentos();
	}

	private void processaDocumentos( ParametrosEntrada parametrosEntrada) {
		
		for (Documento documento : getDocumentos()) {
			documento.processaDocumento(parametrosEntrada, getStopList());
			documento.posProcessaDocumento(parametrosEntrada.getMinLocalFreq());
		}
		System.out.println("Documentos processados com sucesso.");
		
	}

	private void validaInicioFimDocumentos() {
		
		boolean achouInicio;
		boolean achouFim;
		for (Documento documento : documentos) {
			Map<String, Integer> bigramas = documento.getBiGramaFrequencia();
			achouInicio = false;
			achouFim = false;
			for ( String bigrama : bigramas.keySet()) {
				if(bigrama.startsWith("INICIO")){
					achouInicio = true;
				}
				if(bigrama.endsWith("FIM")){
					achouFim = true;
				}
			}
			if(!achouInicio || !achouFim){
				System.out.println("Documento com problema");
				System.out.println(documento.getConteudoOriginal());
				System.out.println(documento.getConteudoProcessado());
				
			}
		}

	}

	/**
	 * 
	 * @param termos
	 * @return
	 */
	private LinkedHashMap<String, Integer> montaDicionario(Set<String> termos) {
		List<String> termosNGramaOrdenados = new ArrayList<String>(termos);
		Collections.sort(termosNGramaOrdenados);
		LinkedHashMap<String, Integer> dicionarioNGrama = new LinkedHashMap<String, Integer>();
		int i = 0;
		for (String termoUniGrama : termosNGramaOrdenados) {
			dicionarioNGrama.put(termoUniGrama, i);
			i++;
		}
		System.out.println("Dicionario montado com sucesso.");
		return dicionarioNGrama;
	}
	
	private void loadStopList(String stopListFileName) {
		
		StopWords stopWords = new StopWords();
		setStopList(stopWords.montaStopList(stopListFileName));
		System.out.println(getStopList().size() + " Termos carregados para a StopList.");
	}
	
	/**
	 * @return the termosUniGrama
	 */
	public Set<String> getTermosUniGrama() {
		return termosUniGrama;
	}

	/**
	 * @param termosUniGrama the termosUniGrama to set
	 */
	public void setTermosUniGrama(Set<String> termosUniGrama) {
		this.termosUniGrama = termosUniGrama;
	}

	/**
	 * @return the termosBiGrama
	 */
	public Set<String> getTermosBiGrama() {
		return termosBiGrama;
	}

	/**
	 * @param termosBiGrama the termosBiGrama to set
	 */
	public void setTermosBiGrama(Set<String> termosBiGrama) {
		this.termosBiGrama = termosBiGrama;
	}

	/**
	 * 
	 * @param dicionarioBiGrama
	 */
	public void addAllTermosBiGrama(List<String> termosBiGrama) {
		this.termosBiGrama.addAll(termosBiGrama);
	}
	
	/**
	 * @return the documentos
	 */
	public Set<Documento> getDocumentos() {
		return documentos;
	}
	/**
	 * @param documentos the documentos to set
	 */
	public void setDocumentos(Set<Documento> documentos) {
		this.documentos = documentos;
	}
	/**
	 * @return the stopList
	 */
	public Set<String> getStopList() {
		return stopList;
	}
	/**
	 * @param stopList the stopList to set
	 */
	public void setStopList(Set<String> stopList) {
		this.stopList = stopList;
	}

	/**
	 * @return the termosFrequenciasGlobal
	 */
	public Map<String, Integer> getTermosFrequenciasGlobal() {
		return termosFrequenciasGlobal;
	}

	/**
	 * @param termosFrequenciasGlobal the termosFrequenciasGlobal to set
	 */
	public void setTermosFrequenciasGlobal(
			Map<String, Integer> termosFrequenciasGlobal) {
		this.termosFrequenciasGlobal = termosFrequenciasGlobal;
	}

	/**
	 * @return the biGramasFrequenciasGlobal
	 */
	public Map<String, Integer> getBiGramasFrequenciasGlobal() {
		return biGramasFrequenciasGlobal;
	}

	/**
	 * @param biGramasFrequenciasGlobal the biGramasFrequenciasGlobal to set
	 */
	public void setBiGramasFrequenciasGlobal(
			Map<String, Integer> biGramasFrequenciasGlobal) {
		this.biGramasFrequenciasGlobal = biGramasFrequenciasGlobal;
	}

	/**
	 * @return the dicionarioUniGrama
	 */
	public LinkedHashMap<String, Integer> getDicionarioUniGrama() {
		//TODO: corrigir essa parte
		if (dicionarioUniGrama == null){
			//loadDicionarios(parametrosEntrada);
		}
		
		return dicionarioUniGrama;
	}

	/**
	 * @return the dicionarioBiGrama
	 */
	public LinkedHashMap<String, Integer> getDicionarioBiGrama() {
		//TODO: corrigir essa parte
		if (dicionarioUniGrama == null){
			//loadDicionarios(parametrosEntrada);
		}
		return dicionarioBiGrama;
	}

	/**
	 * nGramaFrequenciaGlobal eh carregado sob demanda
	 * @return the nGramaFrequenciaGlobal
	 */
	public Map<String, Integer> getnGramaFrequenciaGlobal() {
		if (nGramaFrequenciaGlobal == null){
			loadNGramaFrequenciaGlobal();
		}
		
		return nGramaFrequenciaGlobal;
	}

	private void loadNGramaFrequenciaGlobal() {

		nGramaFrequenciaGlobal = new LinkedHashMap<String, Integer>();
		for (Documento documento: getDocumentos()){
			nGramaFrequenciaGlobal = Util.consolidaNgramaFrequencia(nGramaFrequenciaGlobal, documento.getnGramaFrequencia());
		}
		System.out.println("NGramas carregados com sucesso.");
	}

	/**
	 * @param nGramaFrequenciaGlobal the nGramaFrequenciaGlobal to set
	 */
	public void setnGramaFrequenciaGlobal(Map<String, Integer> nGramaFrequenciaGlobal) {
		this.nGramaFrequenciaGlobal = nGramaFrequenciaGlobal;
	}

	/**
	 * nGramaPresencaGlobal eh carregado sob demanda
	 * @return the nGramaPresencaGlobal
	 */
	public Map<String, Integer> getnGramaPresencaGlobal() {
		if (nGramaPresencaGlobal == null){
			loadNGramaPresencaGlobal();
		}
		
		return nGramaPresencaGlobal;
	}

	private void loadNGramaPresencaGlobal() {
		nGramaPresencaGlobal = new LinkedHashMap<String, Integer>();
		for (Documento documento: getDocumentos()){
			nGramaPresencaGlobal = Util.consolidaNgramaFrequencia(nGramaPresencaGlobal, documento.getnGramaPresenca());
		}
		System.out.println("NGramas carregados com sucesso.");
	}

	/**
	 * @param nGramaPresencaGlobal the nGramaPresencaGlobal to set
	 */
	public void setnGramaPresencaGlobal(Map<String, Integer> nGramaPresencaGlobal) {
		this.nGramaPresencaGlobal = nGramaPresencaGlobal;
	}
	
	
}
