# Devnine Lab - Distributed Lock By Redisson
- Spring Web
- MySQL
- Jooq
- Redisson

## Content
- Redisson Spin Lock 
- Jooq CRUD

## 실행 방법
1. docker-compose up -d 로 redis, mysql 실행
2. src/main/kotlin/generated 폴더(jooq Record 클래스)가 없을 경우 gradle 의 generateJooqCode 실행하여 generated 디렉토리 하위에 파일이 생성되는지 확인
3. 테스트 실행.

## Jooq 사용 후기
1. QueryDSL 의 Q클래스와 같은 Record 클래스를 생성해야함
   1. Record 클래스를 생성하는 방법은 여러가지가 있음, DB를 붙어서 가지고 오는 방법도 있음.
   2. 실무에서 DB 를 붙어서 가지고오는 방법은 로컬 or 개발 환경에서만 사용하고 운영에서는 사용할 수 없을것 같음.
   3. Record 파일은 자주 변경되지 않기 때문에 생성 후 repository에 올려도 괜찮아보임.  
2. 무지성으로 쿼리를 작성할 수 있어 편함. 은근 지원해주는 기능들도 많은듯(returning 등등)
3. CRUD 를 모두 구현해야하기 때문에 ORM 보다는 불편함. Read Query 가 복잡한(통계?) 배치쪽에서는 사용해볼만할듯
4. DB의 종류마다 지원을 하지 않는 기능도 있어서 lib 설명을 잘 봐야함!!!
5. 문서도 잘 정리되어있음.
