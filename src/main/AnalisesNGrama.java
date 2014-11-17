package main;

import java.util.List;
import java.util.Map;

import util.Arquivo;
import util.Util;
import entities.BaseEstruturada;
import entities.ParametrosEntrada;

public class AnalisesNGrama {

	public static void main(String args[]){
		
		AnalisesNGrama analise = new AnalisesNGrama();
		analise.frequenciaNGRAMA();
		analise.presencaNGRAMA();
	}

	private void presencaNGRAMA() {

		//Pega os dados do arquivo do properties
		ParametrosEntrada parametrosEntrada = new ParametrosEntrada();
		//com os parametros passados monta a base
		BaseEstruturada baseEstruturada = new BaseEstruturada(parametrosEntrada);
		
		Map<String, Integer> nGramasPresencas = baseEstruturada.getnGramaPresencaGlobal();
		
		List<String>  linhasNgramasPresenca = Util.MapToListString(nGramasPresencas);
		Arquivo.salvaArquivo(linhasNgramasPresenca, "NGramasPresencas.txt");
		
		
	}
	
	private void frequenciaNGRAMA() {

		//Pega os dados do arquivo do properties
		ParametrosEntrada parametrosEntrada = new ParametrosEntrada();
		//com os parametros passados monta a base
		BaseEstruturada baseEstruturada = new BaseEstruturada(parametrosEntrada);
		
		Map<String, Integer> nGramasFrequencias = baseEstruturada.getnGramaFrequenciaGlobal();
		
		List<String>  linhasNgramasFrequencias = Util.MapToListString(nGramasFrequencias);
		Arquivo.salvaArquivo(linhasNgramasFrequencias, "NGramasFrequencias.txt");
		
	}
	
	
}
