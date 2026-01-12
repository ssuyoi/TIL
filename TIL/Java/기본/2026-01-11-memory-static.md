# 2026-01-11: Java 자바 메모리 구조와 static 학습

- 📚 **강의**: 김영한의 자바 기본 섹션 8

오늘은 자바가 메모리를 어떻게 사용하는지(Stack, Heap, Method Area)와, 이를 바탕으로 **static** 키워드가 어떻게 동작하는지 학습했다.

## 1. 자바 메모리 구조

자바의 메모리 구조는 크게 세 가지 영역으로 나뉜다.

### 메서드 영역 (Method Area)
- 프로그램을 실행하는 데 필요한 공통 데이터를 관리한다.
- **포함 내용**: 클래스 정보(붕어빵 틀), **static 변수**, 상수, 메서드 코드 등.

### 스택 영역 (Stack Area)
- 메서드 실행과 관계된 영역이다. (LIFO - 후입선출)
- 메서드가 호출되면 **스택 프레임(Stack Frame)**이 생성되고, 메서드가 종료되면 제거된다.
- **포함 내용**: 지역 변수, 매개변수, 연산 결과 등.

### 힙 영역 (Heap Area)
- 객체(인스턴스)와 배열이 생성되는 영역이다. (`new` 명령어를 사용하면 이 곳에 생성됨)
- **GC (Garbage Collection)**: 더 이상 참조되지 않는 객체는 GC에 의해 제거된다.

## 2. 스택과 힙 (Stack vs Heap)

- **스택**: 메서드 호출 시 잠시 사용되었다가 사라지는 **임시 저장소**다. 기본형 변수의 실제 값은 여기에 저장된다.
- **힙**: 객체가 생성되고 살아있는 동안 유지되는 **동적 저장소**다. 참조형 변수는 스택에 생성되지만, 실제 객체의 위치(참조값)를 통해 힙 영역의 객체를 가리킨다.

## 3. static 변수

`static` 키워드가 붙은 멤버 변수는 **메서드 영역**에서 관리된다.

### 특징
- **공용 변수**: 인스턴스를 여러 개 생성해도, `static` 변수는 하나만 존재하며 모든 인스턴스가 공유한다.
- **생명 주기**: 프로그램 시작 시 생성되고, 프로그램 종료 시 제거된다. (가장 긴 생명 주기)
- **접근 방법**: 인스턴스 생성 없이 `클래스명.변수명`으로 바로 접근할 수 있다.

```java
public class Data {
    public static int count; // static 변수
}

// 사용
Data.count++;
```

## 4. static 메서드

`static` 키워드가 붙은 메서드 또한 인스턴스 생성 없이 호출할 수 있다.

### 특징
- **유틸리티 기능**: 객체의 상태(인스턴스 변수)와 무관하게 동작하는 기능(예: 수학 계산, 데코레이션 기능)을 만들 때 유용하다.
- **접근 제한**: `static` 메서드는 **static 변수나 static 메서드만 사용할 수 있다.**
    - 인스턴스 변수나 인스턴스 메서드는 사용할 수 없다. (참조할 인스턴스가 없기 때문)

```java
public class DecoUtil {
    public static String deco(String str) {
        return "*" + str + "*";
    }
}

// 사용 (객체 생성 불필요)
String result = DecoUtil.deco("hello");
```

## 5. 실습 예제

`static` 키워드를 활용한 실습 예제 두 가지를 통해 개념을 확실히 익혔다.

### Car 예제 (static 변수 활용)
- **목표**: 생성된 차량의 총 수를 누적해서 관리한다.
- **해결**: 인스턴스 변수가 아닌 `static` 변수 `totalCars`를 만들어 모든 차량 인스턴스가 공유하게 했다.

```java
public class Car {
    private static int totalCars; // 모든 차가 공유하는 변수
    private String name;

    public Car(String name) {
        this.name = name;
        totalCars++; // 차량 생성 시 마다 증가
    }
}
```

### MathArrayUtils 예제 (static 메서드 활용)
- **목표**: 객체 생성 없이 배열의 합, 평균, 최소/최대값을 구하는 유틸리티 기능을 만든다.
- **해결**:
    1.  모든 메서드를 `static`으로 선언하여 인스턴스 생성 없이 호출 가능하게 했다.
    2.  **생성자를 `private`으로 막아** 불필요한 인스턴스 생성을 원천 차단했다.

```java
public class MathArrayUtils {
    // 인스턴스 생성 제약 (생성자를 private으로)
    private MathArrayUtils() {}

    public static int sum(int[] values) { ... }
    public static double average(int[] values) { ... }
}
```
