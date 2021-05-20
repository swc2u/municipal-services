package org.egov.bookings.contract;

import java.io.Serializable;
import java.util.List;

import org.egov.bookings.model.CommercialGrndAvailabilityModel;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommercialGrndAvailabiltyLockRequest implements Serializable {

	private static final long serialVersionUID = -3683257254333784296L;
	
	private List<CommercialGrndAvailabilityModel> commercialGrndAvailabilityLock;
}
