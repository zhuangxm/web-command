(ns web-command.views.welcome
  (:use [noir.core]
        [hiccup.core]
        [hiccup.page-helpers])
  (:require [web-command.command :as command]
            [clojure.data.json :as json]
            [web-command.views.common :as common]
            [noir.content.pages :as pages]
            [noir.response :as resp]
            [noir.pinot.remotes :as remote]))

(def commands (command/get-commands 'clojure.core))

(defn function-data
  "Returns a sorted seq of func-map"
  [func-map]
  (->> func-map vals (map #(dissoc % :func)) (sort-by :title)))

(defpage "/" []
  (html
   (common/page-head)
   (common/command-layout)))

(remote/defremote show-all-func []
  (function-data commands))

(remote/defremote remote-eval [expr]
  (eval (with-in-str expr (read))))

(defpage [:post "/getDoc"] {:keys [msgId]}
  {:headers {"Content-Type" "application/json"}
   :body (json/json-str
          [{:msgID "getDoc"
            :data (function-data commands)}])})

(defn execute-expr
  "execute a expression that is expressed like [+ 1 2 3] ,
  the first is a function name of string
  the rest is other parameters"
  [command-map exprs]
  (prn exprs " first : " (first exprs))
  (let [f (command/func-command (command-map (str (first exprs))))
        result (apply f (rest exprs) )
        _ (prn result)]
    result))

(defpage [:post "/eval.json"] {:keys [msgID code]}
  (try
    (let [command-exprs (read-string code)] 
      (resp/json [{:msgID msgID :result (execute-expr commands command-exprs)}]))
    (catch Exception e
      (do (.printStackTrace e)
          (resp/json {:error true
                      :message (str (.getClass e ) " "
                                    (.getMessage e))})))))

