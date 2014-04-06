(ns chocolatier.engine.watchers
  (:use [chocolatier.utils.logging :only [debug info warn error]])
  (:require [clojure.set :as set]))


(defn record->hashmap [r]
  (into {} (seq r)))

(defn map-difference
  "Get the difference between hashmaps
   http://stackoverflow.com/questions/3387155/difference-between-two-maps

   m1 is the new val m2 is the old val"
  [m1 m2]
  (let [ks1 (set (keys m1))
        ks2 (set (keys m2))
        ks1-ks2 (set/difference ks1 ks2)
        ks2-ks1 (set/difference ks2 ks1)
        ks1*ks2 (set/intersection ks1 ks2)]
    (merge (select-keys m1 ks1-ks2)
           (select-keys m2 ks2-ks1)
           (select-keys m1
                        (remove (fn [k] (= (m1 k) (m2 k)))
                                ks1*ks2)))))

(defn hashmap-watcher
  "Log any differences between two hashmaps"
  [key state old-val new-val]
  (when (not= old-val new-val)
    (debug "State changed" key (map-difference new-val old-val))))

(defn entity-watcher
  "Log any differences between two lists of entities by their :id"
  [key state old-val new-val]
  ;; Only continue when there is a difference between the old and new values
  (when (not= old-val new-val)
    (let [combined (reduce conj old-val new-val)
          grouped (group-by :id combined)]
      ;; For each entity id log the difference between the new and the old
      (doseq [[id coll] (seq grouped)]
        (when id
          ;; We need to convert records to hashmaps here so that
          ;; set operation work in map-difference
          (let [[v1 v2] (map record->hashmap coll)
                diff (map-difference v2 v1)]
            (when-not (empty? diff)
              (debug "State changed" id diff))))))))

