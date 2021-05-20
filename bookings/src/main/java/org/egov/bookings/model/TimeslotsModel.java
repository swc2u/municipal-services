package org.egov.bookings.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "TimeslotsModel")
@Table(name = "BK_TIMESLOTS")
public class TimeslotsModel {

	@Id
	@Column(name = "ID")
	private String id;
	
	@Column(name = "slot")
	private String slot;
	
	@Column(name = "application_number")
	private String applicationNumber;
	
	
}
