(ns lereveilleur.core
  (:require 
    [babashka.fs :as fs]
    [babashka.http-client :as http]
    [babashka.process :refer [shell]]
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
      (assoc :image-header "header.jpg")
      (assoc :video-thumbnail "cover.jpg")
      ;; TODO: deprecate image?
      (assoc :image "cover.jpg")
      (yaml/generate-string)))

(defn- sanitize 
  [str]
  (-> str
      (str/lower-case)
      (str/replace #"[?!:.]" "")
      (str/replace #"[áàâ]" "a")
      (str/replace #"[éèê]" "e")
      (str/replace #"[íìî]" "i")
      (str/replace #"[óòô]" "o")
      (str/replace #"[úùû]" "u")
      (str/replace #"[ç]" "c")
      (str/trim)
      (str/replace #"\s+" "_")))

(defn- article-folder-name 
  [{:keys [date title] :as _video}]
  (let [date-str (str/replace date #"-" "_")
        title-str (sanitize title)]
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
  - copy over the video thumbnail or use default if not found
  - create the index.md file with the filled content
  "
  [{:keys [youtube_id] :as video}]
  (let [directory-name (article-folder-name video)
        path (str "content/post/" directory-name)
        content (article-mardown video)
        asset-path "assets/img/video_thumbnail/"
        video-thumbnail-filename (str asset-path youtube_id ".jpg")
        thumbnail-default (str asset-path "default.jpg")
        thumb (if (fs/exists? video-thumbnail-filename) 
                video-thumbnail-filename 
                thumbnail-default)]
    (fs/create-dir path)
    (fs/copy thumb (str path "/cover.jpg"))
    (fs/copy thumbnail-default (str path "/header.jpg"))
    (spit (str path "/index.md") content)))

(comment
  (fs/exists? "assets/img/video_thumbnail/default.jpg")
  (fs/exists? "assets/img/video_thumbnail/default2.jpg"))

(comment
  
  ;; asset download
  (let [youtube-id "C_UTlTiVQ_0" 
        url "https://i.ytimg.com/vi/C_UTlTiVQ_0/maxresdefault.jpg"
        path (str "/tmp/" youtube-id ".jpg")]
    (io/copy
      (:body (http/get url {:as :stream}))
      (io/file path))
    (.length (io/file path))))

(defn- youtube-id->thumbnail-url 
  "Use `yt-dlp` to find the best thumbnail-url for the youtube video."
  [youtube-id]
  (let [{:keys [out]} (shell {:out :string} "yt-dlp" "--list-thumbnails" youtube-id)]
    (re-find #"https://i.ytimg.com.*maxresdefault.jpg" out)))

(defn- download-asset! 
  "Downloads `url` and stores it at `path`."
  [{:keys [url path]}]
  (io/copy
    (:body (http/get url {:as :stream}))
    (io/file path)))

(defn- youtube-thumbnail-path 
  "Returns the path used to store the youtube thumbnail."
  [{:keys [file-format youtube-id]}]
  (str "assets/img/video_thumbnail/" youtube-id "." file-format))

(defn- download-youtube-thumbnail! 
  "Downloads the best thumbnail for the given `youtube-id`."
  [youtube-id]
  (let [thumbnail-url (youtube-id->thumbnail-url youtube-id)
        file-format "jpg"
        path (youtube-thumbnail-path {:file-format "jpg" :youtube-id youtube-id})]
        ; path (str "assets/img/video_thumbnail/" 
        ;           youtube-id "." file-format)]
    (download-asset! {:url thumbnail-url :path path})))

(comment
  (youtube-id->thumbnail-url "rXlEcth5Gxc")
  (download-youtube-thumbnail! "rXlEcth5Gxc")
  (download-youtube-thumbnail! "C_UTlTiVQ_0")
  (download-asset! {:url url :path "assets/img/covers/rXlEcth5Gxc.jpg"})
  (let [youtube-id "rXlEcth5Gxc"
        {:keys [out]} (shell {:out :string} "yt-dlp" "--list-thumbnails" youtube-id)
        url (re-find #"https://i.ytimg.com.*maxresdefault.jpg" out)]
    (def url url)
    (def out out))
  (re-find #"https://i.ytimg.com.*maxresdefault.jpg" out))

;; Public API used in babashka tasks

(defn download-youtube-thumbnails! 
  "Downloads all video thumbnails. 
  If `force` is set to true, then it will download and overwrite the local thumbnails."
  [{:keys [force] :as _opt}]
  (let [{:keys [videos]} (yaml/parse-string (slurp videos-data-path))]
    (println "Downloading youtube video thumbnails")
    (doseq [youtube_id (map :youtube_id videos)]
      (let [path (youtube-thumbnail-path {:file-format "jpg" :youtube-id youtube_id})]
        (if (fs/exists? path)
          (println "youtube_id: " youtube_id " - skipping")
          (do 
            (println "youtube_id: " youtube_id " - downloading...")
            (download-youtube-thumbnail! youtube_id)))))))

;; TODO
(defn validate-yaml-data-videos!
  "Throws an exception if the yaml data is in the wrong shape."
  []
  (println "Validating yaml data: data/videos.yaml")
  true)
  
(defn generate-video-posts! 
  "Generates all the video posts based on the `videos-data-path"
  []
  (let [{:keys [videos]} (yaml/parse-string (slurp videos-data-path))]
    (doseq [video videos]
      (println (str "Generating post for: " (:title video)))
      (make-video-post! video))))

(defn clean-video-posts! 
  "Cleans the generated video posts"
  []
  (println "Clearing generated video posts")
  ;; TODO: use the var that contains this path
  ; (shell "bash -c" (str "rm -rf content/post/" prefix-str "*")))
  (shell "bash -c" "rm -rf content/post/auto_generated__*"))

(defn main []
  (println "HELLO WORLD"))
