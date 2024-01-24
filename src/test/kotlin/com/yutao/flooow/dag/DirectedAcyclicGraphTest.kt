package com.yutao.flooow.dag

import com.yutao.flooow.dsl.DirectedAcyclicGraph
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class DirectedAcyclicGraphTest {

    @Test
    fun isolateNodeCheckTestPassed() {
        val g = DirectedAcyclicGraph<Int>()
        g.addEdge(1, 2)
        g.addEdge(1, 3)
        g.addEdge(2, 3)
        g.addEdge(3, 4)

        assertEquals(true, g.isolatedCheck())
    }

    @Test
    fun isolateNodeCheckTestFailed() {
        val g = DirectedAcyclicGraph<Int>()
        g.addEdge(1, 2)
        g.addEdge(1, 3)
        g.addEdge(2, 3)
        g.addEdge(3, 4)

        g.addEdge(5, 6)

        assertEquals(false, g.isolatedCheck())
    }

    @Test
    fun circleCheckTestFailed() {
        val g = DirectedAcyclicGraph<Int>()
        g.addEdge(0, 1)
        g.addEdge(1, 2)
        g.addEdge(2, 3)
        g.addEdge(3, 1)

        assertEquals(false, g.circleCheck())
    }

    @Test
    fun circleCheckPassed() {
        val g = DirectedAcyclicGraph<Int>()
        g.addEdge(0, 1)
        g.addEdge(1, 2)
        g.addEdge(2, 5)
        g.addEdge(2, 3)
        g.addEdge(3, 4)
        g.addEdge(3, 5)

        assertEquals(true, g.circleCheck())
    }
}
