package orange.example.grpc

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.StatusRuntimeException
import java.util.concurrent.TimeUnit
import java.util.logging.Level
import java.util.logging.Logger


class HelloWorldServer(host: String, port: Int) {
    val channel: ManagedChannel = createChannel(host, port)
    fun createChannel(host: String, port: Int): ManagedChannel =
            ManagedChannelBuilder.forAddress(host, port)
                    // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                    // needing certificates.
                    .usePlaintext(true)
                    .build()


    val blockingStub: RegistryGrpc.RegistryBlockingStub = RegistryGrpc.newBlockingStub(channel)

    companion object {
        val logger: Logger = Logger.getLogger(HelloWorldServer::class.java.getName())
    }
}