(ns breakout.ball
  (:require [breakout.bricks :as bricks]
            [breakout.canvas :as canvas]
            [breakout.paddle :as paddle]
            [breakout.shapes :as shapes]))

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
  (ballLifeCycle)
  (set! (.-fillStyle ctx) ballColor)
  (shapes/circle ctx @x @y 10))

(defn inBounds? [height]
  (> (+ @y @dy) height))


(defn ballLifeCycle []
  (.dispatchEvent
    js/document
    (js/CustomEvent.
      "ball-movement"
      (js-obj
        "detail"
        (js-obj
          "x"  @x
          "y"  @y
          "dx" @dx
          "dy" @dy))))

  ;; Set ball coordinates
  (updateBallCoordinates! x y))

(defn resetState! []
  (reset! x startingX)
  (reset! y startingY)
  (reset! dx startingDX)
  (reset! dy startingDY))

(defn events! []
  (.addEventListener js/document "draw"
    (fn [e] (draw! (aget e "detail" "canvas"))))
  (.addEventListener js/document "game-over"
    (fn [e] (resetState!)))
  (.addEventListener js/document "wall-hit"
    (fn [e]
      (reverseBallDirection! dx) false))
  (.addEventListener js/document "brick-hit"
    (fn [e]
      (reverseBallDirection! dy) false))
  (.addEventListener js/document "paddle-hit"
    (fn [e]
      (reverseBallDirection! dy) false))
  (.addEventListener js/document "paddle-miss"
    (fn [e]
      (.dispatchEvent js/document (js/Event. "ball-ob"))))
  (.addEventListener js/document "hit-top"
    (fn [e]
      (reverseBallDirection! dy) false)))

(defn init []
  (events!))