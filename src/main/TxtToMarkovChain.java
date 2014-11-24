package main;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import util.Arquivo;
import util.Util;
import entities.BaseEstruturada;
import entities.MarkovTransitionEstruture;
import entities.ParametrosEntrada;

public class TxtToMarkovChain {

	public static void main(String[] args) {
		TxtToMarkovChain txtToMarkovChain = new TxtToMarkovChain();
		txtToMarkovChain.run();
	}

	private void run() {
		Date inicio = new Date();
		
		//Pega os dados do arquivo do properties
		ParametrosEntrada parametrosEntrada = new ParametrosEntrada();
		//com os parametros passados monta a base
		BaseEstruturada baseEstruturada = new BaseEstruturada(parametrosEntrada);
		MarkovTransitionEstruture markovTransitionEstruture =  new MarkovTransitionEstruture(baseEstruturada);

		//imprime o dicionario
		List<String>  textoDicionario = Util.MapToListString(baseEstruturada.getDicionarioBiGrama(),0);
		String nomeDicionarioBiGrama = "DicionarioBiGrama.txt";
		Arquivo.salvaArquivo(textoDicionario, nomeDicionarioBiGrama);
		
		//imprime a matriz de transicao sparsa
		List<String> textoMatrizTransicaoSparsa = Util.intMatrizToListString(markovTransitionEstruture.getSparseMarkovTransitionMatrix());			
		String nomeMatrizTransicaoSparsa = "SparseMatrizTransicao.txt";
		Arquivo.salvaArquivo(textoMatrizTransicaoSparsa, nomeMatrizTransicaoSparsa);
		
		//gera log
		List<String> arquivosGerados = new ArrayList<String>();
		arquivosGerados.add(nomeDicionarioBiGrama);
		arquivosGerados.add(nomeMatrizTransicaoSparsa);
		Date fim = new Date();
		List<String> linhasParametrosEntrada = Arquivo.abreArquivo(parametrosEntrada.getNomeProperties());
		Util.geraLog(inicio, linhasParametrosEntrada, arquivosGerados, fim, parametrosEntrada.getNomeArquivoLog());
	
		
	}

}
