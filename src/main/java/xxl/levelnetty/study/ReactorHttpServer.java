package xxl.levelnetty.study;


import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;

@Slf4j
public class ReactorHttpServer {
    public static void main(String[] args) {
        DisposableServer disposableServer = HttpServer.create()
                .host("0.0.0.0")
                .port(5556)
                .runOn(new NioEventLoopGroup(16, new DefaultThreadFactory("civic")))
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
