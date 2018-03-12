package com.ottd.libs.framework.common

import com.ottd.libs.config.Config
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Created by hardyshi on 2018/2/8.
 */
class ConfigProxy<T>(val key: String, val defValue: T) : ReadOnlyProperty<Any, T> {
    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return when(defValue) {
            is Int -> Config.readConfig(key, defValue)
            is Long -> Config.readConfig(key, defValue)
            is Boolean -> Config.isSwitchEnabled(key, defValue)
            else -> Config.readConfig(key, defValue as String)
        } as T
    }
}