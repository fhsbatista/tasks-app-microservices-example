(ns auth.auth-test
  (:require [clojure.test :refer :all])
  (:require [auth.auth :refer :all]))

(deftest test-valid-password?
  (testing "When password is not valid"
    (is (false? (valid-password? "user" "incorrect-password")))))

(deftest test-authenticate
  (testing "Returns token when password is valid"
    (is (= "alkdsjfiqwenfranciusdfasndfsdaf"
           (authenticate "user" "correct-password")))))