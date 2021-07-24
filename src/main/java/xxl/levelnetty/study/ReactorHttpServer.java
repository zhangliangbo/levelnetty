package xxl.levelnetty.study;


import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;
import reactor.netty.resources.LoopResources;

@Slf4j
public class ReactorHttpServer {
    public static void main(String[] args) {
        DisposableServer disposableServer = HttpServer.create()
                .port(5556)
                .runOn(LoopResources.create("civic", 1, 2, false))
                .accessLog(true)
                .handle((req, res) -> {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return res.sendString(Flux.just("hello"));
                })
                .bind()
                .block();
        log.info("服务绑定成功");
        assert disposableServer != null;
        disposableServer.onDispose().block();
    }
}
