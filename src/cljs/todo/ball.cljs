(ns todo.ball
  (:require [todo.shapes :as shapes]))

;; Circle starting coordinates
(def x (atom 130))
(def y (atom 150))

(def ball-color "#FFFFFF")

;; Direction coordinates
(def dx (atom 2))
(def dy (atom 4))


(defn updateBallCoordinates! [x y]
  (reset! x (+ @x @dx))
  (reset! y (+ @y @dy)))


(defn reverseBallDirection! [axis]
  (reset! axis (- @axis)))


(defn draw! [ctx]
  (set! (.-fillStyle ctx) ball-color)
  (shapes/circle ctx @x @y 10))
