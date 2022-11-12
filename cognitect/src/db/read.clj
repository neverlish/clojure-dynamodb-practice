(ns db.read
  (:require [cognitect.aws.client.api :as aws]
            [db.client :refer [ddb]])) 

(defn query
  [table-name {:keys [key-cond index-name expression-attribute-values]}]
  (aws/invoke ddb {:op :Query
                   :request {:TableName table-name 
                             :IndexName index-name
                             :KeyConditionExpression key-cond
                             :ExpressionAttributeValues expression-attribute-values}}))

(comment
  (aws/invoke ddb {:op :ListTables})
  
  (query "Reply" {:key-cond "Id = :id"
                  :expression-attribute-values {":id" {:S "Amazon DynamoDB#DynamoDB Thread 1"}}})
  (query "Reply" {:key-cond "PostedBy = :user"
                  :index-name "PostedBy-Message-Index"
                  :expression-attribute-values {":user" {:S "User A"}}}))
  
  
