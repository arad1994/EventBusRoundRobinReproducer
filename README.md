# EventBusRoundRobinReproducer
The main purpose of this project is to act as a reproducer to the https://github.com/eclipse-vertx/vert.x/pull/3636
This project consists of three subfolders ->
  1) # MainVerticle -> This is heart of thr project which is responsible for starting up and deploying two verticles (simple classes) in Jar1Loc and Jar2Loc folders
  The run configuration lies inside this Repo. The entry point is the VertxVert.java class which has a configRetriever set up to look  the localConfig.json file and based of that deploy and undeploy a verticle.
  2) # Jar1Loc -> This Folder contains a simple Verticle written in Java to startup and periodically initate a eventBus communication to another Verticle lying in Jar2Loc
  3) # Jar2Loc -> This Folder contains a simple Verticle written in Java to startup and register it's address on eventBus and respond with an incremental number to any message it recieves.
