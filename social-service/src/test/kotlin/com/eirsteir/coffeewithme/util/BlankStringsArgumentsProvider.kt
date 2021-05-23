package com.eirsteir.coffeewithme.util

import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.util.stream.Stream

class BlankStringsArgumentsProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments?>? {
        return Stream.of(Arguments.of(null as String?), Arguments.of(""), Arguments.of("   "))
    }
}