<?xml version='1.0'?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xslthl="http://xslthl.sf.net"
	exclude-result-prefixes="xslthl" version='1.0'>

    <!-- 
        Simple highlighter for HTML output. Follows the Eclipse color scheme.
    -->

	<xsl:import href="urn:docbkx:stylesheet/highlight.xsl" />

	<xsl:template match='xslthl:keyword' mode="xslthl">
		<span class="hl-keyword"><xsl:apply-templates mode="xslthl" /></span>
	</xsl:template>

	<xsl:template match='xslthl:string' mode="xslthl">
		<span class="hl-string"><xsl:apply-templates mode="xslthl" /></span>
	</xsl:template>

	<xsl:template match='xslthl:comment' mode="xslthl">
		<span class="hl-comment" ><xsl:apply-templates mode="xslthl" /></span>
	</xsl:template>

	<xsl:template match='xslthl:directive' mode="xslthl">
		<span class="hl-directive"><xsl:apply-templates mode="xslthl" /></span>
	</xsl:template>

	<xsl:template match='xslthl:tag' mode="xslthl">
		<span class="hl-tag"><xsl:apply-templates mode="xslthl" /></span>
	</xsl:template>

	<xsl:template match='xslthl:attribute' mode="xslthl">
		<span class="hl-attribute"><xsl:apply-templates mode="xslthl" /></span>
	</xsl:template>

	<xsl:template match='xslthl:value' mode="xslthl">
		<span class="hl-value"><xsl:apply-templates mode="xslthl" /></span>
	</xsl:template>

	<xsl:template match='xslthl:html' mode="xslthl">
		<span class="hl-html"><xsl:apply-templates mode="xslthl" /></span>
	</xsl:template>

	<xsl:template match='xslthl:xslt' mode="xslthl">
		<span class="hl-xslt"><xsl:apply-templates mode="xslthl" /></span>
	</xsl:template>

	<!-- Not emitted since XSLTHL 2.0 -->
	<xsl:template match='xslthl:section' mode="xslthl">
		<span class="hl-section"><xsl:apply-templates mode="xslthl" /></span>
	</xsl:template>

	<xsl:template match='xslthl:number' mode="xslthl">
		<span class="hl-number"><xsl:apply-templates mode="xslthl" /></span>
	</xsl:template>

	<xsl:template match='xslthl:annotation' mode="xslthl">
		<span class="hl-annotation"><xsl:apply-templates mode="xslthl" /></span>
	</xsl:template>

	<!-- Not sure which element will be in final XSLTHL 2.0 -->
	<xsl:template match='xslthl:doccomment|xslthl:doctype' mode="xslthl">
		<span class="hl-tag"><xsl:apply-templates mode="xslthl" /></span>
	</xsl:template>

</xsl:stylesheet>
