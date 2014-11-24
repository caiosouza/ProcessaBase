package processamento;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.Arquivo;
import util.Util;

public class ValidaCaracteres {

	private static final String NOME_ARQUIVO_LOG_CARACTERES = "log/LogCaracteres.log";
	private static final String VALIDOS = "Log Validos";
	private static final String INVALIDOS = "Log Invalidos";

	/**
	 * O arquivo a ser processado deve conter apenas as letras de a-z e espaco,
	 * ou seja, nao deve conter pontuacoes, acentos, numeros qualquer caracter
	 * diferente deve ter sido retirado previamente.
	 * 
	 * @param arquivo
	 * @param podeNumero
	 * @return
	 */
	//TODO Fazer adaptacoes para ler de properties
	public boolean validaArquivo(String nomeArquivo, boolean podeNumero) {

		String linhas = Arquivo.pegaLinhas(nomeArquivo);
		return validaLinhas(linhas, podeNumero);
	}

	public boolean validaLinhas(String linhas, boolean podeNumero) {

		boolean valido = true;		

		Map<String, Integer> caracteresInvalidos = new HashMap<String, Integer>();
		Map<String, Integer> caracteresValidos = new HashMap<String, Integer>();

		/* verifica se encontrou algum caracter invalido */
		validaCaracteres(caracteresInvalidos, caracteresValidos, linhas,
				podeNumero);

		/* verifica se a quantidade de caracteres encontrados eh igual ao tamanho do arquivo */
		valido = verificaConsistenciaNumCaracteres(linhas.length(),
				caracteresInvalidos, caracteresValidos);

		if (caracteresInvalidos.size() > 0) {
			valido = false;
		}

		/* salva em um log a lista de caracteres invalidos e a quantidade */
		//criaLogValidacao(caracteresValidos, caracteresInvalidos, nomeArquivo);
		return valido;
	}

	@SuppressWarnings("unused")
	private void criaLogValidacao( Map<String, Integer> caracteresFrequenciasValidos,
			Map<String, Integer> caracteresFrequenciasInvalidos, String nomeArquivo) {

		String novaLinhaLogCaracteres = Util.dateFormat.format(new Date());
		novaLinhaLogCaracteres += " Arquivo: " + nomeArquivo;
		
		/* formato do map para string {a=2, e=4, f=3, e=4, f=5} */
		novaLinhaLogCaracteres += " " + INVALIDOS + caracteresFrequenciasInvalidos.toString();
		novaLinhaLogCaracteres += " " + VALIDOS + caracteresFrequenciasValidos.toString();
		
		Arquivo.insereLinhas(NOME_ARQUIVO_LOG_CARACTERES, novaLinhaLogCaracteres);

	}

	private boolean verificaConsistenciaNumCaracteres(int length,
			Map<String, Integer> caracteresInvalidos,
			Map<String, Integer> caracteresValidos) {

		boolean consistente = true;
		int totalCaracteresInvalidos = 0;
		int totalCaracteresValidos = 0;

		for (Map.Entry<String, Integer> caracterFrequencia : caracteresInvalidos
				.entrySet()) {
			totalCaracteresInvalidos += caracterFrequencia.getValue();
		}
		for (Map.Entry<String, Integer> caracterFrequencia : caracteresValidos
				.entrySet()) {
			totalCaracteresValidos += caracterFrequencia.getValue();
		}

		if (length != (totalCaracteresValidos + totalCaracteresInvalidos)) {
			consistente = false;
		}

		return consistente;
	}

	private void validaCaracteres(Map<String, Integer> caracteresInvalidos,
			Map<String, Integer> caracteresValidos, String linha,
			boolean podeNumero) {

		Pattern pattern;
		if (podeNumero) {
			pattern = Pattern.compile(" |[a-z]|[0-9]");
		} else {
			pattern = Pattern.compile(" |[a-z]");
		}

		for (int i = 0; i < linha.length(); i++) {
			String caractere = "" + linha.charAt(i);
			validaCaracter(caracteresInvalidos, caracteresValidos, caractere,
					pattern);
		}
	}

	private void validaCaracter(Map<String, Integer> caracteresInvalidos,
			Map<String, Integer> caracteresValidos, String caractere,
			Pattern pattern) {

		Matcher matcher;
		matcher = pattern.matcher(caractere);
		if (matcher.matches()) {
			Util.incrementaFrequencia(caracteresValidos, caractere);
		} else {
			Util.incrementaFrequencia(caracteresInvalidos, caractere);
		}
	}
}
