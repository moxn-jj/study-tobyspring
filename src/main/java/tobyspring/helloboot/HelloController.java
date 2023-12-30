package tobyspring.helloboot;

import java.util.Objects;

/*@RestController*/
public class HelloController {

    // 8. 주입받은 오브젝트를 저장할 변수
    private final HelloService helloService;

    // 8. final로 지정했기 때문에 정의할 때 집어넣든, 최소한 생성자에 집어넣는 코드가 있어야한다.
    // 생성자에서 집어넣는 식으로 처리
    // Spring Container는 HelloConroller를 만드려면 생성자를 호출해야하고
    // 생성자에 주입해야 할 파라미터가 HelloService 인터페이스 타입이구나를 확인하고
    // 컨테이너에 등록돼 있는 모든 등록 정보를 다 뒤져서 HelloServiceInterface를
    // 구현한 클래스를 찾아서 주입해준다.
    public HelloController(HelloService helloService) {
        this.helloService = helloService;
    }

    /**
     * 동작되는 내용 :
     * /hello라는 경로로 들어오는 요청을 받아서
     * 파라미터 name 값을 가져와서 hello 뒤에 이름을 붙여 리턴함.
     * RestContoller로 등록해두었기 때문에 웹 응답이 만들어질 때
     * 이 method의 스트링을 보고 Content-type이 자동으로 결정됨.
     * 여기서 리턴 타입이 String이기 때문에 text/plain; 타입의 Content-type이 만들어지고
     * 리턴된 문자열이 그대로 응답 body에 들어감.
     * @param name
     * @return
     */
    /*@GetMapping("/hello")
    public String hello(String name){
        return "Hello " + name;
    }*/

    public String hello(String name){

        // 7. service를 사용하도록 전환
//        return "Hello " + name;

        // 7. 컨트롤러가 해야하는 중요한 역할 중 1개 : 유저의 값을 검증하는 것
        // 8. 직접 오브젝트를 생성하지 않고 어셈블러인 Spring Container가 주입 하는 방식으로 변경
//        SimpleHelloService helloService = new SimpleHelloService();

        return helloService.sayHello(Objects.requireNonNull(name)); // name이 null이면 에러 발생
    }
}
