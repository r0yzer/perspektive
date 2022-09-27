package de.royzer.perspektive

import com.mojang.logging.LogUtils

fun initClient() {
    Perspektive.init()
    LogUtils.getLogger().info("Perspektive initialized")
}
