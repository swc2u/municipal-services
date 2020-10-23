ALTER TABLE cs_ep_application_v1
ADD COLUMN bank_name character varying(64);

ALTER TABLE cs_ep_application_v1
ADD COLUMN transaction_number character varying(64);

ALTER TABLE cs_ep_application_v1
ADD COLUMN amount numeric(12,2);

ALTER TABLE cs_ep_application_v1
ADD COLUMN payment_type character varying(64);
