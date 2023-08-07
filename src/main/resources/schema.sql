DROP TABLE IF EXISTS member;

CREATE TABLE member
(
    member_id       bigint auto_increment NOT NULL PRIMARY KEY,
    id              varchar(50)  NOT NULL,
    password        varchar(60)  NOT NULL,
    email           varchar(100) NOT NULL,
    user_name       varchar(20)  NOT NULL,
    create_datetime datetime     NOT NULL
);


DROP TABLE IF EXISTS ticket;

CREATE TABLE ticket (
    ticket_id	bigint	auto_increment NOT NULL	PRIMARY KEY COMMENT '티켓아이디',
    ticket_serial_number    varchar(255) NOT NULL ,
    grade_id	bigint	NOT NULL,
    concert_id	bigint	NOT NULL	COMMENT '공연 id',
    order_id	bigint	,
    delete_yn	boolean	COMMENT '취소여부',
    purchase_date timestamp COMMENT '구매 일시',
    create_datetime	timestamp	NOT NULL
);


DROP TABLE IF EXISTS ticket_grade;

CREATE TABLE ticket_grade (
    grade_id	bigint  auto_increment  NOT NULL PRIMARY KEY,
    grade	int	NOT NULL,
    grade_name	varchar(20)	NOT NULL,
    seat_count	int	NOT NULL,
    concert_id bigint NOT NULL,
    create_datetime	timestamp	NOT NULL
);