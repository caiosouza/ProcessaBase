package processamento;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import util.Arquivo;
import util.Util;

public class FileTxtToFolder {

	private static final String BASE_TXT = "inputBaseFileName";
	private static final String PARAMETERS_PROPERTIES = "parameters.properties";


	/**
	 * 
	 * @param args
	 */
	//TODO Fazer adaptacoes para receber parametro e para ler de properties
	public static void main(String[] args) {
		
		FileTxtToFolder fileTxtToFolder= new FileTxtToFolder();
		String baseTxt;
		String categsIdsTxt = null;
		 
		if (args.length > 2){
			baseTxt = args[1];
		} else {
//			baseTxt = System.getProperty("user.dir"); 
			File propertiesFile = new File(PARAMETERS_PROPERTIES);
			System.out.println(propertiesFile.getAbsolutePath());
			baseTxt = getLocalFromProperties(propertiesFile);
		}
		fileTxtToFolder.run(baseTxt, categsIdsTxt);
	}
	
	public static String getLocalFromProperties(File propertiesFile){
		return Util.readFromProperties(BASE_TXT, propertiesFile);		
	}
	
	public void run(String baseTxt, String categsTxt) {
	
	  //TODO Codigo atual ta considerando que cada txt vai gerar uma pasta diferente, como se tivesse um txt para cada categoria... 
		//trocar o codigo

				
		File pastaRaiz = new File(baseTxt);
		File[] txtFiles = pastaRaiz.listFiles();
		int idFile = 0;
		for (File actualFile : txtFiles) {
			
			List<String> actualFileLines = new ArrayList<String>();
			
			if (actualFile.getName().endsWith(".txt")){
				
				actualFileLines = Arquivo.abreArquivo(actualFile);
				for (String actualLine : actualFileLines) {
					idFile++;
					Arquivo.salvaArquivo(actualLine, actualFile.getName().replace(".txt", "/")+idFile+".txt");	
				}
			}
		}
		
	}
}
