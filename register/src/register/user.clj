(ns register.user
  (:require [register.db :as base-db]
            [datomic.api :as datomic]
            [clj-http.client :as http]))

(defn new-user [username name email]
  {:user/username username
   :user/name     name
   :user/email    email})

(defn create [username name email password]
  (let [user (new-user username name email)]
    @(datomic/transact (base-db/open-connection!) [user])
    (http/post "http://localhost:8081/create-user"
                {:content-type :json
                 :form-params {:username username
                             :password password}})))
