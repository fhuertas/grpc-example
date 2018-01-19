package orange.example.grpc.client

import com.typesafe.config.ConfigFactory

fun main(args: Array<String>) {
    val config = ConfigFactory.load()
    val port = config.getInt("client.port")
    val host = config.getString("client.host")
    val timeout= config.getLong("client.timeout")
    val times= config.getInt("client.times")
    val client = RegistryTimeClient(host,port)


    for (i: Int in IntRange(0,times)) {
        client.register("Kotlin Service ${System.currentTimeMillis()}")
        Thread.sleep(timeout)
    }
}