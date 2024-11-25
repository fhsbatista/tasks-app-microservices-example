(ns auth.user
  (:require [auth.db :as base-db]
            [datomic.api :as datomic]))

(defn find [username]
  (let [users (datomic/q '[:find (pull ?e [*])
                           :in $ ?username
                           :where
                           [?e :user/username ?username]
                           [?e :user/password ?password]]
                         (base-db/db (base-db/open-connection!))
                         username)
        user (ffirst users)]
    {:username (:user/username user)
     :password (:user/password user)}))