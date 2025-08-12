# Civic Insights - News Article API

Spring Boot 기반의 뉴스 기사 관리 REST API 서비스입니다.

## 📋 프로젝트 개요

시민 참여, 기본소득, 메가트렌드 등의 주제를 다루는 뉴스 기사를 관리하는 백엔드 API 서비스입니다. Spring Boot 3.5.4와 JPA/Hibernate를 사용하여 3-tier 아키텍처로 구현되었습니다.

## 🛠 기술 스택

### Backend
- **Framework**: Spring Boot 3.5.4
- **Language**: Java 17
- **Build Tool**: Gradle
- **ORM**: JPA/Hibernate 6.6.18
- **Database**: MySQL 8.4

### Dependencies
- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- MySQL Connector J
- Lombok
- Spring Boot DevTools

## 🏗 아키텍처

### 3-Tier Architecture
```
Controller Layer (REST API)
    ↓
Service Layer (Business Logic)
    ↓
Repository Layer (Data Access)
    ↓
MySQL Database
```

### 패키지 구조
```
src/main/java/com/makersworld/civic_sights_main/
├── config/           # Configuration Classes
│   ├── CategoryConverter.java    # String to Category enum converter
│   ├── GatewayOnlyFilter.java   # Gateway filter configuration
│   ├── SecurityConfig.java      # Spring Security configuration
│   ├── SecurityProperties.java  # Security properties
│   └── WebConfig.java           # Web MVC configuration
├── controller/       # REST API Controllers
│   └── NewsArticleController.java
├── service/         # Business Logic Services
│   └── NewsArticleService.java
├── repository/      # JPA Repositories
│   └── NewsArticleRepository.java
├── model/          # Entity Classes
│   ├── Category.java           # Category enum with value mapping
│   └── NewsArticle.java        # News article entity
└── CivicSightsMainApplication.java
```

## 📊 데이터 모델

### NewsArticle Entity
| 필드 | 타입 | 설명 | 제약조건 |
|------|------|------|----------|
| id | Long | 기사 ID | Primary Key, Auto Increment |
| title | String | 기사 제목 | NOT NULL, Max 500자 |
| mainImg | String | 메인 이미지 URL | Max 1000자 |
| author | String | 작성자 | NOT NULL, Max 100자 |
| createDate | LocalDateTime | 생성일시 | NOT NULL, Auto Generated |
| updateDate | LocalDateTime | 수정일시 | NOT NULL, Auto Updated |
| content | String | 기사 내용 | TEXT |
| category | Category | 카테고리 | NOT NULL, ENUM |
| isPremium | Boolean | 유료 여부 | NOT NULL, Default false |

### Category Enum
- `BASIC_INCOME` - 기본소득
- `CIVIC_ENGAGEMENT` - 시민 참여
- `MEGATRENDS` - 메가트렌드

## 🚀 설치 및 실행

### 1. 사전 요구사항
- Java 17
- Docker
- Git

### 2. MySQL Docker 컨테이너 실행
```bash
docker run -d --name mysql-civic \
  -e MYSQL_ROOT_PASSWORD=root \
  -p 3311:3306 \
  mysql:8.4
```

### 3. 프로젝트 클론 및 실행
```bash
git clone [repository-url]
cd civic-sights-main

# 애플리케이션 실행
./gradlew bootRun
```

### 4. 애플리케이션 확인
```bash
curl http://localhost:8080/api/articles/health
# Response: "News Article API is running!"

# (선택) 게이트웨이 전용 모드 운용 시, 게이트웨이 내부 헤더를 흉내 내어 호출
# curl -H "X-Gateway-Internal: <YOUR_GATEWAY_TOKEN>" http://localhost:8080/api/articles
```

## 📡 API 엔드포인트

### 기본 URL
```
http://localhost:8080/api/articles
```

### 뉴스 리스트 조회
| Method | Endpoint | 설명 | 페이지네이션 |
|--------|----------|------|-------------|
| GET | `/api/articles` | 전체 뉴스 리스트 | ✅ (page,size) |
| GET | `/api/articles/premium` | 유료 뉴스 리스트 | ✅ (page,size) |
| GET | `/api/articles/free` | 무료 뉴스 리스트 | ✅ (page,size) |
| GET | `/api/articles/category/{category}` | 카테고리별 뉴스 리스트 | ✅ (page,size) |

#### 지원되는 카테고리 형태
- `civic-engagement` (권장)
- `basic-income` (권장)  
- `megatrends` (권장)
- `CIVIC_ENGAGEMENT` (호환성)
- `BASIC_INCOME` (호환성)
- `MEGATRENDS` (호환성)

### 개별 뉴스 조회
| Method | Endpoint | 설명 |
|--------|----------|------|
| GET | `/api/articles/{id}` | 전체 뉴스 상세 조회 (무료/유료 구분 없음) |
| GET | `/api/articles/free/{id}` | 무료 뉴스 상세 조회 |
| GET | `/api/articles/premium/{id}` | 유료 뉴스 상세 조회 |

### 뉴스 관리 (CRUD)
| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | `/api/articles` | 새 뉴스 생성 |
| PUT | `/api/articles/{id}` | 뉴스 수정 |
| DELETE | `/api/articles/{id}` | 뉴스 삭제 |

### 기타
| Method | Endpoint | 설명 |
|--------|----------|------|
| GET | `/api/articles/health` | 헬스체크 |

## 📝 API 사용 예시

### 1. 전체 뉴스 목록 조회
```bash
curl -X GET http://localhost:8080/api/articles
```

### 2. 유료 뉴스만 조회
```bash
curl -X GET http://localhost:8080/api/articles/premium
```

### 3. 카테고리별 조회 (업데이트됨)
```bash
# 하이픈 형태 지원 (권장)
curl -X GET http://localhost:8080/api/articles/category/civic-engagement
curl -X GET http://localhost:8080/api/articles/category/basic-income
curl -X GET http://localhost:8080/api/articles/category/megatrends

# 페이지네이션 포함
curl -X GET "http://localhost:8080/api/articles/category/civic-engagement?page=0&size=10"

# 기존 ENUM 형태도 지원
curl -X GET http://localhost:8080/api/articles/category/CIVIC_ENGAGEMENT
```

### 4. 개별 뉴스 상세 조회
```bash
# 전체 뉴스 조회 (무료/유료 구분 없음)
curl -X GET http://localhost:8080/api/articles/1

# 무료 뉴스만 조회
curl -X GET http://localhost:8080/api/articles/free/1

# 유료 뉴스만 조회
curl -X GET http://localhost:8080/api/articles/premium/2
```

### 5. 새 뉴스 생성
```bash
curl -X POST http://localhost:8080/api/articles \
  -H "Content-Type: application/json" \
  -d '{
    "title": "새로운 뉴스 제목",
    "mainImg": "https://picsum.photos/800/400?random=3",
    "author": "작성자명",
    "content": "뉴스 내용입니다.",
    "category": "CIVIC_ENGAGEMENT",
    "isPremium": false
  }'
```

### 6. 뉴스 수정
```bash
curl -X PUT http://localhost:8080/api/articles/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "수정된 제목",
    "mainImg": "https://picsum.photos/800/400?random=4",
    "author": "수정된 작성자",
    "content": "수정된 내용입니다.",
    "category": "MEGATRENDS",
    "isPremium": true
  }'
```

### 7. 뉴스 삭제
```bash
curl -X DELETE http://localhost:8080/api/articles/1
```

## 🗄 데이터베이스 설정

### application.properties
```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3311/civic_sights?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.properties.hibernate.format_sql=true

# Logging
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

### 테이블 구조 (자동 생성)
```sql
CREATE TABLE news_articles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    author VARCHAR(100) NOT NULL,
    category ENUM('BASIC_INCOME','CIVIC_ENGAGEMENT','MEGATRENDS') NOT NULL,
    content TEXT,
    create_date DATETIME(6) NOT NULL,
    is_premium BIT(1) NOT NULL,
    main_img VARCHAR(1000),
    title VARCHAR(500) NOT NULL,
    update_date DATETIME(6) NOT NULL
);
```

## 🧪 테스트

### 헬스체크
```bash
curl http://localhost:8080/api/articles/health
```

### 데이터베이스 연결 확인
```bash
docker exec mysql-civic mysql -u root -proot -e "USE civic_sights; SHOW TABLES;"
```

## 🔧 주요 기능

### 1. 자동 타임스탬프
- `@CreationTimestamp`: 생성 시 자동 설정
- `@UpdateTimestamp`: 수정 시 자동 업데이트

### 2. 카테고리 기반 필터링
- ENUM 타입으로 안전한 카테고리 관리
- 카테고리별 뉴스 조회 지원

### 3. 유료/무료 구분
- Boolean 타입으로 간단한 구분
- 유료/무료 뉴스 별도 조회 지원

### 4. 이미지 URL 관리
- 외부 이미지 URL 저장
- Placeholder 이미지 지원 (picsum.photos)

### 5. 에러 핸들링
- JPA 연결 실패 시 메모리 데이터 폴백
- Try-catch를 통한 안정적인 서비스 제공


## 🆕 최신 추가 기능 (2025.08.08)

### 1. 카테고리 URL 매핑 자동화
- **CategoryConverter**: URL 경로의 하이픈 형태를 Category enum으로 자동 변환
- **WebConfig**: Spring MVC 커스텀 컨버터 등록
- **지원 형태**:
  - `civic-engagement` → `CIVIC_ENGAGEMENT`
  - `basic-income` → `BASIC_INCOME`
  - `megatrends` → `MEGATRENDS`

### 2. 향상된 에러 처리
- 잘못된 카테고리 입력 시 명확한 안내 메시지 제공
- 지원되는 카테고리 목록 자동 표시

### 3. 페이지네이션 지원
- Spring Data의 `Page<T>` 객체 반환
- 기본 페이지 크기: 25개
- URL 파라미터: `?page=0&size=25`

### 4. 실제 이미지 URL 적용
- Picsum Photos를 활용한 실제 호출 가능한 placeholder 이미지 적용
- DB 샘플 데이터의 `main_img`를 호출 가능한 URL로 교체됨

### 5. Category Enum 확장
- `fromValue(String)` 정적 메서드 추가
- 하이픈(`-`)과 언더스코어(`_`) 형태 모두 지원
- 대소문자 구분 없는 변환 지원

## 🔧 기술적 개선사항

### 자동 타입 변환 시스템 (하이픈 카테고리 → Enum)
```java
// URL: /api/articles/category/civic-engagement
// 자동 변환: "civic-engagement" → Category.CIVIC_ENGAGEMENT
@GetMapping("/category/{category}")
public ResponseEntity<Page<NewsArticle>> getArticlesByCategory(
    @PathVariable("category") Category category) {
    // Spring이 자동으로 변환 처리
}
```

### 에러 처리 향상
```json
// 잘못된 카테고리 요청 시
{
  "message": "Invalid category: invalid-category. Valid categories are: basic-income, civic-engagement, megatrends"
}
```

## 🚧 향후 개선 계획

### 1. 기능 확장
- [x] 페이지네이션 구현 ✅
- [x] 카테고리 URL 매핑 자동화 ✅
- [x] 실제 이미지 URL 연동 ✅
- [ ] 검색 기능 추가
- [ ] 뉴스 태그 시스템
- [ ] 댓글 시스템

### 2. 보안 강화
- [x] 게이트웨이 기반 인증/인가 시스템
- [ ] API Rate Limiting
- [ ] 입력 데이터 검증 강화

### 3. 운영 개선
- [ ] Docker Compose 설정
- [ ] CI/CD 파이프라인
- [ ] 모니터링 및 로깅 시스템
- [ ] API 문서 자동화 (Swagger)

## 📄 라이선스

This project is licensed under the MIT License.