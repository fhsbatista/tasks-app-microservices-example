(ns auth.routes
  (:require [io.pedestal.http.route :as route]
            [io.pedestal.http :as http]
            [io.pedestal.http.body-params :refer [body-params]]
            [clojure.data.json :as json]
            [auth.auth :as auth]
            [auth.user :as user]))

(def json-response-interceptor
  {:name  ::json-response
   :leave (fn [context]
            (let [response (:response context)
                  headers (merge (:headers response {})
                                 {"Content-Type" "application/json;charset=UTF-8"})]
              (if (:body response)
                (assoc context :response
                               (-> response
                                   (update :body json/write-str)
                                   (assoc :headers headers)))
                context)))})

(defn signin [context]
  (let [params (:json-params context)
        username (:username params)
        password (:password params)
        jwt (auth/authenticate username password)]
    (if (= jwt nil)
      {:status 400 :body {:message "Invalid credentials"}}
      {:status 200 :body {:token jwt}})))

(defn create-password [context]
  (let [params (:json-params context)
        username (:username params)
        password (:password params)]
    (user/set-password username password)
    {:status 200
     :body {:message "Password set successfully"}}))

(def routes
  (route/expand-routes
    #{["/signin" :post [json-response-interceptor (body-params) signin] :route-name :signin]
      ["/create-password" :post [json-response-interceptor (body-params) create-password] :route-name :create-password]}))

(def service
  {::http/routes routes
   ::http/type   :jetty
   ::http/port   8081})

(defonce server (atom nil))

(defn start-server []
  (println "Starting server")
  (reset! server
          (http/start (http/create-server
                        (assoc service ::http/join? false))))
  (println "Server started"))

(defn stop-server []
  (http/stop @server))

(defn restart-server []
  (stop-server)
  (start-server))

;(restart-server)