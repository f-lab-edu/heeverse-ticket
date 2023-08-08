DROP TABLE IF EXISTS member;
CREATE TABLE member
(
    member_id       bigint auto_increment NOT NULL PRIMARY KEY,
    id              varchar(50)           NOT NULL COMMENT '회원 id',
    password        varchar(60)           NOT NULL COMMENT '비밀번호',
    email           varchar(100)          NOT NULL COMMENT '이메일',
    user_name       varchar(20)           NOT NULL COMMENT '회원 이름',
    create_datetime datetime              NOT NULL
);

DROP TABLE IF EXISTS concert;

CREATE TABLE concert
(
    concert_id       bigint auto_increment NOT NULL PRIMARY KEY COMMENT '공연 id',
    artist_id        bigint                NOT NULL,
    concert_name     varchar(255)          NOT NULL COMMENT '공연 이름',
    concert_date     datetime              NOT NULL COMMENT '공연 날짜',
    cancelled_yn     boolean DEFAULT false COMMENT '공연 취소 여부',
    ticket_open_time datetime              NOT NULL COMMENT '티켓예매오픈시간',
    ticket_end_time  datetime              NOT NULL COMMENT '티켓예매 종료시간',
    venue_id         bigint                NOT NULL COMMENT '공연장 id',
    create_datetime  datetime              NOT NULL COMMENT 'created_datetime'
);

DROP TABLE IF EXISTS artist;

CREATE TABLE artist
(
    artist_id        bigint auto_increment NOT NULL PRIMARY KEY,
    artiest_name_kor varchar(50)           NOT NULL,
    artist_name_eng  varchar(50)           NOT NULL,
    create_datetime  datetime              NOT NULL
);

DROP TABLE IF EXISTS venue;

CREATE TABLE venue
(
    venue_id        bigint auto_increment NOT NULL PRIMARY KEY COMMENT '공연장 id',
    venue_name      varchar(50)           NOT NULL COMMENT '공연장 이름',
    address         varchar(255)          NOT NULL COMMENT '주소',
    seat_cnt        bigint                NOT NULL COMMENT '공연장 좌석수',
    create_datetime bigint                NOT NULL COMMENT '데이터 생성일'
);

