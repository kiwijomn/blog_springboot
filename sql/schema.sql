create user `myblog`@`localhost` identified by 'myblog123!@#';


create database myblog CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

grant all privileges on myblog.* to `myblog`@`localhost`


CREATE TABLE `tb_post` (
    `id`            bigint(20)    NOT NULL AUTO_INCREMENT COMMENT 'PK',
    `title`         varchar(100)  NOT NULL COMMENT '제목',
    `content`       varchar(3000) NOT NULL COMMENT '내용',
    `writer`        varchar(20)   NOT NULL COMMENT '작성자',
    `view_cnt`      int(11)       NOT NULL COMMENT '조회 수',
    `notice_yn`     tinyint(1)    NOT NULL COMMENT '공지글 여부',
    `delete_yn`     tinyint(1)    NOT NULL COMMENT '삭제 여부',
    `file_name`         varchar(100)   COMMENT '파일명',
    `created_date`  datetime      NOT NULL DEFAULT current_timestamp() COMMENT '생성일시',
    `modified_date` datetime               DEFAULT NULL COMMENT '최종 수정일시',
    PRIMARY KEY (`id`)
) COMMENT '게시글';





ALTER TABLE tb_post ADD file_name varchar(100) NULL;

ALTER TABLE tb_post CHANGE file_name file_name varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL NULL AFTER delete_yn;


ALTER TABLE tb_post ADD orifile_name varchar(100) NULL;
ALTER TABLE tb_post CHANGE orifile_name orifile_name varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL NULL AFTER delete_yn;




