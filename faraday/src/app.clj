(ns app
  (:require [db.write :refer [put-item batch-write]]
            [db.read :refer [get-item query scan]]))

(comment
  (put-item :music {:artist      "No One You Know"
                    :song-title  "Call Me Today"
                    :album-title "Somewhat Famous"
                    :year        2015
                    :price       2.14
                    :genre       :country
                    :tags        {:composers ["Smith" "Jones" "Davis"]
                                  :length-in-seconds 214}})
  (put-item :music
            {:artist      "No One You Know"
             :song-title  "Call Me Today"
             :album-title "Somewhat Famous"
             :year        2015
             :price       2.14
             :genre       :country
             :tags        {:composers ["Smith" "Jones" "Davis"]
                           :length-in-seconds 214}}
            {:cond-expr "attribute_not_exists(artist) AND attribute_not_exists(#st)"
             :expr-attr-names {"#st" "song-title"}})

  (def batch-request
    {:music {:put [{:artist        "No One You Know"
                    :song-title    "My Dog Spot"
                    :album-title   "Hey Now"
                    :price         1.98
                    :genre         :country
                    :critic-rating 8.4}
                   {:artist        "No One You Know"
                    :song-title    "Somewhere Down The Road"
                    :album-title   "Somewhat Famous"
                    :genre         :country
                    :critic-rating 9.4
                    :year          1984}
                   {:artist         "The Acme Band"
                    :song-title     "Still In Love"
                    :album-title    "The Buck Starts Here"
                    :price          2.47
                    :genre          :rock
                    :promotion-info {:radio-stations-playing ["KHCR" "KBQX" "WTNR" "WJJH"]
                                     :tour-dates             {"Seattle"   "20150625"
                                                              "Cleveland" "20150630"}
                                     :rotation               :heavy}}
                   {:artist      "The Acme Band"
                    :song-title  "Look Out, World"
                    :album-title "The Buck Starts Here"
                    :price       0.99
                    :genre       :rock}]}})
  (batch-write batch-request))
  
   
(comment
  (get-item :music {:artist "No One You Know"
                    :song-title "Call Me Today"})
  (get-item :music {:artist "No One You Know"
                    :song-title "Call Me Today"}
            {:attrs [:album-title :year]})
  (get-item :music
            {:artist     "No One You Know"
             :song-title "Call Me Today"}
            {:proj-expr "#a, #y"
             :expr-attr-names {"#y" "year"
                               "#a" "album-title"}})
  (get-item :music
            {:artist     "No One You Know"
             :song-title "Call Me Today"}
            {:proj-expr "#a, #y, tags.composers[0], tags.#l"
             :expr-attr-names {"#y" "year"
                               "#a" "album-title"
                               "#l" "length-in-seconds"}}))
  
(comment
  (query :music {:artist [:eq "No One You Know"]})
  (query :music {:artist [:eq "The Acme Band"]
                 :song-title [:begins-with "S"]}
         {:return [:song-title]})
  (query :music {:artist [:eq "The Acme Band"]
                 :song-title [:begins-with "S"]}
         {:filter-expr "size(#p.#rs) >= :howmany"
          :proj-expr "#s, #p.rotation"
          :expr-attr-names {"#p" "promotion-info"
                            "#rs" "radio-stations-playing"
                            "#s" "song-title"}
          :expr-attr-vals {":howmany" 4}}))
  
(comment
  (scan :music))    
 
(comment
  (query :music {:genre [:eq :country]}
         {:proj-expr       "#s, price"
          :index           "genre-and-price-index"
          :expr-attr-names {"#s" "song-title"}})
  (query :music {:genre [:eq "country"]
                        :price [:gt 2]}
         {:proj-expr       "#s, price"
          :index           "genre-and-price-index"
          :expr-attr-names {"#s" "song-title"}}))
  
