package orange.example.grpc

import io.grpc.Server
import io.grpc.ServerBuilder
import io.grpc.stub.StreamObserver
import java.util.logging.Logger
import orange.example.grpc.RegistryGrpc.RegistryImplBase


class RegistryServer(host: String, port: Int) {

    val server: Server = ServerBuilder.forPort(port).addService(RegistryImpl()).build()
    companion object {
        val logger: Logger = Logger.getLogger(RegistryServer::class.java.getName())
    }

    fun start() {
        server.start()
        Runtime.getRuntime().addShutdownHook(Thread {
            stop
        })
    }

    fun stop(){}
}

class RegistryImpl : RegistryImplBase() {
    var services = emptySet<String>()

    companion object {
        val OK = 0
        val REGISTERED = 1
    }

    override fun registerService(req: Register, responseObserver: StreamObserver<RegisterResult>): Unit {
        val newService = req.name
        val result = when (services.contains(newService)) {
            true -> REGISTERED
            else -> {
                services += newService
                OK
            }


        }

        val response = RegisterResult.newBuilder().setStatus(result).build()
        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }
}
