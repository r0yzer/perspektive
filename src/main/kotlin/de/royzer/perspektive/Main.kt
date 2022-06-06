package de.royzer.perspektive

fun initClient() {
    com.mojang.logging.LogUtils.getLogger().info("Perspektive initialized")
    Perspektive.init()
}
