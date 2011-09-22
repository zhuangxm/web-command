(ns web-command.core
  (:require [pinot.remotes :as remote]
            [pinot.html :as html]
            [goog.dom :as dom]
            [pinot.html.tags :as tag]
            [pinot.events :as pm]
            [goog.events :as events])
  (:require-macros [pinot.macros :as macro]))

(defn log
  [& args]
  (.log js/console (apply str args)))

(macro/defpartial box [text id & contents]
  [:div.box
   [:div.tip [:h2 text]]
   (vec (concat [(keyword (str "div#" id ".box-in")) ] (vec contents)))])

;; Content layout
(macro/defpartial command-layout []
  [:div#content
   [:div.l-nav
    [:div.m-top
     [:h2 "控制台"]]
    [:div.m-list
     [:ul#j_docList]]]
   [:div.r-con 
    (box "文档说明" "j_docContent" "这里是文档说明的内容" )
    (box "参数" "j_argList" "这里是命令参数")
    (box "代码执行" "j_editor"
         [:textarea.editor {:rows 10 :name "content"}
          "请输入您的要执行的指令！" ]
         [:div.btn-box
          [:a.btn-cancel {:href "javascript:;"} "清空指令"]
          [:a.btn-run {:href "javascript:;"} "立即执行"]])
    (box "执行结果" "j_cmdResult")]])

;;Store all the server functions' data
(def all-functions (atom {}))

(macro/defpartial func-item
  [{:keys [title]}]
  [:li {:id title} (tag/link-to "#" title)])

(defn clear-command-pane
  []
  (when-let [command-pane (html/dom-find "textarea.editor")]
    (html/val command-pane "")))

(defn find-set
  "Find a dom element, and set its text to content"
  [name content]
  (when-let [label (html/dom-find name)]
    (html/text label content)))

(defn item-clicked
  "When a function name clicked, it refresh the right pane."
  [item event]
  (let [id (html/attr item :id)]
    (log "item id: " id)
    (when-let [func (@all-functions id)]
      (find-set "#j_docContent" (:doc func))
      (find-set "#j_argList" (:arglists func)))))

(defn add-funcs
  "Display function descriptors to page"
  [display functions]
  (doseq [func functions
          :let [item (func-item func)]]
    (html/append-to display item)
    (pm/on item :click item-clicked)))

(defn functions->map
  "Convert function data returned from the server info a name->data map"
  [functions]
  (into {}
        (for [{title :title :as func} functions]
          [title func])))

(defn setup-events
  "Setup all the events of the page"
  []
  (pm/on (html/dom-find "a.btn-cancel") :click
         clear-command-pane))

(defn start-app []
  (html/append-to (html/dom-find "body") (command-layout))
  (setup-events)

  ;; Query the server to get all the functions
  (macro/remote
   (show-all-func) [functions]
   (log "Function count: " (count functions))
   (reset! all-functions (functions->map functions))
   (when-let [display (html/dom-find "#j_docList")]
     (add-funcs display functions))))

(let [document (dom/getDocument)]
  (events/listen
   document
   goog.events.EventType/READYSTATECHANGE
   (fn []
     (when (== "complete" (.readyState document))
       (start-app)))))
