package tobyspring.helloboot;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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

        // 6.
        // ApplicaitonContext :
        // 어떤 빈이 들어갈 것인가, 리소스에 접근하는 방법, 이벤트를 전달하고 구독하는 방법 등
        // 애플리케이션이라면 필요한 많은 기능을 담당하고 있음
        GenericApplicationContext applicationContext = new GenericApplicationContext(); // applicationContext 중 1개 (구현하기 쉽게 만들어 놓은)
        // spring Container는 오브젝트를 직접 만들어서 넣어주는 것도 가능하지만
        // 일반적으로 어떤 클래스를 이용해서 빈을 만들지 meta 정보를 넣어주는 방식으로 구성
        applicationContext.registerBean(HelloController.class); // bean 등록
        // 8. 생성자 주입하는 방식으로 변경했기 때문에 service도 빈으로 등록
        // 빈으로 등록한다는 것은 => 컨테이너가 그 오브젝트를 만들고
        // 필요하면 다른 오브젝트를 만들 때 생성자 등으로 주입해서 의존관계를 런타임에 맺어줌
        // applicationContext.registerBean(HelloService.class); // 이렇게 인터페이스만 넣으면 안됨!
        applicationContext.registerBean(SimpleHelloService.class); // bean 등록
        applicationContext.refresh(); // 가지고 있는 구성 정보를 이용해서 컨테이너를 초기화

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

            // 5. 프론트 컨트롤러의 개별 로직 분리하기
            // 6. 실제 비지니스 로직은 spring container에 넣는 방식으로 수정하기 위하여 주석처리
//            HelloController helloController = new HelloController();

            // 서블릿 등록하기 : 1. 서블릿 이름, 2. 서블릿 클래스 정보나 서블릿 타입의 오브젝트
            servletContext.addServlet("frontcontroller", new HttpServlet() { // 여기서도 두번째 파라미터에 익명 클래스로 전달
                // 요청을 가져오고 응답을 내보낼 수 있는 메소드
                @Override
                protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

                    // 인증, 보안, 다국어 처리, 각종 공통 기능 처리 로직이 들어갈 자리!

                    // 개별 처리
                    if(req.getRequestURI().equals("/hello") && req.getMethod().equals(HttpMethod.GET.name())){

                        // 요청에서 값 가져오기
                        String name = req.getParameter("name"); // 쿼리스트링 파라미터 받기

                        // 6.applicationContext를 통하여 spring container가 가지고 있는 오브젝트를 가져올 수 있음
                        HelloController helloController = applicationContext.getBean(HelloController.class);

                        // 5. 프론트 컨트롤러의 개별 로직 분리하기
                        // 실제 요청을 처리하는 동안 일어난 중요한 일 두가지 : 매핑, 바인딩
                        // 1) 매핑 : 웹 요청에 들어있는 정보를 활용해서 어떤 로직을 수행하는 코드를 결정하는 것
                        // 2) 바인딩 : helloController는 웹 요청과 응답을 직접 다루지 않음
                        // => 즉, 분리함 (hello 메소드는 평범한 데이터 타입인 name만 받음)
                        String ret = helloController.hello(name);

                        // 응답 만들기 : 응답의 3가지 요소, 여기서 상수는 최대한 spring이나 spring boot에 정의되어있는 enum을 사용하는 것이 좋음
//                        resp.setStatus(HttpStatus.OK.value()); // 응답 상태 : 200은 기본값으로 생략 가능
//                        resp.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE); // body의 type
                        resp.setContentType(MediaType.TEXT_PLAIN_VALUE); // 위와 동일한 코드
                        // getWriter는 오브젝트를 문자열 응답으로 만들 때 편리함
                        resp.getWriter().println(ret); // body 내용
                    }else{
                        resp.setStatus(HttpStatus.NOT_FOUND.value()); // 404
                    }
                }

            // 4. 프론트 컨트롤러 만들기 : 모든 요청을 받음
            }).addMapping("/*"); // 서블릿을 하나 만들 때 매핑을 추가해야 함

        });// servlet container를 만드는 생성 함수
        webServer.start(); // Tomcat Servlet Container가 동작!
        // http -v :8080 요청하면 404 error 리턴함 => 즉, 톰캣이 진짜 떠 있다!
        // 비어있는 servlet Container 만들기 성공




    }

}
