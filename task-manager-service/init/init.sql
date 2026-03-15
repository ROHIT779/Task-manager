create table task(id serial constraint taskpk primary key,title varchar(50) not null,description varchar(500), status smallint, created_at timestamp);
create table dependency(task_id integer, dependencies integer, constraint dependencyfk1 foreign key(task_id) references task(id), constraint dependencyfk2 foreign key(dependencies) references task(id));
