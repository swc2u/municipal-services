-- Table: public.nulm_organization

-- DROP TABLE public.nulm_organization;

CREATE TABLE public.nulm_organization
(
  organization_uuid character varying(64) NOT NULL,
  user_id bigint NOT NULL,
  organization_name character varying(64),
  address character varying(64),
  email_id character varying(64),
  representative_name character varying(64),
  mobile_no character varying(64),
  registration_no character varying(64),
  tenant_id character varying(256) NOT NULL,
  is_active boolean,
  created_by character varying(64),
  created_time bigint,
  last_modified_by character varying(64),
  last_modified_time bigint,
  CONSTRAINT nulm_organization_pkey PRIMARY KEY (organization_uuid, tenant_id),
  CONSTRAINT nulm_organization_name_tenant UNIQUE (organization_name, tenant_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.nulm_organization
  OWNER TO postgres;
