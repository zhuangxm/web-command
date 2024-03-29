(ns web-command.command)

;;because some incaptiable, commect midje temporary

;;abstract the concept command that can be executed in web
;;web include command string, command doc, command arglists

(defn mk-command 
  "make a command that can be executed
   cmd-str : string of the command
   var-f : the actutal execute function of the command"
  [cmd-str var-f]
  (let [m (meta var-f)]
    {:title cmd-str :command cmd-str :func var-f
     :doc (:doc m) :arglists (str (:arglists m)) }))

(defn func-command
  [cmd]
  "return the function of the command"
  (:func cmd))

(defn str-command
  "return the string of the command"
  [cmd]
  (:command cmd))

(defn doc-command 
  "return the doc of the command"
  [cmd]
  (:doc cmd))

(defn arglists-command 
  "return the arglists of the command"
  [cmd]
  (:arglists cmd))

(defn- var-fn? [x]
  "check whether a var is a function.
   if the var has not been bound, then will throw a exception. so catch it "
  (try (fn? (var-get x))
       (catch Exception e)))

(defn get-commands [ns]
  "get all public functions in the namespace ns
  return a map that key is function-name and value is a command"
  (->> (ns-publics ns)
      (filter #(var-fn? (second %)))
      (map #(vector (str (first %))
                    (mk-command (str (first %)) (second %))))
      (flatten)
      (apply hash-map)))
 
