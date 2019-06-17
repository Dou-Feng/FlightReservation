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

