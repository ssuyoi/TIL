# 2026-01-17: Java 열거형 (Enumeration)

- 📚 **강의**: 김영한의 자바 중급1 섹션 6

오늘은 문자열이나 정수 상수의 문제점을 해결하고, 타입 안전성(Type-Safety)을 보장하는 **열거형(Enum)**에 대해 학습했다.

## 1. 상수의 문제점과 해결 과정

### 문제점 (String, int 상수)
- **타입 안전성 부족**: `int PRICE = 10` 같은 상수를 써도, 실수로 `20`(존재하지 않는 코드)을 넣어도 컴파일 오류가 나지 않는다.
- **데이터 일관성 부족**: `String GRADE = "GOLD"`라고 해도, "gold", "G", "Gold" 등 오타가 발생할 수 있다.

### 해결: 타입 안전 열거형 패턴 (Type-Safe Enum Pattern)
- 클래스를 만들고, `private` 생성자로 외부 생성을 막은 뒤, `public static final` 상수로 자기 자신 인스턴스를 미리 만들어두는 방식.
- 하지만 코드가 길어지고 구현이 번거롭다.

## 2. 열거형 (Enum)

자바는 위의 패턴을 언어 차원에서 지원하기 위해 `enum` 키워드를 제공한다.

```java
public enum Grade {
    BASIC, GOLD, DIAMOND
}
```

- **특징**:
    - 자동으로 `java.lang.Enum`을 상속받는다.
    - 외부에서 임의로 인스턴스를 생성할 수 없다. (생성자가 `private`)
    - **타입 안전성(Type-Safety)**이 보장된다. (`Grade` 타입 변수에는 `Grade`에 정의된 값만 들어갈 수 있다.)

### 주요 메서드
- `values()`: 모든 열거형 상수를 배열로 반환한다.
- `valueOf(String name)`: 문자열 이름으로 열거형 상수를 찾는다.
- `name()`: 열거형 상수의 이름을 문자열로 반환한다.
- `ordinal()`: 열거형 상수의 순서(0부터 시작)를 반환한다.
    - **주의**: 중간에 상수가 추가되면 순서가 바뀌어 DB 데이터 등이 꼬일 수 있으므로 **가급적 사용하지 않는다.**

## 3. 열거형의 확장

열거형도 클래스이므로 필드, 메서드, 생성자를 가질 수 있다. 이를 통해 상수와 연관된 데이터를 함께 관리할 수 있다.

```java
public enum Grade {
    BASIC(10), GOLD(20), DIAMOND(30);

    private final int discountPercent;

    Grade(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }
}
```

- **장점**: 관련된 데이터를 하나의 객체(상수) 안에 캡슐화하여 응집도를 높일 수 있다.
