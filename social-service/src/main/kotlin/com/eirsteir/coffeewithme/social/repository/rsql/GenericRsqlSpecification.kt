package com.eirsteir.coffeewithme.social.repository.rsql

import cz.jirutka.rsql.parser.ast.*
import org.springframework.data.jpa.domain.Specification
import java.util.stream.Collectors
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

class GenericRsqlSpecification<T>(
    private val property: String,
    private val operator: ComparisonOperator,
    private val arguments: MutableList<String>
) : Specification<T> {

    override fun toPredicate(
        root: Root<T>, query: CriteriaQuery<*>, builder: CriteriaBuilder
    ): Predicate? {
        val args = castArguments(root)
        val argument = args[0]

        when (RsqlSearchOperation.getSimpleOperator(operator)) {
            RsqlSearchOperation.EQUAL -> {
                return if (argument is String) {
                    builder.like(root.get(property), argument.toString().replace('*', '%'))
                } else run {
                    builder.equal(root.get<Any>(property), argument)
                }
            }
            RsqlSearchOperation.NOT_EQUAL -> {
                return when (argument) {
                    is String -> {
                        builder.notLike(
                            root.get(property), argument.toString().replace('*', '%')
                        )
                    }
                    else -> {
                        builder.notEqual(root.get<Any>(property), argument)
                    }
                }
            }
            RsqlSearchOperation.GREATER_THAN -> {
                return builder.greaterThan(root.get(property), argument.toString())
            }
            RsqlSearchOperation.GREATER_THAN_OR_EQUAL -> {
                return builder.greaterThanOrEqualTo(root.get(property), argument.toString())
            }
            RsqlSearchOperation.LESS_THAN -> {
                return builder.lessThan(root.get(property), argument.toString())
            }
            RsqlSearchOperation.LESS_THAN_OR_EQUAL -> {
                return builder.lessThanOrEqualTo(root.get(property), argument.toString())
            }
            RsqlSearchOperation.IN -> return root.get<Any>(property).`in`(args)
            RsqlSearchOperation.NOT_IN -> return builder.not(root.get<Any>(property).`in`(args))
        }
        return null
    }

    private fun castArguments(root: Root<T>): MutableList<Any> {
        val type = root.get<Any>(property).javaType
        return arguments.stream()
            .map<Any> { arg: String ->
                when (type) {
                    Int::class.java -> {
                        return@map arg.toInt()
                    }
                    Long::class.java -> {
                        return@map arg.toLong()
                    }
                    else -> {
                        return@map arg
                    }
                }
            }
            .collect(Collectors.toList())
    }
}