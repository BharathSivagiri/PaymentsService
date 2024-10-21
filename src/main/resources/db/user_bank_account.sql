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

INSERT INTO user_bank_account (user_name, user_account_no, account_balance, created_by, created_date, record_status, last_updated_date, last_updated_by)
VALUES
('John Doe', 'ACC001', 5000.00, 'System', CURRENT_TIMESTAMP, 'ACTIVE', CURRENT_TIMESTAMP, 'System'),
('Jane Smith', 'ACC002', 3500.50, 'System', CURRENT_TIMESTAMP, 'ACTIVE', CURRENT_TIMESTAMP, 'System'),
('Mike Johnson', 'ACC003', 2750.75, 'System', CURRENT_TIMESTAMP, 'ACTIVE', CURRENT_TIMESTAMP, 'System'),
('Sarah Brown', 'ACC004', 4200.25, 'System', CURRENT_TIMESTAMP, 'ACTIVE', CURRENT_TIMESTAMP, 'System'),
('Alex Wilson', 'ACC005', 6100.00, 'System', CURRENT_TIMESTAMP, 'ACTIVE', CURRENT_TIMESTAMP, 'System');
