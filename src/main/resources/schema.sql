DROP TABLE IF EXISTS member;

CREATE TABLE member
(
    member_id       bigint auto_increment NOT NULL,
    id              varchar(50)  NOT NULL,
    password        varchar(60)  NOT NULL,
    email           varchar(100) NOT NULL,
    user_name       varchar(20)  NOT NULL,
    create_datetime datetime     NOT NULL
);