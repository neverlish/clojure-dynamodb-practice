(ns db.update
  (:require [taoensso.faraday :as far]
            [db.config :refer [client-opts]]))

(defn update-item
  [table-name conds updates]
  (far/update-item client-opts
                   table-name
                   conds
                   updates))

(comment
  (update-item :music
               {:artist     "No One You Know"
                :song-title "Call Me Today"}
               {:update-expr "SET label = :label"
                :expr-attr-vals {":label" "Global Records"}
                :return :all-new})
  
  (update-item :music
               {:artist     "No One You Know"
                :song-title "Call Me Today"}
               {:update-expr    "SET price = :price REMOVE tags.composers[2]"
                :expr-attr-vals {":price" 0.89}
                :return         :all-new})
  
  (update-item :music
               {:artist     "No One You Know"
                :song-title "Call Me Today"}
               {:update-expr    "SET label = :label"
                :expr-attr-vals {":label" "New Wave Recordings"}
                :cond-expr      "attribute_not_exists(label)"
                :return         :all-new})
  
  (update-item :music
               {:artist     "No One You Know"
                :song-title "Call Me Today"}
               {:update-expr    "SET plays = :val"
                :expr-attr-vals {":val" 0}
                :return         :updated-new})
  
  (update-item :music
               {:artist     "No One You Know"
                :song-title "Call Me Today"}
               {:update-expr    "SET plays = plays + :incr"
                :expr-attr-vals {":incr" 1}
                :return         :updated-new}))
  
  
