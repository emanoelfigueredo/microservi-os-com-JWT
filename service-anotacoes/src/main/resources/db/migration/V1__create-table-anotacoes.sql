<<<<<<< HEAD
CREATE TABLE anotacoes (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(50) NOT NULL,
    conteudo VARCHAR(2000) NOT NULL,
    momento DATETIME NOT NULL,
    PRIMARY KEY(id)
=======
CREATE TABLE anotacoes (
    id BIGINT(20) NOT NULL,
    titulo VARCHAR(50) NOT NULL,
    conteudo VARCHAR(2000) NOT NULL,
    momento DATETIME NOT NULL,
    PRIMARY KEY(id)
>>>>>>> 53d1cfec92158846490aa39cdc9c0d52f97104f5
);