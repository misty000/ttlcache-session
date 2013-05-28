(ns ttlcache-session.storage
	"ttl-cache base session storage"
	(:use (ring.middleware.session store))
	(:require [clojure.core.cache :as cache])
	(:import java.util.UUID))

(deftype TTLCacheStore [session-atom]
	SessionStore
	(read-session [_ key]
		(let [session-map (swap! session-atom #(assoc % key (get % key)))] ;reset timeout
			(get session-map key)))
	(write-session [_ key data]
		(let [key (or key (str (UUID/randomUUID)))]
			(swap! session-atom assoc key data)
			key))
	(delete-session [_ key]
		(swap! session-atom dissoc key)
		nil))

(defn- ttl-cache-store* [session-atom]
	(TTLCacheStore. session-atom))

(defn ttl-cache-store
	"Creates an ttl-cache-memory session storage engine.
	ttl: timeout in millis, default 30 minute"
	([]
		(ttl-cache-store (* 30 60 1000)))
	([ttl]
		(println "create session with ttl(millis)" ttl)
		(ttl-cache-store* (atom (cache/ttl-cache-factory {} :ttl ttl)))))