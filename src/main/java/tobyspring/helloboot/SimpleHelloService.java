package tobyspring.helloboot;

public class SimpleHelloService implements HelloService {

    // 8. 인터페이스를 구현하는 방식으로 변경
    @Override
    public String sayHello(String name){
        return "Hello " + name;
    }
}
