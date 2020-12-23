ALTER TABLE IMAGES
    ADD CONSTRAINT "FK_PLANTS_IMAGES" FOREIGN KEY (plant_id)
    REFERENCES PLANTS (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;
CREATE INDEX "FKI_FK_PLANTS_IMAGES"
    ON IMAGES(plant_id);