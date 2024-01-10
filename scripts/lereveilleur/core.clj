(ns lereveilleur.core
  (:require [lereveilleur.video :as video]
            [lereveilleur.donors :as donors]))

(def generate-donors-page! donors/generate-donors-page!)
(def download-youtube-thumbnails! video/download-youtube-thumbnails!)
(def validate-yaml-data-videos! video/validate-yaml-data-videos!)
(def validate-video-sources-markdown! video/validate-video-sources-markdown!)
(def generate-video-posts! video/generate-video-posts!)
(def clean-video-posts! video/clean-video-posts!)

(comment
  (validate-yaml-data-videos!)
  (validate-video-sources-markdown!)
  (generate-donors-page!)
  (download-youtube-thumbnails!)
  (clean-video-posts!)
  (generate-video-posts!))
