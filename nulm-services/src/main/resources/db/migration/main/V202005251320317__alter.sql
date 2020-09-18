
alter table nulm_sep_application_detail
DROP COLUMN bank_details,
DROP COLUMN recommended_amount;

alter table nulm_sep_application_detail
ADD COLUMN account_name character varying(255),
ADD COLUMN bank_name character varying(255),
ADD COLUMN branch_name character varying(255),
ADD COLUMN is_disability_certificate_available Boolean,
ADD COLUMN committee_decision jsonb,
ADD COLUMN bank_processing_details jsonb,
ADD COLUMN sanction_details jsonb;

alter table nulm_suh_application_detail
ADD COLUMN assigned_to character varying(64) ;



ALTER TABLE public.nulm_organization ADD CONSTRAINT  nulm_organization_uuidt UNIQUE (organization_uuid);
ALTER TABLE public.nulm_suh_application_detail ADD CONSTRAINT fk_nulm_suh_application_detail FOREIGN KEY (assigned_to) REFERENCES public.nulm_organization(organization_uuid);