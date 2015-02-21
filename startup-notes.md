➜  chocolatier git:(dev) lein cljsbuild once 
Compiling ClojureScript.
Retrieving prismatic/dommy/0.1.1/dommy-0.1.1.pom from clojars
Retrieving crate/crate/0.2.3/crate-0.2.3.pom from clojars
Retrieving prismatic/cljs-test/0.0.5/cljs-test-0.0.5.pom from clojars
Retrieving com/cemerick/clojurescript.test/0.3.1/clojurescript.test-0.3.1.pom from central
Retrieving com/cemerick/clojurescript.test/0.3.1/clojurescript.test-0.3.1.jar from central
Retrieving crate/crate/0.2.3/crate-0.2.3.jar from clojars
Retrieving prismatic/cljs-test/0.0.5/cljs-test-0.0.5.jar from clojars
Retrieving prismatic/dommy/0.1.1/dommy-0.1.1.jar from clojars
Compiling "resources/public/scripts/app.js" from ["src/cljs"]...
Successfully compiled "resources/public/scripts/app.js" in 40.034135 seconds.

-> chocolatier git:(dev) lein repl
Read info on 3574 names from file: /Users/zand/dev/docs/clojuredocs-snapshot-latest.txt
Snapshot time: Tue May 07 11:37:33 PDT 2013
nREPL server started on port 59088 on host 127.0.0.1 - nrepl://127.0.0.1:59088
REPL-y 0.3.5, nREPL 0.2.6
Clojure 1.5.1
Java HotSpot(TM) 64-Bit Server VM 1.7.0_17-b02
    Docs: (doc function-name-here)
          (find-doc "part-of-name-here")
  Source: (source function-name-here)
 Javadoc: (javadoc java-object-or-class-here)
    Exit: Control+D or (exit) or (quit)
 Results: Stored in vars *1, *2, *3, an exception in *e


user=> (require '[chocolatier.server :as server])
nil
user=> (server/start-server!)
2015-01-10 09:22:55.723:INFO:oejs.Server:jetty-7.6.8.v20121106
2015-01-10 09:22:55.784:INFO:oejs.AbstractConnector:Started SelectChannelConnector@0.0.0.0:9000
Browser-REPL ready @ http://localhost:59139/4703/repl/start
Type `:cljs/quit` to stop the ClojureScript REPL
#<server$start_server_BANG_$fn__7547 chocolatier.server$start_server_BANG_$fn__7547@228186e2>

cljs.user=> (load-namespace 'chocolatier.engine.core)
nil
cljs.user=> (in-ns 'chocolatier.engine.core)
chocolatier.engine.core
chocolatier.engine.core=> (start-game!)