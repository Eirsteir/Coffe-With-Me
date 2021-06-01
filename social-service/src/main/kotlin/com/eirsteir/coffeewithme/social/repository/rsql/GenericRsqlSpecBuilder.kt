package com.eirsteir.coffeewithme.social.repository.rsql

import cz.jirutka.rsql.parser.ast.ComparisonNode
import cz.jirutka.rsql.parser.ast.LogicalNode
import cz.jirutka.rsql.parser.ast.LogicalOperator
import cz.jirutka.rsql.parser.ast.Node
import org.springframework.data.jpa.domain.Specification
import java.util.*
import java.util.stream.Collectors

class GenericRsqlSpecBuilder<T> {

    fun createSpecification(node: Node): Specification<T>? {
        if (node is LogicalNode) {
            return createSpecification(node)
        }
        return if (node is ComparisonNode) {
            createSpecification(node)
        } else null
    }

    fun createSpecification(logicalNode: LogicalNode): Specification<T>? {
        val specs = logicalNode.children.stream()
            .map { node: Node -> createSpecification(node) }
            .filter { obj: Specification<T>? -> Objects.nonNull(obj) }
            .collect(Collectors.toList())

        var result = specs[0]

        if (logicalNode.operator == LogicalOperator.AND)
            for (i in 1 until specs.size)
                result = Specification.where(result).and(specs[i])

        else if (logicalNode.operator == LogicalOperator.OR)
            for (i in 1 until specs.size)
                result = Specification.where(result).or(specs[i])

        return result
    }

    fun createSpecification(comparisonNode: ComparisonNode): Specification<T> {
        return Specification.where(
            GenericRsqlSpecification(
                comparisonNode.selector,
                comparisonNode.operator,
                comparisonNode.arguments
            )
        )
    }
}