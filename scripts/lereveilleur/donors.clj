(ns lereveilleur.donors
  (:require [clojure.string :as str]))

(def donors-filename "scripts/data/donors.txt")
(def donors-content-index-page "content/page/donors/index.md")
(def frontmatter-delimiter "---")

(defn- current-date
  []
  (.format (java.text.SimpleDateFormat. "yyyy-MM-dd") (java.util.Date.)))

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
                (str "## Autres")
                (let [[s e] bucket-range]
                  (str "## " (str/upper-case s) " à " (str/upper-case e))))
        section-content (->> donors
                             (mapv (fn [donor] (str "- " donor "\n")))
                             (reduce str ""))]
    (str title "\n" section-content "\n")))

(defn donors-frontmatter
  [donors]
  (str frontmatter-delimiter
       "\n"
       "title: \"Remerciements\"\n"
       "description: \"\"\n"
       (str "date: \"" (current-date) "\"\n")
       "hideLastModified: true\n"
       "slug: \"remerciements\"\n"
       frontmatter-delimiter
       "\n"))

(defn- donors-markdown-content
  [donors]
  (let [exclusive-ranges [["a" "d"] ["e" "h"] ["i" "l"] ["m" "p"] ["q" "t"]
                          ["u" "z"]]
        bucket-regexes (mapv (fn [[s e]]
                               {:bucket-range [s e],
                                :regex (re-pattern (str "(?i)^[" s "-" e "]"))})
                             exclusive-ranges)
        bucket-range->donors
        (group-by (partial greedy-regex-match bucket-regexes) donors)]
    (->> (into exclusive-ranges [:unmatched])
         (mapv (fn [bucket-range]
                 (bucket-range->markdown-content
                  {:bucket-range bucket-range,
                   :donors (get bucket-range->donors bucket-range)})))
         (reduce str))))

(comment
  (donors-markdown-content donors))

(comment
  (let [sorted-donors (->> donors
                           (remove empty?)
                           (sort))]
    [(count donors) (count sorted-donors)])
  (let [exclusive-ranges [["a" "d"] ["e" "h"] ["i" "l"] ["m" "p"] ["q" "t"]
                          ["u" "z"]]
        bucket-regexes (mapv (fn [[s e]]
                               {:range [s e],
                                :regex (re-pattern (str "(?i)^[" s "-" e "]"))})
                             exclusive-ranges)
        bucket-range->donors
        (group-by (partial greedy-regex-match bucket-regexes) donors)]
    (->> (into exclusive-ranges [:unmatched])
         (mapv (fn [bucket-range]
                 (bucket-range->markdown-content
                  {:bucket-range bucket-range,
                   :donors (get bucket-range->donors bucket-range)})))
         (reduce str))))

(defn generate-donors-page!
  "Generates the donors page that contains all the donators listed with their nicknames."
  []
  (println "Generating the donors page based on the file:" donors-filename)
  (let
   [donors (->> donors-filename
                (slurp)
                (str/split-lines)
                (remove empty?)
                sort
                (into []))
    frontmatter (donors-frontmatter donors)
    markdown-content (donors-markdown-content donors)
    content
    (str
     frontmatter
     "Ce contenu existe et est accessible gratuitement grâce au soutien financier d'une partie de la communauté. Je remercie l'ensemble des donateurs listés ci-dessous :\n"
     markdown-content)]
    (spit donors-content-index-page content)))
