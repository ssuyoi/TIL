# 2026-01-29: 리플렉션과 WAS 프레임워크

- 📚 **강의**: 김영한의 자바 고급2 섹션 13

오늘은 자바의 강력한 기능인 **리플렉션(Reflection)**의 기초를 배우고, 이를 활용해 **Spring MVC처럼 동작하는 유연한 웹 서버(WAS)**를 직접 만들어보았다.

## 1. 리플렉션 (Reflection)

### 1-1. 개념 및 어원
리플렉션은 "거울에 비친 모습"이라는 뜻이다.
일반적인 프로그래밍은 내가 작성한 코드가 어떻게 돌아갈지 알고 있지만, 리플렉션은 **실행 중인 프로그램(런타임)이 자기 자신(거울)을 들여다보고 분석**한다는 의미를 담고 있다.
즉, **컴파일 타임에 알 수 없는 클래스나 메서드**를 런타임에 찾아내어 조작할 수 있는 기술이다.

### 1-2. 주요 기능 및 정보
리플렉션을 통해 클래스, 필드, 메서드의 거의 모든 메타데이터에 접근할 수 있다.

-   **Class**: 클래스 이름, 접근 제어자, 패키지, 부모 클래스, 인터페이스, 애노테이션
-   **Field**: 필드 이름, 타입, 값 조회 및 수정
-   **Method**: 메서드 이름, 반환 타입, 파라미터 정보, 실행(invoke)

#### 💡 메서드 조회 차이점 (`getMethods` vs `getDeclaredMethods`)
| 메서드 | 조회 범위 | 접근 제어자 |
| :--- | :--- | :--- |
| `getMethods()` | 해당 클래스 + **상속받은 메서드** | **public**만 조회 |
| `getDeclaredMethods()` | **해당 클래스에 정의된** 모든 메서드 (상속 제외) | **모든** 접근 제어자 (private 포함) |

```java
Class<?> aClass = calculator.getClass();
Method method = aClass.getMethod("add", int.class, int.class);
int result = (int) method.invoke(calculator, 1, 2);
```

## 2. 이걸 굳이 왜 쓸까? (실무 활용 예시)

일반적인 비즈니스 로직 개발에서는 **절대 사용하지 않는 것이 원칙**이다. 하지만 **프레임워크나 라이브러리 개발**에서는 필수 불가결한 기술이다.

1.  **Spring Framework, JPA**:
    -   `@Controller`, `@Service` 등이 붙은 클래스를 찾아 빈(Bean)으로 등록할 때.
    -   `@Autowired` 필드에 의존성을 주입할 때 (private 필드여도 가능).
2.  **JSON 라이브러리 (Jackson, Gson)**:
    -   객체의 필드 명과 값을 읽어서 JSON 문자열로 변환할 때.
3.  **테스트 프레임워크 (JUnit)**:
    -   `@Test`가 붙은 메서드만 골라서 실행할 때.

## 2. WAS의 진화: 하드코딩에서 프레임워크로

### V5: 서블릿 하드코딩 방식
새로운 기능을 추가할 때마다 **URL과 서블릿을 일일이 매핑**해줘야 했다.
```java
// 새로운 기능 추가 시 계속 코드를 수정해야 함 (OCP 위반)
servletManager.add("/site1", new Site1Servlet());
servletManager.add("/site2", new Site2Servlet());
servletManager.add("/search", new SearchServlet());
```

### V6: 리플렉션 기반 (프레임워크 스타일)
`ReflectionServlet` 하나가 모든 요청을 받아서, **요청 경로와 이름이 같은 메서드**를 찾아 자동으로 실행한다.
-   `/hello` 요청 -> `hello()` 메서드 실행
-   `/search` 요청 -> `search()` 메서드 실행

```java
// ReflectionServlet 내부 로직
for (Method method : controller.getClass().getDeclaredMethods()) {
    if (path.equals("/" + method.getName())) { // 경로와 메서드 이름 일치 확인
        method.invoke(controller, request, response); // 동적 실행!
        return;
    }
}
```

> **💡 핵심**:
> 이제 개발자는 설정 코드를 건드리지 않고, **컨트롤러에 메서드만 추가하면 기능이 바로 동작**한다. 이것이 바로 **Spring MVC**와 같은 프레임워크가 개발자에게 주는 편리함의 원리다.

## 4. 리플렉션의 장단점과 주의사항

-   **장점**: 런타임에 동적으로 유연하게 동작하는 **프레임워크**나 공통 라이브러리를 만들 수 있다.
-   **단점**:
    -   **컴파일 오류 체크 불가**: 메서드 이름을 틀려도 실행해봐야(`NoSuchMethodException`) 안다.
    -   **성능 오버헤드**: 일반적인 메서드 호출보다 훨씬 느리다.
    -   **디버깅 어려움**: 코드가 직관적이지 않다.

### ⚠️ 주의: Private 접근과 OOP 위반
리플렉션은 `setAccessible(true)`를 사용하면 **private 필드나 메서드에도 강제로 접근**할 수 있다.
-   이는 **캡슐화(Encapsulation)**라는 객체 지향 프로그래밍의 핵심 원칙을 무시하는 행위다.
-   따라서 일반적인 비즈니스 로직에서는 절대 사용하면 안 되며, **프레임워크 내부나 테스트 코드** 등 특수한 목적에만 제한적으로 사용해야 한다.

**결론**: 필요할 때만 매우 신중하게 사용해야 한다. (일반 개발자는 쓸 일이 거의 없음)
