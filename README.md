# study-system
# server-impl

## 주요 기능 요약

- 🔐 **JWT 기반 회원 인증** (`members`)
- 🗂️ **스터디 모집/스터디 개설 및 신청**
---

## 🗄️ 데이터베이스 구조 (요약)

| 테이블명            | 설명                  |
|---------------------|---------------------|
| `members`           | 회원 정보 저장, JWT 인증 대상 |
| `studyroom`             | 스터디 정보 저장           |



## ⚙️ 기술 스택

- **Language:** Java 17
- **Framework:** Spring Boot 3.x
- **Build Tool:** Gradle
- **Database:** MySQL 8.x
- **Security:** Spring Security + JWT
---

## 🚀 실행 방법

1. `.env` 파일 또는 `application.yml` 설정
2. MySQL DB 생성 및 `database.sql` 실행
3. Gradle 빌드 후 실행

```bash
./gradlew build
java -jar build/libs/bridgehub-api.jar
