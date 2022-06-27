CREATE TABLE beer_ingredients
(
    beer_id BIGINT NOT NULL,
    name    VARCHAR(255) NULL,
    ratio   DOUBLE NULL
);

CREATE TABLE beers
(
    id        BIGINT AUTO_INCREMENT NOT NULL,
    beer_name VARCHAR(255) NULL,
    brand     VARCHAR(255) NULL,
    type      VARCHAR(255) NULL,
    price     INT NULL,
    alcohol   DOUBLE NULL,
    CONSTRAINT pk_beers PRIMARY KEY (id)
);

CREATE TABLE beers_webshops
(
    beers_id    BIGINT NOT NULL,
    webshops_id BIGINT NOT NULL
);

CREATE TABLE webshops
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    name          VARCHAR(255) NULL,
    email_address VARCHAR(255) NULL,
    CONSTRAINT pk_webshops PRIMARY KEY (id)
);

ALTER TABLE beer_ingredients
    ADD CONSTRAINT fk_beer_ingredients_on_beer FOREIGN KEY (beer_id) REFERENCES beers (id);

ALTER TABLE beers_webshops
    ADD CONSTRAINT fk_beeweb_on_beer FOREIGN KEY (beers_id) REFERENCES beers (id);

ALTER TABLE beers_webshops
    ADD CONSTRAINT fk_beeweb_on_webshop FOREIGN KEY (webshops_id) REFERENCES webshops (id);