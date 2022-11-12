(ns db.write
  (:require [taoensso.faraday :as far]
            [db.config :refer [client-opts]]))

(defn put-item
  [table-name data & opts]
  (far/put-item client-opts table-name data opts))

(defn batch-write
  [batch-request]
  (far/batch-write-item client-opts batch-request))
