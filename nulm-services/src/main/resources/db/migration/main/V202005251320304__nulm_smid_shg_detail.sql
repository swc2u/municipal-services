-- Table: public.nulm_smid_shg_detail

-- DROP TABLE public.nulm_smid_shg_detail;

CREATE TABLE public.nulm_smid_shg_detail
(
  shg_uuid character varying(256) NOT NULL,
  shg_id character varying(256) NOT NULL,
  name character varying(256) NOT NULL,
  type character varying(256),
  groups_nominated_by character varying(256),
  formed_through character varying(256),
  status character varying(256) NOT NULL,
  address character varying(256),
  contact_no character varying(256),
  date_of_formation timestamp without time zone,
  account_no character varying(256),
  date_of_opening_account timestamp without time zone,
  bank_name character varying(256),
  branch_name character varying(256),
  main_activity character varying(256),
  remark character varying(256),
  tenant_id character varying(256),
  is_active boolean,
  created_by character varying(64),
  created_time bigint,
  last_modified_by character varying(64),
  last_modified_time bigint,
  CONSTRAINT nulm_smid_shg_detail_pkey PRIMARY KEY (shg_uuid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.nulm_smid_shg_detail
  OWNER TO postgres;
