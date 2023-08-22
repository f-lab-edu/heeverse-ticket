DROP TABLE IF EXISTS ticket_order cascade;
DROP TABLE IF EXISTS member cascade;
DROP TABLE IF EXISTS ticket cascade;
DROP TABLE IF EXISTS grade_ticket cascade;
DROP TABLE IF EXISTS concert cascade;
DROP TABLE IF EXISTS venue cascade;
DROP TABLE IF EXISTS artist cascade;


CREATE TABLE member
(
    seq             bigint AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT '회원 seq',
    id              varchar(50)           NOT NULL COMMENT '회원 아이디',
    password        varchar(60)           NOT NULL COMMENT '비밀번호',
    email           varchar(100)          NOT NULL COMMENT '이메일',
    user_name       varchar(20)           NOT NULL COMMENT '회원 이름',
    create_datetime datetime              NOT NULL COMMENT '데이터 생성일'
);



CREATE TABLE ticket_order
(
    seq             bigint AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT '예매 seq',
    member_seq      bigint                NOT NULL COMMENT '회원 seq',
    booking_date    datetime              NOT NULL COMMENT '예매 일시',
    booking_status  varchar(20)           NOT NULL COMMENT '완료, 취소 상태',
    delete_flag     boolean               NOT NULL COMMENT '삭제여부',
    create_datetime datetime              NOT NULL COMMENT '데이터 생성일',
    CONSTRAINT member_seq FOREIGN KEY (member_seq) REFERENCES member (seq)
);



CREATE TABLE concert
(
    seq              bigint AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT '공연 seq',
    concert_name     varchar(255)          NOT NULL COMMENT '공연 이름',
    concert_date     datetime              NOT NULL COMMENT '공연 날짜',
    cancelled_flag   boolean               NOT NULL DEFAULT false COMMENT '공연 취소 여부',
    ticket_open_time datetime              NOT NULL COMMENT '티켓예매오픈시간',
    ticket_end_time  datetime              NOT NULL COMMENT '티켓예매 종료시간',
    create_datetime  datetime              NOT NULL COMMENT 'created_datetime',
    venue_seq        bigint                NOT NULL COMMENT '공연장 seq',
    artist_seq       bigint                NOT NULL COMMENT '아티스트 seq'
);



CREATE TABLE ticket
(
    seq                  bigint AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT '티켓 seq',
    concert_seq          bigint                NOT NULL COMMENT '공연 seq',
    purchase_date        datetime              NULL COMMENT '구매 일시',
    ticket_serial_number varchar(255)          NULL COMMENT '티켓 시리얼 넘버',
    grade_name           varchar(255)          NOT NULL COMMENT '티켓등급',
    cancelled_flag       boolean               NOT NULL COMMENT '취소여부',
    create_datetime      datetime              NOT NULL COMMENT '데이터 생성일',
    order_seq            bigint                NOT NULL COMMENT '예매 seq',
    CONSTRAINT ticket_concert_seq FOREIGN KEY (concert_seq) REFERENCES concert (seq)
);


CREATE TABLE grade_ticket
(
    seq             bigint      AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT '티켓 등급 seq',
    grade_name      varchar(20) NOT NULL COMMENT '티켓등급명칭',
    ticket_count    int         NOT NULL COMMENT '등급 할당 티켓수',
    create_datetime datetime    NOT NULL COMMENT '데이터 생성일',
    concert_seq     bigint      NOT NULL COMMENT '공연 seq',
    CONSTRAINT grade_ticket_concert_seq FOREIGN KEY (concert_seq) REFERENCES concert (seq)
);

CREATE TABLE artist
(
    seq              bigint AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT '아티스트 seq',
    artist_name_kor varchar(50)           NOT NULL COMMENT '아티스트 국문명',
    artist_name_eng  varchar(50)           NOT NULL COMMENT '아티스트 영문명',
    create_datetime  datetime              NOT NULL COMMENT '데이터 생성일'
);


CREATE TABLE venue
(
    seq             bigint AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT '공연장 seq',
    venue_name      varchar(50)           NOT NULL COMMENT '공연장 이름',
    address         varchar(255)          NOT NULL COMMENT '주소',
    seat_cnt        bigint                NOT NULL COMMENT '공연장 좌석수',
    create_datetime datetime              NOT NULL COMMENT '데이터 생성일'
);

insert into CONCERT (CONCERT_NAME, CONCERT_DATE, CANCELLED_FLAG, TICKET_OPEN_TIME, TICKET_END_TIME, CREATE_DATETIME, VENUE_SEQ, ARTIST_SEQ)
values ('BTS콘서트','2023-08-24T04:31:27',false,'2023-08-25T04:31:27','2023-08-30T04:31:27',now(),1,1);

insert into CONCERT (CONCERT_NAME, CONCERT_DATE, CANCELLED_FLAG, TICKET_OPEN_TIME, TICKET_END_TIME, CREATE_DATETIME, VENUE_SEQ, ARTIST_SEQ)
values ('아이브콘서트','2023-08-25T04:31:27',false,'2023-08-26T04:31:27','2023-08-30T04:31:27',now(),2,2);

insert into CONCERT (CONCERT_NAME, CONCERT_DATE, CANCELLED_FLAG, TICKET_OPEN_TIME, TICKET_END_TIME, CREATE_DATETIME, VENUE_SEQ, ARTIST_SEQ)
values ('BTS콘서트','2023-08-26T04:31:27',false,'2023-08-27T04:31:27','2023-09-01T04:31:27',now(),1,1);

insert into CONCERT (CONCERT_NAME, CONCERT_DATE, CANCELLED_FLAG, TICKET_OPEN_TIME, TICKET_END_TIME, CREATE_DATETIME, VENUE_SEQ, ARTIST_SEQ)
values ('뉴진스콘서트','2023-09-24T04:31:27',false,'2023-09-25T04:31:27','2023-09-30T04:31:27',now(),4,3);

insert into CONCERT (CONCERT_NAME, CONCERT_DATE, CANCELLED_FLAG, TICKET_OPEN_TIME, TICKET_END_TIME, CREATE_DATETIME, VENUE_SEQ, ARTIST_SEQ)
values ('성시경 10주년 콘서트','2023-09-24T04:31:27',false,'2023-09-25T04:31:27','2023-09-30T04:31:27',now(),4,4);

insert into CONCERT (CONCERT_NAME, CONCERT_DATE, CANCELLED_FLAG, TICKET_OPEN_TIME, TICKET_END_TIME, CREATE_DATETIME, VENUE_SEQ, ARTIST_SEQ)
values ('찰리푸스 내한','2023-09-24T04:31:27',false,'2023-09-25T04:31:27','2023-09-30T04:31:27',now(),2,5);

insert into CONCERT (CONCERT_NAME, CONCERT_DATE, CANCELLED_FLAG, TICKET_OPEN_TIME, TICKET_END_TIME, CREATE_DATETIME, VENUE_SEQ, ARTIST_SEQ)
values ('박재범 워터밤','2023-10-24T04:31:27',false,'2023-10-25T04:31:27','2023-10-30T04:31:27',now(),2,6);

insert into CONCERT (CONCERT_NAME, CONCERT_DATE, CANCELLED_FLAG, TICKET_OPEN_TIME, TICKET_END_TIME, CREATE_DATETIME, VENUE_SEQ, ARTIST_SEQ)
values ('BTS콘서트-고척','2023-10-24T04:31:27',false,'2023-10-25T04:31:27','2023-10-30T04:31:27',now(),2,1);

insert into CONCERT (CONCERT_NAME, CONCERT_DATE, CANCELLED_FLAG, TICKET_OPEN_TIME, TICKET_END_TIME, CREATE_DATETIME, VENUE_SEQ, ARTIST_SEQ)
values ('BTS콘서트-상암','2023-10-31T04:31:27',false,'2023-11-01T04:31:27','2023-11-07T04:31:27',now(),4,1);



insert into VENUE (VENUE_NAME, ADDRESS, SEAT_CNT, CREATE_DATETIME) values ('잠실 주경기장','서울시 송파구',100,now());
insert into VENUE (VENUE_NAME, ADDRESS, SEAT_CNT, CREATE_DATETIME) values ('고척돔','서울시 금천구',150,now());
insert into VENUE (VENUE_NAME, ADDRESS, SEAT_CNT, CREATE_DATETIME) values ('상암월드컵','서울시 마포구',200,now());
insert into VENUE (VENUE_NAME, ADDRESS, SEAT_CNT, CREATE_DATETIME) values ('체조경기장','서울시 동대문구',400,now());




insert into ARTIST (ARTIST_NAME_KOR, ARTIST_NAME_ENG, CREATE_DATETIME) values ('방탄소년단','BTS',now());
insert into ARTIST (ARTIST_NAME_KOR, ARTIST_NAME_ENG, CREATE_DATETIME) values ('아이브','IVE',now());
insert into ARTIST (ARTIST_NAME_KOR, ARTIST_NAME_ENG, CREATE_DATETIME) values ('뉴진스','new-jeans',now());
insert into ARTIST (ARTIST_NAME_KOR, ARTIST_NAME_ENG, CREATE_DATETIME) values ('성시경','Seoung SiKyeong',now());
insert into ARTIST (ARTIST_NAME_KOR, ARTIST_NAME_ENG, CREATE_DATETIME) values ('찰리푸스','Charile Puth',now());
insert into ARTIST (ARTIST_NAME_KOR, ARTIST_NAME_ENG, CREATE_DATETIME) values ('박재범','J-park',now());

