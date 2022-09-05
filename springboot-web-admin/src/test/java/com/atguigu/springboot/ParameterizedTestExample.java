package com.atguigu.springboot;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("参数化测试例子")
public class ParameterizedTestExample {

  @ParameterizedTest
  @DisplayName("参数化测试")
  @ValueSource(ints = {1, 2, 3, 4, 5})
  void testParameterizedTest(int i) {
    System.out.println(i);
  }

  static Stream<String> stringProvider() {
    return Stream.of("apple", "banana");
  }

  @ParameterizedTest
  @DisplayName("参数化测试，使用MethodSource")
  @MethodSource("stringProvider")
  void testParameterizedTest2(String s) {
    System.out.println(s);
  }
}
