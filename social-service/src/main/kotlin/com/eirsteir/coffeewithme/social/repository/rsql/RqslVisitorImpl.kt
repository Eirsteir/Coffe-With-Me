package com.eirsteir.coffeewithme.social.repository.rsql

import cz.jirutka.rsql.parser.ast.AndNode
import cz.jirutka.rsql.parser.ast.ComparisonNode
import cz.jirutka.rsql.parser.ast.OrNode
import cz.jirutka.rsql.parser.ast.RSQLVisitor
import org.springframework.data.jpa.domain.Specification

class RqslVisitorImpl<T>(
    private val builder: GenericRsqlSpecBuilder<T> = GenericRsqlSpecBuilder()
) : RSQLVisitor<Specification<T>, Void> {

    override fun visit(node: AndNode, param: Void): Specification<T>? {
        return builder.createSpecification(node)
    }

    override fun visit(node: OrNode, param: Void): Specification<T>? {
        return builder.createSpecification(node)
    }

    override fun visit(node: ComparisonNode, params: Void): Specification<T> {
        return builder.createSpecification(node)
    }

}