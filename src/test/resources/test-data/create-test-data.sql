
insert into venue (venue_name, address, seat_cnt, create_datetime)
values ('고척돔','서울시 금천구',100000,now());

insert into artist ( artist_name_kor, artist_name_eng, create_datetime)
values ('제로베이스원','zero-base-one',now());

SET @max_venue_seq = (SELECT MAX(seq) FROM venue);

SET @max_artist_seq = (SELECT MAX(seq) FROM artist);

INSERT INTO concert (concert_name, concert_date, cancelled_flag, ticket_open_time, ticket_end_time,
                     create_datetime, venue_seq, artist_seq)
VALUES ('제로베이스원 1주년 콘서트', DATE_ADD(NOW(), INTERVAL 1 DAY), false, DATE_ADD(NOW(), INTERVAL 10 DAY),DATE_ADD(NOW(), INTERVAL 20 DAY), NOW(), @max_venue_seq, @max_artist_seq);

SET @max_concert_seq = (SELECT MAX(seq) FROM concert);
insert into grade_ticket ( grade_name, ticket_count, create_datetime, concert_seq) values ('VIP',200,now(),@max_concert_seq);
insert into grade_ticket ( grade_name, ticket_count, create_datetime, concert_seq) values ('R',400,now(),@max_concert_seq);
insert into grade_ticket ( grade_name, ticket_count, create_datetime, concert_seq) values ('S',600,now(),@max_concert_seq);

insert into ticket (concert_seq, purchase_date, ticket_serial_number, grade_name, cancelled_flag, create_datetime, order_seq)
values (@max_concert_seq, now(),'test-ticket-serial-number','VIP',false,now(),null);
COMMIT;