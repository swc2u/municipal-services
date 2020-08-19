
CREATE TABLE public.nulm_suh_occupancy_log
(
  log_uuid character varying(256) NOT NULL,
  name_of_shelter character varying(256),
  date timestamp without time zone,
  name character varying(256),
  qualification character varying(256),
  gender character varying(256),
  age character varying(256),
  address character varying(256),
  adhar_no character varying(256),
  reason_for_staying character varying(256),
  tenant_id character varying(256),
  is_active boolean,
  created_by character varying(64),
  created_time bigint,
  last_modified_by character varying(64),
  last_modified_time bigint,
  CONSTRAINT nulm_suh_occupancy_log_pkey PRIMARY KEY (log_uuid)
)