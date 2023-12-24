package tobyspring.helloboot;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

        // 3. 서블릿 컨테이너에 서블릿 등록하기
        // getWebServer 메소드는 ServletContextInitializer를 파라미터로 받음
        // Spring의 Web Module 안에 들어있는 Interface로
        // Servlet Context를 프로그램에 의해서 구성하는 작업에 사용되어짐
        // => 즉, Servlet container에 Servlet을 등록하는데 필요한 작업을 수행할 때 사용함
        // 여러번 사용할 것 아니니까 익명 클래스로 진행,
        // 해당 인터메이스는 구현해야하는 메소드가 1개인 Functional Interface 임
        // => 즉, Lambda 식으로 대체 가능 함!
        WebServer webServer = serverFactory.getWebServer(servletContext -> {
            // 서블릿 등록하기 : 1. 서블릿 이름, 2. 서블릿 클래스 정보나 서블릿 타입의 오브젝트
            servletContext.addServlet("hello", new HttpServlet() { // 여기서도 두번째 파라미터에 익명 클래스로 전달
                // 요청을 가져오고 응답을 내보낼 수 있는 메소드
                @Override
                protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                    // 요청에서 값 가져오기
                    String name = req.getParameter("name"); // 쿼리스트링 파라미터 받기

                    // 응답 만들기 : 응답의 3가지 요소, 여기서 상수는 최대한 spring이나 spring boot에 정의되어있는 enum을 사용하는 것이 좋음
                    resp.setStatus(HttpStatus.OK.value()); // 응답 상태
                    resp.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE); // body의 type
                    // getWriter는 오브젝트를 문자열 응답으로 만들 때 편리함
                    resp.getWriter().println("Hello " + name); // body 내용
                }
            }).addMapping("/hello"); // 서블릿을 하나 만들 때 매핑을 추가해야 함
        });// servlet container를 만드는 생성 함수
        webServer.start(); // Tomcat Servlet Container가 동작!
        // http -v :8080 요청하면 404 error 리턴함 => 즉, 톰캣이 진짜 떠 있다!
        // 비어있는 servlet Container 만들기 성공




    }

}
