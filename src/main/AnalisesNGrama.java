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

		//Pega os dados do arquivo do properties
		ParametrosEntrada parametrosEntrada = new ParametrosEntrada();
		//com os parametros passados monta a base
		BaseEstruturada baseEstruturada = new BaseEstruturada(parametrosEntrada);
				
		analise.frequenciaNGRAMA(baseEstruturada, parametrosEntrada);
		analise.presencaNGRAMA(baseEstruturada, parametrosEntrada);
	}

	private void presencaNGRAMA(BaseEstruturada baseEstruturada, ParametrosEntrada parametrosEntrada) {

		Map<String, Integer> nGramasPresencas = baseEstruturada.getnGramaPresencaGlobal();
		
		List<String>  linhasNgramasPresenca = Util.MapToListString(nGramasPresencas, parametrosEntrada.getMinGlobalFreq());
		Arquivo.salvaArquivo(linhasNgramasPresenca, parametrosEntrada.getNomeArquivoOriginal()+ "_NGramasPresencas.txt");
		
	}
	
	private void frequenciaNGRAMA(BaseEstruturada baseEstruturada, ParametrosEntrada parametrosEntrada) {
		
		Map<String, Integer> nGramasFrequencias = baseEstruturada.getnGramaFrequenciaGlobal();
		
		List<String>  linhasNgramasFrequencias = Util.MapToListString(nGramasFrequencias, parametrosEntrada.getMinGlobalFreq());
		Arquivo.salvaArquivo(linhasNgramasFrequencias, parametrosEntrada.getNomeArquivoOriginal()+ "_NGramasFrequencias.txt");
		
	}
	
	
}
