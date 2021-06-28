CREATE TABLE nulm_smid_alf_details
(
  uuid character varying(64) NOT NULL,
  id character varying(64) NOT NULL,
  date_of_formation character varying(64),
  name character varying(255),
  registeration_date character varying(255),
  address character varying(255),
  account_number character varying(255),
  bank_name character varying(255),
  branch_name character varying(255),
  contact_number character varying(255),
  tenant_id character varying(256),
  is_active boolean,
  created_by character varying(64),
  created_time bigint,
  last_modified_by character varying(64),
  last_modified_time bigint
);