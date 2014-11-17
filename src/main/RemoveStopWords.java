package main;

import java.util.ArrayList;
import java.util.List;

import util.Arquivo;
import entities.BaseEstruturada;
import entities.Documento;
import entities.ParametrosEntrada;

public class RemoveStopWords {

	public static void main(String[] args) {
		RemoveStopWords removeStopWords = new RemoveStopWords();
		removeStopWords.run();
	}

	private void run() {
		
		//Pega os dados do arquivo do properties
		ParametrosEntrada parametrosEntrada = new ParametrosEntrada();
		//com os parametros passados monta a base
		BaseEstruturada baseEstruturada = new BaseEstruturada(parametrosEntrada);
		
		System.out.println(baseEstruturada.getDocumentos().size() + " Documentos serão salvos no arquivo de saída.");
		List<String> textosPreProcessados = new ArrayList<String>();
		for (Documento documento : baseEstruturada.getDocumentos()) {
			textosPreProcessados.add(documento.getConteudoProcessado());
		}
		
		Arquivo.salvaArquivo(textosPreProcessados, parametrosEntrada.getNomeArquivoOriginal().replace(".txt", "WithoutStopWords.txt"));
	}
}