package org.egov.bookings.repository;

import org.egov.bookings.model.CommercialGrndAvailabilityModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommercialGrndAvailabilityRepository extends CrudRepository<CommercialGrndAvailabilityModel, String>{

}
