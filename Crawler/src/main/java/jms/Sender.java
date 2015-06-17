package jms;

import java.util.logging.Logger;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;

import webcrawler.Crawler;

public class Sender {

	private static final Logger log = Logger.getLogger(Sender.class.getName());
	
	public static void main(String[] args)  {

		String fileCrawler = new Crawler().executaCrawler();
		String msg = XMLtoString.convertXMLFileToString(fileCrawler);
		try {
			ConnectionFactory cf = InitialContext.doLookup("jms/RemoteConnectionFactory");
			Topic topic = InitialContext.doLookup("jms/topic/MyCNN");
			Connection jmsConnection = cf.createConnection("user", "123");
			Session session = jmsConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageProducer sender = session.createProducer(topic);
			TextMessage mensagemEnviar = session.createTextMessage(msg);
			sender.send(mensagemEnviar);
			sender.close();
			session.close();
			jmsConnection.close();
			log.info("Foi enviada uma mensagem com sucesso.");
		} catch (Exception e) {
			log.severe("O envio de uma mensagem falhou -> " + e.getMessage());
		}
	}

}
