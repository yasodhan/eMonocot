drop table if exists Annotation;
create table Annotation (id bigint generated by default as identity, identifier varchar(255) default RANDOM_UUID(), annotatedObjType varchar(255), annotatedObjId bigint, code varchar(255), dateTime integer, jobId bigint, recordType varchar(255), text clob, type varchar(255), value varchar(255), source_id bigint, primary key (id), unique (identifier));
drop table if exists Distribution;
create table Distribution (id bigint not null, identifier varchar(255), created timestamp, creator varchar(255), license varchar(255), modified timestamp, source varchar(255), region varchar(255), authority_id bigint, taxon_id bigint, primary key (id), unique (identifier));
drop table if exists Distribution_Source;
create table Distribution_Source (Distribution_id bigint not null, sources_id bigint not null, primary key (Distribution_id, sources_id));
drop table if exists Group_permissions;
create table Group_permissions (Group_id bigint not null, permissions integer);
drop table if exists Identifier;
create table Identifier (id bigint not null, identifier varchar(255), created timestamp, creator varchar(255), license varchar(255), modified timestamp, source varchar(255), content clob, format varchar(255), subject varchar(255), title varchar(255), authority_id bigint,  taxon_id bigint, primary key (id), unique (identifier));
drop table if exists Identifier_Source;
create table Identifier_Source (Identifier_id bigint not null, sources_id bigint not null, primary key (Identifier_id, sources_id));
drop table if exists Image;
create table Image (id bigint not null, identifier varchar(255), created timestamp, creator varchar(255), license varchar(255), modified timestamp, source varchar(255), caption varchar(255), description clob, format varchar(255), keywords varchar(255), location BLOB, locality varchar(255), url varchar(255), authority_id bigint, image_id bigint, taxon_id bigint, primary key (id), unique (identifier));
drop table if exists Image_Source;
create table Image_Source (Image_id bigint not null, sources_id bigint not null, primary key (Image_id, sources_id));
drop table if exists Job;
create table Job (id bigint not null, identifier varchar(255), duration timestamp, exitDescription clob, exitCode varchar(255), jobId bigint, jobInstance varchar(255), jobType varchar(255), lastHarvested timestamp, resource varchar(255), family varchar(255), startTime timestamp, status varchar(255), uri varchar(255), source_id bigint, primary key (id), unique (identifier));
drop table if exists Principal;
create table Principal (DTYPE varchar(31) not null, id bigint not null, identifier varchar(255), created timestamp, modified timestamp, accountNonExpired boolean, accountNonLocked boolean, credentialsNonExpired boolean, enabled boolean, password varchar(255), primary key (id), unique (identifier));
drop table if exists Reference;
create table Reference (id bigint not null, identifier varchar(255), created timestamp, creator varchar(255), license varchar(255), modified timestamp, source varchar(255), author varchar(255), citation varchar(255), datePublished varchar(255), keywords varchar(255), number varchar(255), pages varchar(255), publishedIn varchar(255), publishedInAuthor varchar(255), publisher varchar(255), referenceAbstract clob, title varchar(255), type integer, volume varchar(255), authority_id bigint, primary key (id), unique (identifier));
drop table if exists Reference_Source;
create table Reference_Source (Reference_id bigint not null, sources_id bigint not null, primary key (Reference_id, sources_id));
drop table if exists Source;
create table Source (id bigint not null, identifier varchar(255), created timestamp, creator varchar(255), license varchar(255), modified timestamp, source varchar(255), creatorEmail varchar(255), description clob, logoUrl varchar(255), publisherEmail varchar(255), publisherName varchar(255), subject varchar(255), title varchar(255), uri varchar(255), authority_id bigint, primary key (id), unique (identifier));
drop table if exists Source_Source;
create table Source_Source (Source_id bigint not null, sources_id bigint not null, primary key (Source_id, sources_id));
drop table if exists Taxon;
create table Taxon (id bigint not null, identifier varchar(255), created timestamp, creator varchar(255), license varchar(255), modified timestamp, source varchar(255), accordingTo varchar(128), authorship varchar(128), basionymAuthorship varchar(128), class varchar(128), family varchar(128), genus varchar(128), infraGenericEpithet varchar(128), infraSpecificEpithet varchar(128), kingdom varchar(128), name varchar(128), nameIdentifier varchar(255), nomenclaturalCode varchar(255), ordr varchar(128), phylum varchar(128), protologueMicroReference varchar(128), rank varchar(255), specificEpithet varchar(128), status varchar(255), uninomial varchar(128), authority_id bigint, image_id bigint, accepted_id bigint, parent_id bigint, protologue_id bigint, primary key (id), unique (identifier));
drop table if exists Taxon_Image;
create table Taxon_Image (Taxon_id bigint not null, images_id bigint not null);
drop table if exists Taxon_Reference;
create table Taxon_Reference (Taxon_id bigint not null, references_id bigint not null, primary key (Taxon_id, references_id));
drop table if exists Taxon_Source;
create table Taxon_Source (Taxon_id bigint not null, sources_id bigint not null, primary key (Taxon_id, sources_id));
drop table if exists TextContent;
create table TextContent (id bigint not null, identifier varchar(255), created timestamp, creator varchar(255), license varchar(255), modified timestamp, source varchar(255), content clob, feature varchar(255), authority_id bigint, taxon_id bigint, primary key (id), unique (identifier));
drop table if exists TextContent_Reference;
create table TextContent_Reference (TextContent_id bigint not null, references_id bigint not null, primary key (TextContent_id, references_id));
drop table if exists TextContent_Source;
create table TextContent_Source (TextContent_id bigint not null, sources_id bigint not null, primary key (TextContent_id, sources_id));
drop table if exists User_Group;
create table User_Group (User_id bigint not null, groups_id bigint not null, primary key (User_id, groups_id));
drop table if exists User_permissions;
create table User_permissions (User_id bigint not null, permissions integer);
alter table Annotation add constraint FK1A21C74FCF3DA2C4 foreign key (source_id) references Source;
alter table Distribution add constraint FKAB93A2A41EDCD08D foreign key (taxon_id) references Taxon;
alter table Distribution add constraint FKAB93A2A46B53D29C foreign key (authority_id) references Source;
alter table Distribution_Source add constraint FKF9182E36BFDB6E15 foreign key (Distribution_id) references Distribution;
alter table Distribution_Source add constraint FKF9182E36F247EE07 foreign key (sources_id) references Source;
alter table Group_permissions add constraint FK7A63C2A45090CB20 foreign key (Group_id) references Principal;
alter table Identifier add constraint FK165A88C96B53D29C foreign key (authority_id) references Source;
alter table Identifier add constraint FK165A88C9351368A7 foreign key (taxon_id) references Taxon;
alter table Identifier_Source add constraint FK556E0471A0863E36 foreign key (Identifier_id) references Identifier;
alter table Identifier_Source add constraint FK556E0471F247EE07 foreign key (sources_id) references Source;
alter table Image add constraint FK437B93B544A087 foreign key (image_id) references Image;
alter table Image add constraint FK437B93B1EDCD08D foreign key (taxon_id) references Taxon;
alter table Image add constraint FK437B93B6B53D29C foreign key (authority_id) references Source;
alter table Image_Source add constraint FKFFA0CFBF544A087 foreign key (Image_id) references Image;
alter table Image_Source add constraint FKFFA0CFBFF247EE07 foreign key (sources_id) references Source;
alter table Job add constraint FK1239DCF3DA2C4 foreign key (source_id) references Source;
alter table Reference add constraint FK404D5F2B6B53D29C foreign key (authority_id) references Source;
alter table Reference_Source add constraint FK76F19BCF1914B32E foreign key (Reference_id) references Reference;
alter table Reference_Source add constraint FK76F19BCFF247EE07 foreign key (sources_id) references Source;
alter table Source add constraint FK93F5543B6B53D29C foreign key (authority_id) references Source;
alter table Source_Source add constraint FK76ED4BFCF3DA2C4 foreign key (Source_id) references Source;
alter table Source_Source add constraint FK76ED4BFF247EE07 foreign key (sources_id) references Source;
alter table Taxon add constraint FK4CD9EAA54493690 foreign key (accepted_id) references Taxon;
alter table Taxon add constraint FK4CD9EAA544A087 foreign key (image_id) references Image;
alter table Taxon add constraint FK4CD9EAA6B53D29C foreign key (authority_id) references Source;
alter table Taxon add constraint FK4CD9EAACA0AED foreign key (protologue_id) references Reference;
alter table Taxon add constraint FK4CD9EAAA9E98AAD foreign key (parent_id) references Taxon;
alter table Taxon_Image add constraint FK56D693661EDCD08D foreign key (Taxon_id) references Taxon;
alter table Taxon_Image add constraint FK56D69366437564A foreign key (images_id) references Image;
alter table Taxon_Reference add constraint FK164D2BD6968322D1 foreign key (references_id) references Reference;
alter table Taxon_Reference add constraint FK164D2BD61EDCD08D foreign key (Taxon_id) references Taxon;
alter table Taxon_Source add constraint FK9531BF701EDCD08D foreign key (Taxon_id) references Taxon;
alter table Taxon_Source add constraint FK9531BF70F247EE07 foreign key (sources_id) references Source;
alter table TextContent add constraint FK6A10726C1EDCD08D foreign key (taxon_id) references Taxon;
alter table TextContent add constraint FK6A10726C6B53D29C foreign key (authority_id) references Source;
alter table TextContent_Reference add constraint FKF3F2F783968322D1 foreign key (references_id) references Reference;
alter table TextContent_Reference add constraint FKF3F2F7832D1A3054 foreign key (TextContent_id) references TextContent;
alter table TextContent_Source add constraint FK724F8D6E139F7DF foreign key (TextContent_id) references TextContent;
alter table TextContent_Source add constraint FK724F8D6EF247EE07 foreign key (sources_id) references Source;
alter table User_Group add constraint FKE7B7ED0BDA0BABAB foreign key (groups_id) references Principal;
alter table User_Group add constraint FKE7B7ED0B9E0AAB54 foreign key (User_id) references Principal;
alter table User_permissions add constraint FKB4582A309E0AAB54 foreign key (User_id) references Principal;