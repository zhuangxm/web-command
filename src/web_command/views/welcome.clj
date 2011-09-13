(ns web-command.views.welcome
  (:require [web-command.views.common :as common]
            [noir.content.pages :as pages]
            [noir.response :as resp])
  (:use noir.core
        hiccup.core
        hiccup.page-helpers)
  (:require [web-command.command :as command]
            [clojure.contrib.json :as json]))

(def commands (command/get-commands 'clojure.core))

(defpage "/welcome" []
         (common/main-layout
           [:p "Welcome to web-command"]))

(defpage "/" []
  (common/command-layout))

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

