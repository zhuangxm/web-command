(ns web-command.server
  (:require [noir.server :as server]
            [noir.util.middleware :as middleware]
            [noir.pinot.remotes :as remote]))

(server/load-views "src/web_command/views/")

;(server/add-middleware middleware/wrap-utf-8)

(server/add-middleware remote/wrap-remotes)

(defn -main [& m]
  (let [mode (keyword (or (first m) :dev))
        port (Integer. (get (System/getenv) "PORT" "8080"))]
    (server/start port {:mode mode
                        :ns 'web-command})))

