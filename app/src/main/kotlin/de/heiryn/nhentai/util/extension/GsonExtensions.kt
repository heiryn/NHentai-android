package de.heiryn.nhentai.util.extension

import de.heiryn.nhentai.util.GsonUtils

fun <T> T.objectAsJson(): String = GsonUtils.toJson(this)

inline fun <reified T> String.jsonAsObject(): T = GsonUtils.fromJson(this)