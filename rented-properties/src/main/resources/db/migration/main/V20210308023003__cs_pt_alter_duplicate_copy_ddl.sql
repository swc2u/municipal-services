ALTER TABLE cs_pt_duplicate_ownership_application 
 ADD COLUMN IF NOT EXISTS payment_amount numeric(13,6);
 
 ALTER TABLE cs_pt_duplicate_ownership_application 
 ADD COLUMN IF NOT EXISTS bank_name CHARACTER VARYING (100);
 
 ALTER TABLE cs_pt_duplicate_ownership_application 
 ADD COLUMN IF NOT EXISTS transaction_number CHARACTER VARYING (100);
 
 
 ALTER TABLE cs_pt_duplicate_ownership_application_audit 
 ADD COLUMN IF NOT EXISTS payment_amount numeric(13,6);
 
 ALTER TABLE cs_pt_duplicate_ownership_application_audit 
 ADD COLUMN IF NOT EXISTS bank_name CHARACTER VARYING (100);
 
 ALTER TABLE cs_pt_duplicate_ownership_application_audit 
 ADD COLUMN IF NOT EXISTS transaction_number CHARACTER VARYING (100);
 
 
