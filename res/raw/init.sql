BEGIN TRANSACTION;
CREATE TABLE match (overtime NUMERIC, scoreB TEXT, scoreA TEXT, id integer PRIMARY KEY autoincrement, teamA text, teamB text, matchTime datetime, lastModified datetime, desc text, tourid text, info text, comment text, follow Boolean, alarm Boolean, alarmId int);
INSERT INTO match VALUES(NULL,NULL,NULL,1,'波兰','希腊','2012-6-8 16:00','2012-5-1 20:19','揭幕战非常垃圾',1,'A组小组赛',NULL,'FALSE','FALSE',NULL);
INSERT INTO match VALUES(NULL,NULL,NULL,2,'俄罗斯','捷克','2012-6-8 18:45','2012-5-1 20:19','沙皇虐杀捷克',1,'A组小组赛',NULL,'FALSE','FALSE',NULL);
INSERT INTO match VALUES(NULL,NULL,NULL,3,'荷兰','丹麦','2012-6-9 16:00','2012-5-1 20:19','半神出场！',1,'B组小组赛',NULL,'FALSE','FALSE',NULL);
INSERT INTO match VALUES(NULL,NULL,NULL,4,'德国','葡萄牙','2012-6-9 18:45','2012-5-1 20:19','c罗复仇欧冠半决赛？',1,'B组小组赛',NULL,'FALSE','FALSE',NULL);
INSERT INTO match VALUES(NULL,NULL,NULL,5,'西班牙','意大利','2012-6-10 16:00','2012-5-1 20:19',NULL,1,'C组小组赛',NULL,'FALSE','FALSE',NULL);
INSERT INTO match VALUES(NULL,NULL,NULL,6,'爱尔兰','克罗地亚','2012-6-10 18:45','2012-5-1 20:19',NULL,1,'C组小组赛',NULL,'FALSE','FALSE',NULL);
INSERT INTO match VALUES(NULL,NULL,NULL,7,'法国','英格兰','2012-6-11 16:00','2012-5-1 20:19',NULL,1,'D组小组赛',NULL,'FALSE','FALSE',NULL);
INSERT INTO match VALUES(NULL,NULL,NULL,8,'乌克兰','瑞典','2012-6-11 18:45','2012-5-1 20:19',NULL,1,'D组小组赛',NULL,'FALSE','FALSE',NULL);
INSERT INTO match VALUES(NULL,NULL,NULL,9,'希腊','捷克','2012-6-12 16:00','2012-5-1 20:19',NULL,1,'A组小组赛',NULL,'FALSE','FALSE',NULL);
INSERT INTO match VALUES(NULL,NULL,NULL,10,'波兰','俄罗斯','2012-6-12 18:45','2012-5-1 20:19',NULL,1,'A组小组赛',NULL,'FALSE','FALSE',NULL);
INSERT INTO match VALUES(NULL,NULL,NULL,11,'丹麦','葡萄牙','2012-6-13 16:00','2012-5-1 20:19',NULL,1,'B组小组赛',NULL,'FALSE','FALSE',NULL);
INSERT INTO match VALUES(NULL,NULL,NULL,12,'荷兰','德国','2012-6-13 18:45','2012-5-1 20:19',NULL,1,'B组小组赛',NULL,'FALSE','FALSE',NULL);
INSERT INTO match VALUES(NULL,NULL,NULL,13,'意大利','克罗地亚','2012-6-14 16:00','2012-5-1 20:19',NULL,1,'C组小组赛',NULL,'FALSE','FALSE',NULL);
INSERT INTO match VALUES(NULL,NULL,NULL,14,'西班牙','爱尔兰','2012-6-14 18:45','2012-5-1 20:19',NULL,1,'C组小组赛',NULL,'FALSE','FALSE',NULL);
INSERT INTO match VALUES(NULL,NULL,NULL,15,'瑞典','英格兰','2012-6-15 16:00','2012-5-1 20:19',NULL,1,'D组小组赛',NULL,'FALSE','FALSE',NULL);
INSERT INTO match VALUES(NULL,NULL,NULL,16,'乌克兰','法国','2012-6-15 18:45','2012-5-1 20:19',NULL,1,'D组小组赛',NULL,'FALSE','FALSE',NULL);
INSERT INTO match VALUES(NULL,NULL,NULL,17,'希腊','俄罗斯','2012-6-16 18:45','2012-5-1 20:19',NULL,1,'A组小组赛',NULL,'FALSE','FALSE',NULL);
INSERT INTO match VALUES(NULL,NULL,NULL,18,'捷克','波兰','2012-6-16 18:45','2012-5-1 20:19',NULL,1,'A组小组赛',NULL,'FALSE','FALSE',NULL);
INSERT INTO match VALUES(NULL,NULL,NULL,19,'葡萄牙','荷兰','2012-6-17 18:45','2012-5-1 20:19',NULL,1,'B组小组赛',NULL,'FALSE','FALSE',NULL);
INSERT INTO match VALUES(NULL,NULL,NULL,20,'丹麦','德国','2012-6-17 18:45','2012-5-1 20:19',NULL,1,'B组小组赛',NULL,'FALSE','FALSE',NULL);
INSERT INTO match VALUES(NULL,NULL,NULL,21,'克罗地亚','西班牙','2012-6-18 18:45','2012-5-1 20:19',NULL,1,'C组小组赛',NULL,'FALSE','FALSE',NULL);
INSERT INTO match VALUES(NULL,NULL,NULL,22,'意大利','爱尔兰','2012-6-18 18:45','2012-5-1 20:19',NULL,1,'C组小组赛',NULL,'FALSE','FALSE',NULL);
INSERT INTO match VALUES(NULL,NULL,NULL,23,'瑞典','法国','2012-6-19 18:45','2012-5-1 20:19',NULL,1,'D组小组赛',NULL,'FALSE','FALSE',NULL);
INSERT INTO match VALUES(NULL,NULL,NULL,24,'英格兰','乌克兰','2012-6-19 18:45','2012-5-1 20:19',NULL,1,'D组小组赛',NULL,'FALSE','FALSE',NULL);
INSERT INTO match VALUES(NULL,NULL,NULL,25,'A组小组第1','B组小组第2','2012-6-21 18:45','2012-5-1 20:19',NULL,1,'1/4决赛',NULL,'FALSE','FALSE',NULL);
INSERT INTO match VALUES(NULL,NULL,NULL,26,'B组小组第1','A组小组第2','2012-6-22 18:45','2012-5-1 20:19',NULL,1,'1/4决赛',NULL,'FALSE','FALSE',NULL);
INSERT INTO match VALUES(NULL,NULL,NULL,27,'C组小组第1','D组小组第2','2012-6-23 18:45','2012-5-1 20:19',NULL,1,'1/4决赛',NULL,'FALSE','FALSE',NULL);
INSERT INTO match VALUES(NULL,NULL,NULL,28,'D组小组第1','C组小组第2','2012-6-24 18:45','2012-5-1 20:19',NULL,1,'1/4决赛',NULL,'FALSE','FALSE',NULL);
INSERT INTO match VALUES(NULL,NULL,NULL,29,'1/4决赛第1战胜者','1/4决赛第3战胜者','2012-6-27 18:45','2012-5-1 20:19',NULL,1,'半决赛',NULL,'FALSE','FALSE',NULL);
INSERT INTO match VALUES(NULL,NULL,NULL,30,'1/4决赛第2战胜者','1/4决赛第4战胜者','2012-6-28 18:45','2012-5-1 20:19',NULL,1,'半决赛',NULL,'FALSE','FALSE',NULL);
INSERT INTO match VALUES(NULL,NULL,NULL,31,'半决赛第1战胜者','半决赛第2战胜者','2012-7-1 18:45','2012-5-1 20:19',NULL,1,'决赛',NULL,'FALSE','FALSE',NULL);
CREATE TABLE sqlite_sequence(name,seq);
CREATE TABLE team (id integer primary key autoincrement, teamName , teamShortName , lastModified datetime, followed Boolean);
INSERT INTO team VALUES(1,'波兰','pol','2012-5-1 20:04','FALSE');
INSERT INTO team VALUES(2,'希腊','gre','2012-5-1 20:05','FALSE');
INSERT INTO team VALUES(3,'俄罗斯','rus','2012-5-1 20:05','FALSE');
INSERT INTO team VALUES(4,'捷克','cze','2012-5-1 20:05','FALSE');
INSERT INTO team VALUES(5,'荷兰','ned','2012-5-1 20:05','FALSE');
INSERT INTO team VALUES(6,'丹麦','den','2012-5-1 20:05','FALSE');
INSERT INTO team VALUES(7,'德国','ger','2012-5-1 20:05','FALSE');
INSERT INTO team VALUES(8,'葡萄牙','por','2012-5-1 20:05','FALSE');
INSERT INTO team VALUES(9,'西班牙','esp','2012-5-1 20:05','FALSE');
INSERT INTO team VALUES(10,'意大利','ita','2012-5-1 20:05','FALSE');
INSERT INTO team VALUES(11,'爱尔兰','irl','2012-5-1 20:05','FALSE');
INSERT INTO team VALUES(12,'克罗地亚','cro','2012-5-1 20:05','FALSE');
INSERT INTO team VALUES(13,'法国','fra','2012-5-1 20:05','FALSE');
INSERT INTO team VALUES(14,'英格兰','eng','2012-5-1 20:05','FALSE');
INSERT INTO team VALUES(15,'乌克兰','ukr','2012-5-1 20:05','FALSE');
INSERT INTO team VALUES(16,'瑞典','swe','2012-5-1 20:05','FALSE');
CREATE TABLE "tour"(
[id] int UNIQUE NOT NULL
,[name] text UNIQUE NOT NULL
,[shortName] text
, Primary Key(id)
);
INSERT INTO tour VALUES(1,'欧锦赛','euro');
COMMIT;