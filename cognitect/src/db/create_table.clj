(ns db.create-table
  (:require [cognitect.aws.client.api :as aws]
            [db.client :refer [ddb]]))

(defn create-table
  [table-name {:keys [attribute-definitions key-schemas provisioned-throughput global-secondary-indexes]}]
  (aws/invoke ddb
              {:op      :CreateTable
               :request {:TableName             table-name
                         :AttributeDefinitions  attribute-definitions
                         :KeySchema             key-schemas
                         :GlobalSecondaryIndexes global-secondary-indexes
                         :ProvisionedThroughput provisioned-throughput}}))


(comment
  (->> ["Forum" "Reply" "Thread"]
       (map #(aws/invoke ddb {:op      :DeleteTable
                              :request {:TableName %}}))
       (into []))
  (create-table "Forum"
                {:attribute-definitions[{:AttributeName "Name" :AttributeType "S"}]
                 :key-schemas[{:AttributeName "Name" :KeyType"HASH"}]
                 :provisioned-throughput{:ReadCapacityUnits  1 :WriteCapacityUnits 1}})
  (create-table "Thread"
                {:attribute-definitions[{:AttributeName "ForumName" :AttributeType "S"} {:AttributeName "Subject" :AttributeType "S"}]
                 :key-schemas[{:AttributeName "ForumName" :KeyType"HASH"} {:AttributeName "Subject" :KeyType"RANGE"}]
                 :provisioned-throughput {:ReadCapacityUnits  1 :WriteCapacityUnits 1}})
  (create-table "Reply"
                {:attribute-definitions  [{:AttributeName "Id"
                                           :AttributeType "S"}
                                          {:AttributeName "ReplyDateTime"
                                           :AttributeType "S"}
                                          {:AttributeName "PostedBy"
                                           :AttributeType "S"}
                                          {:AttributeName "Message"
                                           :AttributeType "S"}]
                 :key-schemas [{:AttributeName "Id"
                                :KeyType       "HASH"}
                               {:AttributeName "ReplyDateTime"
                                :KeyType       "RANGE"}]
                 :global-secondary-indexes [{:IndexName             "PostedBy-Message-Index"
                                             :KeySchema             [{:AttributeName "PostedBy"
                                                                      :KeyType       "HASH"}
                                                                     {:AttributeName "Message"
                                                                      :KeyType       "RANGE"}]
                                             :Projection            {:ProjectionType "ALL"}
                                             :ProvisionedThroughput {:ReadCapacityUnits  1
                                                                     :WriteCapacityUnits 1}}]
                 :provisioned-throughput {:ReadCapacityUnits  1
                                          :WriteCapacityUnits 1}}) 

  (->> ["Forum" "Reply" "Thread"]
       (map #(aws/invoke ddb {:op      :DescribeTable
                              :request {:TableName %}
                              :ch      (clojure.core.async/promise-chan (comp
                                                                         (map :Table)
                                                                         (map :TableStatus)))}))
       (into #{})))

  
  
