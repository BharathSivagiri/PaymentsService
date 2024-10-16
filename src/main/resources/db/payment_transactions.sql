CREATE TABLE payment_transactions (
  id int NOT NULL AUTO_INCREMENT,
  payment_mode enum('UPI', 'CREDIT_CARD', 'DEBIT_CARD') DEFAULT NULL,
  amount_paid double NOT NULL,
  event_id varchar(10) NOT NULL,
  bank_id int DEFAULT NULL,
  transaction_type enum('CREDIT', 'DEBIT') DEFAULT NULL,
  payment_status enum('PAID', 'NOT_PAID','PAY_CANCELLED') DEFAULT NULL,
  created_by varchar(50) NOT NULL, 
  created_date timestamp NOT NULL,
  record_status enum('ACTIVE','INACTIVE') DEFAULT NULL,
  last_updated_date timestamp NOT NULL,
  last_updated_by varchar(50) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT bank_id FOREIGN KEY (bank_id) REFERENCES user_bank_account (id)
);

describe user_transactions;

select * from user_transactions;