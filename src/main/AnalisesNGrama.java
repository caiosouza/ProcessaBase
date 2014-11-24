package main;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import util.Arquivo;
import util.Util;
import entities.BaseEstruturada;
import entities.ParametrosEntrada;

public class AnalisesNGrama {

	public static void main(String args[]){
		
		Date inicio = new Date();
		AnalisesNGrama analise = new AnalisesNGrama();

		//Pega os dados do arquivo do properties
		ParametrosEntrada parametrosEntrada = new ParametrosEntrada();
		//com os parametros passados monta a base
		BaseEstruturada baseEstruturada = new BaseEstruturada(parametrosEntrada);
		
		//gera log
		List<String> arquivosGerados = new ArrayList<String>();
		arquivosGerados.add(analise.frequenciaNGRAMA(baseEstruturada, parametrosEntrada));
		arquivosGerados.add(analise.presencaNGRAMA(baseEstruturada, parametrosEntrada));
		List<String> linhasParametrosEntrada = Arquivo.abreArquivo(parametrosEntrada.getNomeProperties());
		Date fim = new Date();
		Util.geraLog(inicio, linhasParametrosEntrada, arquivosGerados, fim, parametrosEntrada.getNomeArquivoLog());
	}
	

	private String presencaNGRAMA(BaseEstruturada baseEstruturada, ParametrosEntrada parametrosEntrada) {

		Map<String, Integer> nGramasPresencas = baseEstruturada.getnGramaPresencaGlobal();
		
		List<String>  linhasNgramasPresenca = Util.MapToListString(nGramasPresencas, parametrosEntrada.getMinGlobalFreq());
		//TODO REMOVER A EXTENSAO .txt do final do arquivo
		String nomeArquivoSaida = "outPut" + File.separatorChar+  parametrosEntrada.getNomeArquivoBaseTxt()+ "_NGramasPresencas.txt";
		Arquivo.salvaArquivo(linhasNgramasPresenca,nomeArquivoSaida);
		return nomeArquivoSaida;
	}
	
	private String frequenciaNGRAMA(BaseEstruturada baseEstruturada, ParametrosEntrada parametrosEntrada) {
		
		Map<String, Integer> nGramasFrequencias = baseEstruturada.getnGramaFrequenciaGlobal();
		
		List<String>  linhasNgramasFrequencias = Util.MapToListString(nGramasFrequencias, parametrosEntrada.getMinGlobalFreq());
		//TODO REMOVER A EXTENSAO .txt do final do arquivo
		String nomeArquivoSaida = "outPut" + File.separatorChar+  parametrosEntrada.getNomeArquivoBaseTxt()+ "_NGramasFrequencias.txt";
		Arquivo.salvaArquivo(linhasNgramasFrequencias, nomeArquivoSaida);
		return nomeArquivoSaida;
		
	}
	
	
}
