(ns auth.auth
  (:require [buddy.sign.jwt :as jwt]
            [auth.user :as user]))

(def jwt-secret-key "secret-key")

(defn- valid-password? [username password]
  (let [user (user/find username)
        user-password (:password user)]
    (= password user-password)))

(defn- generate-jwt [user]
  (jwt/sign {:sub user
             :iat (System/currentTimeMillis)}
            jwt-secret-key))

(defn authenticate [username password]
  (if (valid-password? username password)
    (generate-jwt username)
    nil))

