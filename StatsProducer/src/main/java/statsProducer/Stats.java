package statsProducer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Stats implements MessageListener {

	private static final Logger log = Logger.getLogger(Stats.class.getName());

	private TopicConnectionFactory cf;
	private Topic topic;
	private int totalNoticias = 0;
	private int noticiasValidas = 0;

	public Stats () {
		try {
			cf = InitialContext.doLookup("jms/RemoteConnectionFactory");
			topic = InitialContext.doLookup("jms/topic/MyCNN");
			log.info("Conexao ao topico realizada com sucesso.");
		} catch (NamingException e) {
			log.severe("A conexao ao topico falhou -> " + e.getMessage());
			return;
		}
	}

	public void initialize() {
		try {
			JMSContext jmsC = cf.createContext("user", "123");
			jmsC.setClientID("stats");
			JMSConsumer consumer = jmsC.createDurableConsumer(topic,"stats");
			consumer.setMessageListener(this);
			System.out.println("A recepcao de mensagens esta activa");
			System.out.println("#### Pressione enter para sair ####");
			System.in.read();
			jmsC.close();
		} catch (Exception e) {
			log.severe("Ocorreu um erro na recepcao da mensagem -> " + e.getMessage());
			return;
		}
	}

	public static void main(String[] args) {
		Stats rec = new Stats();
		rec.initialize();
	}

	public void onMessage(Message msg) {
		String msgToXML = null;
		Transformer transformer = null;
		try {
			msgToXML = ((TextMessage)msg).getText();
		} catch (JMSException e1) {
			log.severe("A conversao para XML falhou -> " + e1.getMessage());
			return;
		}
		Document file = null;
		try {
			file = StringToXML.stringToXML(msgToXML);
		} catch (ParserConfigurationException | SAXException | IOException e2) {
		} catch (Exception e2) {
			log.severe("A conversao de String para XML falhou -> " + e2.getMessage());
			return;
		}
		try {
			transformer = TransformerFactory.newInstance().newTransformer();
		} catch (TransformerConfigurationException	| TransformerFactoryConfigurationError e1) {
			log.severe("Ocorreu o erro -> " + e1.getMessage());
			return;
		}
		File ficheiro = new File(Paths.get("src/main/resources/noticiasoutput.xml").toString());
		Result output = new StreamResult(ficheiro);
		try {
			XMLvalidation.validateXMLSchema(ficheiro.getPath());
		} catch (Exception e) {
			log.severe("A validacao do XML falhou - > " + e.getMessage());
			return;
		}
		Source input = new DOMSource(file);
		try {
			transformer.transform(input, output);
		} catch (TransformerException e) {
			log.severe("A validação do XML falhou - > " + e.getMessage());
			return;
		}
		this.statsWrite(this.doStats());
		log.info("Foi recebida uma mensagem com sucesso.");
		System.out.println("A recepcao de mensagens esta activa");
		System.out.println("#### Pressione enter para sair ####");
	}

	private void statsWrite (Map <String, Integer> stats) {
		String resumoFile = "Balanço de notícias (por categoria) em " + agora() + ": \n\n";
		for (String s : stats.keySet()) {
			resumoFile = resumoFile + s + "	->	" + stats.get(s) + " notícia(s)\n";
		}
		resumoFile = resumoFile + "\n\nTotal de notícias pesquisadas:	" + totalNoticias + "\nNotícias válidas (menos de 12 horas):	" 
				+ noticiasValidas + "\nNotícias antigas (mais de 12 horas):	" + (totalNoticias-noticiasValidas) + "\n\n************************************\n\n";
		File statsFile = new File(Paths.get("src/main/resources/statsFile/stats.txt").toString());
		if (!statsFile.exists()) {
			try {
				statsFile.createNewFile();
			} catch (IOException e) {
				log.severe("Ocorreu o erro -> " + e.getMessage());
				return;
			}
		}
		FileWriter fw = null;
		try {
			fw = new FileWriter(statsFile, true);
		} catch (IOException e) {
			log.severe("Ocorreu o erro -> " + e.getMessage());
			return;
		}
		BufferedWriter bw = new BufferedWriter(fw);
		try {
			bw.write(resumoFile);
		} catch (IOException e) {
			log.severe("Ocorreu o erro -> " + e.getMessage());
			try {
				bw.close();
			} catch (IOException e1) {
				log.severe("Ocorreu o erro -> " + e.getMessage());
				return;
			}
			return;
		}
		try {
			bw.close();
		} catch (IOException e) {
			log.severe("Ocorreu o erro -> " + e.getMessage());
			return;
		}
		try {
			fw.close();
		} catch (IOException e) {
			log.severe("Ocorreu o erro -> " + e.getMessage());
			return;
		}
	}

	private Map <String, Integer> doStats() {
		File noticias = new File(Paths.get("src/main/resources/noticiasoutput.xml").toString());
		DocumentBuilder dBuilder = null;
		try {
			dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			log.severe("Ocorreu o erro - > " + e.getMessage());
			return null;
		}
		Document doc = null;
		try {
			doc = dBuilder.parse(noticias);
		} catch (SAXException e) {
			log.severe("Ocorreu o erro - > " + e.getMessage());
			return null;
		} catch (IOException e) {
			log.severe("Ocorreu o erro - > " + e.getMessage());
			return null;
		}
		String dataHora=null;
		String data=null;
		String hora=null;
		String categ=null;
		NodeList lista = doc.getElementsByTagName("noticia");
		totalNoticias = lista.getLength();
		noticiasValidas = 0;
		NodeList lista2 = null;
		HashMap <String , Integer> stats = new HashMap <String , Integer>();
		for (int i=0; i<lista.getLength();i++) {
			lista2 = lista.item(i).getChildNodes();
			for (int j=0; j<lista2.getLength(); j++) {
				if (lista2.item(j).getNodeName().equals("data")) {
					dataHora = lista2.item(j).getTextContent();
					data=dataHora.substring(0,10);
					hora=dataHora.substring(11,19);
					if (noticiaValida(data, hora)) {
						for (int h=0; h<lista2.getLength(); h++) {
							if (lista2.item(h).getNodeName().equals("categoria")) {
								categ = lista2.item(h).getTextContent();
								noticiasValidas ++;
								if (stats.containsKey(categ)) {
									stats.put(categ, stats.get(categ) + 1);
								} else {
									stats.put(categ, 1);
								}
							}
						}
					}
				}
			}
		}
		return stats;
	}

	private static boolean noticiaValida(String data, String hora) {
		boolean valida = false;
		String dataHora = data + " " + hora;
		Date publicacao = null;
		try {
			publicacao = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dataHora);
		} catch (ParseException e) {
			log.severe("Ocorreu um erro ao fazer a conversao da data -> " + e.getMessage());
			return false;
		}
		Date agora = Calendar.getInstance().getTime();
		long difMilis = agora.getTime() - publicacao.getTime();
		long difHoras = (difMilis) / 1000L / 60L / 60L;
		if (difHoras < 12) valida = true;
		return valida;
	}

	private static String agora() {
		Calendar agora = new GregorianCalendar(); 
		String timestamp = String.valueOf(agora.get(Calendar.DAY_OF_MONTH)) + "-" +
				String.valueOf((agora.get(Calendar.MONTH) + 1)) + "-" +
				String.valueOf(agora.get(Calendar.YEAR)) + " pelas " +
				String.valueOf(agora.get(Calendar.HOUR_OF_DAY))+ ":" +
				String.valueOf(agora.get(Calendar.MINUTE)) + ":" +
				String.valueOf(agora.get(Calendar.SECOND));
		return timestamp;
	}

}


