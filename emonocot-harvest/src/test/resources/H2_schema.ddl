drop table if exists Content;
create table Content (DTYPE varchar(31) not null, id bigint not null, created timestamp, creator varchar(255), license varchar(255), modified timestamp, source varchar(255), feature varchar(255), content varchar(255), taxon_id bigint, primary key (id));
drop table if exists  Image;
create table Image (id bigint not null, created timestamp, creator varchar(255), license varchar(255), modified timestamp, source varchar(255), url varchar(255), primary key (id));
drop table if exists  Reference;
create table Reference (id bigint not null, created timestamp, creator varchar(255), license varchar(255), modified timestamp, source varchar(255), primary key (id));
drop table if exists  Taxon;
create table Taxon (id bigint not null, created timestamp, creator varchar(255), license varchar(255), modified timestamp, source varchar(255), primary key (id));
drop table if exists  Taxon_Image;
create table Taxon_Image (Taxon_id bigint not null, images_id bigint not null);
drop table if exists  Taxon_Reference;
create table Taxon_Reference (Taxon_id bigint not null, references_id bigint not null, primary key (Taxon_id, references_id));
alter table Content add constraint FK9BEFCC591EDCD08D foreign key (taxon_id) references Taxon;
alter table Taxon_Image add constraint FK56D693661EDCD08D foreign key (Taxon_id) references Taxon;
alter table Taxon_Image add constraint FK56D69366437564A foreign key (images_id) references Image;
alter table Taxon_Reference add constraint FK164D2BD6968322D1 foreign key (references_id) references Reference;
alter table Taxon_Reference add constraint FK164D2BD61EDCD08D foreign key (Taxon_id) references Taxon;
