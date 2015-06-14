<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<html>
			<body>
				<h2>Noticias</h2>
				<table border="1">
				
					<xsl:apply-templates select="noticias/noticia" />

				</table>
			</body>
		</html>
	</xsl:template>
<!-- 	<xsl:template match="noticia[1]"> -->
<!-- 		<tr style="color:blue"> -->
<!-- 			<xsl:apply-templates select="*" mode="header" /> -->
<!-- 		</tr> -->
<!-- 		<xsl:call-template name="standardRow" /> -->
<!-- 	</xsl:template> -->

	<xsl:template match="noticia" name="standardRow">
		<tr >
			<xsl:apply-templates select="*" />

		</tr>
	</xsl:template>

	<xsl:template match="noticia/*" mode="header">
		<th>
			<xsl:value-of select="name()" />
		</th>
	</xsl:template>

	<xsl:template match="noticia/*">
		<td>
			<xsl:apply-templates select="node()" />

		</td>
	</xsl:template>


</xsl:stylesheet>





<!-- <table border="1"> -->
<!-- <tr bgcolor="#9acd32"> -->
<!-- <th>Título</th> -->
<!-- <th>Data</th> -->
<!-- <th>Autor</th> -->
<!-- <th>Corpo</th> -->
<!-- <th>Url</th> -->
<!-- <th>Descrição</th> -->
<!-- <th>Imagem</th> -->
<!-- <th>Video</th> -->
<!-- <th>Categoria</th> -->
<!-- </tr> -->
<!-- <xsl:for-each select="noticias/noticia"> -->
<!-- <tr> -->
<!-- <td><xsl:value-of select="titulo" /></td> -->
<!-- <td><xsl:value-of select="data" /></td> -->
<!-- <td><xsl:value-of select="autor" /></td> -->
<!-- <td><xsl:value-of select="corpo" /></td> -->
<!-- <td><xsl:value-of select="urlPagina" /></td> -->
<!-- <td><xsl:value-of select="descricao" /></td> -->
<!-- <td> -->
<!-- <xsl:element name="img"> -->
<!-- <xsl:attribute name="src"> -->
<!-- <xsl:value-of select="imagem" /> -->
<!-- </xsl:attribute> -->
<!-- </xsl:element> -->
<!-- </td> -->
<!-- <td><xsl:value-of select="video" /></td> -->
<!-- <td><xsl:value-of select="categoria" /></td> -->
<!-- </tr> -->
<!-- </xsl:for-each> -->
<!-- </table> -->
<!-- </body> -->
<!-- </html> -->
<!-- </xsl:template> -->
<!-- </xsl:stylesheet> -->


