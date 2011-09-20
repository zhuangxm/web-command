(ns web-command.views.welcome
  (:require [web-command.views.common :as common]
            [noir.content.pages :as pages]
            [noir.response :as resp])
  (:use noir.core
        hiccup.core
        hiccup.page-helpers)
  (:require [web-command.command :as command]
            [clojure.contrib.json :as json]))

;;define the commands we support.
;;TODO find a better to define the support commands list
(def commands (command/get-commands 'clojure.core))

(defpage "/" []
  (common/command-layout))

;;get the command list
(defpage [:post "/getDoc"] {:keys [msgId]}
  {:headers {"Content-Type" "application/json"}
   :body (json/json-str
          [{:msgID "getDoc"
            :data (sort-by #(:title %)
                           (map #(dissoc % :func) (vals commands)))}])})

(defn execute-expr
  "execute a expression that is expressed like [+ 1 2 3] ,
  the first is a function name of string
  the rest is other parameters"
  [command-map exprs]
  (let [f (command/func-command (command-map (str (first exprs))))
        result (apply f (rest exprs) )]
    result))

;;execute command
;;url /eval.json?msgID=*1&code=*2
(defpage [:post "/eval.json"] {:keys [msgID code]}
  (try
    (let [command-exprs (read-string code)] 
      (resp/json [{:msgID msgID :result (execute-expr commands command-exprs)}]))
    (catch Exception e
      (do (.printStackTrace e)
          (resp/json {:error true
                      :message (str (.getClass e ) " "
                                    (.getMessage e))})))))

