(ns auth.auth-test
  (:require [clojure.test :refer :all]
            [auth.auth :refer :all]
            [buddy.sign.jwt :as jwt]))

(deftest test-authenticate
  (testing "Returns valid jwt when password is valid"
    (let [secret-key auth.auth/jwt-secret-key
          token (authenticate "user" "correct-password")
          unsigned (jwt/unsign token secret-key)
          unsigned-user (:sub unsigned)]
      (is (= unsigned-user "user")))))