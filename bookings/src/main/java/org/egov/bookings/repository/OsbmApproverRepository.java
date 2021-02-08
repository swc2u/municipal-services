package org.egov.bookings.repository;

import java.util.List;

import org.egov.bookings.model.OsbmApproverModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

// TODO: Auto-generated Javadoc
/**
 * The Interface OsbmApproverRepository.
 */
@Repository
public interface OsbmApproverRepository extends JpaRepository<OsbmApproverModel, String>{

	/**
	 * Find by sector.
	 *
	 * @param sector the sector
	 * @return the list
	 */
	public  List<OsbmApproverModel> findBySector(String sector);
	
	/**
	 * Find by uuid.
	 *
	 * @param uuid the uuid
	 * @return the osbm approver model
	 */
	public OsbmApproverModel findByUuid(String uuid);

	/**
	 * Find by uuid and sector.
	 *
	 * @param uuid the uuid
	 * @param sector the sector
	 * @return the osbm approver model
	 */
	public OsbmApproverModel findByUuidAndSector(String uuid, String sector);
	
	/**
	 * Find approver by limit.
	 *
	 * @param offSet the off set
	 * @return the list
	 */
	@Query(
			value = "SELECT * FROM bk_approver LIMIT 100 OFFSET (?1)",
			nativeQuery = true )
			List<OsbmApproverModel> findApproverByLimit( int offSet );
	
	/**
	 * Find by sector and uuid and role code and user id.
	 *
	 * @param sector the sector
	 * @param uuid the uuid
	 * @param roleCode the role code
	 * @param userId the user id
	 * @return the osbm approver model
	 */
	public OsbmApproverModel findBySectorAndUuidAndRoleCodeAndUserId(String sector, String uuid, String roleCode, Long userId);
	

		/**
	* Find by sector and role code.
	*
	* @param sector the sector
	* @param roleCode the role code
	* @return the osbm approver model
	*/
	public OsbmApproverModel findBySectorAndRoleCode(String sector, String roleCode);
	
	
}
