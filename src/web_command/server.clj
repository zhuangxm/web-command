(ns web-command.server
  (:require [noir.server :as server]
            [noir.util.middleware :as middleware]))

(server/load-views "src/web_command/views/")

(server/add-middleware middleware/wrap-utf-8)

(defn -main [& m]
  (let [mode (keyword (or (first m) :dev))
        port (Integer. (get (System/getenv) "PORT" "8080"))]
    (server/start port {:mode mode
                        :ns 'web-command})))

