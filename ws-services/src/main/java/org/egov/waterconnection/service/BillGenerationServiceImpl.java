package org.egov.waterconnection.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.egov.tracer.model.CustomException;
import org.egov.waterconnection.constants.WCConstants;
import org.egov.waterconnection.model.AuditDetails;
import org.egov.waterconnection.model.BillGeneration;
import org.egov.waterconnection.model.BillGenerationFile;
import org.egov.waterconnection.model.BillGenerationRequest;
import org.egov.waterconnection.repository.BillGenerationDao;
import org.egov.waterconnection.util.WaterServicesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BillGenerationServiceImpl implements BillGenerationService {

	@Autowired
	private BillGenerationDao billRepository;

	@Autowired
	private WaterServicesUtil waterServicesUtil;

	@Override
	public List<BillGeneration> saveBillingData(BillGenerationRequest billGenerationRequest) {
		DateFormat dateParser = new SimpleDateFormat("dd/MMM/yy");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		//System.out.println(dateFormatter.format("20/OCT/20"));

	     
		InputStream input = null;
		List<BillGeneration> listOfBills = new ArrayList<BillGeneration>();
		try {
 
			input = new URL(billGenerationRequest.getBillGeneration().getDocument().getFileStroreUrl()).openStream();

			AuditDetails auditDetails = waterServicesUtil
					.getAuditDetails(billGenerationRequest.getRequestInfo().getUserInfo().getUuid(), true);
			try (BufferedReader br = new BufferedReader(new InputStreamReader(input, "UTF-8"))) {
				String line;
				
				while ((line = br.readLine()) != null) {
					String[] values = line.split(",");
					List<String> bill = Arrays.asList(values);
					
					BillGeneration uploadFileData = BillGeneration.builder()
							.billGenerationId(UUID.randomUUID().toString()).auditDetails(auditDetails).isFileGenerated(false).status(WCConstants.STATUS_INITIATED).
							ccCode(bill.get(0).trim()).divSdiv(bill.get(1).trim()).consumerCode(bill.get(2).trim()).billCycle(bill.get(3).trim()).billGroup(bill.get(4).trim()).subGroup(bill.get(5).trim())
							.billType(bill.get(6).trim()).name(bill.get(7).trim()).address(bill.get(8).trim()).cessCharge(bill.get(14).trim()).netAmount(bill.get(15).trim()).surcharge(bill.get(16).trim())
							.grossAmount(bill.get(17).trim()).totalNetAmount(bill.get(18).trim()).totalSurcharge(bill.get(19).trim()).totalSurcharge(bill.get(20).trim())
							.dueDateCash(dateFormatter.format(dateParser.parse(bill.get(23).trim()))).dueDateCheque(dateFormatter.format(dateParser.parse(bill.get(24).trim()))).build();

					listOfBills.add(uploadFileData);
				}
			}
			

			billRepository.saveBillingData(listOfBills);
			return listOfBills;


		} catch (Exception e) {
			throw new CustomException("EXCELREADERROR", e.getMessage());
		} finally {

			try {
				input.close();
			} catch (IOException e) {
				throw new CustomException("EXCELREADERROR", e.getMessage());
			}
		}

	}

	@Override
	public List<BillGenerationFile> generateBillFile(BillGenerationRequest billGenerationRequest) {
		PrintWriter writer;
		List<BillGenerationFile> billFileList = new ArrayList<BillGenerationFile>();
		try {
			String timeStamp = new SimpleDateFormat("hh:mm:ss a").format(new Date());

			List<BillGeneration> bill = billRepository.getBillingEstimation();

			if (bill.isEmpty()) {
				throw new CustomException("FILE_GENERATION_FAILED",
						"Data may not present or may have downloaded earlier please check history");
			}

			writer = new PrintWriter(WCConstants.WS_BILLING_FILENAME, "UTF-8");
			for (BillGeneration billGeneration : bill) {
				writer.println(billGeneration.getCcCode() + " " + timeStamp + " " + billGeneration.getDivSdiv()
						+ billGeneration.getConsumerCode() + " " + billGeneration.getTotalNetAmount() + " "
						+ billGeneration.getBillId());
			}

			writer.close();
			BillGenerationFile billFile = billRepository.getFilesStoreUrl();

			billRepository.savefileHistory(billFile, bill);
			billFileList.add(billFile);

		} catch (Exception e) {
			throw new CustomException("FILE_GENERATION_FAILED", e.getMessage());
		}
		return billFileList;
	}

	@Override
	public List<BillGenerationFile> getBillingFiles() {
		List<BillGenerationFile> billFile = billRepository.getBillingFiles();
		return billFile;
	}

	@Override
	public List<BillGeneration> getBillData(BillGenerationRequest billGenerationRequest) {
		List<BillGeneration> billData = billRepository.getBillData(billGenerationRequest.getBillGeneration());
		if (billData.isEmpty()) {
			throw new CustomException("BILL_DATA_NOT_FOUND", "Bill data not available for given consumer code");
		}
		return billData;
	}

}
