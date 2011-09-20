(defproject web-command "0.1.0-SNAPSHOT"
            :description "A general web interface for learn and execute clojure functions"
            :dependencies [[org.clojure/clojure "1.2.1"]
                           [noir "1.1.0"]
                           [pinot "0.1.0-SNAPSHOT"]
			   [midje "1.2.0"]]
            :main web-command.server)
