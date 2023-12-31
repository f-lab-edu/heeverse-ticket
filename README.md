# heeverse-ticket ![Coverage](.github/badges/jacoco.svg)
아이돌 팬 플랫폼 개발사 "위버스" 사명을 차용한 Heeverse Ticket 입니다.<br>
아이돌 콘서트 티켓팅 API 서버 프로젝트 입니다.

## 🎫 프로젝트 목표

- 고가용성 티켓팅 서비스를 구현해 내는 것이 목표입니다.
- 객체지향 원칙에 근거한 클린 코드를 작성하는 것이 목표입니다.
- 설계 기반 테스트 코드를 통한 기도 없는 코드 리팩토링을 하는 것이 목표입니다.
- 성능 & 부하 테스트에서 발생하는 문제 해결 경험에 집중하는 것이 목표입니다.
- 협업 프로세스 위에서 함께 개발합니다.
    - 📌 F-Lab 전체 상위 1% PR 리뷰 수 달성 (2023년 10월 9일 기준)


<br>

## 🎫 아키텍처


<img src="https://github.com/f-lab-edu/heeverse-ticket/assets/45592236/b4614540-1609-4263-8b96-4135ca219c9e" width="700" height="600"/>

## 🎫 프로젝트 중점사항

### 개발 & 성능

- CAP이론에 근거하여 고가용성, 데이터 정합성을 중점으로 프로젝트 설계
- @Transactional 전파 레벨 설정을 통한 비즈니스 로직 보장
- 데이터베이스 부하를 최소화 하기 위해 레코드 레벨 락 설정
- Vault 서버를 설정하여 민감정보 관리
- Nginx를 Reverse Proxy로 사용하여 로드밸런싱
- 쿼리 분석, 인덱싱을 통한 성능 개선
- Docker로 서버 환경에 격리되고 Github Action으로 자동화된 CI/CD 구축
- nGrinder로 부하&성능 테스트를 거치며 점진적 개선
- Scouter로 WAS의 CPU 사용량, Thread, Heap, GC 모니터링

### 테스트 & 협업

- Stub을 사용한 단위테스트 작성
- 고립된 테스트 코드를 통해 의존적이지 않은 테스트 코드 작성
- 블랙박스와 화이트박스 테스트 코드 작성
- REST API 표준 규격화 
- 의사결정에는 ADR문서를 활용하여 옵션 조사 및 비교 후 결정
- 공식문서에 근거한 PR 리뷰 작성


## 🎫 API 엔트포인트별 3차 개선 지표



## 🎫 Technical issue 해결 과정
- Stub이 시나리오대로 동작하지 않는 문제 ([링크](https://guui-dev-lee.tistory.com/15))

## 🎫 ERD
[링크](https://www.erdcloud.com/d/apAy7QL9TrN6WsDij)


## 🎫 프로젝트 WIKI
[프로젝트 wiki](https://github.com/f-lab-edu/heeverse-ticket/wiki)

