package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

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
		
		//Pega os dados do arquivo do properties
		ParametrosEntrada parametrosEntrada = new ParametrosEntrada();
		//com os parametros passados monta a base
		BaseEstruturada baseEstruturada = new BaseEstruturada(parametrosEntrada);
		MarkovTransitionEstruture markovTransitionEstruture =  new MarkovTransitionEstruture(baseEstruturada);

		//imprime as estruturas geradas
		String temp;
		
		//imprime o dicionario
		List<String> textoDicionario = new ArrayList<String>();
		//TODO trocar isso pela funcao que foi criada no utils
		for	(Entry<String, Integer> palavraId:	baseEstruturada.getDicionarioBiGrama().entrySet()){
			temp = palavraId.getValue()+";"+palavraId.getKey();
			textoDicionario.add(temp);
		}
		Arquivo.salvaArquivo(textoDicionario, "DicionarioBiGrama.txt");
		
		List<String> textoMatrizTransicaoSparsa = new ArrayList<String>();
		//imprime a matriz de transicao sparsa
		int[][] sparseMatrizDeTransicao = markovTransitionEstruture.getSparseMarkovTransitionMatrix();
		for (int i = 0; i < sparseMatrizDeTransicao.length; i++) {
			int[] linhaMatriz = sparseMatrizDeTransicao[i];
			temp = Arrays.toString(linhaMatriz);
			temp = temp.replace("[","");
			temp = temp.replace("]","");
			textoMatrizTransicaoSparsa.add(temp);
		}
		Arquivo.salvaArquivo(textoMatrizTransicaoSparsa, "SparseMatrizTransicao.txt");
	
		
	}

}
