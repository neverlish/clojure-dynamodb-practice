(ns db.delete
  (:require [taoensso.faraday :as far]
            [db.config :refer [client-opts]]))

(defn delete-item
  [table-name conds exprs]
  (far/delete-item client-opts
                   table-name
                   conds
                   updates))
