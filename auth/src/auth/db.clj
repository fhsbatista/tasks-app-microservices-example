(ns auth.db
  (:require [datomic.api :as d]))

(def db-uri "datomic:dev://localhost:4334/todo-auth")

(def schema [{:db/ident       :user/username
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one
              :db/unique      :db.unique/identity}

             {:db/ident       :user/password
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one}])

(defn open-connection! []
  (d/create-database db-uri)
  (d/connect db-uri))

(defn create-schema! [conn]
  (d/transact conn schema))

(defn db [conn]
  (d/db conn))

(let [user {
            :user/username "fhsbatista"
            :user/password "123!@#"
            }]
  (create-schema! (open-connection!))
  (d/transact (open-connection!) [user]))