package jms;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import webcrawler.Crawler;

public class BkpSender {

	private static final Logger log = Logger.getLogger(BkpSender.class.getName());
	
	private final static Path pathFailed= Paths.get("src/main/resources/filesFailed");		
	private final static String strPathFailed= Paths.get("src/main/resources/filesFailed").toString();		
	private final static String strPathSent= Paths.get("src/main/resources/filesSent").toString();		
	
	public static void main(String[] args) throws Exception {
		List<File> ficheirosFalhados = Files.walk(pathFailed)
				.filter(Files::isRegularFile)
				.map(Path::toFile)
				.collect(Collectors.toList());
		for (File f : ficheirosFalhados) {
			String ficheiroOutput = strPathFailed + "//" + f.getName();
			enviaMsg (ficheiroOutput, 1);
		}
		String ficheiroOutput = new Crawler().executaCrawler();
		enviaMsg (ficheiroOutput, 0);
	}

	public static void enviaMsg(String ficheiroOutput, int tipo) {
		boolean ok = false;
		Sender s = new Sender();
		try {
			int nCiclos = 1;
			while ((ok==false) && (nCiclos < 10)) {
				log.info("Tentativa de envio de mensagem " + nCiclos + ".");
				ok = s.enviaMsg(ficheiroOutput);
				nCiclos ++;
				Thread.sleep(500);
			}
			if (ok==false) {
				log.severe("O envio de mensagem falhou.");
				if (tipo==0) {
					moveFile(ficheiroOutput, strPathFailed);
					log.info("Mensagem nao enviada arquivada");
				} else {
					log.info("Mensagem nao enviada arquivada");
				}
			} else {
				moveFile(ficheiroOutput, strPathSent);
				log.info("Mensagem enviada");
			}
		} catch (Exception e) {
			log.severe("Ocorreu o erro -> " + e.getMessage());
		}
	}

	private static boolean moveFile(String ficheiroOutput, String folderOutput) {
		try{
			File file = new File(ficheiroOutput);
			String novoNome = "//noticias_" + agora() + ".xml";
			File origem = new File (ficheiroOutput);
			File destino = new File (folderOutput + novoNome);
			Files.copy(origem.toPath(), destino.toPath());
			file.delete();
			return true;
		} catch(Exception e) {
			log.severe("Ocorreu o erro -> " + e.getMessage());
		}
		return false;
	}

	private static String agora() {
		Calendar agora = new GregorianCalendar(); 
		String timestamp = String.valueOf(agora.get(Calendar.YEAR)) + "_" +
				String.valueOf((agora.get(Calendar.MONTH) + 1)) + "_" +
				String.valueOf(agora.get(Calendar.DAY_OF_MONTH)) + "_" +
				String.valueOf(agora.get(Calendar.HOUR_OF_DAY))+ "_" +
				String.valueOf(agora.get(Calendar.MINUTE)) + "_" +
				String.valueOf(agora.get(Calendar.SECOND)) + "_" +
				String.valueOf(agora.get(Calendar.MILLISECOND));
		return timestamp;
	}

}
