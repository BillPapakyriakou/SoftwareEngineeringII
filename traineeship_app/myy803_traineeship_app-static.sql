DROP DATABASE `myy803_traineeship_app`;
CREATE DATABASE  IF NOT EXISTS `myy803_traineeship_app`;
USE `myy803_traineeship_app`;

create table companies (company_location varchar(255), company_name varchar(255), username varchar(255) not null, primary key (username)) engine=InnoDB;
create table evaluations (effectiveness integer, efficiency integer, id integer not null auto_increment, motivation integer, position_id integer, evaluation_type enum ('COMPANY_EVALUATION','PROFESSOR_EVALUATION'), primary key (id)) engine=InnoDB;
create table professors (interests varchar(255), professor_name varchar(255), username varchar(255) not null, primary key (username)) engine=InnoDB;
create table students (average_grade float(53), looking_for_traineeship bit, position_id integer, am varchar(255), interests varchar(255), preferred_location varchar(255), skills varchar(255), student_name varchar(255), username varchar(255) not null, primary key (username)) engine=InnoDB;
create table traneeship_positions (from_date date, id integer not null auto_increment, is_assigned bit, pass_fail bit, to_date date, company_username varchar(255), description varchar(255), professor_username varchar(255), skills varchar(255), student_log_book varchar(255), title varchar(255), topics varchar(255), primary key (id)) engine=InnoDB;
create table users (password varchar(255), username varchar(255) not null, role enum ('COMMITTEE','COMPANY','PROFESSOR','STUDENT'), primary key (username)) engine=InnoDB;
alter table students add constraint UK6osyijeyw2s5rrla5b50wjquv unique (position_id);
alter table evaluations add constraint FKl12b9501h71oxj1klmq54p2yv foreign key (position_id) references traneeship_positions (id);
alter table students add constraint FK9840xol8yl36xiafj0xjypwcb foreign key (position_id) references traneeship_positions (id);
alter table traneeship_positions add constraint FKdgafmt3g8kjtcwpyetmukoewt foreign key (company_username) references companies (username);
alter table traneeship_positions add constraint FKeoxghkr14s6mwd6csicffvdt2 foreign key (professor_username) references professors (username);


insert into users(password, username, role) values ('$2a$12$1RHsG1ImXPPAyroJMnWXEuaOoSBssZ68BDTNRmoXnzkQizRBnf6vi', 'committee', 'COMMITTEE')