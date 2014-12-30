(ns breakout.countdown
  (:require [enfocus.core :as ef]))

;; Starting countdown value
(def countdownStart 3)

;; Length of a second
(def second 1000)

(defn countdownText [content]
  (ef/at ["#countdown"] (ef/content (.toString content))))

(defn countdown [num]
  (js/setTimeout
    (fn []
      (countdownText num)
      (if-not (= 0 num)
        (countdown (dec num))
        (do
          (countdownText "")
          (.dispatchEvent js/document
            (js/Event. "game-start")))))
    second))

(defn runCountdown [countdownInt]
  ;; Initialize the countdown with the first number
  (countdownText countdownInt)
  ;; Start the recursive countdown
  (countdown (dec countdownInt)))

(defn events! []
  (.addEventListener js/document "game-countdown"
    (fn [e] (runCountdown countdownStart) false)))