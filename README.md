# ttlcache-session

a session storage with expire.

## Dependencies

	[ring/ring-core "1.1.8"]
	[org.clojure/core.cache "0.6.3"]

## Usage

- In compojure:

```clojure
(ns ttlcache-session-app.handler
	(:use compojure.core
		  ring.util.response)
	(:require [compojure.handler :as handler]
			  [compojure.route :as route]
			  [ttlcache-session.storage :as storage]))

(defroutes app-routes
	(GET "/" [] "Home")
	(GET "/put" []
		(-> (response "put")
			(assoc :session {:user "misty"})))
	(GET "/get" {{:keys [user]} :session}
		(response (str "get [" user "]")))
	(GET "/del" {:keys [session]}
		(let [session (dissoc session :user )]
			(-> (response "del")
				(assoc :session session))))
	(route/resources "/")
	(route/not-found "Not Found"))

(def app
	(handler/site app-routes {:session {:store (storage/ttl-cache-store)}}))
	; or (storage/ttl-cache-store 3600000)
```

- In lib-noir

	TODO

## License

Copyright © 2013 misty000

Distributed under the Eclipse Public License, the same as Clojure.