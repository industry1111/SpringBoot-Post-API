# SpringBoot를 이용한 게시판 CRUD API SERVER
## 프로젝트 구조 소개

```css
project-root/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com/
│   │   │   │   ├── mailpug/
│   │   │   │   │   ├── homework/
│   │   │   │   │   │   ├── common/
│   │   │   │   │   │   │   ├── codes/
│   │   │   │   │   │   │   ├── dto/
│   │   │   │   │   │   │   ├── entity/
│   │   │   │   │   │   │   ├── response/
│   │   │   │   │   │   ├── config/
│   │   │   │   │   │   │   ├── exception/
│   │   │   │   │   │   │   ├──...
│   │   │   │   │   │   ├── post/
│   │   │   │   │   │   │   ├── controller/
│   │   │   │   │   │   │   ├── repository/
│   │   │   │   │   │   │   ├── service/
│   │   │   │   │   │   │   ├── dto/
│   │   │   │   │   │   │   ├── entity/
├── build.gradle
├── README.md
└── ...
```
- `src/main/java`: 실제 소스 코드가 위치하는 디렉토리입니다.
- `common`: 공통으로 사용되는 코드들을 포함하는 패키지입니다.
    - `codes`: 에러 및 성공 코드 관련 클래스들이 들어있습니다.
        - `ErrorCode.java`: 에러 코드를 정의한 클래스입니다.
        - `SuccessCode.java`: 성공 코드를 정의한 클래스입니다.
    - `dto`: 데이터 전송 객체들이 들어있습니다.
        - `PageRequestDto.java`: 페이지 요청 정보를 담는 데이터 전송 객체입니다.
        - `PageResultDto.java`: 페이지 결과 정보를 담는 데이터 전송 객체입니다.
    - `entity`: 엔티티 클래스들이 들어있습니다.
        - `BaseEntity.java`: 엔티티의 공통 속성을 담는 클래스입니다.
    - `response`: API 응답과 관련된 클래스들이 들어있습니다.
        - `ApiResponse.java`: API 응답을 나타내는 클래스입니다.
        - `ErrorResponse.java`: 에러 응답을 나타내는 클래스입니다.
- `config`: 프로젝트 관련 설정 코드들을 포함하는 패키지입니다.
    - `exception`: 예외 처리 관련 클래스들이 위치합니다.
        - `BusinessExceptionHandler`: 비즈니스 예외 처리를 담당하는 클래스입니다.
        - `GlobalExceptionHandler`: 전역 예외 처리를 담당하는 클래스입니다.
    - `AppConfig`: 애플리케이션 설정을 관리하는 클래스입니다.
    - `SwaggerConfig`: Swagger API 문서 설정을 관리하는 클래스입니다.
- `post`: 게시글 관련 코드들을 포함하는 패키지입니다.
    - `controller`: API 엔드포인트를 관리하는 컨트롤러 클래스가 위치합니다.
    - `repository`: 데이터베이스와 관련된 코드를 처리하는 레포지토리 클래스들이 위치합니다.
    - `service`: 비즈니스 로직을 처리하는 서비스 클래스들이 위치합니다.
    - `dto`: 데이터 전송 객체(DTO) 클래스들이 위치합니다.
    - `domain`: 도메인(엔티티) 클래스가 위치합니다.

---


## 구현
해당 프로젝트는 `TDD(Test-Driven Development)` 방식을 중심으로 구현되었습니다.

### Test

`RepositoryTest`

- DataJpaTest를 사용하여 데이터베이스 관련 로직을 테스트하였습니다. 이를 통해 JPA 엔티티와 관련된 동작을 검증하고 데이터베이스와의 상호작용을 테스트했습니다.

`ServiceTest`
- Mock 객체를 활용하여 서비스 레이어의 비즈니스 로직을 테스트 진행하였으며, 외부 의존성을 제어하고, 예상되는 동작을 정의하여 테스트했습니다.

`ControllerTest`
- MockMvc를 사용하여 API 엔드포인트의 요청과 응답을 테스트하고, MockBean을 사용하여 레포지토리의 메타모델 매핑 컨텍스트를 가짜 객체로 대체하여 테스트했습니다.

### Exception

- 예외 처리는 전역 예외 핸들러를 활용하여 구현되었습니다. @ControllerAdvice 어노테이션을 이용하여 예외 처리 클래스를 작성하였으며,
  각 예외 상황에 맞는 응답을 생성하도록 구현했습니다. 이를 통해 애플리케이션 전반에 걸쳐 예외를 일관성 있게 처리할 수 있었습니다.

### Requset

- DTO는 각 기능별로 별도의 DTO 클래스를 생성하여 확장석을 높였고. 각 기능마다 필요한 데이터만 포함하여 요청을 처리하였습니다.

### Response

- API 호출시 응답이 성공적인 경우에는 CustomApiResponse 클래스를 사용하여 결과 데이터, 응답 코드, 메시지를 담아 응답처리 하였고
  에러 발생 시에는 ErrorResponse 클래스를 이용하여 에러 발생 시간, HTTP 상태 코드, 에러 코드, 에러 메시지를 응답에 포함시켰습니다.
  이를 통해 모든 응답에서 일관성 있는 구조와 정보를 유지합니다.

### Doc
- [API DOC](http://localhost:8080/swagger-ui/index.html): API 문서를 자동 생성하고 관리하기 위해 Swagger를 사용하였습니다. 해당 링크에서 API 문서를 확인하실 수 있습니다.


### API 엔드포인트
- `GET /posts/{postId}`: 게시글 조회
- `GET /posts`: 게시글 목록 조회 (페이징 및 게시글 카테고리 검색 기능 포함)
- `POST /posts`: 게시글 등록
- `PATCH /posts`: 게시글 수정
- `DELETE /posts/{postId}`: 게시글 삭제


## 개발 환경

프로젝트의 개발 환경은 다음과 같습니다.

- Java 11
- Spring Boot 2.7.14
- SpringBoot JPA
- QueryDSL
- H2 Database
- Swagger3.0

## 빌드 & 실행 방법

프로젝트를 빌드하고 실행하기 위해 다음 단계를 따릅니다.

1. 프로젝트 압출을 풀어줍니다.
2. 터미널/명령프롬프트을 열고 프로젝트 루트 디렉토리로 이동합니다.
3. 다음 명령을 실행하여 프로젝트를 빌드 및 실행 합니다. 해당 경로에서 bootrun.log 파일을 열면 실시간 에러 로그를 확인할 수 있습니다.
#### Mac
   ```bash
   $ ./gradlew build

   $ ./gradlew bootRun > bootrun.log 2>&1
   ```
#### Window
  ```
    gradlew.bat build

    gradlew.bat bootRun > bootrun.log 2>&1
  ```
4. EXCUTING이 나오면 서버가 성공적으로 실행된 상태입니다.
 <img height="100" src="https://github.com/industry1111/SpringBoot-Post-API/assets/98158673/19ea0d90-b7bf-4f76-8ed8-fbebe78289c8">


5.  [API DOC](http://localhost:8080/swagger-ui/index.html) Swagger API 문서를 통해 테스트를 진행 할 수 있습니다.

6. Try Out 버튼을 클릭 -> 양식에 맞는 데이터 입력 -> Excute 버튼 클릭
   <img height="700" src="https://github.com/industry1111/SpringBoot-Post-API/assets/98158673/92a69dc5-f5af-4523-bf02-29270d798b5e">

## 기타 부가 설명
- 제출된 프로젝트의 경우 H2 (메모리) 데이터베이스이기 때문에 별도의 데이터베이스 설정은 필요없습니다.
- Header의 `X-USERID`는 3~10 자 사이여야 합니다.
- [개발 진행 과정](https://velog.io/@industry1111/SpringBoot를-사용한-게시판-API-만들기01)
