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
# ERD

## 와이어 프레임

https://www.figma.com/board/4TIZPWwvOBLWN8NkqFvX5j/%EC%B9%A0%EC%B9%A0%ED%95%98%EC%A1%B0_7%EC%A1%B0?node-id=0-1&t=RWTxFMCGbn9iuI9n-1

## 테이블 명세서

https://www.notion.so/teamsparta/2cb2dc3ef514816a8546d268e904f755

## ERD

<img width="2056" height="729" alt="image" src="https://github.com/user-attachments/assets/eb48965b-4a5c-4f27-b4d1-58f6be0991d8" />

## API 명세서

https://www.notion.so/teamsparta/2cb2dc3ef51481568c75c154542b6637?v=2cb2dc3ef514816fa8bb000c5bdd0511

---
# 아키텍처 구조

<img width="795" height="538" alt="아키텍쳐(다크모드)" src="https://github.com/user-attachments/assets/a4a6c05f-bf08-4072-91c3-4a9bf64bdf39 #gh-dark-mode-only" />

<img width="795" height="555" alt="아키텍쳐" src="https://github.com/user-attachments/assets/224094c0-a735-41d5-bce4-62d3ef91e6cf #gh-light-mode-only" />


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

https://youtu.be/VFXZONS8kQU

---
# 프로젝트 정리본

### https://www.canva.com/design/DAG9sHM7aI8/3wWEBOgKrxzAWkauwf2PEQ/edit?utm_content=DAG9sHM7aI8&utm_campaign=designshare&utm_medium=link2&utm_source=sharebutton

---
# 트러블 슈팅 및 작업 보드

### https://www.notion.so/teamsparta/2cb2dc3ef514810ab502e6c69cfd0be3?v=2cb2dc3ef51481cca552000ca18e3d3d
