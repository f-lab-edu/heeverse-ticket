DROP TABLE IF EXISTS member;

CREATE TABLE member
(
    seq             bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
    id              varchar(50)           NOT NULL,
    password        varchar(60)           NOT NULL,
    email           varchar(100)          NOT NULL,
    user_name       varchar(20)           NOT NULL,
    create_datetime datetime              NOT NULL
);

DROP TABLE IF EXISTS ticket;

CREATE TABLE ticket
(
    seq                  bigint AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT '티켓아이디',
    purchase_date        timestamp             NULL COMMENT '구매 일시',
    ticket_serial_number varchar(255)          NULL,
    order_id             bigint                NOT NULL,
    concert_id           bigint                NOT NULL COMMENT '공연 id',
    grade_name           varchar(255)          NOT NULL,
    delete_yn            boolean               NOT NULL COMMENT '취소여부',
    create_datetime      datetime              NOT NULL
);

DROP TABLE IF EXISTS ticket_order;

CREATE TABLE ticket_order
(
    seq             bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
    member_id       bigint                NOT NULL,
    booking_date    datetime              NOT NULL,
    status          varchar(20)           NOT NULL COMMENT '완료, 취소',
    delete_yn       boolean               NOT NULL COMMENT '삭제여부',
    create_datetime datetime              NOT NULL
);

DROP TABLE IF EXISTS grade_ticket;

CREATE TABLE grade_ticket
(
    seq             bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
    grade_name      varchar(20)           NOT NULL,
    ticket_count    int                   NOT NULL,
    create_datetime bigint                NOT NULL,
    concert_id      bigint                NOT NULL COMMENT '공연 id'
);

DROP TABLE IF EXISTS concert;

CREATE TABLE concert
(
    seq              bigint AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT '공연 id',
    concert_name     varchar(255)          NOT NULL COMMENT '공연 이름',
    concert_date     datetime              NOT NULL COMMENT '공연 날짜',
    cancelled_yn     bigint                NULL DEFAULT false COMMENT '공연 취소 여부',
    ticket_open_time datetime              NOT NULL COMMENT '티켓예매오픈시간',
    ticket_end_time  datetime              NOT NULL COMMENT '티켓예매 종료시간',
    create_datetime  datetime              NOT NULL COMMENT 'created_datetime'
);

DROP TABLE IF EXISTS artist;

CREATE TABLE artist
(
    seq              bigint AUTO_INCREMENT NOT NULL PRIMARY KEY,
    concert_id       bigint                NOT NULL COMMENT '공연 id',
    artiest_name_kor varchar(50)           NOT NULL,
    artist_name_eng  varchar(50)           NOT NULL,
    create_datetime  datetime              NOT NULL
);

DROP TABLE IF EXISTS venue;

CREATE TABLE venue
(
    seq             bigint AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT '공연장 id',
    concert_id      bigint                NOT NULL COMMENT '공연 id',
    venue_name      varchar(50)           NOT NULL COMMENT '공연장 이름',
    address         varchar(255)          NOT NULL COMMENT '주소',
    seat_cnt        bigint                NOT NULL COMMENT '공연장 좌석수',
    create_datetime datetime              NOT NULL COMMENT '데이터 생성일'
);

