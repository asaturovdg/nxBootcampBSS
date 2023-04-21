create table if not exists tariff (
	id varchar(2) primary key,
	inbound_rate numeric(5, 2),
	outbound_rate numeric(5, 2),
	extra_minutes int,
	inbound_rate_on_extra_minutes numeric(5, 2),
	outbound_rate_on_extra_minutes numeric(5, 2),
    receiver_matters boolean,
    inbound_rate_on_receiver_matters numeric(5, 2),
    outbound_rate_on_receiver_matters numeric(5, 2),
	monthly_fee numeric(5, 2)
);


create table if not exists abonent (
	id serial,
	phone_number varchar(15) primary key,
	balance numeric(6, 2),
	tariff_id varchar(2),
	foreign key(tariff_id) references tariff(id)
);

create table if not exists call (
	id serial primary key,
	phone_number varchar(15),
	"type" varchar(2),
	start_time varchar(19),
	end_time varchar(19),
	duration varchar(8),
	"cost" numeric(6, 2),
	foreign key(phone_number) references abonent(phone_number)
);

insert into tariff values ('03', 1.50, 1.50, 0, -1.00, -1.00, false, 0.00, 0.00, 0.00);
insert into tariff values ('06', 1.00, 1.00, 300, 0.00, 0.00, false, 0.00, 0.00, 100.00);
insert into tariff values ('11', 0.00, 1.50, 100, -1.00, 0.50, false, 0.00, 0.00, 0.00);
insert into tariff values ('0X', 1.50, 1.50, 0, -1.00, -1.00, true, 0.00, 0.00, 0.00);