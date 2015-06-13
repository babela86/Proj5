package htmlReceiver;

import java.io.File;
import java.io.StringReader;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class Receiver {

	public static void main(String[] args) throws Exception {

		Properties propriedades = new Properties();
		propriedades.setProperty("java.naming.factory.initial",
				"org.jboss.naming.remote.client.InitialContextFactory");
		propriedades.setProperty("java.naming.provider.url",
				"http-remoting://127.0.0.1:9001");
		propriedades.setProperty("java.naming.security.principal", "joao");
		propriedades.setProperty("java.naming.security.credentials", "123");

		InitialContext ic = new InitialContext(propriedades);

		ConnectionFactory cf = (ConnectionFactory) ic
				.lookup("jms/RemoteConnectionFactory");
		Topic topic = (Topic) ic.lookup("jms/queue/PlayQueue");
		Connection connection = cf.createConnection("user", "123");
		connection.setClientID("joao2");

		Session session = connection.createSession(false,
				Session.AUTO_ACKNOWLEDGE);
		MessageConsumer receiver = session.createDurableConsumer(topic, "user");

		connection.start();

		TextMessage mesage = (TextMessage) receiver.receive();
		String msgToXML = mesage.getText();
		Document file = stringToXML(msgToXML);

		Transformer transformer = TransformerFactory.newInstance()
				.newTransformer();
		Result output = new StreamResult(new File("noticiasoutput.xml"));
		Source input = new DOMSource(file);
		transformer.transform(input, output);
		// cria o ficheiro final xml para o htmlcreator..na pasta target
		// falta chamar a classe que converte de xml para html

		receiver.close();
		session.close();
		connection.close();
		System.out.println("Mensagem Recebida :)");

	}

	public static Document stringToXML(String xml) throws Exception {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(xml));
		return builder.parse(is);

	}
}
