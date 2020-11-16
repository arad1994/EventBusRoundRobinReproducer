package org.example;

import com.google.common.collect.Lists;
import io.vertx.core.Launcher;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.spi.cluster.ignite.IgniteClusterManager;

import java.util.List;

public class PreLauncherVerticle extends Launcher {

    public static void main(String[] args) {
        System.setProperty("vertx.logger-delegate-factory-class-name",
                "io.vertx.core.logging.SLF4JLogDelegateFactory");
        List<String> argsList = Lists.newArrayList(args);
        argsList.add("-cluster");
        new PreLauncherVerticle().dispatch(argsList.toArray(new String[argsList.size()]));
    }

    @Override
    public void beforeStartingVertx(VertxOptions options) {
        super.beforeStartingVertx(options);
        options.setClusterManager(new
                IgniteClusterManager(PreLauncherVerticle.this.getClass().getResource("/ignite.xml")))
                .setHAEnabled(false);
    }

    @Override
    public void afterStartingVertx(Vertx vertx) {
        super.afterStartingVertx(vertx);
    }
}
