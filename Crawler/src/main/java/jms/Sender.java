package jms;

import java.io.IOException;
import java.util.logging.Logger;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.xml.sax.SAXException;

public class Sender {

	private static final Logger log = Logger.getLogger(Sender.class.getName());

	public boolean enviaMsg (String fileCrawler) {
		String msg = null;
		try {
			msg = XMLtoString.convertXMLFileToString(fileCrawler);
		} catch (SAXException | IOException | ParserConfigurationException | TransformerFactoryConfigurationError | TransformerException e2) {
			log.severe("A conversao de XML para String falhou -> " + e2.getMessage());
			return false;
		}
		ConnectionFactory cf = null;
		try {
			cf = InitialContext.doLookup("jms/RemoteConnectionFactory");
		} catch (NamingException e) {
			log.severe("A aquisicao do InicialContext falhou -> " + e.getMessage());
			return false;
		}
		Topic topic = null;
		try {
			topic = InitialContext.doLookup("jms/topic/MyCNN");
		} catch (NamingException e) {
			log.severe("A aquisicao do topico falhou -> " + e.getMessage());
			return false;
		}
		Connection jmsConnection = null;
		try {
			jmsConnection = cf.createConnection("user", "123");
		} catch (JMSException e) {
			log.severe("O estabelecimento da conexao falhou -> " + e.getMessage());
			return false;
		}
		Session session = null;
		try {
			session = jmsConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		} catch (JMSException e) {
			log.severe("A criacao da sessao falhou -> " + e.getMessage());
			return false;
		}
		MessageProducer sender = null;
		try {
			sender = session.createProducer(topic);
		} catch (JMSException e1) {
			log.severe("A criacao do topico falhou -> " + e1.getMessage());
			return false;
		}
		TextMessage mensagemEnviar = null;
		try {
			mensagemEnviar = session.createTextMessage(msg);
		} catch (JMSException e1) {
			log.severe("A criacao da mensagem falhou -> " + e1.getMessage());
			return false;
		}
		try {
			sender.send(mensagemEnviar);
		} catch (JMSException e) {
			log.severe("O envio da mensagem falhou -> " + e.getMessage());
			return false;
		}
		try {
			sender.close();
		} catch (JMSException e) {
			log.severe("Ocorreu o erro -> " + e.getMessage());
			return false;
		}
		try {
			session.close();
		} catch (JMSException e) {
			log.severe("Ocorreu o erro -> " + e.getMessage());
			return false;
		}
		try {
			jmsConnection.close();
		} catch (JMSException e) {
			log.severe("Ocorreu o erro -> " + e.getMessage());
			return false;
		}
		log.info("Foi enviada uma mensagem com sucesso.");
		return true;
	}
}



