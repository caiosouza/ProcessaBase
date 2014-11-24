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
	private Set<Categoria> categorias;
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
		this.categorias = new LinkedHashSet<Categoria>();
		this.stopList = new LinkedHashSet<String>();
		this.termosFrequenciasGlobal = new HashMap<String, Integer>();
		this.biGramasFrequenciasGlobal = new HashMap<String, Integer>();
		this.dicionarioUniGrama = new LinkedHashMap<String, Integer>();
		this.dicionarioBiGrama = new LinkedHashMap<String, Integer>();
		
		loadStopList(parametrosEntrada.getStopListFileName());
		loadCategorias(parametrosEntrada.getNomeArquivoCategoriasDocs(), parametrosEntrada.getNomeArquivoBaseTxt());
		processaCategorias(parametrosEntrada);
		//loadDicionarios(parametrosEntrada);

	}
	
	/**
	 * 
	 * @param nomeArquivoCategoriasDocs
	 * @param nomeArquivoBaseTxt
	 */
	private void loadCategorias(String nomeArquivoCategoriasDocs,
			String nomeArquivoBaseTxt) {
		
		if(nomeArquivoCategoriasDocs == null || nomeArquivoCategoriasDocs.isEmpty()){
			this.categorias.add(new Categoria(nomeArquivoBaseTxt));
		} else {
			// le o arquivo de categorias e de documentos ao mesmo tempo e vai colocando cada documento na categoria correta
			boolean mantemVazias = false;
			List<String> linhasArquivoCategorias = Arquivo.abreArquivo(nomeArquivoCategoriasDocs);
			List<String> linhasArquivoDocumentos = Arquivo.abreArquivo(nomeArquivoBaseTxt, mantemVazias);
			if (linhasArquivoCategorias.size() != linhasArquivoDocumentos.size()){
				System.out.println("Tamanho de arquivos de categorias e documentos diferentes.");
			}
			else {
				String linhaCategoria;
				String linhaDocumento;
				String[] partesLinhaCategoria;
				int tipoDocumento;
				for (int i = 0; i < linhasArquivoCategorias.size(); i++) {
					linhaCategoria = linhasArquivoCategorias.get(i);
					linhaDocumento = linhasArquivoDocumentos.get(i);
					partesLinhaCategoria = linhaCategoria.split(";");
					if (partesLinhaCategoria.length > 1)
					{
						tipoDocumento = Integer.parseInt(partesLinhaCategoria[1]);
					}
					else {
						tipoDocumento = 0; //treino
					}
					Categoria categoriaAtual = procuraCategoria(Integer.parseInt(partesLinhaCategoria[0]));
					categoriaAtual.addDocumento(linhaDocumento, tipoDocumento);
				}
			}
			
		}
	}

	private Categoria procuraCategoria(Integer idCategoriaProcurada) {
		
		Categoria categoriaCorreta = null;
		for (Categoria categoria : categorias) {
			if(categoria.getIdCategoria() == idCategoriaProcurada){
				categoriaCorreta = categoria;
				break;
			}
		}
		return categoriaCorreta;
	}

	@SuppressWarnings("unused")
	private void loadDicionarios(ParametrosEntrada parametrosEntrada) {
		
		for (Categoria categoria : categorias) {
			termosFrequenciasGlobal = Util.consolidaNgramaFrequencia(termosFrequenciasGlobal, categoria.getTermoFrequencia());
			biGramasFrequenciasGlobal = Util.consolidaNgramaFrequencia(biGramasFrequenciasGlobal, categoria.getBiGramaFrequencia());
		}
		
		List<String> termosRemovidos = Util.removeFreqMin(termosFrequenciasGlobal, parametrosEntrada.getMinGlobalFreq());
		List<String> biGramasRemovidos = Util.removeFreqMin(biGramasFrequenciasGlobal, parametrosEntrada.getMinGlobalFreq());
		
		for (Categoria categoria : categorias) {
			categoria.removeTermoFrequencia(termosRemovidos);
			categoria.removeBiGramaFrequencia(biGramasRemovidos);
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
		
		for (Categoria categoria : categorias) {
			categoria.validaInicioFimDocumentos();
		}
	}

	private void processaCategorias( ParametrosEntrada parametrosEntrada) {
		for (Categoria categoria : categorias) {
			categoria.processaCategoria(parametrosEntrada, getStopList());
		}
		System.out.println("Categorias processadas com sucesso.");
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
		for (Categoria categoria : categorias) {
				nGramaFrequenciaGlobal = Util.consolidaNgramaFrequencia(nGramaFrequenciaGlobal, categoria.getnGramaFrequencia());
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
		for (Categoria categoria : categorias) {
				nGramaPresencaGlobal = Util.consolidaNgramaFrequencia(nGramaPresencaGlobal, categoria.getnGramaPresenca());
		}
		System.out.println("NGramas carregados com sucesso.");
	}

	/**
	 * @param nGramaPresencaGlobal the nGramaPresencaGlobal to set
	 */
	public void setnGramaPresencaGlobal(Map<String, Integer> nGramaPresencaGlobal) {
		this.nGramaPresencaGlobal = nGramaPresencaGlobal;
	}

	public int getNumDocumentos() {
		int numDocumentos=0;
		for (Categoria categoria : categorias) {
			numDocumentos = numDocumentos + categoria.getNumDocumentos();
		}
		return numDocumentos;
	}
	/**
	 * @return the categorias
	 */
	public Set<Categoria> getCategorias() {
		return categorias;
	}
	
}

	