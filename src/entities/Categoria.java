package entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import util.Arquivo;
import util.Util;

public class Categoria {

	private int idCategoria;
	private String nomeCategoria;
	private Set<Documento> documentosTreino;
	private Set<Documento> documentosTeste;
	private Map<String, Integer> termoFrequencia;
	private Map<String, Integer> biGramaFrequencia;
	private Map<String, Integer> nGramaFrequencia;
	private Map<String, Integer> nGramaPresenca;
	
	public Categoria(int idCategoria, String nomeCategoria) {
		this.idCategoria = idCategoria;
		this.nomeCategoria = nomeCategoria;
		this.documentosTreino = new LinkedHashSet<Documento>();
		this.documentosTeste = new LinkedHashSet<Documento>();
	}
	public Categoria(String nomeArquivoBaseTxt) {
		//cria categoria default com todos os documentos
		this.documentosTreino = new LinkedHashSet<Documento>();
		this.documentosTeste = new LinkedHashSet<Documento>();
		loadDocumentos(nomeArquivoBaseTxt);
	}
	/**
	 * Recebe um lista de linhas e a cada linha nao vazia associa um documento.
	 * @param linhas
	 */
	public void loadDocumentos(List<String> linhas) {
	 
		//cria o set de documentos para cada linha nao branca do arquivo
		for (String linha : linhas) {
			if (linha.trim().length() > 0){
				documentosTreino.add(new Documento(linha));
			}	
		}
		System.out.println(getDocumentos().size()+ " Documentos carregados para a base.");
	}

	private void loadDocumentos(String nomeArquivoBaseTxt) {
		List<String> linhas = Arquivo.abreArquivo(nomeArquivoBaseTxt);
		loadDocumentos(linhas);
	}

	public int getIdCategoria() {
		return idCategoria;
	}
	public void setIdCategoria(int idCategoria) {
		this.idCategoria = idCategoria;
	}
	public String getNomeCategoria() {
		return nomeCategoria;
	}
	public void setNomeCategoria(String nomeCategoria) {
		this.nomeCategoria = nomeCategoria;
	}
	public void addDocumento(String linhaDocumento, int tipoDocumento) {
		if(tipoDocumento == 0){
			documentosTreino.add(new Documento(linhaDocumento));
		}
		else {
			documentosTeste.add(new Documento(linhaDocumento));
		}
	}
	public Map<String, Integer> getTermoFrequencia() {
		if (termoFrequencia == null){
			loadTermoFrequencia();
		}
		return termoFrequencia;
	}
	private void loadTermoFrequencia() {
		this.termoFrequencia = new HashMap<String, Integer>();
		for (Documento documento : documentosTreino) {
			termoFrequencia = Util.consolidaNgramaFrequencia(termoFrequencia, documento.getTermoFrequencia());
		}
	}
	public Map<String, Integer> getBiGramaFrequencia() {
		if (biGramaFrequencia == null){
			loadBigramaFrequencia();
		}
		return biGramaFrequencia;
	}
	private void loadBigramaFrequencia() {
		this.biGramaFrequencia = new HashMap<String, Integer>();
		for (Documento documento : documentosTreino) {
			biGramaFrequencia = Util.consolidaNgramaFrequencia(biGramaFrequencia, documento.getBiGramaFrequencia());
		}
	}
	public void removeTermoFrequencia(List<String> termosRemovidos) {
		for (Documento documento : documentosTreino) {
			for (String termoRemovido : termosRemovidos) {
				documento.getTermoFrequencia().remove(termoRemovido);
			}
		}
	}
	public void removeBiGramaFrequencia(List<String> biGramasRemovidos) {
		for (Documento documento : documentosTreino) {
			for (String biGramaRemovido : biGramasRemovidos) {
				documento.getBiGramaFrequencia().remove(biGramaRemovido);
			}
		}
	}
	public void processaCategoria( ParametrosEntrada parametrosEntrada, Set<String> stopList) {
		
		for (Documento documento : documentosTreino) {
			documento.processaDocumento(parametrosEntrada, stopList);
			documento.posProcessaDocumento(parametrosEntrada.getMinLocalFreq());
		}
		System.out.println("Documentos processados com sucesso.");
		
	}
	
	public void validaInicioFimDocumentos() {
		
		boolean achouInicio;
		boolean achouFim;
		for (Documento documento : documentosTreino) {
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
	public Map<String, Integer> getnGramaFrequencia() {
		if (nGramaFrequencia == null){
			loadNGramaFrequencia();
		}
		return nGramaFrequencia;
	}

	private void loadNGramaFrequencia() {
		nGramaFrequencia = new LinkedHashMap<String, Integer>();
		for (Documento documento : documentosTreino) {
				nGramaFrequencia = Util.consolidaNgramaFrequencia(nGramaFrequencia, documento.getnGramaFrequencia());
		}
		System.out.println("NGramas carregados com sucesso.");
	}
	public Map<String, Integer> getnGramaPresenca() {
		if (nGramaPresenca == null){
			loadNGramaPresenca();
		}
		return nGramaPresenca;
	}

	private void loadNGramaPresenca() {
		nGramaPresenca = new LinkedHashMap<String, Integer>();
		for (Documento documento : documentosTreino) {
			nGramaPresenca = Util.consolidaNgramaFrequencia(nGramaPresenca, documento.getnGramaPresenca());
		}
		System.out.println("NGramas carregados com sucesso.");
	}
	public int getNumDocumentos() {
		
		return documentosTreino.size()+ documentosTeste.size();
	}
	public List<Documento> getDocumentos() {
		List<Documento> allDocumentos = new ArrayList<Documento>();
		allDocumentos.addAll(documentosTreino);
		allDocumentos.addAll(documentosTeste);
		return allDocumentos;
	}


	
	
}
