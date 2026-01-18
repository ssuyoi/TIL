# 2026-01-18: Java 예외 처리 (Exception)

- 📚 **강의**: 김영한의 자바 중급1 섹션 10, 11

오늘은 자바의 안전성을 지켜주는 핵심 장치인 **예외 처리**에 대해 학습했다. 예외 계층 구조와 실무에서의 예외 처리 방식을 이해하는 것이 중요하다.

## 1. 예외 계층 (Hierarchy)

자바의 모든 예외와 에러는 `Throwable` 클래스를 상속받는다.

- **`Error`**: 시스템 레벨의 심각한 오류(메모리 부족 등). 애플리케이션에서 복구 불가능하므로 잡으려 하면 안 된다.
- **`Exception`**: 애플리케이션 로직에서 발생하는 실질적인 예외.
    - **체크 예외 (Checked Exception)**: `Exception`을 상속받은 하위 예외들 (단, `RuntimeException` 제외).
    - **언체크 예외 (Unchecked Exception)**: `RuntimeException`과 그 하위 예외들.

## 2. 체크 예외 vs 언체크 예외

| 구분 | 체크 예외 (Check Exception) | 언체크 예외 (Unchecked Exception) |
| :--- | :--- | :--- |
| **상속** | `Exception` | `RuntimeException` |
| **처리 여부** | **필수** (`try-catch` 또는 `throws`) | 선택 (그냥 둬도 됨) |
| **장점** | 개발자가 예외 처리를 누락하지 않도록 강제함 | 코드가 간결하고 불필요한 의존성을 줄임 |
| **단점** | 번거롭다. 하위 로직의 변경이 상위로 전파됨(OCP 위배) | 예외를 누락할 위험이 있음 |

### 실무 트렌드
과거에는 체크 예외가 안전하다고 여겨졌으나, 최근에는 **언체크 예외(런타임 예외)를 주로 사용**하는 추세다.
- 대부분의 예외(DB 연결 실패, 네트워크 오류 등)는 어차피 복구가 불가능하다.
- 이를 매번 `throws`로 명시하는 것은 코드만 복잡하게 하고, 기술 종속성(SQLException 등)을 상위 계층으로 전파하는 부작용이 크다.
- **결론**: 복구 불가능한 예외는 런타임 예외로 전환해서 던지고, 중요 예외만 컨트롤러나 서비스에서 일괄 처리(공통 예외 처리)하는 것이 좋다.

## 3. 자원 정리 (Try-with-resources)

파일이나 DB 연결 등은 사용 후 반드시 닫아야(`close()`) 한다. `try-catch-finally` 구문은 코드가 복잡하고 실수가 잦다.

```java
// Try-with-resources: 자동으로 close() 호출
try (NetworkClient client = new NetworkClient()) {
    client.connect();
    client.send(data);
} catch (Exception e) {
    ...
}
```

- **조건**: 해당 클래스가 `AutoCloseable` 인터페이스를 구현하고 있어야 한다.
- **효과**: 예외 발생 여부와 상관없이 블록을 빠져나갈 때 `close()`가 안전하게 호출된다.
