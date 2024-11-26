(ns register.routes
  (:require [io.pedestal.http.route :as route]
            [io.pedestal.http :as http]
            [io.pedestal.http.body-params :refer [body-params]]
            [clojure.data.json :as json]
            [register.user :as user]))

(def json-response-interceptor
  {:name ::json-response
   :leave (fn [context]
            (let [response (:response context)
                  headers (merge (:headers response {})
                                 {"Content-Type" "application/json;charset=UTF-8"})]
              (if (:body response)
                (assoc context :response
                               (-> response
                                   (update :body json/write-str)
                                   (assoc :headers headers))))))})

(defn register [context]
  (let [params (:json-params context)
        username (:username params)
        name (:name params)
        email (:email params)
        password (:password params)]
    (user/create username name email password)
    {:status 200 :body {:message "User created!"}}))

(def routes
  (route/expand-routes
    #{["/register" :post [json-response-interceptor (body-params) register] :route-name :register]}))

(def service
  {::http/routes routes
   ::http/type   :jetty
   ::http/port    8082})

(defonce server (atom nil))

(defn start-server []
  (reset! server
          (http/start (http/create-server
                        (assoc service ::http/join? false)))))

(defn stop-server []
  (http/stop @server))

(defn restart-server []
  (stop-server)
  (start-server))

(restart-server)