(ns web-command.views.common
  (:use noir.core
        hiccup.core
        hiccup.page-helpers))

(def js-list ["jquery-1.4.2.min.js" "jquery.console.js" "web-command.js"])

(defpartial layout [& content]
            (html5
              [:head
               [:title "web-command"]
               (include-css "/css/reset.css")]
              [:body
               [:div#wrapper
                content]]))

(defpartial main-layout [& content]
            (html5
             [:head (include-css "/css/web-command.css")
              (map #(include-js (str "/js/" %)) js-list)]
             [:body
              [:div#wrapper
               [:div#console]
               [:div#results] content]]))


(defpartial build-head []
  [:head
   [:title "remote execute"]
   [:meta {:http-equiv "Content-Type" :content "text/html;charset=UTF-8"}]
   (include-css "/css/main.css")
   (include-js "/js/Debug.js")])

(defpartial box [text id & contents]
  [:div.box
   [:div.tip [:h2 text]]
   (vec (concat [(keyword (str "div#" id ".box-in")) ] (vec contents)))])

(defpartial command-layout []
  (html5
   (build-head)
   [:body
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
     (box "执行结果" "j_cmdResult")]]))






