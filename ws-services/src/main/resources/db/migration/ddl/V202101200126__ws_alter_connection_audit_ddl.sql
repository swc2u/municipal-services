
ALTER TABLE eg_ws_connection_audit  
ADD COLUMN cccode character varying,
ADD COLUMN div character varying,
ADD COLUMN subdiv character varying,
ADD COLUMN ledger_no character varying,
ADD COLUMN ledgergroup character varying;


ALTER TABLE eg_ws_service_audit  
ADD COLUMN meterCount varchar,
ADD COLUMN meterRentCode varchar,
ADD COLUMN mfrCode varchar,
ADD COLUMN meterDigits varchar,
ADD COLUMN meterUnit varchar,   
ADD COLUMN sanctionedCapacity varchar ;