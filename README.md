# 📌 ticketing-service

### Author : 
- [Spring 9] 칠칠맞조 7조
- 김규림, 성주연, 이다희, 이서연, 이서준
---
# 📖 프로젝트 소개
티켓팅 환경에서 가장 중요한 동시성 이슈 제어를 핵심 과제로 삼아<br>
안정적인 예매 흐름을 설계하고 구현한 프로젝트입니다.

---
# 🎯 프로젝트 목표
- 동시성 제어
- 캐싱
- 최적화
- 배포와 CI/CD
---
# ✅ 주요 기능
## 사용자 & 인증
- 회원 가입 / 로그인
- JWT 토큰
- 권한 기반 접근 제어 (ADMIN/USER)
## 공연
- 공연 생성 및 삭제
- 공연 목록 조회 / 상세 조회
- 공연별 좌석 금액 등록
- 인기 검색어 조회
## 예매
- 공연 예매
- 예매 취소
- 회원별 예매 정보
- 예매 상세 정보
- 결제
## 좋아요
- 공연 좋아요 및 취소
- 공연 좋아요 수 조회
## 좌석
- 예매 가능한 좌석 목록 조회
- 좌석 등급별 남은 좌석 수 조회
- 공연별 좌석 금액 목록 조회
- 공연별 좌석 금액 단건 조회
---
# API 명세서 분석
## Entity 도출 및 비교 테이블 명세서

 https://teamsparta.notion.site/API-Entity-2c32dc3ef5148066a94be816b76ceeab

## ERD

<img width="2083" height="865" alt="스크린샷 2025-12-15 10 51 30" src="https://github.com/user-attachments/assets/91f6d27e-0009-4bc8-8436-581a8343dac6" />

## API 명세서
 https://teamsparta.notion.site/TaskFlow-API-2c32dc3ef51481139566e0201d71fe44
---
# 아키텍처 구조
 <img width="1767" height="517" alt="image" src="https://github.com/user-attachments/assets/2055c01e-a83c-404c-a13c-11cbec63239d" />

---
# 기술 스택
- Language : Java 17
- SpringBoot v3.5.9
- Postman
- DB : MySQL
- ORM : Spring Data JPA
- Docker
- AWS

---
# 테스트
- POSTMAN
- 단위 테스트 (JUnit)

---
# 개발 기간
- 2025.12.31 ~ 2026.01.09

---
# 시연 영상


---
# 프로젝트 정리본

### 
