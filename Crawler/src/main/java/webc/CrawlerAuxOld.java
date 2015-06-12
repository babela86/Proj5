package webc;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawlerAuxOld {

	public static void main(String[] args) throws IOException {

		Document doc = Jsoup.connect("http://edition.cnn.com/").get();



		////****************************************Pesquisa de Imagens*****************************
		Elements media = doc.select("[src]");
		for (Element src : media) {
			if (src.tagName().equals("img"))
				print(" * %s: <%s>  ",
						src.tagName(), src.attr("abs:src"));

		}

		////*********************************Pesquisa por Titulos***********************************	   


		Elements resumos = doc.select("h3"); // Noticias
		for (Element resumo : resumos) {

			System.out.println("Noticias disponiveis: "+resumo.text());
		}



		//*******************************Horas*****************************************************


		doc = Jsoup.connect("http://edition.cnn.com/2015/06/11/middleeast/iraqi-baiji-mazraa/index.html").get();
		Element horas=doc.getElementsByClass("update-time").first();

		System.out.println("Hora da Noticia: "+horas.text());




		//	 *******************************Autor********************************
		doc = Jsoup.connect("http://edition.cnn.com/2015/06/11/middleeast/iraqi-baiji-mazraa/index.html").get();

		Element auto=doc.getElementsByClass("metadata__byline__author").first();
		System.out.println("autor da Noticia: "+auto.text());
	}



	// ******************************Descrição************************************



	//********************************URL**********************************************
	//Métodos Auxiliares


	//Metodo para imprimir as mensagens
	private static void print(String msg, Object... args) {
		System.out.println(String.format(msg, args));
	}
}
