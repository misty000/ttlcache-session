(ns ttlcache-session.core-test
	(:use ring.util.response
		  [ring.middleware.session store]
		  [ttlcache-session storage])
	(:require [clojure.test :refer :all ]
			  [ring.mock.request :as mock]))

(deftest a-test
	(testing "read-session"
		(let [store (ttl-cache-store 500)]
			(write-session store "session-key" {:user "user"})
			(let [{:keys [user] :as session} (read-session store "session-key")]
				(is session)
				(is (= user "user")))
			(Thread/sleep 400)
			(let [{:keys [user] :as session} (read-session store "session-key")]
				(is session)
				(is (= user "user")))
			(Thread/sleep 400)
			(let [{:keys [user] :as session} (read-session store "session-key")]
				(is session)
				(is (= user "user")))
			(Thread/sleep 550)
			(let [session (read-session store "session-key")]
				(is (nil? session)))))
	(testing "delete-session"
		(let [store (ttl-cache-store)]
			(write-session store "session-key" {:user "user"})
			(let [{:keys [user] :as session} (read-session store "session-key")]
				(is session)
				(is (= user "user")))
			(delete-session store "session-key")
			(let [session (read-session store "session-key")]
				(is (nil? session))))))