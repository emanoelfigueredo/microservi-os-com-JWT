ALTER TABLE usuarios ADD roles VARCHAR(50) AFTER senha;
ALTER TABLE usuarios ADD active BIT AFTER roles;