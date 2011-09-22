(ns web-command.views.common
  (:use [noir.core :only (defpartial defpage)]
        [hiccup.core :only (html)]
        [hiccup.page-helpers :only (include-css include-js)])
  (:require [web-command.command :as command]
            [noir.response :as resp]
            [noir.pinot.remotes :as remote]))

(defpartial page-head
  []
  (let [jss ["bootstrap.js"]
        jss (map #(str "/cljs/" %) jss)]
    [:head
     [:title "remote execute"]
     [:meta {:http-equiv "Content-Type" :content "text/html;charset=UTF-8"}]
     (include-css "/css/main.css")
     (map include-js jss)]))

(defpartial command-layout []
  [:body])

(def commands (command/get-commands 'clojure.core))

(defn function-data
  "Returns a sorted seq of func-map"
  [func-map]
  (->> func-map vals (map #(dissoc % :func)) (sort-by :title)))

(defpage "/" []
  (html
   (page-head)
   (command-layout)))

(remote/defremote show-all-func []
  (function-data commands))

(remote/defremote remote-eval [expr]
  (eval (with-in-str expr (read))))
