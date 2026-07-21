package com.hazerfen.sifahane.export

import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.zip.ZipInputStream
import javax.xml.parsers.DocumentBuilderFactory

class SimpleXlsxWriterTest {
    @Test fun writesValidOoxmlParts() {
        val output = ByteArrayOutputStream()
        SimpleXlsxWriter.write(
            output,
            listOf(SimpleXlsxWriter.Sheet("İlaçlar", listOf(listOf("İlaç", "Stok"), listOf("Test", 5))))
        )
        val entries = mutableMapOf<String, ByteArray>()
        ZipInputStream(ByteArrayInputStream(output.toByteArray())).use { zip ->
            var entry = zip.nextEntry
            while (entry != null) {
                entries[entry.name] = zip.readBytes()
                entry = zip.nextEntry
            }
        }
        listOf(
            "[Content_Types].xml", "_rels/.rels", "xl/workbook.xml",
            "xl/_rels/workbook.xml.rels", "xl/styles.xml", "xl/worksheets/sheet1.xml"
        ).forEach { assertTrue("Eksik OOXML parçası: $it", entries.containsKey(it)) }
        val factory = DocumentBuilderFactory.newInstance().apply { isNamespaceAware = true }
        entries.filterKeys { it.endsWith(".xml") }.values.forEach { xml ->
            factory.newDocumentBuilder().parse(ByteArrayInputStream(xml))
        }
    }
}
