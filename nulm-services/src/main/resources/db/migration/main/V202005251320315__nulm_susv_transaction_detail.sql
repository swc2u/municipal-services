
CREATE TABLE public.nulm_susv_transaction_detail
(
  uuid character varying(64) NOT NULL,
  transaction_type character varying(64) NOT NULL,
  amount character varying(64),
  mode_of_payment character varying(64),
  payment_details character varying(255),
  donation_received_from character varying(255),
  donor_details character varying(20),
  expenditure_type character varying(255),
  expenditure_details character varying(255),
  email_id character varying(255),
  comments character varying(255),
  tenant_id character varying(256),
  remark character varying(256),
  is_active boolean,
  created_by character varying(64),
  created_time bigint,
  last_modified_by character varying(64),
  last_modified_time bigint,
  CONSTRAINT nulm_susv_transaction_detail_pkey PRIMARY KEY (uuid)
)