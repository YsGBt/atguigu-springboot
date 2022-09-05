package com.atguigu.springboot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.EmptyStackException;
import java.util.Stack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("嵌套测试")
class TestingAStackDemo {

  Stack<Object> stack;

  @Test
  @DisplayName("is instantiated with new Stack()")
  void isInstantiatedWithNew() {
    new Stack<>();
    // 嵌套测试情况下，外层的Test不能驱动内层的Before(After)Each/All之类的方法
    assertNull(stack);
  }

  @Nested
  @DisplayName("when new")
  class WhenNew {

    @BeforeEach
    void createNewStack() {
      stack = new Stack<>();
    }

    @Test
    @DisplayName("is empty")
    void isEmpty() {
      assertTrue(stack.isEmpty());
    }

    @Test
    @DisplayName("throws EmptyStackException when popped")
    void throwsExceptionWhenPopped() {
      assertThrows(EmptyStackException.class, stack::pop);
    }

    @Test
    @DisplayName("throws EmptyStackException when peeked")
    void throwsExceptionWhenPeeked() {
      assertThrows(EmptyStackException.class, stack::peek);
    }

    @Nested
    @DisplayName("after pushing an element")
    class AfterPushing {

      String anElement = "an element";

      @BeforeEach
      void pushAnElement() {
        stack.push(anElement);
      }

      /**
       * 内层的Test可以驱动内层的Before(After)Each/All之类的方法
       */
      @Test
      @DisplayName("it is no longer empty")
      void isNotEmpty() {
        assertFalse(stack.isEmpty());
      }

      @Test
      @DisplayName("returns the element when popped and is empty")
      void returnElementWhenPopped() {
        assertEquals(anElement, stack.pop());
        assertTrue(stack.isEmpty());
      }

      @Test
      @DisplayName("returns the element when peeked but remains not empty")
      void returnElementWhenPeeked() {
        assertEquals(anElement, stack.peek());
        assertFalse(stack.isEmpty());
      }
    }
  }
}
