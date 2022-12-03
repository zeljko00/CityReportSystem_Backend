GRANT SELECT,UPDATE on cityreportsystemdb.`report` to 'cityOfficialUser'@'%';
GRANT SELECT,INSERT,UPDATE on cityreportsystemdb.`event` to 'cityOfficialUser'@'%';
GRANT SELECT on cityreportsystemdb.`citizen` to 'cityOfficialUser'@'%';
GRANT SELECT on cityreportsystemdb.`city_service` to 'cityOfficialUser'@'%';

GRANT SELECT,INSERT on cityreportsystemdb.`report` to 'citizenUser'@'%';
GRANT SELECT on cityreportsystemdb.`event` to 'citizenUser'@'%';
GRANT SELECT,INSERT on cityreportsystemdb.`citizen` to 'citizenUser'@'%';
GRANT SELECT on cityreportsystemdb.`city_service` to 'citizenUser'@'%';

GRANT ALL PRIVILEGES on cityreportsystemdb to 'root'@'localhost'