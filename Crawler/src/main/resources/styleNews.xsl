<?xml version="1.0" encoding="UTF-8"?>

<html xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xsl:version="1.0">
	"

	<head>
		<title>News</title>
	</head>
	<body>
		<h2 style="color:blue;">
			News in Catalog
		</h2>

		<table border="1">
			<tr>
				<td>
					<p sytle="color:blue;font-size:30px;font-style:bolder;">Title</p>
				</td>
				<td>
					<p sytle="color:blue;font-size:30px;font-style:bolder;">Data</p>
				</td>
				<td>
					<p sytle="color:blue;font-size:30px;font-style:bolder;">Author</p>
				</td>
				<td>
					<p sytle="color:blue;font-size:30px;font-style:bolder;">Body</p>
				</td>

			</tr>
			<xsl:for-each select="//noticia">
				<tr>
					<td style="margin-bottom:1em;font-size:10pt;">
						<xsl:value-of select="titulo" />
					</td>
					<td style="margin-bottom:1em;font-size:10pt;color:red">
						<xsl:value-of select="data" />
					</td>
					<td style="margin-bottom:1em;font-size:10pt;color:green">
						<xsl:value-of select="autor" />
					</td>
					<td style="margin-bottom:1em;font-size:10pt;color:green">
						<xsl:value-of select="corpo" />
					</td>

				</tr>
			</xsl:for-each>
		</table>
	</body>
</html>