(ns db.write
  (:require [cognitect.aws.client.api :as aws]
            [db.client :refer [ddb]]))

(defn batch-write
  [requests]
  (aws/invoke ddb {:op :BatchWriteItem
                   :request {:RequestItems requests}}))

(comment
  (let [xform-specified-keys
        (fn [k]
          (get (reduce (fn [a v] (assoc a v (keyword v)))
                       {}
                       ["B" "BOOL" "BS" "Item" "L" "M" "N" "NS" "NULL" "PutRequest" "S" "SS"])
               k
               k))]
    (->> ["Forum.json" "Reply.json" "Thread.json"]
         (map #(-> (clojure.java.io/file "resources" "dynamodb" %)
                   slurp
                   (clojure.data.json/read-str :key-fn xform-specified-keys)))
         (map batch-write))))
