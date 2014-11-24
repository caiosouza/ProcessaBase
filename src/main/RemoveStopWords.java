package main;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import util.Arquivo;
import util.Util;
import entities.BaseEstruturada;
import entities.Categoria;
import entities.Documento;
import entities.ParametrosEntrada;

public class RemoveStopWords {

	public static void main(String[] args) {
		RemoveStopWords removeStopWords = new RemoveStopWords();
		removeStopWords.run();
	}

	private void run() {
		Date inicio = new Date();
		
		//Pega os dados do arquivo do properties
		ParametrosEntrada parametrosEntrada = new ParametrosEntrada();
		//com os parametros passados monta a base
		BaseEstruturada baseEstruturada = new BaseEstruturada(parametrosEntrada);
		
		System.out.println(baseEstruturada.getNumDocumentos() + " Documentos serão salvos no arquivo de saída.");
		List<String> textosPreProcessados = new ArrayList<String>();
		for (Categoria categoria: baseEstruturada.getCategorias()){
			for (Documento documento : categoria.getDocumentos()) {
				textosPreProcessados.add(documento.getConteudoProcessado());
			}
		}
	
		//gera log
		String nomeArquivoSaida = "outPut" + File.separatorChar+  parametrosEntrada.getNomeArquivoBaseTxt().replace(".txt", "_WithoutStopWords.txt");
		Date fim = new Date();
		List<String> linhasParametrosEntrada = Arquivo.abreArquivo(parametrosEntrada.getNomeProperties());
		Util.geraLog(inicio, linhasParametrosEntrada, nomeArquivoSaida, fim, parametrosEntrada.getNomeArquivoLog());
		Arquivo.salvaArquivo(textosPreProcessados, nomeArquivoSaida);
	}
}