(ns web-command.views.common
  (:use [noir.core]
        [hiccup core page-helpers]))

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


