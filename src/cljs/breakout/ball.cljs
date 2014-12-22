(ns breakout.ball
  (:require [breakout.shapes :as shapes]))

;; Circle starting coordinates
(def x (atom 130))
(def y (atom 150))

(def ballColor "#FFFFFF")

;; Direction coordinates
(def dx (atom 3))
(def dy (atom 6))


(defn updateBallCoordinates! [x y]
  (reset! x (+ @x @dx))
  (reset! y (+ @y @dy)))


(defn reverseBallDirection! [axis]
  (reset! axis (- @axis)))


(defn draw! [ctx]
  (set! (.-fillStyle ctx) ballColor)
  (shapes/circle ctx @x @y 10))
