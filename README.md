# 신용카드 결제 서비스

* 결제, 결제취소 요청을 받아 String 데이터로 카드사와 통신하는 서버

### 필수 요구사항

* 결제 API<br>
카드 정보와 금액정보를 입력받아서 카드사와 협의된 String 데이터로 DB에 저장
    * Request
    ```
  Required
  - 카드번호 (10~16자리 숫자)
  - 유효기간 (4자리 숫자, mmyy) 
  - cvs (3자리 숫자)
  - 할부개월수 : 0 (일시불), 1~12
  - 결제금액 (100원 이상, 10억원 이하, 숫자)
  Optinal
  - 부가가치세
    ```
  * Response
  ```
  Required
  - 관리번호(unique id, 20자리)
  Optional
  - 추가로 내려주면 좋을 데이터
  ```
  *기본적은 request에 대한 validate 필요*
* 결제취소 API<br>
    결제에 대한 전체취소는 1번만 가능,<br>
    부가가치세 정보를 넘기지 않을 경우, 결제 데이터의 부가가치세 금액으로 취소,<br>
    할부개월수 데이터는 00(일시불)로 저장
    * Request
  ```
  Required
  - 관리번호 (unique id ,20자리)
  - 취소금액
  Optional
  - 부가가치세
  ```
  * Reponse
  ```
  Required
  - 관리번호 (unique id, 20자리)
  Optional
  - 추가로 내려주면 좋을 데이터
  ```
  *기본적인 validate 필요*
* 데이터 조회 API<br>
DB에 저장된 데이터를 조회해서 응답값을 만들어줍니다.
    * Request
  ```
  Request
  - 관리번호 (unique id)
  ```
  * Response
  ```
  Required
  - 카드 정보 ( 암호화된 데이터를 복호화해서 데이터로 제공 )
    * 카드번호 : 앞6자리와 뒤3자리를 제외한 나머지를 마스킹처리
    * 유효기간
    * cvc
  - 결제/취소 구분
  - 금액정보
    * 결제/취소 금액
    * 부가가치세
  Optional
  - 추가로 내려주면 좋을 데이터
  ```
* API 요청 실패시<br>
    자유롭게 정의한 에러응답, 에러코드를 내려줌.
<br>
<br>

### 선택 요구사항
* 부분 취소 API를 구현
    * 정책
        * 결제 한 건에 대해서 모두 취소가 될때까지 부분 금액으로 계속 취소 할 수 있다.
        * 부가가치세 검증 로직 : 겔제금액의 부가가치세 = 모든 부분취소 부가가치세의 합
<br>
<br>  
 
* TestCase 1, 2, 3 작성 및 검증
* Multi Thread 환경에 대비해주세요.
    * 제약 조건
        * 결제 : 하나의 카드로 동시에 결제를 할 수 없다.
        * 전체취소 : 결제 한건에 대한 전체취소를 동시에 할 수 없다.
        * 부분취소 : 결제 한건에 대한 부분 취소를 동시에 할 수 없다.
     
---
   
### 개발 환경
```
Spring boot 2.2.6
Gradle 6.3
Java 8
H2 (mem)
```

### DB 테이블
```
테이블 명 : transaction_history 
- ID : varchar(20), pk      // 관리 번호
- BEFORE_ID : varchar(20)   // 결제 취소 시 결제 관리번호
- DATA : varchar(450)       // String data
- COMPLETED_DTIME : datetime // 처리 완료 시간
```

### 문제해결 전략
* 데이터의 변환처리<br>
거래정보에 대해서 항상 String 데이터로 바꿔야하는 내용이 있었습니다. 이전 블록체인 블록에 데이터를 쌓는 경험을 통해 이미 정해진 데이터의 규격을 유틸화하여
데이터를 생성과 불러오는 과정에서 사용 할 수 있도록 구현하였습니다.
* DB 설계 <br>
처음에 요구사항을 보고는 String data만 H2에 저장해야하는 걸로 이해했습니다. 하지만 관리번호를 통한 검색을 하기 위해서는 String Data 뿐만 아니라 다른 데이터들도 
컬럼으로 만들어 저장해야 되겠다 라는 생각을 하게 되었습니다. 그리하여 관리번호, 이전관리번호(결제취소의 경우), String data, 완료된 시간으로 테이블을 구성하였습니다.
물론 다른 데이터들도 넣을 수 있지만 데이터를 이미 블록화하였기에 굳이 검색에 필요한 데이터가 아니라면 컬럼을 생성하지 않기로 하였습니다.
* 전체 취소와 부분 취소<br>
전체취소와 부분취소를 구현하기 위해 테이블에 관리번호와 이전관리번호 컬럼을 생성하였습니다.<br>
전체취소의 경우 결제 데이터를 불러와 비교하여 처리할 수 있도록 구현, 부분취소의 경우 결제데이터와 결제취소 데이터를 모두 불러와 비교하여 처리할 수 있도록 구현하였습니다.
    * 단위 테스트 1,2,3에 대해서 `SelectProblemCase1Test, SelectProblemCase2Test, SelectProblemCase3Test`에서 확인하실 수 있습니다.
* 부가가치세 <br>
처음에 부가가치세 또한 이해가 잘 가지 않았습니다. null이면 '금액/11'라고 이해했기에 테스트 코드를 작성 중 UnitTest3의 경우 이해하지 못하고 잠시 고민하게 되었습니다.
그러던 중 부가가치세의 null에 대해서는 2가지 케이스로 '금액/11' 또는 '취소하고 남은 부가가치세'라고 이해하고 코드를 작성하였습니다.
* 트랙잭션 관리
이 부분에 대해서 조금 고민이 되었습니다. timestamp와 카운트를 통해 관리번호를 생성할지 아니면 20자리의 랜덤 문자열을 뽑을지 우선적으로 랜덤 20자리의 문자열을 생성하여 
관리번호로 구현하였습니다. timestamp의 경우 10자리 또는 13자리 + 카운트로 만들게 되는데 그렇게 되면 하루에 약 9999999건 ~ 9999999999건에 대한 생성 제한이 걸리라 생각이 되어
혹시나 모를 중복이 있더라도 생성 제한이 없는 랜덤 문자열20자리를 선택하였습니다.
* 멀티쓰레드 환경 대비<br>
`하나의 카드로 동시에 결제 할수 없습니다.`, `결제 한 건에 대한 전체취소를 동시에 할 수 없습니다.`, `결제 한 건에 대한 부분취소를 동시에 할 수 업습니다.`
와 같은 조건을 달성하기 위해서는 결제 또는 결제 취소 프로세스에 대한 관리가 필요하다고 판단 되었습니다. 어떤 카드번호가 현재 처리중인지, 어떤 건이 취소중인지를 처리하기 위해
static 데이터를 통해 데이터를 관리하도록 구현하였습니다.<br>
물론 멀티쓰레드라는 가정하에 하나의 프로그램에서만 돌린다면 위 처럼 메모리 상에서 관리가 되지면 여러서버에 대해서 관리를 할 경우 DB를 사용하여 관리하면 될 것이라 예상됩니다.
    * `MultiTrheadTest`코드에서 확인하실수 있습니다.<br>
* 카드 정보 암호화<br>
양방향 암호화 방식을 사용해야하기에 AES256을 사용하여 구현하였습니다.
<br>

### 빌드 및 실행
```
./gradlew build -x test
cd build/libs
java -jar credit-0.0.1-SNAPSHOT.jar
```
포트가 5000번이기에 다른 서버와 함께 실행 할 시 유의.
<br>

### API 명세서
결제 API<br>
`POST /credit/payment`<br>

* Request
```
{
    "cardNumber" : "1234567890123456", // 카드번호 숫자(10~16자리)
    "validDate" : "0101", // 유효날짜 숫자(4자리)
    "cvc" : "123",  // 카드 뒷면 cvc 숫자(3자리)
    "installment" : 12, // 할부 0~12
    "amount" : 100000, // 100원 ~ 10억
    "vat" : 1000 // 부가가치세(옵션, 입력하지 않아도 됨), 입력하지 않을 경우 자동으로 amount/11 으로 처리됨.
}
```
* Response
```
{
    "id": "UnfJVnmTjIUejwA5uVPz", // 관리번호 (20자)
    "amount": 100000,   // 결제 된 금액
    "vat": 1000,    // 결제 된 부가가치세
    "completedDtime": "2020-04-15T21:36:22.725709+09:00" // 처리 완료 시간
}
```
<br>

조회 API<br>
`GET /credit/search?id={}`<br>

* Request
```
id : 관리번호 (20자)
```
* Response
```
{
    "id": "UnfJVnmTjIUejwA5uVPz", // 검색한 관리 번호
    "cardInfo": {
        "cardNumber": "123456*******456", // 카드 번호 마스킹처리
        "validDate": "0101", // 카드 유효날짜
        "cvc": "123" // 카드 CVC
    },
    "function": "PAYMENT", // 결제, 취소 구분
    "amountInfo": {
        "amount": 100000, // 결제 금액
        "vat": 1000 // 결제 부가가치세
    },
    "completedDtime": "2020-04-15T21:38:38.857525+09:00" // 처리 시간
```
<br>

결제 취소 API<br>
`DELETE /credit/cancel`<br>

* Request
```
{
  "id":"0nvwAcfSTS1UewlxnjTo",  // 관리 번호 (결제에 대한)
  "amount": 1000,   // 취소 금액
  "vat": 12 // 취소 부가세 (옵션:입력하지 않아도 됨)
}
```

* Response
```
{
    "id": "kBzbi6y1EPpUrdnc0uS3", // 관리 번호 (결제 취소에 대한)
    "amount": 1000, // 취소 된 금액
    "vat": 12,  // 취소 된 부가가치세
    "completedDtime": "2020-04-15T21:46:35.034382+09:00" // 처리 완료된 시간
}
```

* 실패시 Response 공통 
```
{
    "code" : "", // 에러코드
    "msg" : "" // 에러에 대한 피드백
}
```

#### Error Code
| 에러코드 | Http Status | 메세지 | 설명 |
|--------|-------------|------|-----|
|__REQUEST_ERROR__| 400 | | 특정 파라미터 유효성 체크에 대한 피드백 제공
|__CRYPTO_ERROR__| 500 | 카드정보 암호화/복호화 과정에서 에러!! | 암호화/복호화에 대한 에러 발생시에 대한 피드백
|__NOT_EXIST_ID__| 400 | 데이터를 찾을 수 없습니다 | 조회 또는 요청한 관리번호가 없을 경우에 대한 피드백
|__OVER_CANCEL_REQUEST_AMOUNT__| 400 | 취소한 금액이 결제 금액을 초과하였습니다 | 취소 요청에 대한 피드백
|__OVER_CANCEL_REQUEST_VAT__| 400 | 취소한 부가가치세가 결제한 부가가치세를 초과하였습니다 | 취소 요청에 대한 피드백
|__ALREADY_CARD_PROCESS__| 400 | 이미 거래요청/취소 중인...|결제 요청과 취소에 대한 피드백 
|__VAT_IS_BIGGER__| 400 | 남은 부가가치세가 결제금액보다 큽니다 | 결제 취소에 대한 피드