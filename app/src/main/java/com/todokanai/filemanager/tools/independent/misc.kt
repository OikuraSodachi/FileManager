package com.todokanai.filemanager.tools.independent

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.scan

/** 이 method들은 독립적으로 사용 가능함 */

/** Gemini generated code.
 * @return flow of previous value **/
fun <T> Flow<T>.withPrevious_td(): Flow<T?> =
    scan(Pair<T?, T?>(null, null)) { previousPair, currentValue ->
        Pair(previousPair.second, currentValue)
    }.map { it.first }