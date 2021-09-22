package org.egov.cpt.service.pdf;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;

import org.apache.commons.io.IOUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.cpt.models.BillAccountDetailV2;
import org.egov.cpt.models.Document;
import org.egov.cpt.models.ExcelSearchCriteria;
import org.egov.cpt.models.OfflinePaymentDetails;
import org.egov.cpt.models.PdfSearchCriteria;
import org.egov.cpt.models.PropertyCriteria;
import org.egov.cpt.models.enums.CollectionPaymentModeEnum;
import org.egov.cpt.repository.PropertyRepository;
import org.egov.cpt.util.FileStoreUtils;
import org.egov.cpt.util.NotificationUtil;
import org.egov.cpt.util.PTConstants;
import org.egov.cpt.util.ReportUtil;
import org.egov.cpt.util.ReportViewerUtil;
import org.egov.cpt.web.contracts.DuplicateCopyRequest;
import org.egov.cpt.web.contracts.MortgageRequest;
import org.egov.cpt.web.contracts.NoticeGenerationRequest;
import org.egov.cpt.web.contracts.OwnershipTransferRequest;
import org.egov.cpt.web.contracts.PDFAccountStatementRequest;
import org.egov.cpt.web.contracts.PDFPaymentReceiptRequest;
import org.egov.cpt.web.contracts.PropertyRequest;
import org.egov.tracer.model.CustomException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.ExporterConfiguration;
import net.sf.jasperreports.export.ExporterOutput;
import net.sf.jasperreports.export.SimpleCsvExporterConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterConfiguration;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleRtfExporterConfiguration;
import net.sf.jasperreports.export.SimpleTextExporterConfiguration;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.export.SimpleXlsExporterConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static org.egov.cpt.service.pdf.ReportFormat.*;


@Service
public class PdfService {

	@Value("${city_logo_url}")
	private String city_logo_url;

	@Value("${city_watermark_url}")
	private String city_watermark_url;

//	@Value("${city_footer_left_url}")
//	private String city_footer_left_url;
//
//	@Value("${city_footer_right_url}")
//	private String city_footer_right_url;

	@Value("${complete_footer_path}")
	private String complete_footer_path;

	@Value("${complete_header_path}")
	private String complete_header_path;


	@Autowired
	private NotificationUtil notificationUtil;

	@Autowired
	private FileStoreUtils fileStoreUtils;

	@Autowired
	private ReportViewerUtil  reportViewerUtil;

	@Autowired
	private PropertyRepository repository;

	private String LOCALIZATION = "Localization";

	private String PROPERTY = "property";

	private static final String TEMPLATE_EXTENSION = ".jrxml";

	public JasperReport createReport(PdfSearchCriteria searchCriteria) throws JRException {
		String template=null;
		if (searchCriteria.getKey()!=null) {
			template=findPDFTtemplate(searchCriteria.getKey());
		}
		// Fetching the .jrxml file from the resources folder.
		final InputStream stream = this.getClass().getResourceAsStream("/reports/templates/"+template);

		final JasperReport report = JasperCompileManager.compileReport(stream);

		// Compile the Jasper report from .jrxml to .japser
//		ClassLoader classLoader = getClass().getClassLoader();
//		File file = new File(classLoader.getResource("reports/templates/"+template).getFile());
//		JasperCompileManager.compileReportToFile(
//				file.getAbsolutePath(), // the path to the jrxml file to compile
//				"src/main/resources/reports/templates/"+template.replace(".jrxml", "")+".jasper"); // the path and name we want to save the compiled file to

		return report;
	}

	private void enrichParams(RequestInfo requestInfo, Map<String, Object> parameters, String tenantId) {
		//setting loclaizations localization
		String localizationMessages = notificationUtil.getLocalizationMessages(tenantId, requestInfo);
		JSONObject messages = new JSONObject(localizationMessages);
		JSONArray messageArray = (JSONArray) messages.get("messages");
		final Map<String, String> msgMap = new HashMap<>();
		for (int i=0;i<messageArray.length();i++) {
			JSONObject jsonobject = messageArray.getJSONObject(i);
			msgMap.put(jsonobject.getString("code"), jsonobject.getString("message"));
		}

		//Setting localizations
		parameters.put(LOCALIZATION, msgMap);

		//Setting logos
		parameters.put(PTConstants.WATER_MARK_PATH, getCityLogoAsBytes(PTConstants.WATER_MARK_PATH));
		parameters.put(PTConstants.FOOTER_LEFT_PATH, getCityLogoAsBytes(PTConstants.FOOTER_LEFT_PATH));
		parameters.put(PTConstants.FOOTER_RIGHT_PATH, getCityLogoAsBytes(PTConstants.FOOTER_RIGHT_PATH));
		parameters.put(PTConstants.LOGO_PATH, getCityLogoAsBytes(PTConstants.LOGO_PATH));
		parameters.put(PTConstants.COMPLETE_HEADER_PATH, getCityLogoAsBytes(PTConstants.COMPLETE_HEADER_PATH));
		parameters.put(PTConstants.COMPLETE_FOOTER_PATH, getCityLogoAsBytes(PTConstants.COMPLETE_FOOTER_PATH));


	}

	public List<HashMap<String, String>> createPdfReport(PdfSearchCriteria searchCriteria, PropertyRequest propertyRequest) throws JRException {

		final JasperReport report = createReport(searchCriteria);

		// Fetching the employees from the data source.
		final JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(propertyRequest.getProperties());

		// Adding the additional parameters to the pdf.
		final Map<String, Object> parameters = new HashMap<>();

		//setting common params
		enrichParams(propertyRequest.getRequestInfo(),parameters,searchCriteria.getTenantId());

		//setting current owner
		parameters.put("Owner", propertyRequest.getProperties().get(0).getOwners().get(0));

		HashMap<String, String> loclaizationMap = (HashMap<String, String>) parameters.get(LOCALIZATION);
		parameters.put("colony", loclaizationMap.get(propertyRequest.getProperties().get(0).getColony()));

		// Filling the report with the employee data and additional parameters information.
		final JasperPrint print = JasperFillManager.fillReport(report, parameters, source);

		final ReportRequest reportInput = new ReportRequest(findPDFTtemplate(searchCriteria.getKey()), parameters,
				ReportDataSourceType.JAVABEAN);
		List<HashMap<String, String>> fileStoreResp;
		try {
			fileStoreResp = saveFile(print,searchCriteria.getKey(),searchCriteria.getTenantId());
		} catch (IOException e1) {
			throw new CustomException("FILE STORE ERROR","Error while storing file");
		}
		try {
			ReportOutput reportOutput = new ReportOutput(exportReport(reportInput, print), reportInput);
			reportViewerUtil.addReportToTempCache(reportOutput);
		} catch (JRException | IOException e) {
			throw new CustomException("EXCEPTION_IN_REPORT_CREATION", "EXCEPTION_IN_REPORT_CREATION"+e);
		}
		return fileStoreResp;
	}

	public List<HashMap<String, String>> createPdfReport(PdfSearchCriteria searchCriteria, PDFAccountStatementRequest accountStatementRequest) throws JRException {
		final JasperReport report = createReport(searchCriteria);

		// Fetching the employees from the data source.
		final JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(accountStatementRequest.getRentAccountStatements());

		// Adding the additional parameters to the pdf.
		final Map<String, Object> parameters = new HashMap<>();

		enrichParams(accountStatementRequest.getRequestInfo(),parameters,searchCriteria.getTenantId());

		//setting current owner
		parameters.put("Owner", accountStatementRequest.getProperties().get(0).getOwners().get(0));

		//Setting colony with loclaization value
		HashMap<String, String> loclaizationMap = (HashMap<String, String>) parameters.get(LOCALIZATION);
		parameters.put("colony", loclaizationMap.get(accountStatementRequest.getProperties().get(0).getColony()));

		//setting account statement
		parameters.put(PROPERTY, accountStatementRequest.getProperties().get(0));

		parameters.put("RentAccountStatements", source);

		parameters.put("MonthlyRent",accountStatementRequest.getProperties().get(0).getDemands().get(0).getCollectionPrincipal());

		// Filling the report with the employee data and additional parameters information.
		final JasperPrint print = JasperFillManager.fillReport(report, parameters, source);

		final ReportRequest reportInput = new ReportRequest(findPDFTtemplate(searchCriteria.getKey()), parameters,
				ReportDataSourceType.JAVABEAN);
		List<HashMap<String, String>> fileStoreResp;
		try {
			fileStoreResp = saveFile(print,searchCriteria.getKey(),searchCriteria.getTenantId());

		} catch (IOException e1) {
			throw new CustomException("FILE STORE ERROR","Error while storing file");
		}
		try {
			ReportOutput reportOutput = new ReportOutput(exportReport(reportInput, print), reportInput);
			reportViewerUtil.addReportToTempCache(reportOutput);
		} catch (JRException | IOException e) {
			throw new CustomException("EXCEPTION_IN_REPORT_CREATION", "EXCEPTION_IN_REPORT_CREATION"+e);
		}
		return fileStoreResp;

	}

	public List<HashMap<String, String>> createPdfReport(PdfSearchCriteria searchCriteria, DuplicateCopyRequest dcRequest) throws JRException {
		final JasperReport report = createReport(searchCriteria);

		// Adding the additional parameters to the pdf.
		final Map<String, Object> parameters = new HashMap<>();

		//setting common parameters
		enrichParams(dcRequest.getRequestInfo(),parameters,searchCriteria.getTenantId());

		parameters.put("applicant",dcRequest.getDuplicateCopyApplications().get(0).getApplicant().get(0));

		parameters.put(PROPERTY, dcRequest.getDuplicateCopyApplications().get(0).getProperty());

		HashMap<String, String> loclaizationMap = (HashMap<String, String>) parameters.get(LOCALIZATION);

		if(!searchCriteria.getKey().equalsIgnoreCase("rp-dc-allotment-letter")) {
			List<Document> document = dcRequest.getDuplicateCopyApplications().get(0).getApplicationDocuments();

			for(int i=0; i<document.size();i++) {
				String filePath = fileStoreUtils.fetchFileStoreUrl(ExcelSearchCriteria.builder().
						fileStoreId(document.get(i).getFileStoreId()).tenantId(document.get(0).getTenantId()).build());
				File f = new File(filePath);
				String newPath =f.getPath().replace(f.getParent()+"\\", "");
				String fileName = newPath.replace(newPath.substring(newPath.indexOf("?")),"").substring(13).replace("%20", " ").replace("%28","(").replace("%29",")");

				parameters.put("document"+(i+1), loclaizationMap.get("RP_"+document.get(i).getDocumentType())+"\n <b>"+fileName+"</b>");
			}
		}

		parameters.put("dcApplication", dcRequest.getDuplicateCopyApplications().get(0));

		// Filling the report with the employee data and additional parameters information.
		final JasperPrint print = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());

		final ReportRequest reportInput = new ReportRequest(findPDFTtemplate(searchCriteria.getKey()), parameters,
				ReportDataSourceType.JAVABEAN);
		List<HashMap<String, String>> fileStoreResp;
		try {
			fileStoreResp = saveFile(print,searchCriteria.getKey(),searchCriteria.getTenantId());


		} catch (IOException e1) {
			throw new CustomException("FILE STORE ERROR","Error while storing file");
		}
		try {
			ReportOutput reportOutput = new ReportOutput(exportReport(reportInput, print), reportInput);
			reportViewerUtil.addReportToTempCache(reportOutput);
		} catch (JRException | IOException e) {
			throw new CustomException("EXCEPTION_IN_REPORT_CREATION", "EXCEPTION_IN_REPORT_CREATION"+e);
		}

		return fileStoreResp;

	}
	public List<HashMap<String, String>> createPdfReport(PdfSearchCriteria searchCriteria, MortgageRequest mgRequest) throws JRException {
		final JasperReport report = createReport(searchCriteria);

		// Adding the additional parameters to the pdf.
		final Map<String, Object> parameters = new HashMap<>();

		enrichParams(mgRequest.getRequestInfo(),parameters,searchCriteria.getTenantId());

		parameters.put("applicant",mgRequest.getMortgageApplications().get(0).getApplicant().get(0));

		parameters.put(PROPERTY, mgRequest.getMortgageApplications().get(0).getProperty());

		List<Document> document = mgRequest.getMortgageApplications().get(0).getApplicationDocuments();

		HashMap<String, String> loclaizationMap = (HashMap<String, String>) parameters.get(LOCALIZATION);

		for(int i=0; i<document.size();i++) {
			String filePath = fileStoreUtils.fetchFileStoreUrl(ExcelSearchCriteria.builder().
					fileStoreId(document.get(i).getFileStoreId()).tenantId(document.get(0).getTenantId()).build());
			File f = new File(filePath);
			String newPath =f.getPath().replace(f.getParent()+"\\", "");
			String fileName = newPath.replace(newPath.substring(newPath.indexOf("?")),"").substring(13).replace("%20", " ").replace("%28","(").replace("%29",")");

			parameters.put("document"+(i+1), loclaizationMap.get("RP_"+document.get(i).getDocumentType())+"\n <b>"+fileName+"</b>");
		}

		// Fetching the employees from the data source.
		parameters.put("mgApplication", mgRequest.getMortgageApplications().get(0));

		if(mgRequest.getMortgageApplications().get(0).getMortgageApprovedGrantDetails()!=null)
			parameters.put("grantDetails", mgRequest.getMortgageApplications().get(0).getMortgageApprovedGrantDetails().get(0));

		// Filling the report with the employee data and additional parameters information.
		final JasperPrint print = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());

		final ReportRequest reportInput = new ReportRequest(findPDFTtemplate(searchCriteria.getKey()), parameters,
				ReportDataSourceType.JAVABEAN);
		List<HashMap<String, String>> fileStoreResp;

		try {
			fileStoreResp = saveFile(print,searchCriteria.getKey(),searchCriteria.getTenantId());
		} catch (IOException e1) {
			throw new CustomException("FILE STORE ERROR","Error while storing file");
		}
		try {
			ReportOutput reportOutput = new ReportOutput(exportReport(reportInput, print), reportInput);
			reportViewerUtil.addReportToTempCache(reportOutput);
		} catch (JRException | IOException e) {
			throw new CustomException("EXCEPTION_IN_REPORT_CREATION", "EXCEPTION_IN_REPORT_CREATION"+e);
		}

		return fileStoreResp;

	}
	public List<HashMap<String, String>> createPdfReport(PdfSearchCriteria searchCriteria, OwnershipTransferRequest otRequest) throws JRException {
		final JasperReport report = createReport(searchCriteria);

		// Adding the additional parameters to the pdf.
		final Map<String, Object> parameters = new HashMap<>();

		parameters.put(PROPERTY, otRequest.getOwners().get(0).getProperty());

		enrichParams(otRequest.getRequestInfo(),parameters,searchCriteria.getTenantId());

		List<Document> document = otRequest.getOwners().get(0).getOwnerDetails().getOwnershipTransferDocuments();

		HashMap<String, String> loclaizationMap = (HashMap<String, String>) parameters.get(LOCALIZATION);

		for(int i=0; i<document.size();i++) {
			String filePath = fileStoreUtils.fetchFileStoreUrl(ExcelSearchCriteria.builder().
					fileStoreId(document.get(i).getFileStoreId()).tenantId(otRequest.getOwners().get(0).getTenantId()).build());
			File f = new File(filePath);
			String newPath =f.getPath().replace(f.getParent()+"\\", "");
			String fileName = newPath.replace(newPath.substring(newPath.indexOf("?")),"").substring(13).replace("%20", " ").replace("%28","(").replace("%29",")");

			parameters.put("document"+(i+1), loclaizationMap.get("RP_"+document.get(i).getDocumentType())+"\n <b>"+fileName+"</b>");
		}

		// Fetching the employees from the data source.
		parameters.put("owner", otRequest.getOwners().get(0));

		// Filling the report with the employee data and additional parameters information.
		final JasperPrint print = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());

		final ReportRequest reportInput = new ReportRequest(findPDFTtemplate(searchCriteria.getKey()), parameters,
				ReportDataSourceType.JAVABEAN);
		List<HashMap<String, String>> fileStoreResp;
		try {
			fileStoreResp = saveFile(print,searchCriteria.getKey(),searchCriteria.getTenantId());
		} catch (IOException e1) {
			throw new CustomException("FILE STORE ERROR","Error while storing file");
		}
		try {
			ReportOutput reportOutput = new ReportOutput(exportReport(reportInput, print), reportInput);
			reportViewerUtil.addReportToTempCache(reportOutput);
		} catch (JRException | IOException e) {
			throw new CustomException("EXCEPTION_IN_REPORT_CREATION", "EXCEPTION_IN_REPORT_CREATION"+e);
		}

		return fileStoreResp;

	}
	public List<HashMap<String, String>> createPdfReport(PdfSearchCriteria searchCriteria, PDFPaymentReceiptRequest receiptRequest) throws JRException {
		final JasperReport report = createReport(searchCriteria);

		// Adding the additional parameters to the pdf.
		final Map<String, Object> parameters = new HashMap<>();

		JRBeanCollectionDataSource source =null;

		if(receiptRequest.getProperties()!=null && !receiptRequest.getProperties().isEmpty()) {
			source = new JRBeanCollectionDataSource(receiptRequest.getProperties());
		}else {
			source = new JRBeanCollectionDataSource(receiptRequest.getPayments());
		}

		enrichParams(receiptRequest.getRequestInfo(),parameters,searchCriteria.getTenantId());

		//setting payment details
		parameters.put("payment", receiptRequest.getPayments().get(0));
		parameters.put("paymentDetails", receiptRequest.getPayments().get(0).getPaymentDetails().get(0));

		if(!searchCriteria.getKey().equalsIgnoreCase("rp-payment-receipt") && receiptRequest.getProperties()!=null) {
			parameters.put("transitNumber", receiptRequest.getProperties().get(0).getTransitNumber());

			List<OfflinePaymentDetails> offlinePaymentDetailsFromDB = repository.getPropertyOfflinePaymentDetails(
					PropertyCriteria.builder().propertyId(receiptRequest.getProperties().get(0).getId()).limit(1l).build());

			if(offlinePaymentDetailsFromDB != null && !offlinePaymentDetailsFromDB.isEmpty()) {
				Optional<OfflinePaymentDetails> offlinePaymentDetails = offlinePaymentDetailsFromDB.stream().filter(opd->opd.getTransactionNumber().equalsIgnoreCase(receiptRequest.getPayments().get(0).getTransactionNumber())).findAny();

				if(offlinePaymentDetails.isPresent())
					parameters.put("offlinePayemntDetails", offlinePaymentDetails.get());
			}
		}

		//converting date to IST time
		String paymentDate;
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy , hh:mm:ss a");
		formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata")); 
		if(receiptRequest.getPayments().get(0).getPaymentMode().equals(CollectionPaymentModeEnum.OFFLINE_NEFT)||receiptRequest.getPayments().get(0).getPaymentMode().equals(CollectionPaymentModeEnum.OFFLINE_RTGS)) {
			paymentDate =  new SimpleDateFormat("dd/MM/yyyy").format(receiptRequest.getPayments().get(0).getInstrumentDate());
		}else {
			paymentDate =  formatter.format(receiptRequest.getPayments().get(0).getPaymentDetails().get(0).getAuditDetails().getLastModifiedTime());
		}
		parameters.put("paymentDate", paymentDate);

		parameters.put("paymentMode", receiptRequest.getPayments().get(0).getPaymentMode().equals(CollectionPaymentModeEnum.OFFLINE_NEFT)?"Direct Bank - Vikas Nagar":
			receiptRequest.getPayments().get(0).getPaymentMode().equals(CollectionPaymentModeEnum.OFFLINE_RTGS)?"Direct Bank - Sec.52-53":
				receiptRequest.getPayments().get(0).getPaymentMode().toString());

		if(searchCriteria.getKey().equalsIgnoreCase("rp-payment-receipt")) {
			List<BillAccountDetailV2> billAoccuntDetails = receiptRequest.getPayments().get(0).getPaymentDetails().get(0).getBill().getBillDetails().get(0).getBillAccountDetails();
			parameters.put("dueAmount", billAoccuntDetails.stream().filter(accDtl->accDtl.getTaxHeadCode().equalsIgnoreCase("RP_DUE")).findAny().get().getAmount());
			if(billAoccuntDetails.size()>1)
				parameters.put("publicationCharge",  billAoccuntDetails.stream().filter(accDtl->accDtl.getTaxHeadCode().equalsIgnoreCase("RP_CHARGES")).findAny().get().getAmount());
		}

		// Filling the report with the employee data and additional parameters information.
		final JasperPrint print = JasperFillManager.fillReport(report, parameters, source);

		final ReportRequest reportInput = new ReportRequest(findPDFTtemplate(searchCriteria.getKey()), parameters,
				ReportDataSourceType.JAVABEAN);
		List<HashMap<String, String>> fileStoreResp;
		try {
			fileStoreResp = saveFile(print,searchCriteria.getKey(),searchCriteria.getTenantId());

		} catch (IOException e1) {
			throw new CustomException("FILE STORE ERROR","Error while storing file");
		}
		try {
			ReportOutput reportOutput = new ReportOutput(exportReport(reportInput, print), reportInput);
			reportViewerUtil.addReportToTempCache(reportOutput);
		} catch (JRException | IOException e) {
			throw new CustomException("EXCEPTION_IN_REPORT_CREATION", "EXCEPTION_IN_REPORT_CREATION"+e);
		}
		return fileStoreResp;
	}
	public List<HashMap<String, String>> createPdfReport(PdfSearchCriteria searchCriteria, NoticeGenerationRequest noticeRequest) throws JRException {

		final JasperReport report = createReport(searchCriteria);
		// Fetching the employees from the data source.
		final JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(noticeRequest.getNoticeApplications());

		// Adding the additional parameters to the pdf.
		final Map<String, Object> parameters = new HashMap<>();

		enrichParams(noticeRequest.getRequestInfo(),parameters,searchCriteria.getTenantId());

		parameters.put("notice", noticeRequest.getNoticeApplications().get(0));
		parameters.put(PROPERTY, noticeRequest.getNoticeApplications().get(0).getProperty());


		// Filling the report with the employee data and additional parameters information.
		final JasperPrint print = JasperFillManager.fillReport(report, parameters, source);

		final ReportRequest reportInput = new ReportRequest(findPDFTtemplate(searchCriteria.getKey()), parameters,
				ReportDataSourceType.JAVABEAN);
		List<HashMap<String, String>> fileStoreResp;
		try {
			fileStoreResp = saveFile(print,searchCriteria.getKey(),searchCriteria.getTenantId());

		} catch (IOException e1) {
			throw new CustomException("FILE STORE ERROR","Error while storing file");
		}
		try {
			ReportOutput reportOutput = new ReportOutput(exportReport(reportInput, print), reportInput);
			reportViewerUtil.addReportToTempCache(reportOutput);
		} catch (JRException | IOException e) {
			throw new CustomException("EXCEPTION_IN_REPORT_CREATION", "EXCEPTION_IN_REPORT_CREATION"+e);
		}

		return fileStoreResp;
	}


	private byte[] exportReport(ReportRequest reportInput, JasperPrint jasperPrint) throws JRException, IOException {
		try (ByteArrayOutputStream reportOutputStream = new ByteArrayOutputStream()) {
			Exporter exporter = getExporter(reportInput, jasperPrint, reportOutputStream);
			exporter.exportReport();
			return reportOutputStream.toByteArray();
		} catch (Exception e) {
			throw new CustomException("EXCEPTION_IN_REPORT_CREATION", "EXCEPTION_IN_REPORT_CREATION"+e);
		}
	}

	private Exporter getExporter(ReportRequest reportInput, JasperPrint jasperPrint, OutputStream outputStream) {
		Exporter exporter;
		ExporterConfiguration exporterConfiguration;
		ExporterOutput exporterOutput = null;
		if (PDF.equals(reportInput.getReportFormat())) {
			SimplePdfExporterConfiguration pdfExporterConfiguration = new SimplePdfExporterConfiguration();
			exporter = new JRPdfExporter();
			exporterConfiguration = pdfExporterConfiguration;
		} else if (XLS.equals(reportInput.getReportFormat())) {
			exporter = new JRXlsExporter();
			exporterConfiguration = new SimpleXlsExporterConfiguration();
		} else if (RTF.equals(reportInput.getReportFormat())) {
			exporter = new JRRtfExporter();
			exporterConfiguration = new SimpleRtfExporterConfiguration();
			exporterOutput= new SimpleWriterExporterOutput(outputStream);
		} else if (HTM.equals(reportInput.getReportFormat())) {
			exporter = new HtmlExporter();
			exporterConfiguration = new SimpleHtmlExporterConfiguration();
			exporterOutput = new SimpleHtmlExporterOutput(outputStream);
		} else if (TXT.equals(reportInput.getReportFormat())) {
			exporter = new JRTextExporter();
			exporterConfiguration = new SimpleTextExporterConfiguration();
		} else if (CSV.equals(reportInput.getReportFormat())) {
			exporter = new JRCsvExporter();
			exporterConfiguration = new SimpleCsvExporterConfiguration();
		} else {
			throw new CustomException("EXCEPTION_IN_REPORT_CREATION","Invalid report format [" + reportInput.getReportFormat() + "]");
		}

		exporter.setConfiguration(exporterConfiguration);
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setExporterOutput(exporterOutput == null ? new SimpleOutputStreamExporterOutput(outputStream) : exporterOutput);
		return exporter;
	}
	private List<HashMap<String, String>> saveFile(JasperPrint print, String templatekey, String tenantId) throws IOException {
		byte[] fileInByte = null;
		try {
			fileInByte = JasperExportManager.exportReportToPdf(print);

		} catch (JRException e) {
			e.printStackTrace();
		}

		String stateLevelTenantId = this.getStateLevelTenantId(tenantId);

		ByteArrayOutputStream baos = new ByteArrayOutputStream(fileInByte.length);
		baos.write(fileInByte, 0, fileInByte.length);
		String fileName = String.format("%s-%s.pdf", templatekey,System.currentTimeMillis());
		List<HashMap<String, String>> fileStoreResponse = fileStoreUtils.uploadStreamToFileStore(baos,
				stateLevelTenantId, fileName, ReportUtil.contentType(PDF));

		baos.close();
		return fileStoreResponse;
	}

	private String getStateLevelTenantId(String tenantId) {
		String[] components = tenantId.split(".");
		if (components.length == 0) {
			return "ch";
		}
		return components[0];
	}

	private String findPDFTtemplate(String key) {
		String templateName=null;
		switch(key) {
		case "rp-original-allotment-letter":
		case "rp-account-statement-generation" :
		case "rp-duplicate-copy-fresh" :
		case "rp-duplicate-copy-paid" :
		case "rp-duplicate-copy-charges" :
		case "rp-dc-allotment-letter" :
		case "rp-mortgage-fresh" :
		case "rp-mortgage-letter" :
		case "rp-mortgage-approved-alternate" :
		case "rp-ot-allotment-letter" :
		case "rp-ownership-transfer-fresh" :
		case "rp-ownership-transfer-paid" :
		case "rp-ownership-transfer-charges" :
		case "rp-payment-history-receipt" :
		case "rp-payment-receipt" :
		case "rp-rent-payment-receipt" :
		case "rp-recovery-notice" :
		case "rp-violation-notice" :
			templateName= key+TEMPLATE_EXTENSION;
			break;
		}
		return templateName;
	}

	public InputStream getCityLogoAsStream(String logoPath) {
		return new ByteArrayInputStream(getCityLogoAsBytes(logoPath));
	}

	public byte[] getCityLogoAsBytes(String logoPath) {
		String logo_http_url = null;
		switch(logoPath) {
		case PTConstants.LOGO_PATH:
			logo_http_url = this.city_logo_url;
			break;
		case PTConstants.WATER_MARK_PATH:
			logo_http_url = this.city_watermark_url;
			break;
//		case PTConstants.FOOTER_LEFT_PATH:
//			logo_http_url = this.city_footer_left_url;
//			break;
//		case PTConstants.FOOTER_RIGHT_PATH:
//			logo_http_url = this.city_footer_right_url;
//			break;
		case PTConstants.COMPLETE_HEADER_PATH:
			logo_http_url = this.complete_header_path;
			break;
		case PTConstants.COMPLETE_FOOTER_PATH:
			logo_http_url = this.complete_footer_path;
			break;
		default:
			logo_http_url = this.city_logo_url;
			break; 

		}
		InputStream stream = this.getClass().getResourceAsStream("/images/"+logo_http_url);
		byte[] cityLogo = null;
		try {
			cityLogo = IOUtils.toByteArray(stream);
		} catch (IOException e) {
			throw new CustomException("ERROR IN IMAGE STREAM READING","error in image stream reading: "+e);
		}
		return cityLogo;
	}


	public byte[] httpImageAsByteArray(String imageUrl){
		try {
			URL url = new URL(imageUrl);
			ByteArrayOutputStream output = new ByteArrayOutputStream();

			try (InputStream inputStream = url.openStream()) {
				int n = 0;
				byte [] buffer = new byte[ 1024 ];
				while (-1 != (n = inputStream.read(buffer))) {
					output.write(buffer, 0, n);
				}
				return output.toByteArray();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return new byte[0];
	}


}
