package com.solera.poc

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

