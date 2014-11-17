package processamento;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import util.Arquivo;

public class FolderesToTxtFiles {

	//TODO Fazer adaptacoes para receber parametro e para ler de properties
	public static void folderesToTxtFiles(String[] args){
		
		String local = System.getProperty("user.dir"); 
		
		List<String> allTexts = new ArrayList<String>();
		List<String> clusterGabaritoOrdem = new ArrayList<String>();
		List<String> clusterCategoria = new ArrayList<String>();
		List<String> categorias = new ArrayList<String>();
	
		
		File pastaRaiz = new File(local);
		File[] subpastas = pastaRaiz.listFiles();
		
		int clusterId = 0;
		for (File pastaAtual : subpastas) {
			
			if (pastaAtual.isDirectory()){
				//monta o relacionamento entre cluster e categoria
				clusterId = clusterId + 1;
				clusterCategoria.add(clusterId + ";"+ pastaAtual.getName());
				
				//gera a lista das categorias
				categorias.add(pastaAtual.getName());
				
				File[] arquivosPastaAtual = pastaAtual.listFiles();
				
				for (File arquivoAtual : arquivosPastaAtual) {
				
					//monta a ordem dos documentos no cluster usando a categoria como gabarito
					clusterGabaritoOrdem.add(""+ clusterId);
					
					//monta o arquivo contendo todos os documentos separados por uma linha em branco
					allTexts.add(Arquivo.contatenaLinhas(Arquivo.abreArquivo(arquivoAtual)));
					//coloca uma linha separando cada arquivo
					allTexts.add("");
					
				}
			}
			
		}
		
		Arquivo.salvaArquivo(allTexts, pastaRaiz+"_allTextTxtFile.txt");
		Arquivo.salvaArquivo(clusterGabaritoOrdem, pastaRaiz+"_clusterGabaritoOrdem.txt");
		Arquivo.salvaArquivo(clusterCategoria, pastaRaiz+"_clusterCategoria.txt");
		Arquivo.salvaArquivo(categorias, pastaRaiz+"_categorias.txt");

		
		
	}

}
