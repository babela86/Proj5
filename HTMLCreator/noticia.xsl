<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<html>
			<body>
				<h2>Noticias</h2>
				<table border="3">
					<tr bgcolor="grey">
						<th>Título</th>
						<th>Data</th>
						<th>Autor</th>
						<th>Corpo</th>
						<th>Url</th>
						<th>Descrição</th>
						<th>Imagem</th>
						<th>Video</th>
						<th>Categoria</th>
					</tr>
					<xsl:for-each select="noticias/noticia">
						<tr>
							<td><xsl:value-of select="titulo" /></td>
							<td><xsl:value-of select="data" /></td>
							<td><xsl:value-of select="autor" /></td>
							<td><xsl:value-of select="corpo" /></td>
							<td><xsl:value-of select="urlPagina" /></td>
							<td><xsl:value-of select="descricao" /></td>
							<td>
								<xsl:element name="img">
									<xsl:attribute name="src">
										<xsl:value-of select="imagem" />
									</xsl:attribute>
								</xsl:element>
							</td>
							<td><xsl:value-of select="video" /></td>
							<td><xsl:value-of select="categoria" /></td>
						</tr>
					</xsl:for-each>
				</table>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>


