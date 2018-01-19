package orange.example.grpc.client

import io.grpc.ManagedChannelBuilder
import io.grpc.StatusRuntimeException
import orange.example.grpc.Register
import orange.example.grpc.RegistryGrpc
import orange.example.grpc.server.RegistryServer
import java.util.concurrent.TimeUnit
import java.util.logging.Level
import java.util.logging.Logger

class RegistryTimeClient(host: String, port: Int) {
    companion object {
        val logger: Logger = Logger.getLogger(RegistryTimeClient::class.java.name)
    }

    val channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext(true)
            .build()
    val blockStub: RegistryGrpc.RegistryBlockingStub = RegistryGrpc.newBlockingStub(channel)

    fun shutDown() = channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)

    fun register(service: String) {
        logger.warning("Sending a new register")
        val request = Register.newBuilder().setName(service).build()
        try {

            val response = blockStub.registerService(request)
            when(response.status) {
                RegistryServer.OK -> logger.info("The service $service  has been registered")
                RegistryServer.REGISTERED -> logger.info("The service $service  hasn't been registered")
            }
        } catch (e: StatusRuntimeException) {
            logger.info("ERROR")
        }
    }


}