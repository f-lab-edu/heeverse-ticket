# heeverse-ticket

<p align="center"><img src="https://github.com/f-lab-edu/heeverse-ticket/assets/45592236/3695851d-bbb9-4dc7-893d-ade79cb79558" width="200" height="200"/></p>

아이돌 팬 플랫폼 개발사 "위버스" 사명을 차용한 Heeverse Ticket 입니다.<br>
아이돌 콘서트 티켓팅 API 서버 프로젝트 입니다.

## 🎫 프로젝트 목표

- “아이돌 티켓팅 서비스는 어떻게 구현할까?”를 고민하였습니다.
- 고가용성 티켓팅 서비스를 구현해 내는 것이 목표입니다.
- 객체지향 원칙에 근거한 클린 코드를 작성하는 것이 목표입니다.
- 설계 기반 테스트 코드를 통한 기도 없는 코드 리팩토링을 하는 것이 목표입니다.
- 성능 & 부하 테스트에서 발생하는 문제 해결 경험에 집중하는 것이 목표입니다.
- 협업 프로세스 위에서 함께 개발합니다.

## 🎫 아키텍처


<img src="https://github.com/f-lab-edu/heeverse-ticket/assets/45592236/b4614540-1609-4263-8b96-4135ca219c9e" width="700" height="600"/>

## 🎫 프로젝트 중점사항

### 개발 & 성능

- CAP이론에 근거하여 고가용성, 데이터 정합성을 중점으로 프로젝트 설계
- @Transactional 전파 레벨 설정을 통한 비즈니스 로직 보장
- 데이터베이스 부하를 최소화 하기 위해 레코드 레벨 락 설정
- Vault 서버를 설정하여 민감정보 관리
- Nignx를 Reverse Proxy로 사용하여 로드밸런싱
- 쿼리 분석, 인덱싱을 통한 성능 개선
- Github Action을 사용한 자동화된 CI/CD 구축
- nGrinder로 부하&성능 테스트를 거치며 점진적 개선
- Scouter로 WAS의 CPU 사용량, Thread, Heap, GC 모니터링
- Docker로 서버 환경에 의존적이지 않은 배포 환경

### 테스트 & 협업

- Stub을 사용한 단위테스트 작성
- 고립된 테스트 코드를 통해 의존적이지 않은 테스트 코드 작성
- REST API 표준 규격화 & 문서화
- 의사결정에는 ADR문서를 활용하여 옵션 조사 및 비교 후 결정
- 공식문서에 근거한 PR 리뷰 작성
  

## 🎫 API 엔트포인트별 3차 개선 지표



## 🎫 Technical issue 해결 과정

## 🎫 ERD

![Heeverse Ticket_erd (3)](https://github.com/f-lab-edu/heeverse-ticket/assets/45592236/b196f732-d2c5-4384-8af8-8329a32d9ce6)


## 🎫 서비스 구성 서버 스펙
| 서버 | CPU | RAM |
| --- | --- | --- |
| WAS 01 | 2 core | 8GB |
| WAS 02 | 2 core | 8GB |
| Nginx | 2 core | 4GB |
| ngrinder-agent | 4 core | 4GB |
| ngrinder-controller | 1 core | 2GB |

## 🎫 프로젝트 WIKI
[프로젝트 wiki](https://github.com/f-lab-edu/heeverse-ticket/wiki)

## 🎫 코드 커버리지
![Coverage](.github/badges/jacoco.svg)
