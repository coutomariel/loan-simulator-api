CREATE TABLE IF NOT EXISTS interest_rate (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    min_age INT NOT NULL,
    max_age INT,
    annual_rate DECIMAL(5, 2) NOT NULL
);

DELETE FROM interest_rate;

-- Populando com os dados iniciais
INSERT INTO interest_rate (min_age, max_age, annual_rate) VALUES
(0, 25, 0.05),      -- Até 25 anos: 5% ao ano
(26, 40, 0.03),     -- De 26 a 40 anos: 3% ao ano
(41, 60, 0.02),     -- De 41 a 60 anos: 2% ao ano
(61, NULL, 0.04);   -- Acima de 60 anos: 4% ao ano