(ns auth.auth
  (:require [buddy.sign.jwt :as jwt]
            [auth.user :as user]
            [java-time.api :as time]))

(def jwt-secret-key "secret-key")

(defn expiration []
  (let [current (time/instant)
        expiration (time/plus current (time/seconds 30))
        expiration-millis (inst-ms expiration)
        expiration-seconds (/ expiration-millis 1000)]
    expiration-seconds))

(defn- valid-password? [username password]
  (let [user (user/find username)
        user-password (:password user)]
    (= password user-password)))

(defn- generate-jwt [user]
  (jwt/sign {:username user
             :iat (System/currentTimeMillis)
             :exp (expiration)}
            jwt-secret-key))

(defn authenticate [username password]
  (if (valid-password? username password)
    (generate-jwt username)
    nil))

