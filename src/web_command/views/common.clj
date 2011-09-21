(ns web-command.views.common
  (:use [noir.core]
        [hiccup core page-helpers]))

(defpartial page-head
  []
  (let [jss ["bootstrap.js" "web_command/core.js"]
        jss (map #(str "/cljs/" %) jss)]
    [:head
     [:title "remote execute"]
     [:meta {:http-equiv "Content-Type" :content "text/html;charset=UTF-8"}]
     (include-css "/css/main.css")
     (map include-js jss)]))

(defpartial box [text id & contents]
  [:div.box
   [:div.tip [:h2 text]]
   (vec (concat [(keyword (str "div#" id ".box-in")) ] (vec contents)))])

(defpartial command-layout []
  [:body
   [:div#content]
   [:div.l-nav
    [:div.m-top
     [:h2 "控制台"]]
    [:div.m-list
     [:ul#j_docList
      [:li "command1"]
      [:li "command2"]]]]
   [:div.r-con 
    (box "文档说明" "j_docContent" "这里是文档说明的内容" )
    (box "参数" "j_argList" "这里是命令参数")
    (box "代码执行" "j_editor"
         [:textarea.editor {:rows 10 :name "content"}
          "请输入您的要执行的指令！" ]
         [:div.btn-box
          [:a.btn-cancel {:href "javascript:;"} "清空指令"]
          [:a.btn-run {:href "javascript:;"} "立即执行"]])
    (box "执行结果" "j_cmdResult")]
   (javascript-tag "goog.require('web-command.core')")])
