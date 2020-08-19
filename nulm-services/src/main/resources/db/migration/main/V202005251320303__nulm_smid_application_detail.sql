-- Table: public.nulm_smid_application_detail

-- DROP TABLE public.nulm_smid_application_detail;

CREATE TABLE public.nulm_smid_application_detail
(
  application_uuid character varying(64) NOT NULL,
  application_id character varying(64) NOT NULL,
  nulm_application_id character varying(64),
  application_status character varying(64),
  name character varying(255),
  gender character varying(255),
  dob timestamp without time zone,
  date_of_opening_account timestamp without time zone,
  adhar_no character varying(20),
  mother_name character varying(255),
  father_or_husband_name character varying(255),
  address character varying(255),
  phone_no character varying(255),
  mobile_no character varying(255),
  qualification character varying(255),
  email_id character varying(255),
  is_urban_poor boolean,
  is_minority boolean,
  is_pwd boolean,
  is_street_vendor boolean,
  is_homeless boolean,
  is_insurance boolean,
  bpl_no character varying(255),
  minority character varying(255),
  caste character varying(255),
  ward_no character varying(255),
  name_as_per_adhar character varying(255),
  adhar_acknowledgement_no character varying(255),
  insurance_through character varying(255),
  document_attachemnt character varying(255),
  account_no character varying(255),
  bank_name character varying(255),
  branch_name character varying(255),
  remark character varying(256),
  tenant_id character varying(256),
  is_active boolean,
  created_by character varying(64),
  created_time bigint,
  last_modified_by character varying(64),
  last_modified_time bigint,
  CONSTRAINT nulm_smid_application_detail_pkey PRIMARY KEY (application_uuid),
  CONSTRAINT smid_application_id UNIQUE (application_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.nulm_smid_application_detail
  OWNER TO postgres;
