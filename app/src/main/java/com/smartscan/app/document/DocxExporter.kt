package com.smartscan.app.document

import com.smartscan.app.domain.model.DocumentPage
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.nio.charset.StandardCharsets
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

object DocxExporter {

    fun generateDocx(
        title: String,
        pages: List<DocumentPage>,
        outputFile: File
    ): File {
        val ocrParagraphs = pages.map { page ->
            if (page.ocrText.isNotBlank()) page.ocrText else "Scanned Page ${page.pageNumber}"
        }

        val documentXml = buildDocumentXml(title, ocrParagraphs)

        ZipOutputStream(BufferedOutputStream(FileOutputStream(outputFile))).use { zip ->
            // [Content_Types].xml
            zip.putNextEntry(ZipEntry("[Content_Types].xml"))
            zip.write(CONTENT_TYPES_XML.toByteArray(StandardCharsets.UTF_8))
            zip.closeEntry()

            // _rels/.rels
            zip.putNextEntry(ZipEntry("_rels/.rels"))
            zip.write(RELS_XML.toByteArray(StandardCharsets.UTF_8))
            zip.closeEntry()

            // word/_rels/document.xml.rels
            zip.putNextEntry(ZipEntry("word/_rels/document.xml.rels"))
            zip.write(WORD_RELS_XML.toByteArray(StandardCharsets.UTF_8))
            zip.closeEntry()

            // word/document.xml
            zip.putNextEntry(ZipEntry("word/document.xml"))
            zip.write(documentXml.toByteArray(StandardCharsets.UTF_8))
            zip.closeEntry()
        }

        return outputFile
    }

    private fun buildDocumentXml(title: String, paragraphs: List<String>): String {
        val sb = StringBuilder()
        sb.append("""<?xml version="1.0" encoding="UTF-8" standalone="yes"?>""")
        sb.append("""<w:document xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main">""")
        sb.append("<w:body>")

        // Document Title Header
        sb.append("<w:p>")
        sb.append("<w:pPr><w:pStyle w:val=\"Title\"/></w:pPr>")
        sb.append("<w:r><w:rPr><w:b/><w:sz w:val=\"36\"/></w:rPr>")
        sb.append("<w:t>").append(escapeXml(title)).append("</w:t></w:r>")
        sb.append("</w:p>")

        // Empty spacer
        sb.append("<w:p/>")

        // Paragraphs from OCR
        paragraphs.forEachIndexed { pageIndex, pageText ->
            sb.append("<w:p>")
            sb.append("<w:r><w:rPr><w:b/><w:color w:val=\"0066CC\"/></w:rPr>")
            sb.append("<w:t>--- Page ").append(pageIndex + 1).append(" ---</w:t></w:r>")
            sb.append("</w:p>")

            pageText.split("\n").forEach { line ->
                if (line.isNotBlank()) {
                    sb.append("<w:p>")
                    sb.append("<w:r><w:t>").append(escapeXml(line.trim())).append("</w:t></w:r>")
                    sb.append("</w:p>")
                }
            }
            sb.append("<w:p/>")
        }

        sb.append("</w:body></w:document>")
        return sb.toString()
    }

    private fun escapeXml(text: String): String {
        return text.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&apos;")
    }

    private const val CONTENT_TYPES_XML = """<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types">
    <Default Extension="rels" ContentType="application/vnd.openxmlformats-package.relationships+xml"/>
    <Default Extension="xml" ContentType="application/xml"/>
    <Override PartName="/word/document.xml" ContentType="application/vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml"/>
</Types>"""

    private const val RELS_XML = """<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
    <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument" Target="word/document.xml"/>
</Relationships>"""

    private const val WORD_RELS_XML = """<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
</Relationships>"""
}
