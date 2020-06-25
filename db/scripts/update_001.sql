drop table if exists seats;

create table seats (
  row           int not null,
  seat          int not null,
  order_name    text not null,
  order_phone   text
);