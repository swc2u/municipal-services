-- Table: public.nulm_sep_application_detail

-- DROP TABLE public.nulm_sep_application_detail;

CREATE TABLE public.nulm_sep_application_detail
(
  application_uuid character varying(64) NOT NULL,
  application_id character varying(64) NOT NULL,
  nulm_application_id character varying(64),
  application_status character varying(64),
  name character varying(255),
  gender character varying(255),
  age integer,
  dob timestamp without time zone,
  adhar_no character varying(20),
  mother_name character varying(255),
  father_or_husband_name character varying(255),
  occupation character varying(255),
  address character varying(255),
  contact character varying(255),
  since_how_long_in_chandigarh character varying(255),
  qualification character varying(255),
  category character varying(255),
  is_urban_poor boolean,
  is_minority boolean,
  is_handicapped boolean,
  is_loan_from_bankinginstitute boolean,
  is_repayment_made boolean,
  bpl_no character varying(255),
  minority character varying(255),
  type_of_business_to_be_started character varying(255),
  previous_experience character varying(255),
  place_of_work character varying(255),
  bank_details character varying(255),
  no_of_family_members character varying(255),
  project_cost numeric,
  loan_amount numeric,
  recommended_amount numeric,
  recommended_by character varying(255),
  representative_name character varying(255),
  representative_address character varying(255),
  tenant_id character varying(256),
  remark character varying(256),
  is_active boolean,
  created_by character varying(64),
  created_time bigint,
  last_modified_by character varying(64),
  last_modified_time bigint,
  CONSTRAINT nulm_sep_application_detail_pkey PRIMARY KEY (application_uuid),
  CONSTRAINT application_id UNIQUE (application_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.nulm_sep_application_detail
  OWNER TO postgres;
