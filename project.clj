(defproject web-command "0.1.0-SNAPSHOT"
            :description "A general web interface for learn and execute clojure functions"
            :dependencies [[org.clojure/clojure "1.3.0-beta3"]
                           [org.clojure/data.json "0.1.1"]
                           [noir "1.1.1-SNAPSHOT"]
                           [pinot "0.1.0-SNAPSHOT"]]
            :dev-dependencies [[org.clojars.pmbauer/goog.compiler "1.0.0-SNAPSHOT"]
                               [org.clojars.pmbauer/clojurescript "1.0.0-SNAPSHOT"]]
            :main web-command.server)
