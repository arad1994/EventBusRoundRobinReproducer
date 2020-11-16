package org.example.vertLauncher;

import com.google.common.collect.Lists;
import io.reactivex.Completable;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.config.ConfigRetriever;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Vertx;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VertxVert extends AbstractVerticle {
    Map<String, String> map = new HashMap<>();
    Boolean verticle1Deployed = false;
    Boolean verticle2Deployed = false;


    @Override
    public Completable rxStart() {
        Vertx vertx = Vertx.vertx();

        String path = new File("src/main/resources/localConfig.json")
                .getAbsolutePath();
        System.out.println("path: " + path);

        ConfigStoreOptions fileStore = new ConfigStoreOptions()
                .setType("file")
                .setConfig(new JsonObject().put("path", path));
        ConfigRetrieverOptions options = new ConfigRetrieverOptions()
                .setScanPeriod(500)
                .addStore(fileStore);

        ConfigRetriever retriever = ConfigRetriever.create(vertx, options);

        retriever.listen(change -> {
            JsonObject json = change.getNewConfiguration();
            System.out.println(json.toString());
            if (json.containsKey("verticle1") && !verticle1Deployed) {
                deployFirstVerticle();
            }
            if (json.containsKey("verticle2") && !verticle2Deployed) {
                deploySecondVerticle();
            }
            if (!json.containsKey("verticle1") && verticle1Deployed) {
                unDeployFirstVerticle();
            }
            if (!json.containsKey("verticle2") && verticle2Deployed) {
                unDeploySecondVerticle();
            }
        });
        return Completable.complete();
    }

    @Override
    public Completable rxStop() {
        System.out.println("Am I getting TRiggered");
        return vertx.rxClose();
    }

    private void deployFirstVerticle() {
        JsonObject jsonDepOptions = new JsonObject();
        jsonDepOptions.put("isolationGroup", UUID.randomUUID().toString());
        jsonDepOptions.put("extraClasspath", Lists.newArrayList("/Users/aradhakrishnan/Desktop/WorkSpace/ExampleVerticle/Jar1Loc"));
        DeploymentOptions depOptions = new DeploymentOptions(jsonDepOptions);
        this.vertx.deployVerticle("java:ExVert1.java", depOptions, ar -> {
            if (ar.succeeded()) {
                this.map.put("verticle1", ar.result());
                this.verticle1Deployed = true;
            } else {
                System.out.println("Verticle Deployment Failed: " + ar.cause());
            }
        });
    }

    private void unDeployFirstVerticle() {
        System.out.println("Undeploying First Verticle...");
        String depId = map.get("verticle1");
        this.vertx.undeploy(depId, ar -> {
            if (ar.succeeded()) {
                System.out.println("Verticle Undeployed Successfully");
                this.map.remove("verticle1");
                this.verticle1Deployed = false;
            } else {
                System.out.println("Verticle UnDeployment Failed: " + ar.cause());
            }
        });
    }

    private void deploySecondVerticle() {
        JsonObject jsonDepOptions = new JsonObject();
        jsonDepOptions.put("isolationGroup", UUID.randomUUID().toString());
        jsonDepOptions.put("extraClasspath", Lists.newArrayList("/Users/aradhakrishnan/Desktop/WorkSpace/ExampleVerticle/Jar2Loc"));
        DeploymentOptions depOptions = new DeploymentOptions(jsonDepOptions);
        this.vertx.deployVerticle("java:ExVert2.java", depOptions, ar -> {
            if (ar.succeeded()) {
                this.map.put("verticle2", ar.result());
                this.verticle2Deployed = true;
            } else {
                System.out.println("Verticle Deployment Failed: " + ar.cause());
            }
        });
    }

    private void unDeploySecondVerticle() {
        System.out.println("Undeploying Second Verticle...");
        String depId = map.get("verticle2");
        this.vertx.undeploy(depId, ar -> {
            if (ar.succeeded()) {
                System.out.println("Verticle Undeployed Successfully");
                this.map.remove("verticle2");
                this.verticle2Deployed = false;
            } else {
                System.out.println("Verticle UnDeployment Failed: " + ar.cause());
            }
        });
    }
}
