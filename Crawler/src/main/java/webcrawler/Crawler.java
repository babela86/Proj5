package webcrawler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.JAXBException;

import marshall.JAXBHandler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import classes.Noticia;
import classes.Noticias;
public class Crawler {

	public static void main(String[] args) {

		int cont1=0,cont2=0, cont3=0;//RETIRAR

		Document paginaPrincipal = null;	
		Document paginaInterior = null;
		Element link = null;

		Noticias noticias = null;
		Noticia noticia = null;

		final String urlGeral = "http://edition.cnn.com";

		String urlPagina = null;
		String categoria = null;
		String data = null;
		String autor = null;
		String titulo = null;
		String descricao = null;
		String corpo = null;
		String imagem = null;
		String video = null;

		ArrayList<String> urlsAlvo = new ArrayList<String>();
		String url = "";

		//descobre LINKS
		try {
			paginaPrincipal = Jsoup.connect(urlGeral).get();
		} catch (IOException e) {
			e.getMessage();
		}
		Elements urlsElement = paginaPrincipal.getElementsByClass("cd__headline");
		for (Element urlElement : urlsElement) {
			link = urlElement.getElementsByTag("a").first();
			if ((link.attr("href").contains("/asia/")
					|| link.attr("href").contains("/us/")
					|| link.attr("href").contains("/china/")

					|| link.attr("href").contains("/world/")

					|| link.attr("href").contains("/europe/")
					|| link.attr("href").contains("/middleeast/")
					|| link.attr("href").contains("/africa/") || link.attr(
							"href").contains("/americas/"))
							&& !link.attr("href").contains("/videos/")) {
				url = link.attr("href");
				if (!urlsAlvo.contains(url)) {
					urlsAlvo.add(url);
					System.out.println(url); //RETIRAR
					cont1++;  //RETIRAR
				}
			}
		}

		//percorre LINKS
		for (String urlAlvo : urlsAlvo) {
			try {
				paginaInterior = Jsoup.connect(urlGeral + urlAlvo).get();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
			noticias = new Noticias();
			urlPagina = urlGeral + urlAlvo;
			corpo = paginaInterior.getElementsByClass("zn-body__paragraph").text();
			for(Element meta : paginaInterior.select("meta")) {
				String key = meta.attr("itemprop");
				String value = meta.attr("content");
				noticia = new Noticia();
				switch (key) {
				case "articleSection":
					categoria = value;
					break;	
				case "dateModified":
					data= value;
					break;	
				case "author":
					autor = value;
					break;	
				case "headline":
					titulo = value;
					break;
				case "description":
					descricao = value;
					break;	
				case "image":
					imagem = value;
					break;	
				case "url":
					if (value.contains(".cnn")) {
						video = value;
						cont3++;//RETIRAR
					}
					break;	
				}
			}
			noticia.setCategoria(categoria);
			noticia.setDescricao(descricao);
			noticia.setUrlPagina(urlPagina);
			noticia.setTitulo(titulo);
			noticia.setData(data);
			noticia.setAutor(autor);
			noticia.setCorpo(corpo);
			noticia.setImagem(imagem);
			noticia.setVideo(video);
			cont2++;//RETIRAR
			noticias.getNoticia().add(noticia);
			for (Noticia n:noticias.getNoticia()) {//RETIRAR
				System.out.println(n.toString());//RETIRAR
			}//RETIRAR
		}
		System.out.println("Total links -> " + cont1);//RETIRAR
		System.out.println("Total noticias -> " + cont2);//RETIRAR
		System.out.println(cont3);//RETIRAR
		new JAXBHandler();
		try {
			System.out.println(noticias.getNoticia().size());//RETIRAR
			JAXBHandler.marshal(new Noticias().getNoticia(), new File ("C:\\textexml\\teste.xml"));
			System.out.println("Marshall ok");//RETIRAR
		} catch (IOException | JAXBException e) {
			System.out.println(e.getMessage());
		}	
	}	
}