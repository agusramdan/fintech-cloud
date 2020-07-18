insert into account (number,version,name,balance,overdraft)
values
('123456789',1,'',10,100000000)
,('234567891',1,'',10,100000000)
,('UPDATE-INT-TEST-ACCOUNT',3,'',10,100000000)
,('UPDATE-TEST-FAIL',3,'',10,100000000)
,('UPDATE-INT-BAL-NOT-CHANGE',3,'UPDATE INT BAL NOT CHANGE',11,100000000)
,('HISTORY-ACCOUNT-D',3,'HISTORY ACCOUNT D',90,100000000)
,('HISTORY-ACCOUNT-K',3,'HISTORY ACCOUNT K',110,100000000)
,('TRF-ACCOUNT-D',3,'HISTORY ACCOUNT D',100,100000000)
,('TRF-ACCOUNT-K',3,'HISTORY ACCOUNT K',200,100000000)

;

insert into journal (number,trx_time,amount,remark1,remark2)
values
('TEST-001','2020-01-02',10,NULL,NULL)
;
-- debit - , credit +
insert into detail (number,idx,trx_time,account,amount,balance,remark1,remark2)
values
('TEST-001',1,'2020-01-02','HISTORY-ACCOUNT-D',-10,90,NULL,NULL)
,('TEST-001',2,'2020-01-02','HISTORY-ACCOUNT-K',10,110,NULL,NULL)
;