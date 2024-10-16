CREATE TABLE user_bank_account (
  id int NOT NULL AUTO_INCREMENT,
  user_name varchar(20) NOT NULL,
  user_account_no varchar(50) NOT NULL,
  account_balance double NOT NULL,
  created_by varchar(50) NOT NULL, 
  created_date timestamp NOT NULL,
  record_status enum('ACTIVE','INACTIVE') DEFAULT NULL,
  last_updated_date timestamp NOT NULL,
  last_updated_by varchar(50) NOT NULL,
  PRIMARY KEY (id)
);

describe user_bank_account;

select * from user_bank_account;