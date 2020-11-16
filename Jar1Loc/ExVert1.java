import io.reactivex.Completable;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.eventbus.EventBus;
import io.vertx.reactivex.core.eventbus.MessageConsumer;

import java.util.ArrayList;
import java.util.List;

public class ExVert1 extends AbstractVerticle {
    List<MessageConsumer<String>> lst = new ArrayList<>();
    long timerId = 0L;
    @Override
    public Completable rxStart() {
        System.out.println("Hello Vertx: " + "org.example.vert1.ExVert1");
        EventBus eb = vertx.eventBus();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        timerId = vertx.setPeriodic(1000,
                v -> eb.rxRequest("exVert2HealthCheck", "ping ExVert2").map(x -> {
                    System.out.println("Received resp from address: " + x.address() + " msg: " + x.body().toString());
                    return x;
                }).subscribe());
        return Completable.complete();
    }


    @Override
    public Completable rxStop() {
        System.out.println("Bye Vertx: " + "org.example.vert1.ExVert1");
        System.out.println("Am I getting TRiggered vertx1");
        vertx.cancelTimer(this.timerId);
        return Completable.complete();
    }
}
