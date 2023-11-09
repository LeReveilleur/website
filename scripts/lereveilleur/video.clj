(ns lereveilleur.video
  (:require
   [babashka.fs :as fs]
   [babashka.http-client :as http]
   [babashka.process :refer [shell]]
   [clj-yaml.core :as yaml]
   [clojure.java.io :as io]
   [clojure.string :as str]))

(def videos-data-path "data/videos.yaml")
(def prefix-str "auto_generated__")
(def video-source-folder "scripts/data/video/source/")

;; -------------------------------
;; Utils to work with bibliography
;; -------------------------------

(defn- make-youtube-id->source-data!
  "Returns a `youtube-id` to a map with the following keys:
  - `dir`: str - directory of the content
  - `bibliography-folder`: str [optional] - directory folder when there are some files in it
  - `index-filename`: str [optional] - the filename of the markdown content
  - `index-markdown`: str [optional] - the content of the markdown
  "
  [video-source-folder]
  (let [source-paths (fs/list-dir video-source-folder)]
    (->> source-paths
         (filter fs/directory?)
         (mapv (fn [dir]
                 (let [bibliography-folder (str dir "/bibliography")
                       index-filename (str dir "/index.md")]
                   [(fs/file-name dir)
                    (cond-> {:dir (str dir)}
                      (and (fs/exists? bibliography-folder)
                           (fs/directory? bibliography-folder))
                      (assoc :bibliography-folder bibliography-folder)

                      (fs/exists? index-filename)
                      (merge {:index-filename index-filename
                              :index-markdown (slurp index-filename)}))])))
         (into {}))))

;; --------------------------------------
;; End of Utils to work with bibliography
;; --------------------------------------

(defn- sanitize
  [str]
  (-> str
      (str/lower-case)
      (str/replace #"['\"\/]" "_")
      (str/replace #"[?!:.#]" "")
      (str/replace #"[áàâã]" "a")
      (str/replace #"[éèêẽ]" "e")
      (str/replace #"[íìîĩ]" "i")
      (str/replace #"[óòôõ]" "o")
      (str/replace #"[úùûũ]" "u")
      (str/replace #"[ç]" "c")
      (str/trim)
      (str/replace #"\s+" "_")))

(defn- frontmatter-yaml-str
  "Returns the frontmatter in yaml string format given a video entry."
  [video]
  (-> video
      (select-keys [:title :date :description :youtube_id :tags :categories])
      ;; TODO: reorganize the sanitizing functions
      (update :slug (fn [slug] (or slug (sanitize (:title video)))))
      (assoc :article_type "youtube_video")
      (assoc :image-header "header.jpg")
      (assoc :video-thumbnail "cover.jpg")
      ;; TODO: deprecate image?
      (assoc :image "cover.jpg")
      (yaml/generate-string)))

(comment
  (str/replace "partie2/2" #"\/" "_"))

(defn- article-folder-name
  [{:keys [date title] :as _video}]
  (let [date-str (str/replace date #"-" "_")
        title-str (sanitize title)]
    (str prefix-str
         date-str
         "_"
         title-str)))

(defn- video-source-content
  [source-markdown]
  (str "<hr>\n\n" source-markdown))

(defn- article-mardown
  "Returns a markdown string for the video. It includes the sources if it 
  exists."
  [{:keys [video source-markdown]}]
  (let [{:keys [youtube_id description]} video
        frontmatter-delimiter "---"
        video-content (str "## Vidéo\n\n"
                           "{{< youtube " youtube_id " >}}"
                           "\n\n"
                           description)
        frontmatter-content (str frontmatter-delimiter "\n"
                                 (frontmatter-yaml-str (dissoc video :description))
                                 frontmatter-delimiter)]
    (cond-> frontmatter-content
      :always (str "\n\n")
      :always (str video-content)
      source-markdown (str "\n\n" (video-source-content source-markdown)))))

(defn- make-video-post!
  "Creates the actual video post entry. It does the following: 
  - create a directory in content/post/ with the name of the video
  - copy over the video thumbnail or use default if not found
  - copy over the bibliography folder that contains assets
  - create the index.md file with the filled content
  "
  [{:keys [video source-data] :as _opt}]
  (let [{:keys [youtube_id]} video
        {:keys [index-markdown bibliography-folder]} source-data
        directory-name (article-folder-name video)
        path (str "content/post/" directory-name)
        content (article-mardown {:video video :source-markdown index-markdown})
        asset-path "assets/img/video_thumbnail/"
        video-thumbnail-filename (str asset-path youtube_id ".jpg")
        thumbnail-default (str asset-path "default.jpg")
        thumb (if (fs/exists? video-thumbnail-filename)
                video-thumbnail-filename
                thumbnail-default)]

    (fs/create-dir path)
    (fs/copy thumb (str path "/cover.jpg"))
    (fs/copy thumbnail-default (str path "/header.jpg"))
    (spit (str path "/index.md") content)

    (when bibliography-folder
      (println "copying bibliography folder")
      (fs/copy-tree bibliography-folder
                    (str path "/bibliography")
                    {:replace-existing true}))))

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

;; ---------------------------------
;; Public API used in babashka tasks
;; ---------------------------------

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

;; TODO: 
(defn validate-video-sources-markdown!
  "Throws an exception if the markdown in scripts/data/video/source is not a properly formed markdown."
  []
  (println "Validating yaml data: scripts/data/video/source/ folder")
  true)

(defn generate-video-posts!
  "Generates all the video posts based on the `videos-data-path"
  []
  (let [{:keys [videos]} (yaml/parse-string (slurp videos-data-path))
        youtube-id->source-data (make-youtube-id->source-data! video-source-folder)]
    (doseq [{:keys [youtube_id title] :as video} videos]
      (println (str "Generating post for: " title))
      (make-video-post! {:video video
                         :source-data (get youtube-id->source-data youtube_id)}))))

(defn clean-video-posts!
  "Cleans the generated video posts"
  []
  (println "Clearing generated video posts")
  ;; TODO: use the var that contains this path
  ; (shell "bash -c" (str "rm -rf content/post/" prefix-str "*")))
  (shell "bash -c" "rm -rf content/post/auto_generated__*"))

(comment
  (clean-video-posts!)
  (generate-video-posts!))
