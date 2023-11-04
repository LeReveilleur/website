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
(def video-source-folder "scripts/data/video/source/")
(def donors-filename "scripts/data/donors.txt")
(def donors-content-index-page  "content/page/donors/index.md")
(def frontmatter-delimiter "---")

;; -------------------------
;; Utils to work with donors
;; -------------------------

(defn- current-date []
  (.format
   (java.text.SimpleDateFormat. "yyyy-MM-dd")
   (java.util.Date.)))

(comment
  (def donors
    (str/split-lines (slurp donors-filename))))

(comment
  (filter (fn [donor] (re-find #"(?i)^[^a-z]" donor)) donors))

(defn- greedy-regex-match
  [range-regexes s]
  (if (empty? range-regexes)
    :unmatched
    (if (re-find (:regex (first range-regexes)) s)
      (:bucket-range (first range-regexes))
      (greedy-regex-match (rest range-regexes) s))))

(defn- bucket-range->markdown-content
  [{:keys [bucket-range donors]}]
  (let [title (if (= bucket-range :unmatched)
                (str "## Autres [" (count donors) "]")
                (let [[s e] bucket-range]
                  (str "## " (str/upper-case s) " à " (str/upper-case e) " [" (count donors) "]")))
        section-content (->> donors
                             (mapv (fn [donor] (str "- " donor "\n")))
                             (reduce str ""))]
    (str title "\n"
         section-content "\n")))

(defn donors-frontmatter [donors]
  (str frontmatter-delimiter "\n"
       "title: \"Remerciements\"\n"
       "description: \"\"\n"
       (str "date: \"" (current-date) "\"\n")
       "slug: \"remerciements\"\n"
       frontmatter-delimiter "\n"))

(defn- donors-markdown-content
  [donors]
  (let [exclusive-ranges [["a" "d"]
                          ["e" "h"]
                          ["i" "l"]
                          ["m" "p"]
                          ["q" "t"]
                          ["u" "z"]]
        bucket-regexes (mapv (fn [[s e]]
                               {:bucket-range [s e]
                                :regex (re-pattern (str "(?i)^[" s "-" e "]"))})
                             exclusive-ranges)
        bucket-range->donors (group-by (partial greedy-regex-match bucket-regexes) donors)]
    (->> (into exclusive-ranges [:unmatched])
         (mapv (fn [bucket-range] (bucket-range->markdown-content
                                   {:bucket-range bucket-range
                                    :donors (get bucket-range->donors bucket-range)})))
         (reduce str))))

(comment
  (donors-markdown-content donors))

(comment
  (let [sorted-donors (->> donors
                           (remove empty?)
                           (sort))]
    [(count donors)
     (count sorted-donors)])
  (let [exclusive-ranges [["a" "d"]
                          ["e" "h"]
                          ["i" "l"]
                          ["m" "p"]
                          ["q" "t"]
                          ["u" "z"]]
        bucket-regexes (mapv (fn [[s e]]
                               {:range [s e]
                                :regex (re-pattern (str "(?i)^[" s "-" e "]"))})
                             exclusive-ranges)
        bucket-range->donors (group-by (partial greedy-regex-match bucket-regexes) donors)]
    (->> (into exclusive-ranges [:unmatched])
         (mapv (fn [bucket-range] (bucket-range->markdown-content
                                   {:bucket-range bucket-range
                                    :donors (get bucket-range->donors bucket-range)})))
         (reduce str))
    ; bucket-range

    ; (map (fn [[r xs]] [r (count xs)]) bucket-range->donors)
    ; (->> bucket-regexes
    ;      (mapv (fn [bucket-regex])))
    ; (->> donors 
    ;      (filter (fn [donor] (re-find regex donor)))
    ;      (count))
    #_(->> donors
           (map (fn [donor] [donor (re-find (first bucket-predicates) donor)]))
           (drop 10)
           (take 10))))

;; -------------------------------
;; Utils to work with bibliography
;; -------------------------------

(defn- source-path->youtube-id
  "Given a `source-path`, returns the `youtube-id` string."
  [path]
  (let [regex (re-pattern (str video-source-folder "(.*).md"))]
    (->> path
         (str)
         (re-find regex)
         (second))))

(defn- source-path->markdown!
  "Given a `source-path` returns a `markdown` content."
  [path]
  (slurp (str path)))

(defn- make-youtube-id->markdown!
  "Returns a youtube-id to markdown content mapping."
  [video-source-folder]
  (let [source-paths (fs/list-dir video-source-folder)]
    (->> source-paths
         (filter (complement fs/directory?))
         (mapv (fn [source-path] [(source-path->youtube-id source-path)
                                  (source-path->markdown! source-path)]))
         (into {}))))

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
         ; first
         ; second
         ; :index-markdown)))

         ; (mapv (fn [source-path] [(source-path->youtube-id source-path)
         ;                          (source-path->markdown! source-path)]))
         ; (into {}))))

;; --------------------------------------
;; End of Utils to work with bibliography
;; --------------------------------------

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
      (str/replace #"['\"]" "_")
      (str/replace #"[?!:.]" "")
      (str/replace #"[áàâã]" "a")
      (str/replace #"[éèêẽ]" "e")
      (str/replace #"[íìîĩ]" "i")
      (str/replace #"[óòôõ]" "o")
      (str/replace #"[úùûũ]" "u")
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

(comment
  (clean-video-posts!)
  (generate-video-posts!))

(comment
  (get {:a 1} :a)
  (get {:a 1} :b))

(defn clean-video-posts!
  "Cleans the generated video posts"
  []
  (println "Clearing generated video posts")
  ;; TODO: use the var that contains this path
  ; (shell "bash -c" (str "rm -rf content/post/" prefix-str "*")))
  (shell "bash -c" "rm -rf content/post/auto_generated__*"))

(comment
  (re-pattern (str video-source-folder "(.*).md")))

(defn generate-donors-page!
  "Generates the donors page that contains all the donators listed with their nicknames."
  []
  (println "Generating the donors page based on the file:" donors-filename)
  (let [donors (->> donors-filename
                    (slurp)
                    (str/split-lines)
                    (remove empty?)
                    sort
                    (into []))
        frontmatter (donors-frontmatter donors)
        markdown-content (donors-markdown-content donors)
        content (str frontmatter
                     "Ce contenu existe et est accessible gratuitement grâce au soutien financier d'une partie de la communauté. Je remercie l'ensemble des donateurs listés ci-dessous :\n"
                     markdown-content)]
    (spit donors-content-index-page content)))

(comment
  (generate-donors-page!))

(comment
  (fs/list-dir "scripts/data/video/source/")
  (source-path->youtube-id (first (fs/list-dir "scripts/data/video/source/")))
  (source-path->markdown! (first (fs/list-dir "scripts/data/video/source/")))
  (def youtube-id->markdown (make-youtube-id->markdown! video-source-folder))
  youtube-id->markdown
  (->> (fs/list-dir "scripts/data/video/source/")
       first
       str
       (re-find #"scripts/data/video/source/(.*).md"))
  (println "test"))
