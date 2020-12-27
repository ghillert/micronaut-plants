/**
  You may need to do this manually as super-user permissions are required.
 */
CREATE EXTENSION postgis;

CREATE SEQUENCE hibernate_sequence start with 1 increment by 1;

CREATE TABLE PLANTS (
    id bigint not null,
    created_by varchar(255),
    created_on timestamp,
    updated_by varchar(255),
    updated_on timestamp,
    version integer,
    common_name varchar(255),
    genus varchar(255),
    species varchar(255),
    primary key (id)
);

SELECT AddGeometryColumn ('public','plants','location',4326,'POINT',2);
