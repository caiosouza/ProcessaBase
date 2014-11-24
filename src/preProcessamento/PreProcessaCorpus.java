package preProcessamento;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import preProcessaTexto.PorterStemmer;
import preProcessaTexto.StopWords;
import util.Arquivo;
import util.Util;

public class PreProcessaCorpus {

	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		
		PreProcessaCorpus preProcessaCorpus = new PreProcessaCorpus();
		preProcessaCorpus.exec(args);
		
	}
	
	public void exec(String[] args){
		
		String nomeDiretorio;
		if (args.length > 0){
			nomeDiretorio = args[0];
		}
		else {
			System.out.println("Entre com o nome do Diretorio a ser preprocessado");
			@SuppressWarnings("resource")
			Scanner scanner = new Scanner(System.in);
			nomeDiretorio = scanner.next();
		}
		
		File diretorio = new File(nomeDiretorio);
		Boolean removeStopWords = true;
		Boolean reduzStemme = true;
		Boolean podeNumero = false;
		String stopListNameFile = "StopList.txt";
		preProcessaDiretorio(diretorio, removeStopWords, reduzStemme, stopListNameFile, podeNumero);
		
	}
	
	private void preProcessaDiretorio(File diretorio, Boolean removeStopWords, Boolean reduzStemme, String stopListNameFile, Boolean podeNumero) {

		try{
			File[] files = diretorio.listFiles();
			for (int i = 0; i < files.length; i++) {
				File f = files[i];
				if (f.isDirectory()) {
					preProcessaDiretorio(f, removeStopWords, reduzStemme, stopListNameFile, podeNumero);
				}
				else {
					List<String> linhasPreProcessadas = preProcessaArquivo(f, removeStopWords, reduzStemme, stopListNameFile, podeNumero);
					//salva o documento preprocessado na nova pasta
					String nomeDiretorioSaida = f.getAbsolutePath().replace("Original", "preProssed");
					Arquivo.salvaArquivo(linhasPreProcessadas, nomeDiretorioSaida);
				}
			}
		} catch (Exception e) {
			System.out.println("[ERROR] Erro ao preprocessar diretorio: " + diretorio.getAbsoluteFile());
			System.exit(-1);
		}
	}

	public List<String> preProcessaArquivo(File arquivo, Boolean removeStopWords, Boolean reduzStemme, String stopListNameFile, Boolean podeNumero) {
		
		List<String> linhasOriginais = new ArrayList<String>();
		List<String> linhasPreProcessadas = new ArrayList<String>();
		
		try {
			linhasOriginais = Arquivo.abreArquivo(arquivo.getAbsolutePath());
		} catch (Exception e) {
			System.out.println("[ERROR] Erro ao ler arquivo: " + arquivo);
			System.exit(-1);
		}
		
		for (String linha : linhasOriginais) {
			linhasPreProcessadas.add(preProcessaLinha(linha, removeStopWords, reduzStemme, stopListNameFile, podeNumero, null,0,1000));	
		}
		 
		return linhasPreProcessadas;
		
	}

	public String preProcessaLinha(String linha, Boolean removeStopWords, Boolean reduzStemme, 
			String stopListNameFile, Boolean podeNumero, Set<String> stopList, int minSizeWord, int maxSizeWord) {
		
		linha = linha.toLowerCase();
		linha = Util.limpacaracteres(linha);
		Pattern pattern;
		if (podeNumero) {
			pattern = Pattern.compile(" |[a-z]|[0-9]");
		} else {
			pattern = Pattern.compile(" |[a-z]");
		}
		
		String linhatemp = "";
		Matcher matcher;
		
		for (int i = 0; i < linha.length(); i++) {
			String caractere = "" + linha.charAt(i);
			matcher = pattern.matcher(caractere);
			if (matcher.matches()) {
				linhatemp =  linhatemp + linha.charAt(i);
			}
		}
		linha = linhatemp;
		
		String outPutText = "";
		String [] tokens = linha.split(" ");
		StopWords stopwords = new StopWords();
		PorterStemmer stemmer = new PorterStemmer();
		Boolean remove;
		
		for (String word : tokens) {
			remove = false;
			if (word.trim().length() > 0 ){
				if(removeStopWords && stopwords.isStopWord(stopList, word)){
					remove = true;
				} else	if (reduzStemme){
					word = stemmer.stem(word);
				}
				if ((word.length() < minSizeWord) || (word.length() > maxSizeWord)){
					remove = true;
				}
				
				if(!remove){
					outPutText = outPutText + " "+ word;
				}
			}
		}
		
		/*remove o primeiro caractere*/
		if (outPutText.startsWith(" ")) {
			outPutText = outPutText.substring(1);
		}
		return outPutText; 
	}

}
