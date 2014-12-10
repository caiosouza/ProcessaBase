package main;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import util.Arquivo;
import util.Util;
import entities.ParametrosEntrada;

public class AmostraArquivo {

	public static void main(String[] args) {
		AmostraArquivo amostraArquivo = new AmostraArquivo();
		amostraArquivo.kfold(5);
	}

	private void kfold(int kfold) {
		Date dataInicio = new Date();
		
		ParametrosEntrada parametrosEntrada = new ParametrosEntrada();
		
		List<String> documentos = Arquivo.abreArquivo(parametrosEntrada.getNomeArquivoBaseTxt());
		List<String> categorias = Arquivo.abreArquivo(parametrosEntrada.getNomeArquivoCategoriasDocs());
		
		
		int inicio;
		int fim;
		Double sizeFold = 1.0 * documentos.size()/kfold;
		
		List<String> arquivosGerados = new ArrayList<String>();
		
		for (int i = 0; i < kfold; i++) {
			List<String> documentosAmostrados = new ArrayList<String>();
			List<String> categoriasAmostradas = new ArrayList<String>();
			
			inicio = (int) (i * Math.round(sizeFold));
			if (i == (kfold - 1)){
				fim = documentos.size();
			} else {
				fim = (int) ((i+1) * Math.round(sizeFold));
			}
			
			
			for (int j = inicio; j < fim; j++) {
				documentosAmostrados.add(documentos.get(j));
				categoriasAmostradas.add(categorias.get(j));
			}
			
			String arquivoBaseFold = i+ "_fold_"+parametrosEntrada.getNomeArquivoBaseTxt();
			String arquivoCategFold = i+ "_fold_"+parametrosEntrada.getNomeArquivoCategoriasDocs();
			Arquivo.salvaArquivo(documentosAmostrados, arquivoBaseFold);
			Arquivo.salvaArquivo(categoriasAmostradas, arquivoCategFold);
			arquivosGerados.add(arquivoBaseFold);
			arquivosGerados.add(arquivoCategFold);			
		}

		
		//gera log
		Date dataFim = new Date();
		List<String> linhasParametrosEntrada = Arquivo.abreArquivo(parametrosEntrada.getNomeProperties());
		Util.geraLog(dataInicio, linhasParametrosEntrada, arquivosGerados, dataFim, parametrosEntrada.getNomeArquivoLog());
	
		
	}

	
}
