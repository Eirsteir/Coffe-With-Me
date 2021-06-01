package com.eirsteir.coffeewithme.social.domain

interface RequestStatus {
    fun getStatus(): String?
    fun getValue(): Int?
}