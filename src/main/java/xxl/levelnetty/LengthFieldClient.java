package xxl.levelnetty;

import io.reactivex.rxjava3.subscribers.DisposableSubscriber;
import org.apache.commons.cli.*;

public class LengthFieldClient {
    public static void main(String[] args) throws InterruptedException, ParseException {
        String[] keys = new String[]{"help", "h", "p", "o", "l", "a", "s"};
        Options options = new Options()
                .addOption(Option.builder(keys[0]).desc("help").build())
                .addOption(Option.builder(keys[1]).hasArg().desc("host").build())
                .addOption(Option.builder(keys[2]).hasArg().desc("port").build())
                .addOption(Option.builder(keys[3]).hasArg().desc("offset").build())
                .addOption(Option.builder(keys[4]).hasArg().desc("length").build())
                .addOption(Option.builder(keys[5]).hasArg().desc("adjustment").build())
                .addOption(Option.builder(keys[6]).hasArg().desc("strip").build());
        CommandLine cli = new DefaultParser().parse(options, args);
        if (cli.hasOption(keys[0])) {
            System.err.println(options);
            return;
        }
        final String host = "192.168.24.101";
        final int port = 9000;
        NettyTcp.lengthField(host, port, 6, 4, 0, 10)
                .blockingSubscribe(new DisposableSubscriber<byte[]>() {
                    @Override
                    protected void onStart() {
                        System.err.println("subscribe");
                        request(1);
                    }

                    @Override
                    public void onNext(byte[] bytes) {
                        System.err.println(new String(bytes));
                        request(1);
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.err.println(t.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        System.err.println("complete");
                    }
                });
    }
}
