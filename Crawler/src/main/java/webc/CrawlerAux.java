package webc;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawlerAux {
	public static void main(String[] args) throws IOException {

		Document doc = Jsoup.connect("http://edition.cnn.com/").get();
		/*
		//titulos
		Elements titulos = doc.select("h3");
		for (Element titulo : titulos) {
			System.out.println(titulo.getElementsByClass("cd__headline-text").text());
		}
		*/
		//url
		Elements urls = doc.select("h3");
		for (Element url : urls) {
			System.out.println(url.getElementsByTag("a").);
		}
	}
}
