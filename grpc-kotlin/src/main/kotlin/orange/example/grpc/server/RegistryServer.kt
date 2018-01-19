package orange.example.grpc.server

import io.grpc.Server
import io.grpc.ServerBuilder
import io.grpc.stub.StreamObserver
import orange.example.grpc.Register
import orange.example.grpc.RegisterResult
import java.util.logging.Logger
import orange.example.grpc.RegistryGrpc.RegistryImplBase


class RegistryServer(host: String, port: Int) {

    private val server: Server = ServerBuilder.forPort(port).addService(RegistryImpl()).build()

    companion object {
        val logger: Logger = Logger.getLogger(RegistryServer::class.java.name)
        const val OK = 0
        const val REGISTERED = 1

    }

    fun start() {
        server.start()
        Runtime.getRuntime().addShutdownHook(Thread {
            this.stop()
        })
    }

    fun stop() {
        logger.info("*** shutting down gRPC server since JVM is shutting down")
        server.shutdown()
        logger.info("*** server shut down")
    }

    fun blockUntilShutdown() {
        server.awaitTermination();
    }
}

class RegistryImpl : RegistryImplBase() {
    var services = emptySet<String>()

    override fun registerService(req: Register, responseObserver: StreamObserver<RegisterResult>) {
        val newService = req.name
        val result = when (services.contains(newService)) {
            true -> {
                RegistryServer.logger.info("This service is registered: $newService")
                RegistryServer.REGISTERED
            }
            else -> {
                RegistryServer.logger.info("New service: $newService")
                services += newService
                RegistryServer.OK
            }
        }

        val response = RegisterResult.newBuilder().setStatus(result).build()
        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }
}
