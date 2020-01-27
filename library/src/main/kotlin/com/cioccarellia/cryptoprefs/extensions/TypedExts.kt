package com.cioccarellia.cryptoprefs.extensions

fun Set<*>.toStringSet() = map { it.toString() }.toSet()