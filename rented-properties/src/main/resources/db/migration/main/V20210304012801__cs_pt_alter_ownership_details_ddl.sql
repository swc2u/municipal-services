ALTER TABLE cs_pt_ownershipdetails_v1 
 ADD COLUMN IF NOT EXISTS payment_amount numeric(13,6);
 
 ALTER TABLE cs_pt_ownershipdetails_v1 
 ADD COLUMN IF NOT EXISTS bankname CHARACTER VARYING (100);
 
 ALTER TABLE cs_pt_ownershipdetails_v1 
 ADD COLUMN IF NOT EXISTS transactionnumber CHARACTER VARYING (100);
 
 
 ALTER TABLE cs_pt_ownershipdetails_audit_v1 
 ADD COLUMN IF NOT EXISTS payment_amount numeric(13,6);
 
 ALTER TABLE cs_pt_ownershipdetails_audit_v1 
 ADD COLUMN IF NOT EXISTS bankname CHARACTER VARYING (100);
 
 ALTER TABLE cs_pt_ownershipdetails_audit_v1 
 ADD COLUMN IF NOT EXISTS transactionnumber CHARACTER VARYING (100);
 
 
