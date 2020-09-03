package org.egov.assets.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.egov.assets.common.DomainService;
import org.egov.assets.common.Pagination;
import org.egov.assets.model.MaterialBalanceRate;
import org.egov.assets.model.MaterialReceipt;
import org.egov.assets.model.MaterialReceiptDetail;
import org.egov.assets.model.MaterialReceiptDetailSearch;
import org.egov.assets.model.MaterialReceiptSearch;
import org.egov.assets.model.PurchaseOrderDetail;
import org.egov.assets.model.PurchaseOrderDetailSearch;
import org.egov.assets.model.Store;
import org.egov.assets.model.StoreGetRequest;
import org.egov.assets.model.Supplier;
import org.egov.assets.model.SupplierGetRequest;
import org.egov.assets.model.SupplierResponse;
import org.egov.assets.repository.MaterialReceiptJdbcRepository;
import org.egov.assets.repository.StoreJdbcRepository;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MaterialReceiptService extends DomainService {

	@Autowired
	private MaterialReceiptJdbcRepository materialReceiptJdbcRepository;

	@Autowired
	private MaterialReceiptDetailService materialReceiptDetailService;

	@Autowired
	private StoreJdbcRepository storeJdbcRepository;

	@Autowired
	private PurchaseOrderDetailService purchaseOrderDetailService;

	@Autowired
	private MaterialService materialService;

	@Autowired
	private SupplierService supplierService;

	public Pagination<MaterialReceipt> search(MaterialReceiptSearch materialReceiptSearch) {
		Pagination<MaterialReceipt> materialReceiptPagination = materialReceiptJdbcRepository
				.search(materialReceiptSearch);

		if (!materialReceiptPagination.getPagedData().isEmpty()) {
			for (MaterialReceipt materialReceipt : materialReceiptPagination.getPagedData()) {
				materialReceipt.receivingStore(
						getStore(materialReceiptSearch.getTenantId(), materialReceipt.getReceivingStore().getCode()));

				if (materialReceipt.getIssueingStore() != null)
					materialReceipt.issueingStore(getStore(materialReceiptSearch.getTenantId(),
							materialReceipt.getIssueingStore().getCode()));

				if (materialReceipt.getSupplier() != null)
					materialReceipt.setSupplier(
							getSupplier(materialReceipt.getSupplier().getCode(), materialReceiptSearch.getTenantId()));

				List<MaterialReceiptDetail> materialReceiptDetail = getMaterialReceiptDetail(
						materialReceipt.getMrnNumber(), materialReceiptSearch.getTenantId());
				materialReceipt.setReceiptDetails(materialReceiptDetail);
			}
		}
		return materialReceiptPagination;
	}

	public JSONArray getInventoryReport(MaterialReceiptSearch materialReceiptSearch) {
		return materialReceiptJdbcRepository.getInventoryReport(materialReceiptSearch);
	}

	private Supplier getSupplier(String code, String tenantId) {
		SupplierGetRequest supplierGetRequest = SupplierGetRequest.builder().code(Collections.singletonList(code))
				.tenantId(tenantId).active(true).build();
		SupplierResponse suppliers = supplierService.search(supplierGetRequest);
		if (!suppliers.getSuppliers().isEmpty()) {
			return suppliers.getSuppliers().get(0);
		}
		return null;
	}

	private Store getStore(String tenantId, String code) {
		StoreGetRequest storeEntity = StoreGetRequest.builder().code(Collections.singletonList(code)).tenantId(tenantId)
				.active(true).build();
		Pagination<Store> store = storeJdbcRepository.search(storeEntity);
		if (!store.getPagedData().isEmpty()) {
			return store.getPagedData().get(0);
		}
		return null;
	}

	public Pagination<MaterialBalanceRate> searchBalanceRate(MaterialReceiptSearch materialReceiptSearch) {
		return materialReceiptJdbcRepository.searchBalanceRate(materialReceiptSearch);
	}

	private List<MaterialReceiptDetail> getMaterialReceiptDetail(String mrnNumber, String tenantId) {
		MaterialReceiptDetailSearch materialReceiptDetailSearch = MaterialReceiptDetailSearch.builder()
				.mrnNumber(Arrays.asList(mrnNumber)).tenantId(tenantId).build();
		Pagination<MaterialReceiptDetail> materialReceiptDetails = materialReceiptDetailService
				.search(materialReceiptDetailSearch);

		if (!materialReceiptDetails.getPagedData().isEmpty()) {
			for (MaterialReceiptDetail detail : materialReceiptDetails.getPagedData()) {

				if (detail.getPurchaseOrderDetail() != null && detail.getPurchaseOrderDetail().getId() != null) {
					PurchaseOrderDetailSearch purchaseOrderDetailSearch = new PurchaseOrderDetailSearch();
					purchaseOrderDetailSearch.setIds(Arrays.asList(detail.getPurchaseOrderDetail().getId()));
					purchaseOrderDetailSearch.setTenantId(tenantId);
					Pagination<PurchaseOrderDetail> detailPagination = purchaseOrderDetailService
							.search(purchaseOrderDetailSearch);

					detail.setPurchaseOrderDetail(
							!detailPagination.getPagedData().isEmpty() ? detailPagination.getPagedData().get(0) : null);

				}

				detail.setMaterial(materialService.fetchMaterial(tenantId, detail.getMaterial().getCode(),
						new org.egov.common.contract.request.RequestInfo()));
			}
		}

		return !materialReceiptDetails.getPagedData().isEmpty() ? materialReceiptDetails.getPagedData()
				: Collections.EMPTY_LIST;
	}
}