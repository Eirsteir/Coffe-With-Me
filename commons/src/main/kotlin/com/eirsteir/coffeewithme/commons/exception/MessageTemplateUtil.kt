package com.eirsteir.coffeewithme.commons.exception

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.text.MessageFormat
import java.util.*

@Component
class MessageTemplateUtil @Autowired constructor(propertiesConfig: PropertiesConfig?) {
    companion object {
        var propertiesConfig: PropertiesConfig? = null

    }

    init {
        Companion.propertiesConfig = propertiesConfig
    }
}