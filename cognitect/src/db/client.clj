(ns db.client
  (:require [cognitect.aws.client.api :as aws]))

(def ddb (aws/client {:api :dynamodb
                      :endpoint-override {:protocol :http
                                          :hostname "localhost"
                                          :port     8000}}))
