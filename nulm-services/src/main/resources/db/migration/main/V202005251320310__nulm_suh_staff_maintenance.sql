
CREATE TABLE public.nulm_suh_staff_maintenance
(
  suh_uuid character varying(256) NOT NULL,
  staff_uuid character varying(256) NOT NULL,
  is_manager boolean,
  manager_remark character varying(256),
  is_security_staff boolean,
  security_staff_remark character varying(256),
  is_cleaner boolean,
  cleaner_remark character varying(256),
  tenant_id character varying(256) NOT NULL,
  is_active boolean,
  created_by character varying(64),
  created_time bigint,
  last_modified_by character varying(64),
  last_modified_time bigint,
  CONSTRAINT nulm_suh_staff_maintenance_pkey PRIMARY KEY (staff_uuid),
  CONSTRAINT nulm_suh_staff_maintenance_uk UNIQUE (staff_uuid, tenant_id)
)