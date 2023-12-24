package tobyspring.helloboot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/*@RestController*/
public class HelloController {

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
        return "Hello " + name;
    }
}
