(ns register.user
  (:require [register.db :as base-db]
            [datomic.api :as datomic]))

(defn new-user [username name email]
  {:user/username username
   :user/name     name
   :user/email    email})

(defn create [username name email password]
  (let [user (new-user username name email)]
    (datomic/transact (base-db/open-connection!) [user])))

