package jms;

import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import webcrawler.Crawler;
import auxiliar.XMLtoString;

public class Sender {

	public static void main(String[] args) throws NamingException, JMSException {

		// final String message= criar o ficheiro do crawler , ja em string
		// chamar o XMLtoString apartir do crawler
		String ficheiro = new Crawler().executaCrawler();
		new XMLtoString();
		String msg = XMLtoString.convertXMLFileToString(ficheiro);
		System.out.println(msg);
		
		
		
		Properties propriedades = new Properties();
		propriedades.setProperty("java.naming.factory.initial",	"org.jboss.naming.remote.client.InitialContextFactory");
		propriedades.setProperty("java.naming.provider.url", "http-remoting://127.0.0.1:9001");
		propriedades.setProperty("java.naming.security.principal", "joao");
		propriedades.setProperty("java.naming.security.credentials", "br1o+sa*");

		InitialContext ic = new InitialContext(propriedades);

		ConnectionFactory cf = (ConnectionFactory) ic.lookup("jms/RemoteConnectionFactory");
		Topic topic = (Topic) ic.lookup("jms/queue/PlayQueue");
		Connection jmsConnection = cf.createConnection("joao", "br1o+sa*");
		Session session = jmsConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		MessageProducer sender = session.createProducer(topic);
		TextMessage mensagemEnviar = session.createTextMessage(msg);
		sender.send(mensagemEnviar);
		System.out.println("Mensagem Enviada!");
		sender.close();
		session.close();
		jmsConnection.close();

	}

}
