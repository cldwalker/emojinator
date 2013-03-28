(ns emojinator.service
    (:require [io.pedestal.service.http :as bootstrap]
              [io.pedestal.service.http.route :as route]
              [io.pedestal.service.http.body-params :as body-params]
              [io.pedestal.service.http.route.definition :refer [defroutes]]
              [io.pedestal.service.interceptor :refer [defon-response]]
              [emoji.core :refer [emoji-response]]
              [ring.util.response :as ring-resp]))

(defn html-response [string]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body string})

(defn emojified-page
  [request]
  (html-response (slurp (-> request :path-params :url))))

(defn home-page
  [request]
  (html-response "<html><body>
<p style=\"font-size: 50px\">
This is a simple, silly app that slurps a url and emojinates any word that matches an emoji name.
</p>
<p style=\"font-size: 50px\">
 For example, to emojinate http://www.nytimes.com/, just add it to the root of this app e.g. <a href=\"/http://www.nytimes.com/\">/http://www.nytimes.com/</a>
</p>
</body></html"))

(defon-response emoji-interceptor
  [response]
  (emoji-response response :wild true))

(defroutes routes
  [[["/" {:get home-page}
     ["/*url"  {:get emojified-page}
     ^:interceptors [emoji-interceptor]]]]])

;; You can use this fn or a per-request fn via io.pedestal.service.http.route/url-for
(def url-for (route/url-for-routes routes))

;; Consumed by emojinator.server/create-server
(def service {:env :prod
              ;; You can bring your own non-default interceptors. Make
              ;; sure you include routing and set it up right for
              ;; dev-mode. If you do, many other keys for configuring
              ;; default interceptors will be ignored.
              ;; :bootstrap/interceptors []
              ::bootstrap/routes routes
              ;; Root for resource interceptor that is available by default.
              ::bootstrap/resource-path "/public"
              ;; Either :jetty or :tomcat (see comments in project.clj
              ;; to enable Tomcat)
              ::bootstrap/type :jetty
              ::bootstrap/port (Integer. (or (System/getenv "PORT") 8080))})
