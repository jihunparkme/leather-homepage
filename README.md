# leather-homepage

- TDD

- Back-End

  - Kotlin
  - Spring Boot / Spring MVC / Spring Data JPA
  - JPA / Querydsl / ~~JOOQ~~
  - Mockito, Spock
  - Gradle
  - Redis

- DevOps

  - MySQL
  - Jenkins
  - Nginx
  - AWS-EC2

- Front-End
  - Thymeleaf / Javascript / JQuery

---

**적용 기능**

- Thymleaf
  - 템플릿 레이아웃 적용
- Controller
  - API 용과 View Resolver 용 분리. (accept 로 구분)
- Exception
  - 서블릿 예외 처리
  - 스프링 부트 오류 페이지
  - API 예외 처리
- 로그인
  - 스프링 인터셉터 (로그인 세션 체크)
  - 쿠키에 아이디 저장
- Data Validation
  - 타입 에러 메시지 생성
  - Bean Validation
- 파일 업로드

---

## Member

```sql
-- 사용자
ALTER TABLE `MEMBER`
	DROP PRIMARY KEY;

DROP TABLE IF EXISTS `MEMBER` RESTRICT;

CREATE TABLE `MEMBER` (
	`member_id` INT          NOT NULL, -- 사용자 ID
	`user_id`   VARCHAR(20)  NOT NULL, -- 아이디
	`password`  VARCHAR(100) NOT NULL, -- 비밀번호
	`name`      VARCHAR(20)  NOT NULL, -- 이름
	`email`     VARCHAR(20)  NOT NULL, -- email
	`auth`      VARCHAR(10)  NOT NULL DEFAULT MEMBER -- 권한
);

ALTER TABLE `MEMBER`
	ADD CONSTRAINT `PK_MEMBER` -- 사용자 기본키
		PRIMARY KEY (
			`member_id` -- 사용자 ID
		);

ALTER TABLE `MEMBER`
	MODIFY COLUMN `member_id` INT NOT NULL AUTO_INCREMENT;

ALTER TABLE `MEMBER`
	AUTO_INCREMENT = 1;
'''
CREATE TABLE LOGIN_LOG (
    LOG_ID int(1) NOT NULL AUTO_INCREMENT PRIMARY KEY,
	USER_ID int(1) NOT NULL,
    USERNAME varchar(20) NOT NULL,
    LOGIN_DATE_TIME DATETIME NOT NULL default now()
)
'''
```

- Login `POST`

  - 아이디 저장 기능(쿠키)
  - setMaxInactiveInterval > 세션 설정(24시간)
  - 아이디 찾기(가입 시 작성한 이메일 입력 시 아이디 앞부분 보여주기)
  - 비밀번호 찾기 (가입 시 입력 이메일로 임시 PW 전송 및 DB는 임시 PW 로 수정)
  - admin 로그인 및 권한 관련 (Spring Security)
  - 카카오로 시작하기 기능

- Sign in `POST`
  - 비밀번호 숫자/영문 포함 10자 이상
- View (개인정보 확인) `POST`
  - 접근 시 로그인 세션 체크
- Edit (개인정보 변경) `POST`

  - 접근 시 로그인 세션 체크
  - 저장 시 현재 비밀번호 확인 (본인 확인 목적)

- 관리자 전용 페이지
  - 상품 등록/수정/삭제
  - 후기 삭제
  - ~~회원 관리(수정/삭제)~~

## Product

```java
@Data
public class Item {
    private Long id;
    private String itemName;
    private UploadFile attachFile;
    private List<UploadFile> imageFiles;
}

@Data
public class UploadFile {
    private String uploadFileName;
    private String storeFileName;
    public UploadFile(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }
}
```

```mysql
CREATE TABLE PRODUCT (
    product_id int(1) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title varchar(50) NOT NULL,
    contents varchar(MAX) NOT NULL,
    thumbnail_id int(1) NULL,
    category_id int(1) NOT NULL, -- foreign key (P:C - N:1)
    hits int(1) NOT NULL default 0,
    delete_yn varchar(1) NOT NULL,
    dekete_date_time DATETIME NOT NULL,
    created_date_time DATETIME NOT NULL,
    modified_date_time DATETIME NULL
)

CREATE TABLE PRODUCT_ATTACHMENTS (
    attachments_id int(1) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    product_id int(1) NOT NULL, -- foreign key (A:P - N:1)
    orig_filename varchar(100) NOT NULL, -- 이건 알아보고 삭제할지 판단
    file_name varchar(100) NOT NULL, -- UUID
    file_path varchar(100) NOT NULL,
    created_date_time DATETIME NOT NULL,
    modified_date_time DATETIME NULL
)

CREATE TABLE PRODUCT_CATEGORY (
    category_id int(1) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title varchar(20) NOT NULL,
    order_no int(1) NOT NULL, -- 관리자 페이지에서 드래그로 우선순위 정하는거 확인
    category_use_yn varchar(1) NOT NULL
)
```

- List
  - Infinite Scrolling
- View `GET`
  - 이미지 클릭 시 view layout 띄우기
  - admin 권한일 경우 수정 버튼 활성화
- New & Edit `POST`
  - CKeditor Library
  - 이미지 업로드
  - 사진 수정 시, 기존 첨부 리스트와 삭제된 첨부 리스트 체크
  - 접근 시 ADMIN 권한 체크 (서버단에서 ROLE 확인)
  - 수정 시 삭제 버튼 활성

---

## Guest book

- 후기

```sql
CREATE TABLE GUEST_BOOK (
    guest_book_id int(1) NOT NULL AUTO_INCREMENT PRIMARY KEY,
   	name varchar(50) NOT NULL,
  	password varchar(50) NOT NULL,
    contents varchar(MAX) NOT NULL,
    created_date_time DATETIME NOT NULL,
    modified_date_time DATETIME NULL
)
```

- New
  - layout 으로
- Edit 버튼
  - 접근 시 작성한 비밀번호 입력
  - admin 권한의 경우 바로 접근

방명록 남기면 관리자에게 메일/카카오톡 알림

## Contact Us

문의하기
문의가 접수되면 메일/카카오톡으로 안내.

```sql
CREATE TABLE CONTACT_US (
    contact_us int(1) NOT NULL AUTO_INCREMENT PRIMARY KEY,
   	name varchar(50) NOT NULL,
    email varchar(50) NULL,
    phone_number varchar(15) NOT NULL,
  	title varchar(50) NOT NULL,
    contents varchar(MAX) NOT NULL,
    created_date_time DATETIME NOT NULL,
    modified_date_time DATETIME NULL
)
```

## About

- html 로
- About 관리 페이지?

## ETC

**Jacoco Code Coverage 확인**

**database backup**

- DB 데이터 백업 스케쥴러?(https://server-talk.tistory.com/30)

**hosting**

- AWS EC2

**네이버 예약 기능**

> Reference

- https://bootstrapmade.com/sailor-free-bootstrap-theme/
- https://yhworkshop.com/

---

# Pending

## Post

블로그

```mysql
CREATE TABLE POST (
	POST_ID int(1) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    USER_ID int(1) NOT NULL,
    TITLE varchar(50) NOT NULL,
    CONTENTS varchar(MAX) NOT NULL,
    PERIOD DATETIME NOT NULL,
    VIEWS int(1) NOT NULL default 0,
    CATEGORY_ID int(1) NOT NULL, -- foreign key (P:C - N:1) @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="CATEGORY_ID")
    THUMBNAIL_ID Long NULL,
    CREATED_DATE_TIME DATETIME NOT NULL,
    MODIFIED_DATE_TIME DATETIME NULL
    -- commentsList
)

CREATE TABLE POST_THUMBNAIL (
    THUMBNAIL_ID int(1) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    POST_ID int(1) NOT NULL, -- foreign key (I:P - 1:1)
    ORIG_FILENAME varchar(100) NOT NULL,
    FILE_NAME varchar(100) NOT NULL,
    FILE_PATH varchar(100) NOT NULL,
    FILE_SIZE int(1) NOT NULL,
    CREATED_DATE_TIME DATETIME NOT NULL,
    MODIFIED_DATE_TIME DATETIME NULL
)

CREATE TABLE POST_CATEGORY (
    CATEGORY_ID int(1) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    TITLE varchar(20) NOT NULL,
    ORDER_NO int(1) NOT NULL,
    USE TINYINT(1) NOT NULL default 1
)

CREATE TABLE POST_TAG (
    SEQ int(1) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    POST_ID int(1) NOT NULL, -- foreign key (T:P - N:1)
    TAG varchar(200) NOT NULL
)
```

- List `GET`
  - 검색 (제목, 내용, 카테고리, 태그)
  - 카테고리별 조회
  - 최근 게시물 리스트
  - 인기 게시물 Top5 리스트
  - 인기 태그 Top10 리스트
  - paging
- View `GET`
  - admin 권한일 경우 수정 버튼 활성화
- New & Edit `POST`
  - CKeditor
  - 메인 이미지 파일 첨부 칸
  - 접근 시 ADMIN 권한 체크 (서버단에서 ROLE 확인)
    - @PreAuthorize("hasWildCardAnyRole('')")
    - @PreAuthorize("hasAnyRole(')")
  - 태그는`;`(세미콜론) 단위로 입력받고, 서버단에서 split & trim 후 저장
    - 또는 입력 칸 만들고 태그 영역에서 엔터 누르면 칸 추가되도록.
  - 수정 시 삭제 버튼 활성

## Comments

댓글

```sql
CREATE TABLE POST_COMMENTS (
    COMMENTS_ID int(1) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    POST_ID int(1) NOT NULL, -- @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name = "POST_ID")
   	NAME varchar(50) NOT NULL,
  	PASSWORD varchar(50) NOT NULL,
    CONTENTS varchar(MAX) NOT NULL,
    CREATED_DATE_TIME DATETIME NOT NULL,
    MODIFIED_DATE_TIME DATETIME NULL

    -- @OneToMany(mappedBy="postComments") private List<PostCommentsReply> postCommentsReplyList;
)

CREATE TABLE POST_COMMENTS_REPLY (
    COMMENTS_ID int(1) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    POST_COMMENTS_ID int(1) NOT NULL, -- @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="POST_COMMENTS_ID")
   	NAME varchar(50) NOT NULL,
  	PASSWORD varchar(50) NOT NULL,
    CONTENTS varchar(MAX) NOT NULL,
    CREATED_DATE_TIME DATETIME NOT NULL,
    MODIFIED_DATE_TIME DATETIME NULL
)
```

- Edit 버튼
  - 접근 시 작성한 비밀번호 입력
  - admin 권한의 경우 바로 접근
- Reply 버튼 클릭 시 Comment 내용에 @아이디 내용 써주고 forcous
  - 각 로우에 data-name, data-comments-id 정보가 필요할 듯
  - 저장 시 new와 reply 구분해서 저장 분리

## REVIEW

후기

```mysql
CREATE TABLE REVIEW (
    REVIEW_ID int(1) NOT NULL AUTO_INCREMENT PRIMARY KEY,
   	NAME varchar(50) NOT NULL,
  	PASSWORD varchar(50) NOT NULL,
    CONTENTS varchar(MAX) NOT NULL,
    SECRET TINYINT(1) NOT NULL default 0,
    CREATED_DATE_TIME DATETIME NOT NULL,
    MODIFIED_DATE_TIME DATETIME NULL
)

CREATE TABLE REVIEW_REPLY (
    REPLY_ID int(1) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    REVIEW_ID int(1) NOT NULL, -- foreign key (RP:RV - N:1)
   	NAME varchar(50) NOT NULL,
  	PASSWORD varchar(50) NOT NULL,
    CONTENTS varchar(MAX) NOT NULL,
    SECRET TINYINT(1) NOT NULL default 0,
    CREATED_DATE_TIME DATETIME NOT NULL,
    MODIFIED_DATE_TIME DATETIME NULL
)
```

https://freehoon.tistory.com/115?category=735500

https://freehoon.tistory.com/121?category=735500

- List
  - admin 권한의 경우에만 삭제 버튼 활성화
- View
  - 수정 / 삭제 버튼
  - 댓글
    - 관리자의 경우 관리자 정보 표시
  - 수정 혹은 삭제 시 입력 시 작성한 비밀번호 입력
    - 관리자는 바로 수정 가능
- New & Edit
  - 이름, 비밀번호, Comment 입력
  - 비밀글 체크박스
