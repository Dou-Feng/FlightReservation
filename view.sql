create definer = root@localhost view v_flight as
select `flightreservation`.`seats`.`Date`         AS `Date`,
       `flightreservation`.`flight`.`FNo`         AS `FNo`,
       `flightreservation`.`flight`.`TakeoffCity` AS `TakeoffCity`,
       `flightreservation`.`flight`.`TakeoffTime` AS `TakeoffTime`,
       `flightreservation`.`flight`.`LandingCity` AS `LandingCity`,
       `flightreservation`.`flight`.`LandingTime` AS `LandingTime`,
       `flightreservation`.`flight`.`Duration`    AS `Duration`,
       `flightreservation`.`seats`.`FCRemain`     AS `FCRemain`,
       `flightreservation`.`seats`.`FCPrice`      AS `FCPrice`,
       `flightreservation`.`seats`.`BCRemain`     AS `BCRemain`,
       `flightreservation`.`seats`.`BCPrice`      AS `BCPrice`,
       `flightreservation`.`seats`.`ECRemain`     AS `ECRemain`,
       `flightreservation`.`seats`.`ECPrice`      AS `ECPrice`
from `flightreservation`.`flight`
         join `flightreservation`.`seats`
where (`flightreservation`.`flight`.`FNo` = `flightreservation`.`seats`.`FNo`)
order by `flightreservation`.`seats`.`Date`, `flightreservation`.`flight`.`FNo`;

create definer = root@localhost view v_nofity as
select `flightreservation`.`t_order_ticket`.`OrderNo` AS `OrderNo`,
       `flightreservation`.`t_order_ticket`.`TNo`     AS `TNo`,
       `flightreservation`.`passenger`.`PName`        AS `PName`,
       `flightreservation`.`t_order`.`takeoffDate`    AS `takeoffDate`,
       `flightreservation`.`flight`.`TakeoffCity`     AS `TakeoffCity`,
       `flightreservation`.`flight`.`LandingCity`     AS `LandingCity`,
       `flightreservation`.`ticket`.`ClassType`       AS `ClassType`
from `flightreservation`.`t_order_ticket`
         join `flightreservation`.`ticket`
         join `flightreservation`.`passenger`
         join `flightreservation`.`t_order`
         join `flightreservation`.`flight`
         join `flightreservation`.`t_bill`
where ((`flightreservation`.`t_order`.`OrderNo` = `flightreservation`.`t_order_ticket`.`OrderNo`) and
       (`flightreservation`.`t_order_ticket`.`TNo` = `flightreservation`.`ticket`.`TNo`) and
       (`flightreservation`.`ticket`.`FNo` = `flightreservation`.`flight`.`FNo`) and
       (`flightreservation`.`ticket`.`PNo` = `flightreservation`.`passenger`.`PNo`) and
       (`flightreservation`.`t_bill`.`OrderNo` = `flightreservation`.`t_order_ticket`.`OrderNo`) and
       (`flightreservation`.`t_bill`.`IsPay` = 1) and (`flightreservation`.`t_order`.`takeoffDate` >= now()));

create definer = root@localhost view v_order as
select `flightreservation`.`t_order`.`OrderNo`     AS `OrderNo`,
       `flightreservation`.`t_order`.`ContactName` AS `ContactName`,
       `flightreservation`.`t_order`.`ContactPNo`  AS `ContactPNo`,
       `flightreservation`.`t_order`.`takeoffDate` AS `takeoffDate`,
       `flightreservation`.`t_order`.`TicketNum`   AS `TicketNum`,
       `flightreservation`.`t_bill`.`Amount`       AS `Amount`,
       `flightreservation`.`t_bill`.`IsPay`        AS `IsPay`,
       `flightreservation`.`t_order`.`DateTime`    AS `DateTime`
from `flightreservation`.`t_order`
         join `flightreservation`.`t_bill`
where (`flightreservation`.`t_order`.`OrderNo` = `flightreservation`.`t_bill`.`OrderNo`);

create view v_total as
select `flightreservation`.`ticket`.`FNo`                      AS `FNo`,
       `flightreservation`.`t_order`.`takeoffDate`             AS `TDate`,
       sum(`flightreservation`.`ticket`.`TicketPrice`)         AS `Amount`,
       count(0)                                                AS `SellNum`,
       ((`flightreservation`.`aircraft`.`ECSeats` + `flightreservation`.`aircraft`.`BCSeats`) +
        `flightreservation`.`aircraft`.`FCSeats`)              AS `SeatsNum`,
       (count(0) / ((`flightreservation`.`aircraft`.`ECSeats` + `flightreservation`.`aircraft`.`BCSeats`) +
                    `flightreservation`.`aircraft`.`FCSeats`)) AS `Percent`
from `flightreservation`.`ticket`
         join `flightreservation`.`t_order_ticket`
         join `flightreservation`.`t_order`
         join `flightreservation`.`aircraft`
         join `flightreservation`.`flight`
where ((`flightreservation`.`t_order_ticket`.`OrderNo` = `flightreservation`.`t_order`.`OrderNo`) and
       (`flightreservation`.`t_order_ticket`.`TNo` = `flightreservation`.`ticket`.`TNo`) and
       (`flightreservation`.`ticket`.`FNo` = `flightreservation`.`flight`.`FNo`) and
       (`flightreservation`.`flight`.`AirCraftNo` = `flightreservation`.`aircraft`.`AirCraftNo`))
group by `flightreservation`.`ticket`.`FNo`, `flightreservation`.`t_order`.`takeoffDate`;

