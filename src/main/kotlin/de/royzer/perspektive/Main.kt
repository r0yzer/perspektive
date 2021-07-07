package de.royzer.perspektive

import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager

fun initClient() {
    LogManager.getLogger().log(Level.INFO, "Perspektive initialized")
    Perspektive.onInitialize()
}