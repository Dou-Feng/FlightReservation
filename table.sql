-- auto-generated definition
create table AirCraft
(
    AirCraftNo char(10) not null
        primary key,
    FCSeats    int      not null,
    BCSeats    int      not null,
    ECSeats    int      not null
);

-- auto-generated definition
create table Flight
(
    FNo         char(8)     not null
        primary key,
    AirCraftNo  char(10)    not null,
    TakeoffCity varchar(20) not null,
    LandingCity varchar(20) not null,
    TakeoffTime time        not null,
    LandingTime time        not null,
    Duration    time        not null,
    constraint Flight_AirCraft_AirCraftNo_fk
        foreign key (AirCraftNo) references AirCraft (AirCraftNo)
);

-- auto-generated definition
create table Passenger
(
    PNo     char(12)          not null
        primary key,
    PName   char(10)          not null,
    CerType tinyint           not null,
    CerNo   varchar(20)       not null,
    Ptype   tinyint default 1 not null
)
    comment 'Record passenger''s info';

create index Passenger_PName_index
    on Passenger (PName);

-- auto-generated definition
create table Seats
(
    FNo      char(8)        not null,
    Date     date           not null,
    Time     time           null,
    FCRemain int            not null,
    BCRemain int            not null,
    ECRemain int            not null,
    FCPrice  decimal(10, 2) not null,
    BCPrice  decimal(10, 2) not null,
    ECPrice  decimal(10, 2) not null,
    primary key (FNo, Date),
    constraint Seats_Flight_FNo_fk
        foreign key (FNo) references Flight (FNo)
            on update cascade on delete cascade
);

-- auto-generated definition
create table T_Bill
(
    OrderNo char(8)        not null
        primary key,
    Amount  decimal(10, 2) not null,
    IsPay   tinyint        not null,
    constraint T_Bill_T_Order_OrderNo_fk
        foreign key (OrderNo) references T_Order (OrderNo)
            on delete cascade
);

-- auto-generated definition
create table T_Order
(
    OrderNo     char(8)  not null
        primary key,
    ContactName char(10) not null,
    ContactPNo  char(11) not null,
    DateTime    datetime not null,
    TicketNum   int      not null,
    takeoffDate date     not null
)
    comment 'manger order from users';

create index Order_ContactName_index
    on T_Order (ContactName);


-- auto-generated definition
create table T_User
(
    userName char(6)  not null
        primary key,
    password char(6)  not null,
    Name     char(20) null
)
    comment 'record username and password';

-- auto-generated definition
create table T_User_Order
(
    Username char(6) not null,
    OrderNo  char(8) not null,
    primary key (OrderNo, Username),
    constraint T_User_Order_T_Order_OrderNo_fk
        foreign key (OrderNo) references T_Order (OrderNo)
            on delete cascade,
    constraint T_User_Order_T_User_userName_fk
        foreign key (Username) references T_User (userName)
            on delete cascade
)
    comment 'record user''s order';

-- auto-generated definition
create table Ticket
(
    TNo         char(13)       not null
        primary key,
    FNo         char(8)        not null,
    PNo         char(12)       not null,
    ClassType   tinyint        not null,
    TicketPrice decimal(10, 2) not null,
    constraint Ticket_Flight_FNo_fk
        foreign key (FNo) references Flight (FNo)
            on update cascade on delete cascade,
    constraint Ticket_Passenger_PNo_fk
        foreign key (PNo) references Passenger (PNo)
            on delete cascade
)
    comment 'Flight ticket info';

create index Ticket_FlightNo_index
    on Ticket (FNo);

create index Ticket_PNo_index
    on Ticket (PNo);

-- auto-generated definition
create table T_Order_Ticket
(
    OrderNo char(8)  not null,
    TNo     char(13) not null,
    primary key (OrderNo, TNo),
    constraint T_Order_Ticket_T_Order_OrderNo_fk
        foreign key (OrderNo) references T_Order (OrderNo)
            on delete cascade,
    constraint T_Order_Ticket_Ticket_TNo_fk
        foreign key (TNo) references Ticket (TNo)
            on delete cascade
)
    comment 'Relation between order and ticket';



