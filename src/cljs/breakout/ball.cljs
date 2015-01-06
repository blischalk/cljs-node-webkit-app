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

(defn wallInteraction [width]
  (if (or (> (+ @x @dx) width)
        (< (+ @x @dx) 0))
    (.dispatchEvent js/document (js/CustomEvent. "wall-hit"))))

(defn outOfBounds? []
  (< (+ @y @dy) 0))

(defn inBounds? [height]
  (> (+ @y @dy) height))


(defn ballLifeCycle []

  ;; Brick contact
  (bricks/brickInteraction x y)

  ;; If ball is about to go out of
  ;; bounds on x axis, reverse direction
  (wallInteraction canvas/WIDTH)

  ;; If ball is about to go out of bounds
  ;; on y axis, reverse direction
  (if (outOfBounds?)
    (reverseBallDirection! dy)
    (if (inBounds? canvas/HEIGHT)
      ;; If ball hits the paddle, reverse ball direction
      (if (paddle/ballTouchingPaddle? x paddle/paddlex paddle/paddlew)
        (reverseBallDirection! dy)
        ;; Otherwise ball missed paddle, game over!
        (.dispatchEvent js/document (js/Event. "ball-ob")))))

  ;; Set ball coordinates to directionality
  ;; derived above
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
      (reverseBallDirection! dy) false)))

(defn init []
  (events!))