package com.yutao.flooow.dsl

import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class DirectedAcyclicGraph<Node> {
    private val graph: MutableMap<Node, MutableList<Node>> = mutableMapOf()
    private val reverseGraph:  MutableMap<Node, MutableList<Node>> = mutableMapOf()

    private fun deepCloneGraph(map: MutableMap<Node, MutableList<Node>>): MutableMap<Node, MutableList<Node>> {
        val result = HashMap<Node, MutableList<Node>>(map.size)
        map.entries.forEach {
            result[it.key] = ArrayList(it.value)
        }
        return result
    }

    // 环检查
    fun circleCheck(): Boolean {
        val tempGraph = deepCloneGraph(graph)
        tailrec fun topoSort(graph: MutableMap<Node, MutableList<Node>>): MutableMap<Node, MutableList<Node>> {
            val foundNodeWithIncomeZero = graph.entries.filter { it.value.isEmpty() }
            if (foundNodeWithIncomeZero.isEmpty()) {
                return graph
            }

            val foundNodeKey = foundNodeWithIncomeZero.map { it.key }
            foundNodeKey.forEach {
                graph.remove(it)
                reverseGraph[it]!!.forEach { removedNode ->
                    graph[removedNode]!!.remove(it)
                }
            }

            return topoSort(graph)
        }

        val resultMap = topoSort(tempGraph)
        return resultMap.isEmpty()
    }

    // 孤立节点检查
    fun isolatedCheck(): Boolean {
        val first = getFirst() ?: return false
        if (graph.size == 1) return true
        if (getLasts().intersect(setOf(first)).isNotEmpty()) {
            return false
        }
        // 存在孤立岛节点，也就是说graph中存在了多个图
        val walkedNodes = walk(first)
        if (walkedNodes.size < graph.keys.size) {
            return false
        }

        return true
    }

    private fun walk(node: Node): List<Node> {
        tailrec fun walkInternal(
            node: Node,
            nextNodes: HashSet<Node> = HashSet(),
            acc: MutableList<Node> = mutableListOf()
        ): List<Node> {
            if (!acc.contains(node)) {
                acc.add(node)
                nextNodes.addAll(graph[node] ?: emptyList())
            }

            if (nextNodes.isEmpty()) {
                return acc.toList()
            }
            return walkInternal(nextNodes.random().also { nextNodes.remove(it) }, nextNodes, acc)
        }
        return walkInternal(node)
    }

    // 空图检查
    fun emptyCheck(): Boolean {
        return graph.isNotEmpty()
    }

    fun addNode(node: Node) {
        if (graph[node] == null) {
            graph[node] = mutableListOf()
        }
        if (reverseGraph[node] == null) {
            reverseGraph[node] = mutableListOf()
        }
    }

    fun addEdge(from: Node, to: Node) {
        addNode(from)
        addNode(to)
        if (!graph[from]?.contains(to)!!) {
            graph[from]?.add(to)
        }
        if (!reverseGraph[to]?.contains(from)!!) {
            reverseGraph[to]?.add(from)
        }
    }

    fun next(node: Node): Optional<List<Node>> {
        return Optional.ofNullable(graph[node])
    }

    fun previous(node: Node): Optional<List<Node>> {
        return Optional.ofNullable(reverseGraph[node])
    }

    fun getFirst(): Node? {
        // only one start Node
        return reverseGraph.filter {
            it.value.isEmpty()
        }.keys.firstOrNull()
    }

    fun getLasts(): List<Node> {
        return graph.filter {
            it.value.isEmpty()
        }.keys.toList()
    }

    fun tasks(): List<Node> {
        return graph.keys.toList()
    }

}

