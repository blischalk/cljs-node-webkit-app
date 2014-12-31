(ns breakout.score
  (:require [enfocus.core :as ef]))

(def score (atom 0))

(def brick-worth 100)

(defn resetState! []
  (reset! score 0))

(defn updateScore! []
  (reset! score (+ brick-worth @score))
  (ef/at ["#score .counter"] (ef/content (.toString @score))))

(defn events! []
  (.addEventListener js/document "brick-hit"
    (fn [e] (updateScore!)) false) )

(defn init []
  (events!))