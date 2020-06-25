ALTER TABLE seats
    add constraint check_name check (order_name <> ''),
    add constraint check_phone check (order_phone <> '');