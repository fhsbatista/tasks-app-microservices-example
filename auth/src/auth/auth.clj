(ns auth.auth)

(defn valid-password? [user password]
  (and (= user "user") (= password "correct-password")))

(defn authenticate [user password]
  (if (valid-password? user password)
    "alkdsjfiqwenfranciusdfasndfsdaf"
    nil))

