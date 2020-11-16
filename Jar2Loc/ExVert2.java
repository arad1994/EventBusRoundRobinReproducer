import io.reactivex.Completable;
import io.reactivex.disposables.Disposable;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.eventbus.EventBus;
import io.vertx.reactivex.core.eventbus.MessageConsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ExVert2 extends AbstractVerticle {
    List<MessageConsumer<String>> lst = new ArrayList<>();
    Disposable subscribe;
    @Override
    public Completable rxStart() {
        System.out.println("Hello Vertx: " + "org.example.vert2.ExVert2");
        EventBus eb = vertx.eventBus();
        AtomicInteger prev = new AtomicInteger();

        MessageConsumer<String> msg = eb.consumer("exVert2HealthCheck");
        lst.add(msg);
        subscribe = msg.toFlowable()
                .doOnError(Throwable::printStackTrace)
                .subscribe(message -> {
                    System.out.println("Recieved message from : "
                            + message.address() + " reply address: " + message.replyAddress()
                            + " what's my message: " + message.body());
                    prev.addAndGet(1);
                    message.reply(message.body() + prev.get());
                });
        System.out.println(msg.isRegistered());
        return Completable.complete();
    }


    @Override
    public Completable rxStop() {
        System.out.println("Am I getting TRiggered vertx2");
        System.out.println("Bye Vertx: " + "org.example.vert2.ExVert2");
        this.subscribe.dispose();
        for (MessageConsumer<String> msg : lst)
            msg.unregister();
        return Completable.complete();
    }
}

