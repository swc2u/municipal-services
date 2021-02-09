package org.egov.bookings.contract;

import java.io.Serializable;

/**
 * The Class BookingConfigJsonFields.
 */
public class BookingConfigJsonFields implements Serializable{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6055478037205618088L;

	/** The key. */
	private String key;
	
	/** The value. */
	private String value;

	/**
	 * Gets the key.
	 *
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Sets the key.
	 *
	 * @param key the new key
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	
}
