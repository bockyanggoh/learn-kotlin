package com.example.jpacodeval

import org.junit.jupiter.api.Test
import org.springframework.test.util.AssertionErrors.assertEquals

class RandoTests {
    @Test
    internal fun test1() {
        var x = arrayListOf<String>("1", "2", "3")
        var x2 = arrayListOf<String>("1", "2")
        var x3 =  x.minus(x2)
        print(x3)
        assertEquals("equal", x3[0],"3")
    }
}