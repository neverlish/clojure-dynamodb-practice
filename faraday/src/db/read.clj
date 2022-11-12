(ns db.read
  (:require [taoensso.faraday :as far]
            [db.config :refer [client-opts]]))

(defn get-item
  [table-name kv & opts]
  (far/get-item client-opts table-name kv opts))
  
(defn query
  [table-name key-cond & opts]
  (far/query client-opts table-name key-cond opts))

(defn scan
  [table-name & opts]
  (far/scan client-opts table-name opts))
