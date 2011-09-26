# web-command

a web application that user can use web to execute commands that have been exported.
A web site written in noir and pinot (for clojurescript). 

## Developement

Since clojurescript is still in pre-alpha phase, there is no mature
solution for integrate cljs development into normal clojure
project.

 1. follow [clojurescript QuickStart](https://github.com/clojure/clojurescript/wiki/Quick-Start)
 to setup clojurescript environment.
 1. follow [cljs-watch](https://github.com/ibdknox/cljs-watch) to
 setup cljs-watch to have the cljs compiled automatically.

After cljs was compiled, call leiningen to run the server.

```bash
lein deps
lein run
```

open http://localhost:8080 to try.

## License

Copyright (C) 2011 FIXME

Distributed under the Eclipse Public License, the same as Clojure.

