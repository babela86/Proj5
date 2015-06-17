<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<html>
			<body>
			
				<div style="margin-top:20px;text-align:center">
				
					<h2>Resultado da pesquisa de noticias</h2>
					<h3>Site pesquisado: CNN (<a href="http://cnn.com">http://cnn.com</a>)</h3>
				</div>
				<xsl:for-each select="noticias/noticia">
					<div style="margin:20px">
						<table border="3" style="text-align:center">
							<tr>
								<th colspan="2" style="font-size:20px">
									<xsl:value-of select="titulo" />
								</th>
							</tr>
							<tr>
								<td style="font-style:italic">
									<b> Autor: </b>
									<xsl:value-of select="autor" />
								</td>
								<td>
									<b>Data: </b>
									<xsl:value-of select="data" />
								</td>
							</tr>
							<tr>
								<td style="font-size:15px">
									<xsl:value-of select="descricao" />
								</td>
								<td>
									<b>Categoria: </b>
									<xsl:value-of select="categoria" />
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<xsl:element name="img">
										<xsl:attribute name="src">
										<xsl:value-of select="imagem" />
									</xsl:attribute>
									</xsl:element>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<b>Vídeo: </b>
									<a>
										<xsl:attribute name="href">
											<xsl:value-of select="video" />
										</xsl:attribute>
										<xsl:value-of select="video" />
									</a>
								</td>
							</tr>
							<tr>
								<td colspan="2" style="text-align:justify;padding:20px;">
									<xsl:value-of select="corpo" />
								</td>
							</tr>
							<tr>
								<td colspan="2" style="font-style:bold">
									<b>URL da página: </b>
									<a>
										<xsl:attribute name="href">
											<xsl:value-of select="urlPagina" />
										</xsl:attribute>
										<xsl:value-of select="urlPagina" />
									</a>
								</td>
							</tr>
						</table>
					</div>
				</xsl:for-each>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>


