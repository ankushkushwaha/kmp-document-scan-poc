package com.solera.poc

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}



//class xyz {
//
//    fun startScan () {
//        commonMain.startScan()
//    }
//
//    fun upload (byteArray): result {
//
//        APIManager().upload(path)
//    }
//
//}


