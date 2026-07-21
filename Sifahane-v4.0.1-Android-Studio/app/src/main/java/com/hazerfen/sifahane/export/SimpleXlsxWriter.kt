package com.hazerfen.sifahane.export

import java.io.File
import java.io.OutputStream
import java.time.Instant
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/** Apache POI gerektirmeyen, metin hücreli ve Excel uyumlu küçük OOXML yazıcısı. */
object SimpleXlsxWriter {
    data class Sheet(val name: String, val rows: List<List<Any?>>)

    fun write(file: File, sheets: List<Sheet>) {
        require(sheets.isNotEmpty()) { "En az bir çalışma sayfası gereklidir." }
        file.parentFile?.mkdirs()
        file.outputStream().buffered().use { output -> write(output, sheets) }
    }

    fun write(output: OutputStream, sheets: List<Sheet>) {
        require(sheets.isNotEmpty()) { "En az bir çalışma sayfası gereklidir." }
        val safeSheets = sheets.mapIndexed { index, sheet ->
            sheet.copy(name = sanitizeSheetName(sheet.name, index + 1))
        }
        ZipOutputStream(output).use { zip ->
            zip.textEntry("[Content_Types].xml", contentTypes(safeSheets.size))
            zip.textEntry("_rels/.rels", packageRelationships())
            zip.textEntry("docProps/app.xml", appProperties(safeSheets))
            zip.textEntry("docProps/core.xml", coreProperties())
            zip.textEntry("xl/workbook.xml", workbook(safeSheets))
            zip.textEntry("xl/_rels/workbook.xml.rels", workbookRelationships(safeSheets.size))
            zip.textEntry("xl/styles.xml", styles())
            safeSheets.forEachIndexed { index, sheet ->
                zip.textEntry("xl/worksheets/sheet${index + 1}.xml", worksheet(sheet.rows))
            }
        }
    }

    private fun worksheet(rows: List<List<Any?>>): String = buildString {
        append("""<?xml version="1.0" encoding="UTF-8" standalone="yes"?>""")
        append("""<worksheet xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main"><sheetData>""")
        rows.forEachIndexed { rowIndex, row ->
            append("<row r=\"").append(rowIndex + 1).append("\">")
            row.forEachIndexed { columnIndex, raw ->
                val reference = columnName(columnIndex) + (rowIndex + 1)
                when (raw) {
                    null -> append("<c r=\"").append(reference).append("\"/>")
                    is Number -> append("<c r=\"").append(reference).append("\"><v>")
                        .append(raw.toString()).append("</v></c>")
                    is Boolean -> append("<c r=\"").append(reference).append("\" t=\"b\"><v>")
                        .append(if (raw) "1" else "0").append("</v></c>")
                    else -> append("<c r=\"").append(reference)
                        .append("\" t=\"inlineStr\"><is><t xml:space=\"preserve\">")
                        .append(xml(raw.toString())).append("</t></is></c>")
                }
            }
            append("</row>")
        }
        append("</sheetData></worksheet>")
    }

    private fun contentTypes(sheetCount: Int): String = buildString {
        append("""<?xml version="1.0" encoding="UTF-8" standalone="yes"?>""")
        append("""<Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types">""")
        append("""<Default Extension="rels" ContentType="application/vnd.openxmlformats-package.relationships+xml"/>""")
        append("""<Default Extension="xml" ContentType="application/xml"/>""")
        append("""<Override PartName="/xl/workbook.xml" ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml"/>""")
        append("""<Override PartName="/xl/styles.xml" ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.styles+xml"/>""")
        append("""<Override PartName="/docProps/core.xml" ContentType="application/vnd.openxmlformats-package.core-properties+xml"/>""")
        append("""<Override PartName="/docProps/app.xml" ContentType="application/vnd.openxmlformats-officedocument.extended-properties+xml"/>""")
        repeat(sheetCount) { index ->
            append("<Override PartName=\"/xl/worksheets/sheet").append(index + 1)
                .append(".xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml\"/>")
        }
        append("</Types>")
    }

    private fun packageRelationships(): String =
        """<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
        <Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
          <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument" Target="xl/workbook.xml"/>
          <Relationship Id="rId2" Type="http://schemas.openxmlformats.org/package/2006/relationships/metadata/core-properties" Target="docProps/core.xml"/>
          <Relationship Id="rId3" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/extended-properties" Target="docProps/app.xml"/>
        </Relationships>""".trimIndent()

    private fun workbook(sheets: List<Sheet>): String = buildString {
        append("""<?xml version="1.0" encoding="UTF-8" standalone="yes"?>""")
        append("""<workbook xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main" xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships"><sheets>""")
        sheets.forEachIndexed { index, sheet ->
            append("<sheet name=\"").append(xml(sheet.name)).append("\" sheetId=\"")
                .append(index + 1).append("\" r:id=\"rId").append(index + 1).append("\"/>")
        }
        append("</sheets></workbook>")
    }

    private fun workbookRelationships(sheetCount: Int): String = buildString {
        append("""<?xml version="1.0" encoding="UTF-8" standalone="yes"?>""")
        append("""<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">""")
        repeat(sheetCount) { index ->
            append("<Relationship Id=\"rId").append(index + 1)
                .append("\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet\" Target=\"worksheets/sheet")
                .append(index + 1).append(".xml\"/>")
        }
        append("<Relationship Id=\"rId").append(sheetCount + 1)
            .append("\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles\" Target=\"styles.xml\"/>")
        append("</Relationships>")
    }

    private fun styles(): String =
        """<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
        <styleSheet xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main">
          <fonts count="1"><font><sz val="11"/><name val="Calibri"/></font></fonts>
          <fills count="2"><fill><patternFill patternType="none"/></fill><fill><patternFill patternType="gray125"/></fill></fills>
          <borders count="1"><border/></borders>
          <cellStyleXfs count="1"><xf numFmtId="0" fontId="0" fillId="0" borderId="0"/></cellStyleXfs>
          <cellXfs count="1"><xf numFmtId="0" fontId="0" fillId="0" borderId="0" xfId="0"/></cellXfs>
        </styleSheet>""".trimIndent()

    private fun appProperties(sheets: List<Sheet>): String =
        """<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
        <Properties xmlns="http://schemas.openxmlformats.org/officeDocument/2006/extended-properties" xmlns:vt="http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes">
          <Application>Şifahane</Application><TitlesOfParts><vt:vector size="${sheets.size}" baseType="lpstr">${sheets.joinToString("") { "<vt:lpstr>${xml(it.name)}</vt:lpstr>" }}</vt:vector></TitlesOfParts>
        </Properties>""".trimIndent()

    private fun coreProperties(): String {
        val timestamp = Instant.now().toString()
        return """<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
        <cp:coreProperties xmlns:cp="http://schemas.openxmlformats.org/package/2006/metadata/core-properties" xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
          <dc:creator>Şifahane</dc:creator><cp:lastModifiedBy>Şifahane</cp:lastModifiedBy>
          <dcterms:created xsi:type="dcterms:W3CDTF">$timestamp</dcterms:created>
          <dcterms:modified xsi:type="dcterms:W3CDTF">$timestamp</dcterms:modified>
        </cp:coreProperties>""".trimIndent()
    }

    private fun sanitizeSheetName(value: String, fallbackIndex: Int): String {
        val sanitized = value.replace(Regex("[\\\\/*?:\\[\\]]"), "_").trim().take(31)
        return sanitized.ifBlank { "Sayfa$fallbackIndex" }
    }

    private fun columnName(index: Int): String {
        var value = index + 1
        val result = StringBuilder()
        while (value > 0) {
            val remainder = (value - 1) % 26
            result.append(('A'.code + remainder).toChar())
            value = (value - 1) / 26
        }
        return result.reverse().toString()
    }

    private fun xml(value: String): String = value
        .replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
        .replace("\"", "&quot;")
        .replace("'", "&apos;")

    private fun ZipOutputStream.textEntry(name: String, text: String) {
        putNextEntry(ZipEntry(name))
        write(text.toByteArray(Charsets.UTF_8))
        closeEntry()
    }
}
