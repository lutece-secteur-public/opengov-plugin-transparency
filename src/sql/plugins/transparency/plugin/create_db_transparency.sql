
--
-- Structure for table transparency_appointment
--

DROP TABLE IF EXISTS transparency_appointment;
CREATE TABLE transparency_appointment (
id_appointment int AUTO_INCREMENT,
title varchar(255) default '' NOT NULL,
description long varchar,
start_date date NOT NULL,
end_date date,
type_id int default '0' NOT NULL,
type_label varchar(255) default '',
url varchar(255) default '',
contacts varchar(255) default '',
PRIMARY KEY (id_appointment)
);

--
-- Structure for table transparency_elected_official
--

DROP TABLE IF EXISTS transparency_elected_official;
CREATE TABLE transparency_elected_official (
id_elected_official int AUTO_INCREMENT,
first_name varchar(255) default '' NOT NULL,
last_name varchar(255) default '' NOT NULL,
title varchar(50) default '' NOT NULL,
PRIMARY KEY (id_elected_official)
);

--
-- Structure for table transparency_lobby
--

DROP TABLE IF EXISTS transparency_lobby;
CREATE TABLE transparency_lobby (
id_lobby int AUTO_INCREMENT,
name varchar(255) default '' NOT NULL,
national_id varchar(50) default '',
national_id_type varchar(50) default '',
url varchar(255) default '',
json_data long varchar,
version_date date NOT NULL,
PRIMARY KEY (id_lobby)
);


--
-- Structure for table transparency_delegation
--

DROP TABLE IF EXISTS transparency_delegation;
CREATE TABLE transparency_delegation (
id_delegation int AUTO_INCREMENT,
id_user int default '0' NOT NULL,
id_elected_official int default '0',
date_creation date NOT NULL,
PRIMARY KEY (id_delegation)
);

--
-- Structure for table transparency_elected_official_appointment
--

DROP TABLE IF EXISTS transparency_elected_official_appointment;
CREATE TABLE transparency_elected_official_appointment (
id_elected_official int default '0' NOT NULL,
id_appointment int default '0' NOT NULL,
PRIMARY KEY (id_electedofficial, id_appointment)
);

--
-- Structure for table transparency_lobby_appointment
--

DROP TABLE IF EXISTS transparency_lobby_appointment;
CREATE TABLE transparency_lobby_appointment (
id_lobby int default '0' NOT NULL,
id_appointment int default '0' NOT NULL,
PRIMARY KEY (id_lobby, id_appointment)
);