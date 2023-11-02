(ns lereveilleur.core
  (:require 
    [babashka.process :refer [shell]]
    [babashka.fs :as fs]
    [babashka.http-client :as http]
    [clj-yaml.core :as yaml]
    [clojure.java.io :as io]
    [clojure.string :as str]))

(def videos-data-path "data/videos.yaml")
(def prefix-str "auto_generated__")

(defn- frontmatter-yaml-str
  "Returns the frontmatter in yaml string format given a video entry."
  [video]
  (-> video
      (select-keys [:title :date :description :youtube_id :tags])
      (assoc :article_type "youtube_video")
      (assoc :image "cover.jpg")
      (yaml/generate-string)))

(defn- article-folder-name 
  [{:keys [date title] :as _video}]
  (let [date-str (str/replace date #"-" "_")
        title-str (-> title
                      (str/trim)
                      (str/lower-case)
                      (str/replace #"\s+" "_"))]
    (str prefix-str
         date-str
         "_"
         title-str)))

(defn- article-mardown 
  "Returns a markdown string for the video"
  [video]
  (let [{:keys [youtube_id]} video
        frontmatter-delimiter "---"]
    (str frontmatter-delimiter 
         "\n"
         (frontmatter-yaml-str video)
         frontmatter-delimiter
         "\n\n"
         "{{< youtube " youtube_id " >}}")))

(defn- make-video-post!
  "Creates the actual video post entry. It does the following: 
  - create a directory in content/post/ with the name of the video
  - copy over the image assets
  - create the index.md file with the filled content
  "
  [video]
  (let [directory-name (article-folder-name video)
        path (str "content/post/" directory-name)
        content (article-mardown video)]
    (fs/create-dir path)
    (fs/copy "assets/img/covers/default.jpg" 
             (str path "/cover.jpg"))
    (spit (str path "/index.md") content)))

(comment
  
  ;; asset download
  (let [youtube-id "C_UTlTiVQ_0" 
        url "https://i.ytimg.com/vi/C_UTlTiVQ_0/maxresdefault.jpg"
        path (str "/tmp/" youtube-id ".jpg")]
    (io/copy
      (:body (http/get url {:as :stream}))
      (io/file path))
    (.length (io/file path))))

;; Public API used in babashka tasks

;; TODO
(defn validate-yaml-data-videos!
  "Throws an exception if the yaml data is in the wrong shape."
  []
  (println "Validating yaml data: data/videos.yaml")
  true)
  
(defn generate-video-posts! 
  "Generates all the video posts based on the `videos-data-path"
  []
  (let [videos-data-path "data/videos.yaml"
        {:keys [videos]} (yaml/parse-string (slurp videos-data-path))]
    (doseq [video videos]
      (println (str "Generating post for: " (:title video)))
      (make-video-post! video))))

(defn- clean-video-posts! 
  "Cleans the generated video posts"
  []
  (println "Clearing generated video posts")
  ;; TODO: use the var that contains this path
  ; (shell "bash -c" (str "rm -rf content/post/" prefix-str "*")))
  (shell "bash -c" "rm -rf content/post/auto_generated__*"))
