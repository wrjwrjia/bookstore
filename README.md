# bookstore
java swing, mysql

Here is the SQL schema:
#create Book table
create table Book(
ISBN varchar(200) primary key,
name varchar(200) not null,
partOf int not null,
foreign key(partOf) references Bookstore(id)
on update cascade
on delete no action

),
#create BookAuthor table as a multi-value attribute table for Book
create table BookAuthor(
book varchar(200) not null,
name varchar(200) not null,
primary key(book, name),
foreign key(book) references Book(ISBN)
on update cascade
on delete cascade

8

),
#create hierarchies for Book table
create table Hardcopy(
id varchar(200) primary key,
foreign key(id) references Book(ISBN)
on update cascade
on delete cascade
), -- Inheritance strategy is JOINED
create table Digital_media(
id varchar(200) primary key,
foreign key(id) references Book(ISBN)
on update cascade
on delete cascade
), -- Inheritance strategy is JOINED
#create Bookstore table
create table Bookstore(
id int primary key AUTO_INCREMENT,
location varchar(200) not null,
ISBN varchar(200) not null unique
),
#create Category table
create table Category(
id int primary key AUTO_INCREMENT,
major varchar(200) not null,
location varchar(200) not null,
partOf int not null,
foreign key(partOf) references Bookstore(id)
on update cascade
on delete cascade

),
#create Classification table

9

create table Classification(
belongs varchar(200) not null,
foreign key(belongs) references Book(ISBN)
on update cascade
on delete cascade,
isBelongedTo int not null,
foreign key(isBelongedTo) references Category(id)
on update cascade
on delete cascade,
primary key(belongs, isBelongedTo),
addedDate date not null
),
