package org.egov.bookings.service.impl;

import javax.transaction.Transactional;

import org.egov.bookings.contract.OsbmApproverRequest;
import org.egov.bookings.model.OsbmApproverModel;
import org.egov.bookings.repository.OsbmApproverRepository;
import org.egov.bookings.service.OsbmApproverService;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OsbmApproverServiceImpl implements OsbmApproverService{

	@Autowired
	private OsbmApproverRepository osbmApproverRepository; 
	
	/* (non-Javadoc)
	 * @see org.egov.bookings.service.OsbmApproverService#createOsbmApprover(org.egov.bookings.contract.OsbmApproverRequest)
	 */
	@Override
	public OsbmApproverModel createOsbmApprover(OsbmApproverRequest osbmApproverRequest) {
		OsbmApproverModel osbmApproverModel = null;
		try {
			osbmApproverModel = new OsbmApproverModel();
//			osbmApproverModel.setSector(osbmApproverRequest.getSector());
//			osbmApproverModel.setUuid(osbmApproverRequest.getUuid());
			osbmApproverModel = osbmApproverRepository.save(osbmApproverModel);

		}catch (Exception e) {
			throw new CustomException("APPROVER_SAVE_ERROR", "ERROR WHILE SAVING OSBM SECTOR");
		}
		return osbmApproverModel;
	}

}
