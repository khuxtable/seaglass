<?xml version="1.0" encoding="utf-8"?>
	<!-- 
    This is the XSL HTML configuration file for the Spring
    Reference Documentation.
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:d="http://docbook.org/ns/docbook"
	xmlns:ng="http://docbook.org/docbook-ng" xmlns:db="http://docbook.org/ns/docbook" xmlns:exsl="http://exslt.org/common"
	exclude-result-prefixes="db ng exsl d" version='1.0'>

	<xsl:import href="urn:docbkx:stylesheet" />
	<xsl:import href="highlight.xsl" />
	<xsl:import href="titlepage.xsl" />

	<!--
		################################################### HTML Settings
		###################################################
	-->

	<!-- These extensions are required for table printing and other stuff -->
	<xsl:param name="tablecolumns.extension">
		0
	</xsl:param>
	<xsl:param name="graphicsize.extension">
		0
	</xsl:param>
	<xsl:param name="ignore.image.scaling">
		1
	</xsl:param>

	<!--
		################################################### Table Of Contents
		###################################################
	-->

	<!-- Generate the TOCs for named components only -->
	<xsl:param name="generate.toc">
		book toc
	</xsl:param>

	<!-- Show only Sections up to level 3 in the TOCs -->
	<xsl:param name="toc.section.depth">
		3
	</xsl:param>

	<!--
		################################################### Labels
		###################################################
	-->

	<!-- Label Chapters and Sections (numbering) -->
	<xsl:param name="section.label.includes.component.label" select="1" />

	<!--
		################################################### Callouts
		###################################################
	-->

	<!-- Use images for callouts instead of (1) (2) (3) -->
	<xsl:param name="callout.graphics">
		1
	</xsl:param>

	<!-- Place callout marks at this column in annotated areas -->
	<xsl:param name="callout.defaultcolumn">
		90
	</xsl:param>

	<!--
		################################################### Admonitions
		###################################################
	-->

	<!-- Use nice graphics for admonitions -->
	<xsl:param name="admon.graphics">
		1
	</xsl:param>
	<xsl:param name="admon.graphics.path">
		images/admons/
	</xsl:param>
	<!--
		################################################### Misc
		###################################################
	-->
	<!-- Placement of titles -->
	<xsl:param name="formal.title.placement">
		figure after
		example before
		equation before
		table before
		procedure before
	</xsl:param>
	<xsl:template match="author" mode="titlepage.mode">
		<xsl:if test="name(preceding-sibling::*[1]) = 'author'">
			<xsl:text>, </xsl:text>
		</xsl:if>
		<span class="{name(.)}">
			<xsl:call-template name="person.name" />
			(
			<xsl:value-of select="affiliation" />
			)
			<xsl:apply-templates mode="titlepage.mode" select="./contrib" />
		</span>
	</xsl:template>
	<xsl:template match="authorgroup" mode="titlepage.mode">
		<div class="{name(.)}">
			<h2>Authors</h2>
			<p />
			<xsl:apply-templates mode="titlepage.mode" />
		</div>
	</xsl:template>

	<xsl:template name="tr.attributes">
        <xsl:param name="row" select="." />
        <xsl:param name="rownum" select="0" />
		<xsl:variable name="tabstyle">
			<xsl:call-template name="tabstyle" />
		</xsl:variable>

		<xsl:if test="$tabstyle = 'striped'">
			<xsl:if test="$rownum mod 2 = 0">
				<xsl:attribute name="class">a</xsl:attribute>
			</xsl:if>
			<xsl:if test="$rownum mod 2 = 1">
				<xsl:attribute name="class">b</xsl:attribute>
			</xsl:if>
		</xsl:if>

	</xsl:template>
</xsl:stylesheet>

