DROP TABLE IF EXISTS member cascade;

CREATE TABLE member
(
    seq             bigint AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT '회원 seq',
    id              varchar(50)           NOT NULL COMMENT '회원 아이디',
    password        varchar(60)           NOT NULL COMMENT '비밀번호',
    email           varchar(100)          NOT NULL COMMENT '이메일',
    user_name       varchar(20)           NOT NULL COMMENT '회원 이름',
    create_datetime datetime              NOT NULL COMMENT '데이터 생성일'
);

DROP TABLE IF EXISTS ticket_order cascade;

CREATE TABLE ticket_order
(
    seq             bigint AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT '예매 seq',
    member_seq      bigint                NOT NULL COMMENT '회원 seq',
    booking_date    datetime              NOT NULL COMMENT '예매 일시',
    booking_status  varchar(20)           NOT NULL COMMENT '완료, 취소 상태',
    delete_flag     boolean               NOT NULL COMMENT '삭제여부',
    create_datetime datetime              NOT NULL COMMENT '데이터 생성일',
    CONSTRAINT member_seq FOREIGN KEY (seq) REFERENCES member (seq)
);

DROP TABLE IF EXISTS concert cascade;

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

DROP TABLE IF EXISTS ticket cascade;

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
    CONSTRAINT ticket_concert_seq FOREIGN KEY (seq) REFERENCES concert (seq)
);


DROP TABLE IF EXISTS grade_ticket cascade;

CREATE TABLE grade_ticket
(
    seq             bigint      NOT NULL PRIMARY KEY COMMENT '공연 seq',
    grade_name      varchar(20) NOT NULL COMMENT '티켓등급명칭',
    ticket_count    int         NOT NULL COMMENT '등급 할당 티켓수',
    create_datetime bigint      NOT NULL COMMENT '데이터 생성일',
    CONSTRAINT grade_ticket_concert_seq FOREIGN KEY (seq) REFERENCES concert (seq)
);

DROP TABLE IF EXISTS artist cascade;

CREATE TABLE artist
(
    seq              bigint AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT '아티스트 seq',
    artiest_name_kor varchar(50)           NOT NULL COMMENT '아티스트 국문명',
    artist_name_eng  varchar(50)           NOT NULL COMMENT '아티스트 영문명',
    create_datetime  datetime              NOT NULL COMMENT '데이터 생성일'
);


DROP TABLE IF EXISTS venue cascade;

CREATE TABLE venue
(
    seq             bigint AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT '공연장 seq',
    venue_name      varchar(50)           NOT NULL COMMENT '공연장 이름',
    address         varchar(255)          NOT NULL COMMENT '주소',
    seat_cnt        bigint                NOT NULL COMMENT '공연장 좌석수',
    create_datetime datetime              NOT NULL COMMENT '데이터 생성일'
);

