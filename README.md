도서명 - 자바 웹 개발 워크북  
IDE - IntelliJ IDEA 2023.1 (Ultimate Edition)  
자바 버전 - JDK 11  
웹 서버 - 톰캣 9.0.73  
DB - MariaDB 10.5(x64)  
SQL 툴 - HeidiSQL 11.3.0.6295  

1. JDBC 프로그래밍을 위한 API
  * java.sql.connection : db와 네트워크상의 연결,작업 후에는 반드시 close()로 연결 종료 해야 한다.
  * java.sql.Statement/PreparedStatement : SQL을 db로 보내기 
  * Statement : SQL 내부에 모든 데이터를 같이 전송하는 방식
  * PreparedStatement : SQL을 먼저 전달 후 데이터 전송하는 방식
  * executeUpdate() : DML 실행하과 결과를 int 타입으로 반환한다.
  * executeQuery() : 쿼리를 실행할 때 사용, ResultSet 타입으로 리턴 받는다.
  * java.sql.ResultSet : 네트워크를 통해 데이터를 읽어들이기에 작업후 close()로 연결 종료
  * Connection pool : 미리 Connection 객체를 생성해서 보관하고 필요할 때마 쓰는 방식
  * javax.sql.DataSource : Connection pool을 자바에서 API 형태로 지원
  * DAO(Data Access Object) : 데이터를 전문으로 처리하는 객체
  * VO(Value Object) : 데이터를 객체 단위로 처리

2. build.gradle 설정
```
dependencies {
    compileOnly('javax.servlet:javax.servlet-api:4.0.1')

    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
    // https://mvnrepository.com/artifact/org.mariadb.jdbc/mariadb-java-client
    implementation group: 'org.mariadb.jdbc', name: 'mariadb-java-client', version: '3.0.4'

    //Lombok 라이브러리 추가
    compileOnly 'org.projectlombok:lombok:1.18.26'
    annotationProcessor 'org.projectlombok:lombok:1.18.26'

    testCompileOnly group: 'org.projectlombok', name: 'lombok', version: '1.18.24'
    testAnnotationProcessor group: 'org.projectlombok', name: 'lombok', version:'1.18.24'

    implementation group: 'com.zaxxer', name: 'HikariCP', version: '5.0.0'

    // https://mvnrepository.com/artifact/org.modelmapper/modelmapper
    implementation group: 'org.modelmapper', name: 'modelmapper', version: '3.0.0'

    implementation group: 'org.apache.logging.log4j', name: 'log4j-core', version: '2.17.2'
    implementation group: 'org.apache.logging.log4j', name: 'log4j-api', version: '2.17.2'
    implementation group: 'org.apache.logging.log4j', name: 'log4j-slf4j-impl', version: '2.17.2'

    implementation group: 'jstl', name: 'jstl', version: '1.2'
}
}
```
3. Lombok 의 @Cleanup 을 사용하면 try-catch 문을 생략하고, close()가 호출 되는 것을 보장한다.
```
public String getTime2() throws Exception {
    @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
    @Cleanup PreparedStatement preparedStatement = connection.prepareStatement("select now()");
    @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

    resultSet.next();

    String now = resultSet.getString(1);

    return now;
}
```
4. log4j2.xml 설정(보안 이슈 해결 버전인 2.17.0 이상을 권장)
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<Configuration xmlns="http://logging.apache.org/log4j/2.0/config" status="WARN">
    <Appenders>
        <Console name="Console" target="SYSTEM_OUT">
            <PatternLayout pattern="%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n"/>
        </Console>
    </Appenders>
    <Loggers>
        <Root level="info">
            <AppenderRef ref="Console"/>
        </Root>
    </Loggers>
</Configuration>
```
5. 세션이란?
   * 웹은 기본적으로 무상태 연결(요청 -> 처리 -> 응답 -> 연결 종료)
   * 기존의 방문자를 기억하기 위해 세션, 쿠기, 토큰을 이용하게 됨
   * 로그인 유지를 위한 모든 기능을 세션 트랙킹(session tracking)이라고 한다.
   * 세션은 서버에 문자열(이름/값)으로 저장된다.
   * 톰켓에선 JSESSIONID 라는 이름의 쿠키로 생성된다.
   * 톰켓은 주기적으로 세션 저장소를 조사하면서 더 이상 사용하지 않으면 값을 정리한다.(기본 30분)
6. 필터란?
   * 컨트롤러 마다 동일하게 로그인 체크 로직을 작성하면 코드 중복이 생긴다.
   * 그래서 대부분 필터를 이용하여 처리한다.
   * 필터는 서블릿/jsp 에 도달하는 과정에서 필터링 하는 역할을 한다.
   * WebFilter 어노테이션으로 지정한 경로에 접근할 때 필터가 작동하도록 하면 동일한 로직을 필터로 분리할 수 있다.
7. EL의 Scope 와 HttpSession 접근하기
   * EL 의 Scope 는 setAttribute() 되어 있을 데이터를 찾을 때 사용된다.
   * Page Scope : JSP 에서 EL을 통해 <c:set> 으로 저장한 변수
   * Request Scope : HttpServletRequest 에 setAttribute() 로 저장한 변수
   * Session Scope : HttpSession 을 이용해서 setAttribute() 로 저장한 변수
   * Application Scope : ServletContext 를 이용해서 setAttribute() 로 저장한 변수
8. 사용자 정의 쿠키
   * 생성 : 개발자가 직접 newCookie() 로 생성(name, value)
   * 전송 : 반드시 HttpServletResponse 에 addCookie() 를 통해 전송
   * 유효 기간 : 쿠키 생성할 때 초 단위로 지정 가능
   * 브라우저의 보관 방식 : 유효기간이 없는 경우에는 메모리에만 보관, 유효 기간에 있는 경우 파일로 보관
   * 쿠키의 크기 : 4kb
9. 자동 로그인 
   * 사용자가 로그인 할 때 임의의 문자열을 생성하고 이를 DB에 보관
   * 쿠키에는 생성된 문자열을 값으로 삼고 유효기간은 1주일로 지정
   * 현자 사용자의 HttpSession 에 로그인 정보가 없는 경우에만 쿠키 확인
   * 쿠키의 값과 DB 값을 비교하고 같다면 사용자 정보를 읽어와서 HttpSession 에 사용자 정보 추가
10. 로그인 체크  
    * HttpServletRequest 를 이용해서 모든 쿠키 중에서 'remember-me' 이름의 쿠키를 검색
    * 해당 쿠키의 value 를 이용해서 MemberService 를 통해 MemberDTO 를 구성
    * HttpSession 을 이용해서 'loginInfo' 라는 이름으로 MemberDTO 를 setAttribute()
    * 정상적으로 FilterChain 의 doFilter() 을 수행