
CREATE TABLE public.nulm_susv_application_document
(
  document_uuid character varying(64) NOT NULL,
  filestore_id character varying NOT NULL,
  application_uuid character varying(64) NOT NULL,
  document_type character varying(256) NOT NULL,
  tenant_id character varying(256),
  is_active boolean NOT NULL,
  created_by character varying(64),
  created_time bigint,
  last_modified_by character varying(256),
  last_modified_time bigint,
  CONSTRAINT nulm_usv_application_document_pkey PRIMARY KEY (document_uuid),
  CONSTRAINT susv_application_uuid_document_type UNIQUE (application_uuid, document_type, tenant_id)
)