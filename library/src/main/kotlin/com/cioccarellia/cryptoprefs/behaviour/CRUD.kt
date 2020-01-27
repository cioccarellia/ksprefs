package com.cioccarellia.cryptoprefs.behaviour

interface CRUD {
    fun <T : Any> get(key: String, default: T): T
    fun <T : Any> set(key: String, value: T)

    val all: MutableMap<String, *>?

    fun contains(key: String): Boolean
    fun remove(key: String)
    fun erase()
}