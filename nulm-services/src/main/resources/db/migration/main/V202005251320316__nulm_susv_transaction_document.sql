
CREATE TABLE public.nulm_susv_transaction_document
(
  document_uuid character varying(64) NOT NULL,
  filestore_id character varying NOT NULL,
  uuid character varying(64) NOT NULL,
  document_type character varying(256) NOT NULL,
  tenant_id character varying(256),
  is_active boolean NOT NULL,
  created_by character varying(64),
  created_time bigint,
  last_modified_by character varying(256),
  last_modified_time bigint,
  CONSTRAINT nulm_susv_transaction_document_pkey PRIMARY KEY (document_uuid),
  CONSTRAINT nulm_susv_transaction_document_uuid FOREIGN KEY (uuid)
      REFERENCES public.nulm_susv_transaction_detail (uuid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT nulm_susv_transaction_document_type UNIQUE (uuid, document_type, tenant_id)
)