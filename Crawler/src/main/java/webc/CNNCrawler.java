package webc;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.DecimalFormat;

import javax.xml.bind.JAXBException;

import marshall.JAXBHandler;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import classes.Noticia;
import classes.Noticias;

public class CNNCrawler {

	public static void main(String[] args) throws IOException, SocketTimeoutException {
		final String cnnURL = "http://edition.cnn.com";
		final String cnnPrefix = "http://edition.cnn.com";
		Connection connection = Jsoup.connect(cnnURL);
		Document html = connection.get();
		Elements headlines = html.select("[data-analytics=News%20+%20buzz_list-xs_article_] > a[href]");
		Noticias newsAgg = new Noticias();
		int count = 1;
		System.out.println("A carregar not�cias...\n0,00%");
		for (Element e:headlines) {
			// Saltar se não for notícia (link começar por "2015/06/08"
			if (e.attr("href").startsWith("/2015")) {
				if (count > 100) break;
				Connection c = Jsoup.connect(cnnPrefix + e.attr("href")).timeout(0);
				Document newsHTML = c.get();
				Noticia n = new Noticia();
				n.setTitulo(e.text());
				n.setData("2015-01-01");
				n.setUrl(cnnPrefix + e.attr("href"));
				Elements newsEl = newsHTML.select("p.zn-body__paragraph");
				String body = "";
				for (Element el:newsEl) {
					body += el.text();
				}
				n.setCorpo(body);
				newsAgg.getNoticia().add(n);
			}
			int tamanhototal = headlines.size();
			double p = (((double) count/(double) tamanhototal)*100);
			DecimalFormat df = new DecimalFormat("##0.00");
			System.out.println(df.format(p)+"%");
			count++;
		}

		try {
			JAXBHandler.marshal(newsAgg.getNoticia(), new File("news.xml"));
			for (Noticia n:newsAgg.getNoticia()) {
				System.out.println(n.getTitulo());
			}
		} catch (JAXBException e1) {
			e1.printStackTrace();
		}


	}

}
