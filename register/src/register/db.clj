(ns register.db
  (:require [datomic.api :as d]))

(def db-uri "datomic:dev://localhost:4334/todo-register")

(def schema [{:db/ident       :user/username
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one
              :db/unique      :db.unique/identity}

             {:db/ident       :user/name
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one}

             {:db/ident       :user/email
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one}])

(defn open-connection! []
  (d/create-database db-uri)
  (d/connect db-uri))

(defn create-schema! [conn]
  (d/transact conn schema))

(defn db [conn]
  (d/db conn))