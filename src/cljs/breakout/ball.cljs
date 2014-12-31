(ns breakout.ball
  (:require [breakout.shapes :as shapes]))

(def startingX 130)
(def startingY 150)
(def startingDX 2)
(def startingDY 4)

;; Circle starting coordinates
(def x (atom startingX))
(def y (atom startingY))

(def ballColor "#FFFFFF")

;; Direction coordinates
(def dx (atom startingDX))
(def dy (atom startingDY))

(defn updateBallCoordinates! [x y]
  (reset! x (+ @x @dx))
  (reset! y (+ @y @dy)))

(defn reverseBallDirection! [axis]
  (reset! axis (- @axis)))

(defn draw! [ctx]
  (set! (.-fillStyle ctx) ballColor)
  (shapes/circle ctx @x @y 10))

(defn wallInteraction [width]
  (if (or (> (+ @x @dx) width)
        (< (+ @x @dx) 0))
    (.dispatchEvent js/document (js/CustomEvent. "wall-hit"))))

(defn outOfBounds? []
  (< (+ @y @dy) 0))

(defn inBounds? [height]
  (> (+ @y @dy) height))

(defn resetState! []
  (reset! x startingX)
  (reset! y startingY)
  (reset! dx startingDX)
  (reset! dy startingDY))

(defn events! []
  (.addEventListener js/document "game-over"
    (fn [e] (resetState!)))
  (.addEventListener js/document "wall-hit"
    (fn [e]
      (reverseBallDirection! dx) false))
  (.addEventListener js/document "brick-hit"
    (fn [e]
      (reverseBallDirection! dy) false)))

(defn init []
  (events!))
