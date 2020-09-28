package org.egov.cpt.service.xlsxparsing;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.model.SharedStrings;
import org.apache.poi.xssf.model.Styles;
import org.apache.poi.xssf.model.StylesTable;
import org.egov.cpt.models.RentDemand;
import org.egov.cpt.models.RentDemandResponse;
import org.egov.cpt.models.RentPayment;
import org.egov.cpt.service.xlsxparsing.StreamingSheetContentsHandler.StreamingRowProcessor;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Service;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReadExcelStreamingService extends AbstractExcelService implements IReadExcelService {

    @Override
    public RentDemandResponse getDatafromExcel(InputStream is, int sheetIndex) {
        try {
            OPCPackage opcPackage = OPCPackage.open(is);
            return this.process(opcPackage, sheetIndex);
        } catch (IOException | OpenXML4JException | SAXException e) {
            log.error("Error while parsing Excel", e);
            throw new CustomException("PARSE_ERROR", "Could not parse excel. Error is " + e.getMessage());
        }
    }

    @Override
    public RentDemandResponse getDatafromExcel(File file, int sheetIndex) {
        try {
            OPCPackage opcPackage = OPCPackage.open(file);
            return this.process(opcPackage, sheetIndex);
        } catch (IOException | OpenXML4JException | SAXException e) {
            log.error("Error while parsing Excel", e);
            throw new CustomException("PARSE_ERROR", "Could not parse excel. Error is " + e.getMessage());
        }
    }

    /**
     * Parses and shows the content of one sheet using the specified styles and
     * shared-strings tables.
     *
     * @param styles           The table of styles that may be referenced by cells
     *                         in the sheet
     * @param strings          The table of strings that may be referenced by cells
     *                         in the sheet
     * @param sheetInputStream The stream to read the sheet-data from.
     * 
     * @exception java.io.IOException An IO exception from the parser, possibly from
     *                                a byte stream or character stream supplied by
     *                                the application.
     * @throws SAXException if parsing the XML data fails.
     */
    private void processSheet(Styles styles, SharedStrings strings, SheetContentsHandler sheetHandler,
            InputStream sheetInputStream) throws IOException, SAXException {
        DataFormatter formatter = new DataFormatter();
        InputSource sheetSource = new InputSource(sheetInputStream);
        try {
            SAXParserFactory saxFactory = SAXParserFactory.newInstance();
            saxFactory.setNamespaceAware(false);
            SAXParser saxParser = saxFactory.newSAXParser();
            XMLReader sheetParser = saxParser.getXMLReader();
            ContentHandler handler = new MyXSSFSheetXMLHandler(styles, null, strings, sheetHandler, formatter, false);
            sheetParser.setContentHandler(handler);
            sheetParser.parse(sheetSource);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("SAX parser appears to be broken - " + e.getMessage());
        }
    }

    private RentDemandResponse process(OPCPackage xlsxPackage, int sheetNo)
            throws IOException, OpenXML4JException, SAXException, CustomException {
        ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(xlsxPackage);
        XSSFReader xssfReader = new XSSFReader(xlsxPackage);
        StylesTable styles = xssfReader.getStylesTable();
        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
        int index = 0;
        while (iter.hasNext()) {
            try (InputStream stream = iter.next()) {
                // String sheetName = iter.getSheetName();
                if (index == sheetNo) {
                    SheetContentsProcessor processor = new SheetContentsProcessor();
                    processSheet(styles, strings, new StreamingSheetContentsHandler(processor), stream);
                    return new RentDemandResponse(processor.demands, processor.payments);
                }
                index++;
            }
        }
        throw new CustomException("PARSE_ERROR", "Could not process sheet no " + sheetNo);
    }

    private class SheetContentsProcessor implements StreamingRowProcessor {
        boolean isParsing = false;
        List<RentDemand> demands = new ArrayList<RentDemand>();
        List<RentPayment> payments = new ArrayList<RentPayment>();

        @Override
        public void processRow(Row currentRow) {
            String firstCell = String
                    .valueOf(getValueFromCell(currentRow, 0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
            if (!this.isParsing) {
                if (HEADER_CELL.equalsIgnoreCase(firstCell)) {
                    this.isParsing = true;
                }
                return;
            }
            if (FOOTER_CELL.equalsIgnoreCase(firstCell)) {
                this.isParsing = false;
                return;
            }
            if (HEADER_CELL.equalsIgnoreCase(firstCell)) {
                return;
            }
            RentDemandPayment demandPayment = getDemandAndPaymentFromRow(currentRow);
            if (demandPayment == null) {
                return;
            }
            demands.add(demandPayment.demand);
            if (demandPayment.payment != null) {
                payments.add(demandPayment.payment);
            }
        }
    }
}
