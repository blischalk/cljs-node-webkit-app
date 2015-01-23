(ns breakout.score
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [breakout.lib-async :as lasync]
            [cljs.core.async :as async
             :refer [>! <! put! chan alts!]]
            [enfocus.core :as ef]
            [goog.events :as events]))


(def brick-worth 100)

(defn updateScore! [score]
  (ef/at ["#score .counter"] (ef/content (.toString score))))

(defn events! []
  (let [scored (lasync/events->chan js/document "brick-hit")
        game-over (lasync/events->chan js/document "game-over")]
    (go
      (loop [score 0]
        (let [[v c] (alts! [scored game-over])
              updated-score (if (= c game-over) 0 (+ score brick-worth))]
          (updateScore! updated-score)
          (recur updated-score))))))

(defn init []
  (events!))
