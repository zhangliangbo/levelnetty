package xxl.levelnetty.study;


import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.netty.http.server.HttpServer;

@Slf4j
public class ReactorHttpServer {
    public static void main(String[] args) {
        HttpServer.create()
                .port(5556)
                .accessLog(true)
                .handle((req, res) -> res.sendString(Flux.just("hello")))
                .bind()
                .block();
    }
}
