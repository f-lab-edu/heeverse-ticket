DROP TABLE IF EXISTS ticket_order cascade;
DROP TABLE IF EXISTS member cascade;
DROP TABLE IF EXISTS ticket cascade;
DROP TABLE IF EXISTS grade_ticket cascade;
DROP TABLE IF EXISTS concert cascade;
DROP TABLE IF EXISTS venue cascade;
DROP TABLE IF EXISTS artist cascade;
DROP TABLE IF EXISTS ticket_order_log cascade;
DROP TABLE IF EXISTS ticket_order_log_copy cascade;
DROP TABLE IF EXISTS ticket_order_log_denormalization_copy cascade;
DROP TABLE IF EXISTS ticket_order_result cascade;


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
    cancelled_flag   boolean DEFAULT FALSE COMMENT '공연 취소 여부',
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
    cancelled_flag       boolean DEFAULT FALSE COMMENT '취소여부',
    create_datetime      datetime              NOT NULL COMMENT '데이터 생성일',
    order_seq            bigint                NULL COMMENT '예매 seq'
);


CREATE TABLE grade_ticket
(
    seq             bigint      AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT '티켓 등급 seq',
    grade_name      varchar(20) NOT NULL COMMENT '티켓등급명칭',
    ticket_count    int         NOT NULL COMMENT '등급 할당 티켓수',
    create_datetime datetime    NOT NULL COMMENT '데이터 생성일',
    concert_seq     bigint      NOT NULL COMMENT '공연 seq'
);

CREATE TABLE artist
(
    seq             bigint AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT '아티스트 seq',
    artist_name_kor varchar(50)           NOT NULL COMMENT '아티스트 국문명',
    artist_name_eng varchar(50)           NOT NULL COMMENT '아티스트 영문명',
    create_datetime datetime              NOT NULL COMMENT '데이터 생성일'
);


CREATE TABLE venue
(
    seq             bigint AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT '공연장 seq',
    venue_name      varchar(50)           NOT NULL COMMENT '공연장 이름',
    address         varchar(255)          NOT NULL COMMENT '주소',
    seat_cnt        bigint                NOT NULL COMMENT '공연장 좌석수',
    create_datetime datetime              NOT NULL COMMENT '데이터 생성일'
);

create table ticket_order_log
(
    seq              int auto_increment primary key,
    member_seq       int      null,
    ticket_seq       int      null,
    ticket_order_seq int      null,
    create_datetime  datetime null,
    concert_seq      int      null
)
;

create table ticket_order_log_copy
(
    seq              bigint auto_increment PRIMARY KEY,
    member_seq       bigint   ,
    ticket_seq       bigint   ,
    ticket_order_seq bigint   ,
    create_datetime  datetime ,
    concert_seq      bigint
)
;

create table ticket_order_log_denormalization_copy
(
    concert_seq      bigint         not null,
    ticket_seq       bigint      ,
    member_seq       bigint      ,
    grade_name       varchar(20) ,
    ticket_order_seq bigint      ,
    create_datetime  datetime
)
;


create table ticket_order_result
(
    concert_seq     int                                not null,
    grade_name      varchar(20)                        not null,
    total_tickets   int                                null,
    order_try       int                                null,
    create_datetime datetime default CURRENT_TIMESTAMP null
)
;