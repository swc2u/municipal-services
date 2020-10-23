CREATE TABLE nulm_sep_application_document (
	document_uuid varchar(64) NOT NULL,
	filestore_id varchar NOT NULL,
	application_uuid varchar(64) NOT NULL,
	document_type varchar(256) NOT NULL,
	tenant_id varchar(256) NULL,
	is_active bool NOT NULL,
	created_by varchar(64) NULL,
	created_time int8 NULL,
	last_modified_by varchar(256) NULL,
	last_modified_time int8 NULL,
	CONSTRAINT application_uuid_document_type UNIQUE (application_uuid, document_type, tenant_id),
	CONSTRAINT nulm_sep_application_document_pkey PRIMARY KEY (document_uuid),
	CONSTRAINT fk_nulm_sep_application_document_uuid FOREIGN KEY (application_uuid) REFERENCES nulm_sep_application_detail(application_uuid)
);