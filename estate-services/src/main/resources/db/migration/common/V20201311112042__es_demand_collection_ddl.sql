DROP TABLE IF EXISTS cs_ep_demand;
DROP TABLE IF EXISTS cs_ep_payment;
DROP TABLE IF EXISTS cs_ep_collection;
DROP TABLE IF EXISTS cs_ep_account;
DROP TABLE IF EXISTS cs_ep_demand_audit;
DROP TABLE IF EXISTS cs_ep_account_audit;


--> Demand table
CREATE TABLE cs_ep_demand (
   id           			CHARACTER VARYING (256) NOT NULL,
   property_details_id      CHARACTER VARYING (256) NOT NULL,
   demandDate   			bigint,
   isPrevious       		boolean,
   rent  					numeric(13,6),
   penaltyInterest   		numeric(13,6),
   gstInterest				numeric(13,6),
   gst 						numeric(13,6),
   collectedRent 			numeric(13,6),
   collectedGST 			numeric(13,6),
   noOfDays 				numeric(13,6),
   paid 					numeric(13,6),
   mode						CHARACTER VARYING (64),
   status               	CHARACTER VARYING (64),
  
   created_by           CHARACTER VARYING (128) NOT NULL,
   last_modified_by     CHARACTER VARYING (128),
   created_time         bigint NOT NULL,
   last_modified_time   bigint,
  
  CONSTRAINT pk_cs_ep_demand PRIMARY KEY (id),
  CONSTRAINT fk_cs_ep_demand FOREIGN KEY (property_details_id) REFERENCES cs_ep_property_details_v1 (id)
);

--> Payment Table
CREATE TABLE cs_ep_payment (
   id           		   CHARACTER VARYING (256) NOT NULL,
   property_details_id          CHARACTER VARYING (256) NOT NULL,
   receiptNo	    	   CHARACTER VARYING(64),
   amountPaid   		   numeric(13,6),
   dateOfPayment   		bigint,
   mode					   CHARACTER VARYING (64),
   processed            BOOLEAN,
  
   created_by           CHARACTER VARYING (128),
   created_date         bigint,
   modified_by     		CHARACTER VARYING (128),
   modified_date       	bigint,
	
  CONSTRAINT pk_cs_ep_payment PRIMARY KEY (id), 
  CONSTRAINT fk_cs_ep_payment FOREIGN KEY (property_details_id) REFERENCES cs_ep_property_details_v1 (id)
);

--> Collection Table
CREATE TABLE cs_ep_collection (
   id           		CHARACTER VARYING (256) NOT NULL,
   demand_id            CHARACTER VARYING (256) NOT NULL,
   interestCollected    numeric(13,6),
   principalCollected   numeric(13,6),
  
   created_by           CHARACTER VARYING (128),
   created_date         bigint,
   collectedAt         bigint,
   modified_by     		CHARACTER VARYING (128),
   modified_date       	bigint,

  CONSTRAINT pk_cs_ep_collection PRIMARY KEY (id), 
  CONSTRAINT fk_cs_ep_collection_demand FOREIGN KEY (demand_id) REFERENCES cs_ep_demand (id) ON DELETE CASCADE
);

--> Account Table
CREATE TABLE cs_ep_account (
   id           		CHARACTER VARYING (256) NOT NULL,
   property_details_id          CHARACTER VARYING (256) NOT NULL,
   remainingAmount	    numeric(13,6),
  
   created_by           CHARACTER VARYING (128),
   created_date         bigint,
   modified_by     		CHARACTER VARYING (128),
   modified_date       	bigint,
	
  CONSTRAINT pk_cs_ep_account PRIMARY KEY (id), 
  CONSTRAINT fk_cs_ep_account_payment FOREIGN KEY (property_details_id) REFERENCES cs_ep_property_details_v1 (id)
);

--> Audit Tables
CREATE TABLE cs_ep_demand_audit(
   id           		CHARACTER VARYING (256) NOT NULL,
   property_details_id          CHARACTER VARYING (256) NOT NULL,
   initialGracePeriod   int,
   generationDate       bigint,
   collectionPrincipal  numeric(13,6),
   remainingPrincipal   numeric(13,6),
   interestSince		bigint,
   mode					CHARACTER VARYING (64),
   status            CHARACTER VARYING (64),
  
   created_by           CHARACTER VARYING (128),
   created_date         bigint,
   modified_by     		CHARACTER VARYING (128),
   modified_date       	bigint
);

CREATE TABLE cs_ep_account_audit (
   id           		CHARACTER VARYING (256) NOT NULL,
   property_details_id          CHARACTER VARYING (256) NOT NULL,
   remainingAmount	    numeric(13,6),
  
   created_by           CHARACTER VARYING (128),
   created_date         bigint,
   modified_by     		CHARACTER VARYING (128),
   modified_date       	bigint
);