create definer = root@localhost trigger Passenger_validation
    before INSERT
    on Passenger
    for each row
begin
    declare msg varchar(200);



    if (new.CerType<=0 or new.CerType>4) then
        set msg = "Please input right ceritification type!";
        signal sqlstate 'HY000' set message_text = msg;
    end if;
    if (new.Ptype<=0 or new.Ptype>3) then
        set msg = "Please input right passenger type!";
        signal sqlstate 'HY000' set message_text = msg;
    end if;
    end;

create definer = root@localhost trigger Tri_Seat_insert
    before INSERT
    on Seats
    for each row
begin
        declare maxFCSeats int;
        declare maxBCSeats int;
        declare maxECSeats int;
        declare msg varchar(200);
        set new.Time = time(now());
        if (new.FCRemain <0 or new.ECRemain <0 or new.BCRemain<0) then
            set msg = "Seats remain input error";
            signal sqlstate 'HY000' SET message_text = msg;
        end if;
        set maxFCSeats = (select AirCraft.FCSeats
                    from AirCraft, Flight
                    where new.FNo=Flight.FNo and Flight.AirCraftNo=AirCraft.AirCraftNo);
        set maxBCSeats = (select AirCraft.BCSeats
                    from AirCraft, Flight
                    where new.FNo=Flight.FNo and Flight.AirCraftNo=AirCraft.AirCraftNo);
        set maxECSeats = (select AirCraft.ECSeats
                    from AirCraft, Flight
                    where new.FNo=Flight.FNo and Flight.AirCraftNo=AirCraft.AirCraftNo);
        if (new.BCRemain>maxBCSeats or new.FCRemain>maxFCSeats or new.ECRemain >maxECSeats) then
            set msg = "Input wrong seat remain";
            signal sqlstate 'HY000' set message_text = msg;
        end if;
    end;

create definer = root@localhost trigger Tri_Seat_update
    before UPDATE
    on Seats
    for each row
begin
    declare msg varchar(200);
    declare maxFCSeats int;
    declare maxBCSeats int;
    declare maxECSeats int;
    set new.Time = time(now());
    if (new.FCRemain<0 or new.ECRemain<0 or new.BCRemain<0) then
        set msg = "Seats remain update error";
        signal sqlstate 'HY000' set message_text = msg;
    end if;
    set maxFCSeats = (select AirCraft.FCSeats
                      from AirCraft, Flight
                      where new.FNo=Flight.FNo and Flight.AirCraftNo=AirCraft.AirCraftNo);
    set maxBCSeats = (select AirCraft.BCSeats
                      from AirCraft, Flight
                      where new.FNo=Flight.FNo and Flight.AirCraftNo=AirCraft.AirCraftNo);
    set maxECSeats = (select AirCraft.ECSeats
                      from AirCraft, Flight
                      where new.FNo=Flight.FNo and Flight.AirCraftNo=AirCraft.AirCraftNo);
    if (new.BCRemain>maxBCSeats or new.FCRemain>maxFCSeats or new.ECRemain>maxECSeats) then
        set msg = "Input wrong seat remain";
        signal sqlstate 'HY000' set message_text = msg;
    end if;
end;

create definer = root@localhost trigger Bill_validation
    before INSERT
    on T_Bill
    for each row
begin
    declare msg varchar(200);
    if (new.IsPay<>0 and new.IsPay<>1) then
        set msg = "Please input right value of IsPay!";
        signal sqlstate 'HY000' set message_text = msg;
    end if;
end;

create definer = root@localhost trigger insert_dateTime
    before INSERT
    on T_Order
    for each row
begin
        set new.DateTime = now();
    end;

create definer = root@localhost trigger update_dateTime
    before UPDATE
    on T_Order
    for each row
begin
        set new.DateTime = now();
    end;

create definer = root@localhost trigger ticket_validation
    before INSERT
    on Ticket
    for each row
begin
        declare msg varchar(200);
        declare child_num int;
        declare baby_num int;
        if (new.ClassType<=0 or new.ClassType>3) then
            set msg = "Please input right class type!";
            signal sqlstate 'HY000' set message_text = msg;
        end if;
    end;

