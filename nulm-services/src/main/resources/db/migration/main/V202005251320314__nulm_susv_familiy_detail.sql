
CREATE TABLE public.nulm_susv_familiy_detail
(
  uuid character varying(64) NOT NULL,
  application_uuid character varying(64) NOT NULL,
  name character varying(64),
  age character varying(64),
  relation character varying(64),
  tenant_id character varying(256),
  is_active boolean,
  created_by character varying(64),
  created_time bigint,
  last_modified_by character varying(64),
  last_modified_time bigint,
  CONSTRAINT nulm_susv_familiy_detail_pkey PRIMARY KEY (uuid),
  CONSTRAINT "FK_nulm_susv_familiy_detail_application_uuid" FOREIGN KEY (application_uuid)
      REFERENCES public.nulm_susv_application_detail (application_uuid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)