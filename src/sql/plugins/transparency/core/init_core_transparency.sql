
--
-- Data for table core_admin_right
--
DELETE FROM core_admin_right WHERE id_right = 'TRANSPARENCY_LOBBIES_MANAGEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('TRANSPARENCY_LOBBIES_MANAGEMENT','transparency.adminFeature.ManageLobbies.name',3,'jsp/admin/plugins/transparency/ManageLobbies.jsp','transparency.adminFeature.ManageLobbies.description',0,'transparency',NULL,NULL,NULL,4);


--
-- Data for table core_user_right
--
DELETE FROM core_user_right WHERE id_right = 'TRANSPARENCY_LOBBIES_MANAGEMENT';
INSERT INTO core_user_right (id_right,id_user) VALUES ('TRANSPARENCY_LOBBIES_MANAGEMENT',1);


--
-- Data for table core_admin_right
--
DELETE FROM core_admin_right WHERE id_right = 'TRANSPARENCY_APPOINTMENTS_MANAGEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('TRANSPARENCY_APPOINTMENTS_MANAGEMENT','transparency.adminFeature.ManageAppointements.name',3,'jsp/admin/plugins/transparency/ManageAppointments.jsp','transparency.adminFeature.ManageAppointements.description',0,'transparency',NULL,NULL,NULL,4);


--
-- Data for table core_user_right
--
DELETE FROM core_user_right WHERE id_right = 'TRANSPARENCY_APPOINTMENTS_MANAGEMENT';
INSERT INTO core_user_right (id_right,id_user) VALUES ('TRANSPARENCY_APPOINTMENTS_MANAGEMENT',1);


