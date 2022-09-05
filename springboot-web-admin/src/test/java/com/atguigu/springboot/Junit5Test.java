package com.atguigu.springboot;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/**
 * @BootstrapWith(SpringBootTestContextBootstrapper.class)
 * @ExtendWith(SpringExtension.class) ==
 * @SpringBootTest
 */
//@SpringBootTest
@DisplayName("JUnit5功能测试类")
public class Junit5Test {

  private static int i;

  @BeforeAll
  static void testBeforeAll() {
    i = 1;
    System.out.println("所有测试就要开始了...");
  }

  @AfterAll
  static void testAfterAll() {
    System.out.println("所有测试已经结束了...");
  }

  @BeforeEach
  void testBeforeEach() {
    System.out.println("测试 " + i + " 就要开始了...");
  }

  @AfterEach
  void testAfterEach() {
    System.out.println("测试 " + i++ + " 就要结束了...");
  }

  @Test
  @DisplayName("测试displayName注解")
  void testDisplayName() {
    System.out.println(1);
  }

  @Disabled
  @Test
  @DisplayName("测试方法2")
  void test2() {
    System.out.println(2);
  }

  @Disabled
  @Test
  @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    // 规定方法的超时时间，超出时间当前测试出异常
  void testTimeOut() throws InterruptedException {
    Thread.sleep(600);
  }

  @RepeatedTest(5)
  void test3() {
    System.out.println(3);
  }

  @Test
  @DisplayName("测试简单断言")
  void testSimpleAssertions() {
    int cal = cal(2, 3);
    Assertions.assertEquals(5, cal, "业务逻辑计算失败");
  }

  @Test
  @DisplayName("array assertion")
  void array() {
    Assertions.assertArrayEquals(new int[]{1, 2}, new int[]{1, 2});
  }

  @Test
  @DisplayName("assert all")
  void all() {
    Assertions.assertAll("test assert all",
        () -> Assertions.assertTrue(true && true),
        () -> Assertions.assertEquals(1, cal(1, 0)));
  }

  @Test
  @DisplayName("异常断言")
  void testException() {
    Assertions.assertThrows(ArithmeticException.class, () -> {
      int i = 10 / 0;
    }, "业务没有正常抛出异常");
  }

  int cal(int i, int j) {
    return i + j;
  }

  @Test
  @DisplayName("快速失败")
  void testFail() {
    if (i == 2) {
      Assertions.fail("测试失败");
    }
  }

  // 测试前置条件
  @Test
  @DisplayName("测试前置条件")
  void testAssumptions() {
    Assumptions.assumeTrue(true, "结果不是true");
    System.out.println(4);
  }
}
