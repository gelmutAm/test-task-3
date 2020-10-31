create table doctors(
    id bigint identity primary key,
    name varchar(50) not null,
    surname varchar(50) not null,
    patronymic varchar(50),
    specialization varchar(200) not null
);

create table patients(
    id bigint identity primary key not null,
    name varchar(50) not null,
    surname varchar(50) not null,
    patronymic varchar(50),
    phone_number varchar(50) not null
);

create table recipes(
    id bigint identity primary key not null,
    doctor_id bigint not null,
    patient_id bigint not null,
    description varchar(200) not null,
    creation_date date not null,
    validity smallint not null,
    priority varchar(50) not null,
    foreign key(doctor_id) references doctors(id),
    foreign key(patient_id) references patients(id)
);