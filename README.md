# EventBusRoundRobinReproducer

### Cluster Manager -> Ignite Cluster Manager
### Service Discovery -> Zookeeper
### java Version -> Java8
### Vertx Version -> Vertx 4.0.0-milestone5

##### Steps to run ->
1) Start Docker 
2) Start Zookeeper
3) Open the project in IntellliJ and import this runConfiguaration ```MainVerticle/runConfigurations/RunStuff.xml ``` and run


### Information on the Project
The main purpose of this project is to act as a reproducer to the https://github.com/eclipse-vertx/vert.x/pull/3636
This project consists of three subfolders ->
  1) MainVerticle -> This is heart of the project which is responsible for starting up and deploying two verticles (simple classes) in Jar1Loc and Jar2Loc folders (in isolation)
  The run configuration lies inside this Repo ```MainVerticle/runConfigurations/RunStuff.xml ```. The entry point is the VertxVert.java class which has a configRetriever set up to look at the localConfig.json file and based of that deploy and undeploy a verticle. The localConfig looks like this
  ```json
    {
      "verticle1": "ExVert1",
      "verticle2": "ExVert2"
    }
  ```
  If these two entries exists then both the verticles will be deployed. On removing verticle1 key value pair the first verticle will be undeployed. and same for the second one 
  2) Jar1Loc -> This Folder contains a simple Verticle written in Java to startup and periodically initate a eventBus communication to another Verticle lying in Jar2Loc
  3) Jar2Loc -> This Folder contains a simple Verticle written in Java to startup and register it's address on eventBus and respond with an incremental number to any message it recieves. Example Response -> ``` Recieved message from : exVert2HealthCheck reply address: __vertx.reply.97bee46d-18a9-4f08-8a56-f12bc6afc122 what's my message: ping ExVert2 ```
  
It was observed that on undeploying and deploying the second verticle (Since that registers on the eventBus). Only 50% of the requests recieve a response the other 50% get a timeout. 
The output logs are located at ``` MainVerticle/SupportLogs.txt ```

Preview of the logs
```
"verticle1":"ExVert1","verticle2":"ExVert2"}
Hello Vertx: org.example.vert1.ExVert1
Hello Vertx: org.example.vert2.ExVert2
true
Recieved message from : exVert2HealthCheck reply address: __vertx.reply.97bee46d-18a9-4f08-8a56-f12bc6afc122 what's my message: ping ExVert2
Received resp from address: __vertx.reply.97bee46d-18a9-4f08-8a56-f12bc6afc122 msg: ping ExVert21
Recieved message from : exVert2HealthCheck reply address: __vertx.reply.1828d30f-bb17-4418-9c5f-8ee78aaa998b what's my message: ping ExVert2
Received resp from address: __vertx.reply.1828d30f-bb17-4418-9c5f-8ee78aaa998b msg: ping ExVert22
Recieved message from : exVert2HealthCheck reply address: __vertx.reply.6c761162-398d-4e30-9a6f-79d005eee8c6 what's my message: ping ExVert2
Received resp from address: __vertx.reply.6c761162-398d-4e30-9a6f-79d005eee8c6 msg: ping ExVert23
{"verticle1":"ExVert1"}
Undeploying Second Verticle...
Am I getting TRiggered vertx2
Bye Vertx: org.example.vert2.ExVert2
Verticle Undeployed Successfully
io.reactivex.exceptions.OnErrorNotImplementedException: Timed out after waiting 30000(ms) for a reply. address: __vertx.reply.c1471a3e-a14d-4fd3-a63b-6074d328b0b2, repliedAddress: exVert2HealthCheck
	at io.reactivex.internal.functions.Functions$OnErrorMissingConsumer.accept(Functions.java:704)
	at io.reactivex.internal.functions.Functions$OnErrorMissingConsumer.accept(Functions.java:701)
	at io.reactivex.internal.observers.ConsumerSingleObserver.onError(ConsumerSingleObserver.java:47)
	at io.reactivex.internal.operators.single.SingleMap$MapSingleObserver.onError(SingleMap.java:69)
	at io.vertx.reactivex.impl.AsyncResultSingle.lambda$subscribeActual$0(AsyncResultSingle.java:56)
	at io.vertx.reactivex.core.eventbus.EventBus$1.handle(EventBus.java:150)
	at io.vertx.reactivex.core.eventbus.EventBus$1.handle(EventBus.java:145)
	at io.vertx.core.impl.AbstractContext.emit(AbstractContext.java:181)
	at io.vertx.core.impl.AbstractContext.lambda$dispatch$0(AbstractContext.java:84)
	at io.vertx.core.impl.EventLoopContext.schedule(EventLoopContext.java:59)
	at io.vertx.core.impl.AbstractContext.schedule(AbstractContext.java:94)
	at io.vertx.core.impl.AbstractContext.dispatch(AbstractContext.java:84)
	at io.vertx.core.impl.FutureImpl.doDispatch(FutureImpl.java:125)
	at io.vertx.core.impl.FutureImpl.dispatch(FutureImpl.java:119)
	at io.vertx.core.impl.FutureImpl.tryFail(FutureImpl.java:170)
	at io.vertx.core.eventbus.impl.ReplyHandler.fail(ReplyHandler.java:63)
	at io.vertx.core.eventbus.impl.ReplyHandler.lambda$new$0(ReplyHandler.java:42)
	at io.vertx.core.impl.VertxImpl$InternalTimerHandler.handle(VertxImpl.java:922)
	at io.vertx.core.impl.VertxImpl$InternalTimerHandler.handle(VertxImpl.java:889)
	at io.vertx.core.impl.AbstractContext.emit(AbstractContext.java:181)
	at io.vertx.core.impl.AbstractContext.lambda$dispatch$0(AbstractContext.java:84)
	at io.vertx.core.impl.EventLoopContext.schedule(EventLoopContext.java:59)
	at io.vertx.core.impl.AbstractContext.schedule(AbstractContext.java:94)
	at io.vertx.core.impl.AbstractContext.dispatch(AbstractContext.java:84)
	at io.vertx.core.impl.AbstractContext.dispatch(AbstractContext.java:89)
	at io.vertx.core.impl.EventLoopContext.dispatch(EventLoopContext.java:24)
	at io.vertx.core.impl.VertxImpl$InternalTimerHandler.run(VertxImpl.java:912)
	at io.netty.util.concurrent.PromiseTask.runTask(PromiseTask.java:98)
	at io.netty.util.concurrent.ScheduledFutureTask.run(ScheduledFutureTask.java:170)
	at io.netty.util.concurrent.AbstractEventExecutor.safeExecute(AbstractEventExecutor.java:164)
	at io.netty.util.concurrent.SingleThreadEventExecutor.runAllTasks(SingleThreadEventExecutor.java:472)
	at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:500)
	at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:989)
	at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)
	at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)
	at java.lang.Thread.run(Thread.java:748)
Caused by: (TIMEOUT,-1) Timed out after waiting 30000(ms) for a reply. address: __vertx.reply.c1471a3e-a14d-4fd3-a63b-6074d328b0b2, repliedAddress: exVert2HealthCheck
	... 20 more

{"verticle1":"ExVert1","verticle2":"ExVert2"}
Hello Vertx: org.example.vert2.ExVert2
true

Recieved message from : exVert2HealthCheck reply address: __vertx.reply.1b58c78a-288d-45f2-9666-afe6e1443326 what's my message: ping ExVert2
Received resp from address: __vertx.reply.1b58c78a-288d-45f2-9666-afe6e1443326 msg: ping ExVert21
io.reactivex.exceptions.OnErrorNotImplementedException: Timed out after waiting 30000(ms) for a reply. address: __vertx.reply.5b288364-17e3-4393-a54c-ed9b664dd240, repliedAddress: exVert2HealthCheck
	at io.reactivex.internal.functions.Functions$OnErrorMissingConsumer.accept(Functions.java:704)
	at io.reactivex.internal.functions.Functions$OnErrorMissingConsumer.accept(Functions.java:701)
	at io.reactivex.internal.observers.ConsumerSingleObserver.onError(ConsumerSingleObserver.java:47)
	at io.reactivex.internal.operators.single.SingleMap$MapSingleObserver.onError(SingleMap.java:69)
	at io.vertx.reactivex.impl.AsyncResultSingle.lambda$subscribeActual$0(AsyncResultSingle.java:56)
	at io.vertx.reactivex.core.eventbus.EventBus$1.handle(EventBus.java:150)
	at io.vertx.reactivex.core.eventbus.EventBus$1.handle(EventBus.java:145)
	at io.vertx.core.impl.AbstractContext.emit(AbstractContext.java:181)
	at io.vertx.core.impl.AbstractContext.lambda$dispatch$0(AbstractContext.java:84)
	at io.vertx.core.impl.EventLoopContext.schedule(EventLoopContext.java:59)
	at io.vertx.core.impl.AbstractContext.schedule(AbstractContext.java:94)
	at io.vertx.core.impl.AbstractContext.dispatch(AbstractContext.java:84)
	at io.vertx.core.impl.FutureImpl.doDispatch(FutureImpl.java:125)
	at io.vertx.core.impl.FutureImpl.dispatch(FutureImpl.java:119)
	at io.vertx.core.impl.FutureImpl.tryFail(FutureImpl.java:170)
	at io.vertx.core.eventbus.impl.ReplyHandler.fail(ReplyHandler.java:63)
	at io.vertx.core.eventbus.impl.ReplyHandler.lambda$new$0(ReplyHandler.java:42)
	at io.vertx.core.impl.VertxImpl$InternalTimerHandler.handle(VertxImpl.java:922)
	at io.vertx.core.impl.VertxImpl$InternalTimerHandler.handle(VertxImpl.java:889)
	at io.vertx.core.impl.AbstractContext.emit(AbstractContext.java:181)
	at io.vertx.core.impl.AbstractContext.lambda$dispatch$0(AbstractContext.java:84)
	at io.vertx.core.impl.EventLoopContext.schedule(EventLoopContext.java:59)
	at io.vertx.core.impl.AbstractContext.schedule(AbstractContext.java:94)
	at io.vertx.core.impl.AbstractContext.dispatch(AbstractContext.java:84)
	at io.vertx.core.impl.AbstractContext.dispatch(AbstractContext.java:89)
	at io.vertx.core.impl.EventLoopContext.dispatch(EventLoopContext.java:24)
	at io.vertx.core.impl.VertxImpl$InternalTimerHandler.run(VertxImpl.java:912)
	at io.netty.util.concurrent.PromiseTask.runTask(PromiseTask.java:98)
	at io.netty.util.concurrent.ScheduledFutureTask.run(ScheduledFutureTask.java:170)
	at io.netty.util.concurrent.AbstractEventExecutor.safeExecute(AbstractEventExecutor.java:164)
	at io.netty.util.concurrent.SingleThreadEventExecutor.runAllTasks(SingleThreadEventExecutor.java:472)
	at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:500)
	at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:989)
	at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)
	at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)
	at java.lang.Thread.run(Thread.java:748)
Caused by: (TIMEOUT,-1) Timed out after waiting 30000(ms) for a reply. address: __vertx.reply.5b288364-17e3-4393-a54c-ed9b664dd240, repliedAddress: exVert2HealthCheck
	... 20 more
io.reactivex.exceptions.OnErrorNotImplementedException: Timed out after waiting 30000(ms) for a reply. address: __vertx.reply.f984bfda-f551-4882-91e2-fe5500f33e98, repliedAddress: exVert2HealthCheck
	at io.reactivex.internal.functions.Functions$OnErrorMissingConsumer.accept(Functions.java:704)
	at io.reactivex.internal.functions.Functions$OnErrorMissingConsumer.accept(Functions.java:701)
	at io.reactivex.internal.observers.ConsumerSingleObserver.onError(ConsumerSingleObserver.java:47)
	at io.reactivex.internal.operators.single.SingleMap$MapSingleObserver.onError(SingleMap.java:69)
	at io.vertx.reactivex.impl.AsyncResultSingle.lambda$subscribeActual$0(AsyncResultSingle.java:56)
	at io.vertx.reactivex.core.eventbus.EventBus$1.handle(EventBus.java:150)
	at io.vertx.reactivex.core.eventbus.EventBus$1.handle(EventBus.java:145)
	at io.vertx.core.impl.AbstractContext.emit(AbstractContext.java:181)
	at io.vertx.core.impl.AbstractContext.lambda$dispatch$0(AbstractContext.java:84)
	at io.vertx.core.impl.EventLoopContext.schedule(EventLoopContext.java:59)
	at io.vertx.core.impl.AbstractContext.schedule(AbstractContext.java:94)
	at io.vertx.core.impl.AbstractContext.dispatch(AbstractContext.java:84)
	at io.vertx.core.impl.FutureImpl.doDispatch(FutureImpl.java:125)
Exception in thread "vert.x-eventloop-thread-5" io.reactivex.exceptions.OnErrorNotImplementedException: Timed out after waiting 30000(ms) for a reply. address: __vertx.reply.f984bfda-f551-4882-91e2-fe5500f33e98, repliedAddress: exVert2HealthCheck
	at io.reactivex.internal.functions.Functions$OnErrorMissingConsumer.accept(Functions.java:704)
	at io.reactivex.internal.functions.Functions$OnErrorMissingConsumer.accept(Functions.java:701)
	at io.reactivex.internal.observers.ConsumerSingleObserver.onError(ConsumerSingleObserver.java:47)
	at io.reactivex.internal.operators.single.SingleMap$MapSingleObserver.onError(SingleMap.java:69)
	at io.vertx.reactivex.impl.AsyncResultSingle.lambda$subscribeActual$0(AsyncResultSingle.java:56)
	at io.vertx.reactivex.core.eventbus.EventBus$1.handle(EventBus.java:150)
	at io.vertx.reactivex.core.eventbus.EventBus$1.handle(EventBus.java:145)
	at io.vertx.core.impl.AbstractContext.emit(AbstractContext.java:181)
	at io.vertx.core.impl.AbstractContext.lambda$dispatch$0(AbstractContext.java:84)
	at io.vertx.core.impl.EventLoopContext.schedule(EventLoopContext.java:59)
	at io.vertx.core.impl.AbstractContext.schedule(AbstractContext.java:94)
	at io.vertx.core.impl.AbstractContext.dispatch(AbstractContext.java:84)
Recieved message from : exVert2HealthCheck reply address: __vertx.reply.ff7cf7bb-42ac-451e-ae86-c9500afe4cf6 what's my message: ping ExVert2
Received resp from address: __vertx.reply.ff7cf7bb-42ac-451e-ae86-c9500afe4cf6 msg: ping ExVert22
```
	at io.reactivex.internal.functions.Functions$OnErrorMissingConsumer.accept(Functions.java:704)
