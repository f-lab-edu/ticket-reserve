# Ticket Reserve
영화 티켓을 예매할 수 있는 백엔드 서비스입니다.

## Environment
* Java 17 : [OpenJDK 17](https://adoptium.net/temurin/releases/?version=17)
* Spring Boot 3.2.5
* MySQL 8.0.35

## 주요 기능
* 회원 가입 : 사용자는 이메일을 사용하여 회원 가입할 수 있습니다.
* 영화 검색 및 조회 : 사용자가 원하는 영화를 검색하고 상세 정보를 확인할 수 있습니다.
* 티켓 예매 : 특정 영화의 상영시간에 해당하는 티켓을 예매할 수 있습니다. (결제 기능은 미구현)
* 관리자 기능 : 관리자는 영화관, 영화, 좌석, 상영시간표 정보를 등록하고 수정 및 삭제할 수 있습니다.

## 주요 API 설명
### 1. 회원 가입 - `POST` /signup
* 이메일과 비밀번호를 입력값으로 받아 회원 가입을 처리합니다.
* 이미 가입한 이메일인 경우 400 상태코드와 함께 오류 메시지가 반환됩니다.
* 구현 코드 : [SignupController](https://github.com/f-lab-edu/ticket-reserve/blob/main/web/src/main/java/com/kjh/web/controller/SignupController.java)

### 2. 로그인 - `POST` /signin
* 이메일과 비밀번호를 입력값으로 받아 정보가 일치할 경우 JWT 액세스 토큰을 반환합니다.
* 이메일 또는 비밀번호가 맞지 않을 경우 400 상태코드와 함께 오류 메시지가 반환됩니다.
* 구현 코드 : [SigninController](https://github.com/f-lab-edu/ticket-reserve/blob/main/web/src/main/java/com/kjh/web/controller/SigninController.java)

### 3. 영화관 검색 - `GET` /theaters
* 페이지 번호, 페이지 크기, 영화관 이름, 주소를 받아 영화관 목록을 페이지 단위로 검색합니다.
* 구현 코드 : [TheaterController](https://github.com/f-lab-edu/ticket-reserve/blob/main/web/src/main/java/com/kjh/web/controller/TheaterController.java#L31)

### 4. 영화 검색 - `GET` /movies
* 페이지 번호, 페이지 크기, 영화 제목, 상영일을 받아 영화 목록을 페이지 단위로 검색합니다.
* 구현 코드 : [MovieController](https://github.com/f-lab-edu/ticket-reserve/blob/main/web/src/main/java/com/kjh/web/controller/MovieController.java#L28)

### 5. 상영시간표 검색 - `GET` /showtimes
* 페이지 번호, 페이지 크기, 영화관 아이디, 영화 아이디, 일자를 받아 상영시간표 목록을 페이지 단위로 검색합니다.
* 구현 코드 : [ShowtimeController](https://github.com/f-lab-edu/ticket-reserve/blob/main/web/src/main/java/com/kjh/web/controller/ShowtimeController.java#L37)

### 6. 상영시간표 좌석 조회 - `GET` /showtimes/{id}/seats
* 상영시간표 아이디를 받아 해당 상영 일정에 대한 모든 좌석 목록을 조회합니다.
* 구현 코드 : [ShowtimeController](https://github.com/f-lab-edu/ticket-reserve/blob/main/web/src/main/java/com/kjh/web/controller/ShowtimeController.java#L50)

### 7. 영화 예매 - `POST` /reservations
* 상영시간표 아이디와 좌석 아이디를 입력값으로 받아 해당 상영 일정의 좌석을 예매합니다.
* 좌석이 이미 예매된 경우 400 상태코드와 함께 오류 메시지가 반환됩니다.
* 예매 성공 시 예매 상세 정보가 사용자의 이메일로 발송됩니다.
* 낙관적 락을 적용하여 여러 명이 동일한 좌석을 예매할 때 생기는 동시성 문제를 해결하였습니다.
* AWS SQS를 사용하여 메일 발송 큐에 메시지를 전송하고, SES를 사용해 메일 발송을 처리하였습니다.
* 구현 코드 : [ReservationController](https://github.com/f-lab-edu/ticket-reserve/blob/main/web/src/main/java/com/kjh/web/controller/ReservationController.java#L26), [ReservationService](https://github.com/f-lab-edu/ticket-reserve/blob/main/core/src/main/java/com/kjh/core/service/ReservationService.java#L40)
* 테스트 코드 : [PostTests](https://github.com/f-lab-edu/ticket-reserve/blob/main/web/src/test/java/com/kjh/web/reservation/PostTests.java#L109)


## ERD
![erd](./erd.png)
