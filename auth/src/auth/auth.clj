(ns auth.auth
  (:require [buddy.sign.jwt :as jwt]))

(def jwt-secret-key "secret-key")

(defn- valid-password? [user password]
  (and (= user "user")
       (= password "correct-password")))

(defn- generate-jwt [user]
  (jwt/sign {:sub user
             :iat (System/currentTimeMillis)}
            jwt-secret-key))

(defn authenticate [user password]
  (if (valid-password? user password)
    (generate-jwt user)
    nil))

