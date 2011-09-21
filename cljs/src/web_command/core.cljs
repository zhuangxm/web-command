(ns web-command.core
  (:require [pinot.remotes :as remote]
            [pinot.html :as html]
            [pinot.html.tags :as tag]
            [pinot.events :as pm]
            [goog.events :as events])
  (:require-macros [pinot.macros :as macro]))

;;Store all the server functions' data
(def all-functions (atom {}))

(defn log
  [& args]
  (.log js/console (apply str args)))

(macro/defpartial func-item
  [{:keys [title]}]
  [:li {:id title} (tag/link-to "#" title)])

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

;; Query the server to get all the functions
(macro/remote
 (show-all-func) [functions]
 (log "Function count: " (count functions))
 (reset! all-functions (functions->map functions))
 (when-let [display (html/dom-find "#j_docList")]
   (add-funcs display functions)))
