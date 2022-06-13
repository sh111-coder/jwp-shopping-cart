# 장바구니
장바구니 미션 저장소

#1단계 요구사항 정리

- [x] 회원가입
  - [x] 요청으로 이름, 이메일, 비밀번호를 받는다.
  - [x] 응답으로 이름, 이메일을 반환한다. 201
- [x] 로그인
  - [x] 요청으로 이메일, 비밀번호를 받는다.
  - [x] 응답으로 이름, 이메일, 토큰을 반환한다. 200
- [x] 회원 정보 조회
  - [x] 요청으로 토큰을 받는다.
  - [x] 응답으로 이름, 이메일을 반환한다. 200
- [x] 회원 정보 수정 (비밀번호 수정)
  - [x] 요청으로 기존 비밀번호와 바꿀 비밀번호, 토큰을 받는다.
  - [x] 응답으로 이름을 반환하다. 200
- [x] 회원 탈퇴
  - [x] 요청으로 비밀번호와 토큰을 받는다.
  - [x] 204

[API 문서](https://www.notion.so/brorae/1-API-c10e17f6fdc940bbb2379ec7e07b1cb4)
## ✏️ Code Review Process
[텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

## 1단게 수정 사항 & 피드백 정리

- [x] Acceptance test를 하면서 실제 사용자가 생각하는 흐름을 정리해보자
  - 위의 두가지 경우처럼 나눌 수도 있고 더 다양하게 나누는 방법이 있을텐데요.
    알린이 생각하는 시나리오를 한번 생각해보고 그것에 맞게 테스트를 작성해 보면 좋을 것 같아요!
    시나리오로 모든 경우를 테스트할 수 없기 때문에 controller로 통합테스트를 진행하는 것이지 않을까요?
  - 이전까지 인수 테스트에 대한 감이 잘 안와서 모든 E2E 테스트를 인수테스트에서 한 것 같습니다.
  - 피드백에 대해 생각해보다 보니 인수테스트에서는 사용자 입장에서의 시나리오를 작성하여 테스트하고 컨트롤러 테스트에서는 기능 테스트와 예외에 대한 테스트를 하도록 분리할 수 있을 것 같았습니다.
  - 인수 테스트를 할 때 제가 생각하는 시나리오로 인수테스트를 할 수 있도록 수정해보고 주석으로 시나리오를 써봤습니다.
  - 예외 테스트 등은 컨트롤러 테스트로 옮겼습니다.
  - 컨트롤러 테스트에서는 DirtiesContext를 하지 않으면서 속도가 매우 빨라졌습니다.
- [x] interceptor가 ui 패키지에 포함되어 있다. ui보다 config에 있는게 맞지 않을까? ui에 포함하게된 기준이 무엇이었을까?
  - 제 프로젝트에서는 ui 패키지에 Advice, Resolver, Interceptor가 모두 있습니다.
  - 저는 auth.config에는 리졸버와 인터셉터를 등록하고 설정하는 XXXConfig만을 넣어야 한다고 생각했고 사용자의 요청, 응답과 관련된 것들을 ui패키지에 포함시켰습니다.
  - Controller(사용자 요청, 응답 처리), Advice(Controller에서 발생한 문제 핸들링), Resolver(특정 값을 만들어서 Controller에게 전달해줌), Interceptor(Controller가 실행되기 전 요청을 가로채서 특정 로직을 처리함)
- [x] webpage를 보며 me라는 표현을 본적이 없지 않나요?
  - 팀에서 `/{username}`으로 내 username을 요청하는 것이 아닌 `/me`라는 URL을 사용한 이유는 나만의 정보를 조회, 수정, 탈퇴하게 하기 위해서 입니다.
  - `/{username}`처럼 url요청을 받으면 나의 유효한 토큰으로 다른 username의 정보를 조회, 수정, 탈퇴하지 못하게 하려면 토큰의 정보와 `/{username}`의 정보를 비교하는 추가적인 작업이 필요합니다.
  - 이런 가능성을 처음부터 없애기 위해 `/me`처럼 url을 구성했습니다.
  - 그리고 `/me`라는 URL으로 정한 이유는 두 가지입니다.
    1. [prolog](https://github.com/woowacourse/prolog)
    2. [네이버 회원 프로필 조회 API](https://developers.naver.com/docs/login/profile/profile.md)
  - jwt Bearer 토큰을 사용하는 API를 찾아보면서 위 두개를 찾았습니다.
  - prolog는 아직 적용되지는 않았지만 현재 수정되고 있는 코드에서는 내 정보를 조회하거나 할 때 `/me`를 사용하고 있고 네이버 회원 프로필 조회 API에서도 `/me`를 사용하고 있었습니다. 
  - 하지만 myInfo라는 표현이 조금 더 명확해보여서 팀원들과 이야기를 해보겠습니다.
- [x] exception과 runtimeException을 같이 잡은 이유가 있을까요??
  - 둘의 차이점을 크게 생각하지 않고 내가 예상할 수 있는 예외를 제외한 모든 예외를 잡아야겠다는 생각으로 Exception을 핸들링하고 있던 것 같습니다.
  - 프로그램이 실행되면서 발생하는 예외만 잡는다면 runtimeException만 잡아도 될 것 같습니다.
- [x] 로그인 시 `isValidPasswordByEmail()`에 대한 테스트가 없는 것 같다.
- [x] (JwtTokenProvider.getPayload) 한줄에 여러개를 모두 표현하기보다 한줄에 한개씩 표현하는게 더 명확한거 같아요!
- [x] service에서 dto로 변환하여 넘겨주는 것이 역할에 맞을까요??
  - service에서 도메인을 넘겨주고 컨트롤러에서 변환하는 방법과 dto를 넘겨주는 것으로 크루들과 토론한 적이 있습니다. 
  - 도메인 반환 파
    1. 외부의 상황에 따라 컨트롤러가 다를 수 있는데(콘솔/웹 등) 각 컨트롤러에 맞는 DTO를 반환하는 서비스나 메서드를 만들것인가? 도메인 반환이 재사용성이 좋다.
    2. 도메인을 넘기면 앞단에서 수정이 발생해도 서비스의 수정이 생기지 않을 수 있다.
  - DTO 반환 파
    1. 단 하나의 도메인을 반환하는 간단한 경우라면 하나의 도메인 반환으로 해결이 되겠지만 여러 도메인을 반환해야하는 등 로직이 복잡해진다면 모든 도메인을 반환할 것인가?
    2. 도메인의 수정이 생기면 컨트롤러까지 영향을 받는것이 아닐까?
    3. 컨트롤러에서 도메인 로직이 실행되거나 수정될 수 있는 위험을 감수할 필요가 있나?
  - 해당 토론에서는 이런 이야기들이 나왔던 것 같습니다. 저는 DTO 반환파의 1번째, 2번째 이유로 서비스에서 DTO를 반환하는 편입니다.
  - 코드를 다시 보니 서비스에서 로직으로 dto 생성까지 하고 있는데 이부분은 조금 수정할 수 있을 것이라고 생각하여 service가 아닌 dto에서 정적 팩터리 메서드를 이용하도록 수정했습니다.
- [x] Request Dto 의 기본 생성자가 private여도 될 것 같다.

## 2단게 수정 사항 & 피드백 정리

- [x] 로그인을 하지 않은 상태에서도 장바구니에 물품을 넣을 수 있어야 하지 않을까요?
  - 보통 비회원이어도 장바구니에 담고 비회원용 주문을 할 수 있게 되어있지만 이번 미션에서는 구현하지 않았습니다. 
  - 우선 토큰을 이용한 인증이 된 사용자만이 장바구니에 물건을 담을 수 있도록 하는 것으로 팀 합의를 했습니다.
  - 그 이유는 다음과 같습니다. 
    - 비회원 장바구니의 정보를 프론트엔드에서 가지고 있을 것인가? 서버에서 가지고 있을 것인가?
    - 서버에서 가지고 있다면 회원 장바구니의 회원 id같은 그 비회원의 고유 식별자를 어떤 것으로 할 것인가?
    - 비회원의 고유 식별자가 바뀐다면 데이터베이스에 이미 저장된 데이터는 평생 남는 것인가? 수정해 줄 것인가? 수정한다면 어떻게 바뀐 식별자의 데이터를 찾아 수정할 것인가?
    - 일반 회원의 장바구니가 아닌 비회원용 테이블이 새로 만들어져야 한다.
    - 비회원 전용 api 엔드 포인트도 생겨야한다.
    - 프론트엔드에서도 새로운 api에 맞는 페이지가 만들어져야한다.
    - 비회원 장바구니의 정보를 프론트엔드가 가지고 있어도 위 내용의 대부분이 해당한다.
    - 제한된 시간과 다른 구현이 되지 않은 시점에서 현실적인 시간 내에서 이것들을 할 수 있는가?
    - 비회원 장바구니 기능을 구현하느라 회원의 장바구니 기능 구현이 뒤로 우선순위가 밀리는게 맞는가?
  - 위 이유에서 저희 팀은 우선 회원의 장바구니라는 요구사항을 완성한 뒤 비회원 장바구니에 대한 기능을 추가하는 것으로 정해졌습니다.
  - 방학식에 코치님의 애자일 강의가 있었습니다. 작은 가치를 바라보고 완성해 나가면서 구현하는 방식이라고 이해했는데 어쩌면 이런 방식도 애자일이라고 할 수 있지 않을까요?
- [x] interceptor 설정이 auth package에 있는데 해당 url의 controller 들은 shoppingcart에 있는데 auth에서 설정하는 것이 맞을까요?
  - 해당 리뷰는 인터셉터 파일의 위치에 대한 리뷰가 아닌 설정을 하는 곳에 대한 이야기셨던 것 같습니다. 
  - `/token/refresh`를 제외한 나머지 경로 설정을 shoppingcart 패키지에서 하도록 수정했습니다!
  - 해당 리뷰에 대해 처음에 잘못 이해했던 부분 정리
    - 제가 인터셉터를 auth package에 두고있던 이유는 인터셉터에서 `jwtTokenProvider.validateToken()`를 사용하고 있기 때문이었습니다.
    - 토큰 생성, 검증, 페이로드 추출 등 토큰에 대한 작업은 auth package에서 하고 다른 로직에서 필요하다면 auth package에 있는 서비스, 인터셉터, 리졸버를 사용하자 라는 기준을 잡았었습니다.
    - 인터셉터와 리졸버의 위치도 팀원들과 다른 크루들과 이야기해봤던 주제였는데 거의 대부분이 저와 같은 생각으로 auth package에 두었던 것 같습니다.
    - shoppingcart 외에 추가로 다른 서비스를 하는 패키지가 생기고 그곳에서도 로그인과 관련된 인터셉터가 사용된다면 그곳에서도 LoginInterceptor를 만드는것보다 auth 패키지에 두고 다른 패키지에서 사용하는 게 좋은 것 같다고 생각했습니다.
    - LoginInterceptor에는 토큰을 갱신하는 `/token/refresh`에서도 사용하고 있어 shoppingcart에서만 사용되는 것은 아닙니다.
    - 물론 `/token/refresh`는 LoginInterceptor가 아닌 다른 인터셉터를 만들어 관리하고 여러 서비스에서는 그 서비스에 맞는 로그인 방식에 맞춘 LoginInterceptor를 만들어야 한다면 해당 패키지에 두는 것이 맞는 것 같기도 합니다.
- [x] (AuthorizationExtractor) extract 메서드를 분리하면 좋을 것 같아요ㅎㅎ
- [x] resolver에서 값을 넣어주기만 하고 있는데요. 검증을 하지 않아도 될까요? 
  - interceptor가 적용된 url에서 모두 @AuthenticationPrincipal가 활용되고 있는데 interceptor에서 검증을 할 필요가 있을까요? 
  - interceptor와 resolver의 역할을 분리했다면 맞을 것 같은데 interceptor와 resolver의 역할을 어떻게 정의했을까요?
  - 저는 이 둘의 차이점을 보면서 둘의 역할을 나름대로 구분해봤습니다.
    - interceptor : 
      1. 반환을 boolean으로 한다. 
      2. url을 등록하여 사용한다. 
      3. preHandle(컨트롤러 실행 전), postHandel(컨트롤러 실행 후), afterCompletion(사용자의 요청에 대한 응답 직전) 는 모두 실행되는 시점이 다르다.
    - resolver : 
      1. 반환을 원하는 객체로 한다. 
      2. 어노테이션을 달아 사용한다. 
      3. 컨트롤러가 실행되기 직전 동작한다.
    - 2번의 차이점으로 인터셉터는 조금 더 특정 url과 그 하위 url 등에 일괄적으로 적용하기 쉽고 특정 패턴을 넣어서 특별한 url에 만 적용할 수 있는 등 장점이 있고 리졸버는 그때 그때 내가 필요한 곳에만 적용할 수 있는 장점이 있다고 느꼈습니다.
    - 1번의 차이점으로 무언가를 반환해야 한다면 2번을 사용할 수 밖에 없다고 생각하였습니다. 
    - 미션에서는 preHandle만을 사용했는데 둘 다 컨트롤러 실행 직전이지만 인터셉터가 실행되고 이후 리졸버가 실행됩니다.
    - 이런 차이점으로 저는 어떤 데이터를 반환할 필요가 없는 토큰 검증 과정은 리졸버 실행 전 인터셉터에서 하게되었습니다. 그리고 토큰에서 페이로드를 확인하여 사용자 정보를 만들어 반환하는 것을 리졸버에서 하게 되었습니다.
    - 이번 미션은 어쩌면 우연히 리졸버를 쓴 곳과 인터셉터가 같았기 때문에 리졸버에 인터셉터에서 검증할 필요가 없다고 느낄 수 있지만 리졸버를 사용하지 않지만, 또는 다른 리졸버 어노테이션이 생기면서 복잡해진다면 리졸버에 토큰 검증 과정은 여러 리졸버의 중복 코드가 될 수 있다고 생각합니다.
    - 그리고 계획에는 있었지만 구현하지 못했던 것 중 권한 부분이었습니다. 관리자, VIP 회원, 일반 회원 등 등급을 나누고 등급에 따라 할 수 있는 행위를 제한해보자는 의견이었습니다.
    - 저는 토큰을 검증하는 것을 인증, 등급을 나누고 어떤 일을 할 수 있는지 권한을 부여하는 것을 인가라고 생각합니다. 
    - 인증과 인가를 하는 순서는 인증을 한 뒤 통과하면 인가를 하는 것이 맞다고 생각합니다. 인가는 토큰 페이로드에 포함된 등급을 확인하여 권한을 주게될 것이고 리졸버에서 하게 될 것입니다. 그렇다면 인증은 인가의 코드 위에서 하거나 그 이전에 인터셉터에서 해야합니다.
    - 코드의 순서로 역할을 구분지으면 다른 개발자가 유지보수를 하면서 코드 이해를 잘못하여 수정될 수 있다고 생각했고 확실한 역할 분리를 위해 인터셉터에서 토큰 검증을 하게 되었습니다.
- [x] 전역 변수와 멤버변수 사이에 빈줄을 추가해주는게 더 명확한 거 같아요!
- [x] ValidationException을 만들고 메세지를 전달받아 생성할 수 있게 하는 방식으로 검증 exception 들을 줄일 수도 있을 거 같습니다~
  - 입력값의 형식 등이 잘못된 경우 ValidationException을 사용하는 것으로 수정해봤습니다. 
  - 그런데 InvalidXXXException 모두를 ValidationException로 수정하는 것은 어색하다고 느껴 수정하지는 않았습니다. 
  - 제대로 입력했지만 값이 없는 경우, 비밀번호를 제대로 입력했지만 틀린 경우, 이미 탈퇴한 회원의 토큰을 사용하여 없는 회원의 정보를 요청하는 경우 등 이런 상황에선느 ValidationException가 아닌 InvalidXXXException이 맞다고 생각했습니다.
- [x] productService가 있는데 productDao를 사용한 이유가 있을까요?
  - 레거시 코드에 dao를 사용하고 있어서 그대로 사용했습니다.
  - 크루들과 이야기하면서 Service가 다른 Dao를 가지는것과 Service를 가지는 것 중 무엇이 올바른지 이야기한 적이 있습니다.
  - 당시 가장 큰 논쟁거리는 다음과 같습니다.
    - Service -> Service인 경우 순환 참조를 하게 된다면 큰 문제가 발생한다 vs 순환 참조를 하지 않도록 주의하면된다.
    - Dao를 직접 가지면 데이터를 더 쉽게 불러올 수 있어서 유연하게 데이터 활용이 가능하다 vs 여러 서비스가 같은 table에 직접 연결하면서 데이터 관리나 유지보수가 어려워 질 것 같다.
  - 저는 규모가 작은 프로젝트라면 Dao를 직접 가져와도 된다고 생각했습니다. 구현이 간단해지고 빠르게 구현할 수 있다고 생각했습니다.
  - 하지만 규모가 커져 하나의 Dao를 여서 Service에서 사용하게되는 경우가 많아지고 중복된 코드가 많아진다면 Service가 Service를 가지는 것도 좋다고 생각합니다.
  - Service를 사용한다면 ProductService 뿐 아니라 CustomerService도 Dao 대신 사용할 수 있었을 것 같습니다.
  - 피드백을 받고 이번 미션에서 CartService가 ProductService와 CustomerService를 가지도록 수정해도 큰 어려움은 없을 것 같았지만 문제가 하나 있었습니다.
  - CartService는 CustomerDao.findByUsername()를 CustomerService.findMe()로 수정하면 Dao가 아닌 Serivce를 가지게 할 수 있었지만 CustomerService.findMe()에는 customer의 id가 포함되어있지 않습니다.
  - 따라서 이 문제를 해결하기위해서 세 가지정도 방법을 생각해봤습니다.
    - CustomerService.findMe()가 id를 포함하여 반환하도록 한다.
      - (API 명세를 바꾸지 않는다면) Service -> Controller로 가는 반환 Dto를 id가 포함된 dto로 새로 만든다.
      - (API 명세를 바꾼다면) CustomerResponse에 id를 포함하도록 수정한다.
    - CustomerService.findMe()가 엔티티 또는 도메인 그 자체를 반환하도록 수정한다.
      - 이 방법은 이전 피드백에서 로운이 말씀해주신 서비스 도메인 반환과 이어질 것 같습니다. 
      - 단순히 서비스와 컨트롤러뿐 아니라 서비스와 서비스의 관계에서도 도메인 반환의 장점을 느끼게 되는 것 같습니다.
      - 하지만 개인적인 기준에서는 Dao를 사용해도 될 것 같았고 서비스를 모두 도메인을 반환하는 서비스로 리펙터링 하기엔 너무 비효율적인 것 같아서 수정해보지는 않았습니다.
- [x] CartResponse에 정적 팩토리 메서드를 만들어서 활용할 수도 있지 않을까요?
- [x] 순회를 하면서 조회를 하는데 한번에 조회하는 방법도 있지 않을까요?
  - 레거시 코드의 `List<Long> findIdsByCustomerId`를 `List<Cart> findAllByCustomerId`로 수정하여 해결해봤습니다. 
- [x] 서버에서 에러가 발생했습니다 와 같이 표현해주는게 더 파악하기 쉽지 않을까 하는 개인적인 의견입니다~
- [x] 주문상세를 여러개 가진 주문 한건을 의미하는 것 같은데 Orders가 맞을까요?
  - Order로 수정했습니다. 
  - 구현하지는 못했지만 (A, B, C 물품을 구매한 주문 ㄱ), (B, C, D를 구매한 주문 ㄴ)이 있다면 ㄱ과 ㄴ은 Order이 되어야 하기 때문에 Order가 맞다고 이해했습니다.
- [x] 도메인과 service에 대한 테스트가 없는 것들이 있는것 같아요!
- [x] Impl을 붙이는 방식은 이제 사용을 권장하지 않는 것 같아서요. 
  - CartItemRepository를 interface로 하고 CartItemDao가 implments하는 방식으로 해도되지 않을까 싶습니다
