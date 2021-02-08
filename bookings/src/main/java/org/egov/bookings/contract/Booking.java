package org.egov.bookings.contract;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.egov.bookings.model.BookingsModel;
import org.egov.bookings.model.OsujmNewLocationModel;

// TODO: Auto-generated Javadoc
/**
 * The Class Booking.
 */
public class Booking implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3683257254333784296L;
	
	/** The booking model list. */
	private List< BookingsModel > bookingsModelList;
	
	/** The bookings count. */
	private int bookingsCount;
	
	/** The document map. */
	private Map< String, String > documentMap;
	
	/** The business service. */
	private String businessService;
	
	/** The osujm new location model list. */
	private List< OsujmNewLocationModel > osujmNewLocationModelList;
	
	/** The osujm newlocation map. */
	private Map< String, List< OsujmNewLocationFields > > osujmNewlocationMap;
	
	/** The document list. */
	private List<DocumentFields> documentList;
	
	/**
	 * Gets the bookings model list.
	 *
	 * @return the bookings model list
	 */
	public List<BookingsModel> getBookingsModelList() {
		return bookingsModelList;
	}

	/**
	 * Sets the bookings model list.
	 *
	 * @param bookingsModelList the new bookings model list
	 */
	public void setBookingsModelList(List<BookingsModel> bookingsModelList) {
		this.bookingsModelList = bookingsModelList;
	}

	/**
	 * Gets the bookings count.
	 *
	 * @return the bookings count
	 */
	public int getBookingsCount() {
		return bookingsCount;
	}
	
	/**
	 * Sets the bookings count.
	 *
	 * @param bookingsCount the new bookings count
	 */
	public void setBookingsCount(int bookingsCount) {
		this.bookingsCount = bookingsCount;
	}

	/**
	 * Gets the document map.
	 *
	 * @return the document map
	 */
	public Map< String, String > getDocumentMap() {
		return documentMap;
	}

	/**
	 * Sets the document map.
	 *
	 * @param documentMap the document map
	 */
	public void setDocumentMap(Map< String, String > documentMap) {
		this.documentMap = documentMap;
	}

	/**
	 * Gets the business service.
	 *
	 * @return the business service
	 */
	public String getBusinessService() {
		return businessService;
	}

	/**
	 * Sets the business service.
	 *
	 * @param businessService the new business service
	 */
	public void setBusinessService(String businessService) {
		this.businessService = businessService;
	}

	/**
	 * Gets the osujm new location model list.
	 *
	 * @return the osujm new location model list
	 */
	public List< OsujmNewLocationModel > getOsujmNewLocationModelList() {
		return osujmNewLocationModelList;
	}

	/**
	 * Sets the osujm new location model list.
	 *
	 * @param osujmNewLocationModelList the new osujm new location model list
	 */
	public void setOsujmNewLocationModelList(List< OsujmNewLocationModel > osujmNewLocationModelList) {
		this.osujmNewLocationModelList = osujmNewLocationModelList;
	}

	public Map<String, List<OsujmNewLocationFields>> getOsujmNewlocationMap() {
		return osujmNewlocationMap;
	}

	public void setOsujmNewlocationMap(Map<String, List<OsujmNewLocationFields>> osujmNewlocationMap) {
		this.osujmNewlocationMap = osujmNewlocationMap;
	}

	/**
	 * Gets the document list.
	 *
	 * @return the document list
	 */
	public List<DocumentFields> getDocumentList() {
		return documentList;
	}

	/**
	 * Sets the document list.
	 *
	 * @param documentList the new document list
	 */
	public void setDocumentList(List<DocumentFields> documentList) {
		this.documentList = documentList;
	}
}
