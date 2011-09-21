(ns web-command.core
  (:require [pinot.remotes :as remote]
            [pinot.html :as html]
            [clojure.browser.repl :as repl])
  (:require-macros [pinot.macros :as macro]))

(macro/defpartial func-item
  [{:keys [title]}]
  [:li title])

(defn add-funcs
  [display functions]
  (doseq [func functions]
    (html/append-to display (func-item func))))

(macro/remote
 (show-all-func) [functions]
 (when-let [display (html/dom-find "#j_docList")]
   (add-funcs display functions)))
