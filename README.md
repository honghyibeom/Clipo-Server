# SpringBoot-Project-SNS
- server -> springBoot
- client -> React
SNS 웹 + 모바일 프로젝트 입니다.
이 프로젝트를 통해 자바와 리엑트 그리고 client-server 의 구조를 파악하였습니다.

## 🖥️ 프로젝트 소개
Thread Instagram 를 참고하여 만든 SNS 사이트입니다.
이것을 배포하여 주변 친구들과 함께 일상을 공유하는 앱을 만들고자 개발하였습니다. 
<br>

## 🕰️ 개발 기간
* 24.06.26일 - 진행중
* 2명 개발이고 서로 직장을 다니며 개발하였기 때문에 느려졌습니다;

### 🧑‍🤝‍🧑 맴버구성
 - 팀원1  : 홍희범 - String-Boot Server
 - 팀원2  : 유현우 - React Client

### ⚙️ 개발 환경
- `Java 17`
- `JDK 1.8.0`
- **IDE** : STS 3.9
- **Framework** : Springboot(3.3.1)
- **Database** : Maria DB(11xe)
- **ORM** : JPA

## 📌 주요 기능
#### 로그인 - <a href="url" >상세보기 - WIKI 이동</a>
- jwt 방식 로그인 구현
- 소셜 로그인(OAuth2)
- 토큰 검증
- ID찾기, PW찾기
- 로그인 시 쿠키(Cookie) 및 세션(Session) 생성

#### 회원가입 - <a href="url" >상세보기 - WIKI 이동</a>
- ID 중복 체크
- radis를 통한 문자인증

#### 마이 페이지 - <a href="url" >상세보기 - WIKI 이동</a>
- 회원정보 변경
- 자신의 게시글 및 댓글 관리

#### 게시글 CRUD - <a href="url" >상세보기 - WIKI 이동</a>
- 게시글 : 이미지 = 1:n
- 글, 이미지, 태그 작성 가능
- 작성시 좋아요와 댓글 막기 여부
- 좋아요 기능
- 댓글 기능
#### 메인 페이지 - <a href="url" >상세보기 - WIKI 이동</a>
- 최신순으로 게시글 목록 보여줌 (스레드처럼 알고리즘으로 게시글을 보여주고 싶음 -> 돈 + 시간의 문제) 
- 게시글 작성 기능

#### 검색 기능 - <a href="url" >상세보기 - WIKI 이동</a>
- 유저 검색
- 테그 검색
