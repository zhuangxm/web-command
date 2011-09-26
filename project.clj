(defproject web-command "0.1.0-SNAPSHOT"
            :description "A general web interface for execute clojure functions"
            :dependencies [[org.clojure/clojure "1.3.0"]
                           [noir "1.1.1-SNAPSHOT"]
                           [pinot "0.1.0-SNAPSHOT"]]
            :main web-command.server
            :source-path "src/clj")
