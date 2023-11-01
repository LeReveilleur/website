#!/usr/bin/env bb

(require '[babashka.http-client :as http])
(require '[clojure.java.io :as io])

(comment
  (let [youtube-id "C_UTlTiVQ_0" 
        url "https://i.ytimg.com/vi/C_UTlTiVQ_0/maxresdefault.jpg"
        path (str "/tmp/" youtube-id ".jpg")]
    (io/copy
      (:body (http/get url {:as :stream}))
      (io/file path))
    (.length (io/file path))))

(println "hello world")
