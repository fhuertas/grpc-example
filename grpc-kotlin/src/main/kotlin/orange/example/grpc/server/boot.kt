package orange.example.grpc.server

import com.google.common.collect.ComparisonChain.start
import com.typesafe.config.ConfigFactory


fun main(args: Array<String>) {
//    require(args.size == 1) { "The application need the configuration path in Hocon format" }
//    val config = ConfigFactory.load(args[0])
    val config = ConfigFactory.load()
    val port = config.getInt("server.port")
    val host = config.getString("server.host")
    println("Starting Registry server")
    val server = RegistryServer(host,port)
    server.start()
    server.blockUntilShutdown()
}