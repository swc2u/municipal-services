Create table testForStore(
	id varchar(50),
	tenantId varchar(128) NOT NULL 
);
alter table material add constraint pk_material primary key (id,tenantId);