package statsProducer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class Stats implements MessageListener {

	private static final Logger log = Logger.getLogger(Stats.class.getName());

	private TopicConnectionFactory cf;
	private Topic topic;

	public Stats () {
		try {
			cf = InitialContext.doLookup("jms/RemoteConnectionFactory");
			topic = InitialContext.doLookup("jms/topic/MyCNN");
			log.info("Conexão ao tópico realizada com sucesso.");
		} catch (NamingException e) {
			log.severe("A conexão ao tópico falhou -> " + e.getMessage());
		}
	}

	public void initialize() {
		try {
			JMSContext jmsC = cf.createContext("user", "123");
			jmsC.setClientID("stats");
			JMSConsumer consumer = jmsC.createDurableConsumer(topic,"stats");
			consumer.setMessageListener(this);
			System.in.read();
			jmsC.close();
		} catch (Exception e) {
			log.severe("Ocorreu um erro na recepção da mensagem -> " + e.getMessage());
		}
	}

	public static void main(String[] args) {
		Stats rec = new Stats();
		rec.initialize();
	}

	public void onMessage(Message msg) {
		try {
			String msgToXML = ((TextMessage)msg).getText();
			Document file = StringToXML.stringToXML(msgToXML);
			Transformer transformer  = TransformerFactory.newInstance().newTransformer();
			File ficheiro = new File("src//main//resources//noticiasoutput.xml");
			Result output = new StreamResult(ficheiro);
			try {
				XMLvalidation.validateXMLSchema(ficheiro.getPath());
			} catch (Exception e) {
				log.severe("A validação do XML falhou - > " + e.getMessage());
			}
			Source input = new DOMSource(file);
			transformer.transform(input, output);
			this.statsWrite(this.doStats());
			log.info("Foi recebida uma mensagem com sucesso.");
			System.out.println("A recepção de mensagens está activa");
			System.out.println("#### Pressione enter para sair ####");
		} catch (Exception e) {
			log.severe("A recepção da mensagem falhou -> " + e.getMessage());
		}
	}

	private void statsWrite (Map <String, Integer> stats) {
		String resumoFile = "Balanço de notícias (por categoria): \n\n";
		for (String s : stats.keySet()) {
			resumoFile = resumoFile + s + " -> " + stats.get(s) + " notícia(s)\n";
		}
		resumoFile = resumoFile + "\n\n************************************\n\n";
		try {
			File statsFile = new File("src//main//resources//stats.txt");
			if (!statsFile.exists()) {
				statsFile.createNewFile();
			}
			FileWriter fw = new FileWriter(statsFile, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(resumoFile);
			bw.close();
			fw.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	private Map <String, Integer> doStats() {
		try {
			File noticias = new File("src//main//resources//noticiasoutput.xml");
			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = dBuilder.parse(noticias);
			String dataHora=null;
			String data=null;
			String hora=null;
			String categ=null;
			NodeList lista = doc.getElementsByTagName("noticia");
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
		} catch (Exception e) {
			return null;
		}
	}

	private static boolean noticiaValida(String data, String hora) throws ParseException {
		boolean valida = false;
		String dataHora = data + " " + hora;
		Date publicacao = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dataHora);
		Date agora = Calendar.getInstance().getTime();
		long difMilis = agora.getTime() - publicacao.getTime();
		long difHoras = (difMilis) / 1000L / 60L / 60L;
		if (difHoras < 12) valida = true;
		return valida;
	}

}


