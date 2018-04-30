<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        version="1.0">

    <xsl:output method="html"/>

    <xsl:template match="/">
        <html>
            <head>
                <title>Article</title>
            </head>
            <body>
                <article>
                    <xsl:apply-templates/>
                </article>
            </body>
        </html>
    </xsl:template>

    <!-- Heading -->

    <xsl:template match="/article">
        <header>
            <xsl:apply-templates/>
        </header>
    </xsl:template>

    <xsl:template match="/article/title">
        <h1 class="main-title">
            <xsl:apply-templates/>
        </h1>
    </xsl:template>

    <xsl:template match="/article/author">
        <a rel="author" class="author">
            <xsl:apply-templates/>
        </a>
    </xsl:template>

    <xsl:template match="/article/date">
        <time>
            <xsl:apply-templates/>
        </time>
    </xsl:template>

    <!-- Content -->

    <xsl:template match="/article">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="/article//paragraph">
        <p>
            <xsl:apply-templates/>
        </p>
    </xsl:template>

    <xsl:template match="/article//paragraph/strong">
        <strong>
            <xsl:apply-templates/>
        </strong>
    </xsl:template>

    <xsl:template match="/article//paragraph/emphasize">
        <em>
            <xsl:apply-templates/>
        </em>
    </xsl:template>

    <!-- content - section -->
    <xsl:template match="/article/section">
        <section class="section">
            <xsl:apply-templates/>
        </section>
    </xsl:template>

    <xsl:template match="/article/section/title">
        <h2 class="title">
            <xsl:apply-templates/>
        </h2>
    </xsl:template>

    <xsl:template match="/article//subsection">
        <section class="subsection">
            <xsl:apply-templates/>
        </section>
    </xsl:template>

    <xsl:template match="/article//subsection/title">
        <h3 class="title">
            <xsl:apply-templates/>
        </h3>
    </xsl:template>

    <xsl:template match="/article//subsubsection">
        <section class="subsubsection">
            <xsl:apply-templates/>
        </section>
    </xsl:template>

    <xsl:template match="/article//subsubsection/title">
        <h4 class="title">
            <xsl:apply-templates/>
        </h4>
    </xsl:template>

    <xsl:template match="/article//quote">
        <blockquote class="quote">
            <xsl:apply-templates/>
        </blockquote>
    </xsl:template>

    <xsl:template match="/article//items">
        <ul>
            <xsl:apply-templates select="ul"/>
        </ul>
        <ol>
            <xsl:apply-templates select="ol"/>
        </ol>
    </xsl:template>


</xsl:stylesheet>