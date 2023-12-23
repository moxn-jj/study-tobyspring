package tobyspring.helloboot;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;

// 0. 이 코드 없이 스프링 부트랑 똑같이 동작 시켜보자
//@SpringBootApplication
public class HellobootApplication {

    public static void main(String[] args) {

        // 0. 이 코드 없이 스프링 부트랑 똑같이 동작 시켜보자
        // SpringApplication.run(HellobootApplication.class, args);

        // 1. 메인 메소드가 진짜 동작하는게 맞나? 확인해보자.
        System.out.println("Hello Containerless Standalone Application");

        // 2. 서블릿 컨테이너 만들기
        // => Embedded Tomcat. 즉, 내장형 톰캣 사용해보자
        // new Tomcat().start(); // 이건 완전 생짜 Tomcat. 이 코드로 톰캣 동작 시키려면 설정이 많이~ 필요함
        // 그래서 Spring Boot가 미리 설정 다해둔 도우미 클래스가 바로 이것!
        TomcatServletWebServerFactory serverFactory =  new TomcatServletWebServerFactory();
        WebServer webServer = serverFactory.getWebServer();// servlet container를 만드는 생성 함수
        webServer.start(); // Tomcat Servlet Container가 동작!
        // http -v :8080 요청하면 404 error 리턴함 => 즉, 톰캣이 진짜 떠 있다!



    }

}
