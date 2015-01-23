(ns breakout.ball
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [breakout.bricks :as bricks]
            [breakout.canvas :as canvas]
            [breakout.lib-async :as lasync]
            [breakout.paddle :as paddle]
            [breakout.shapes :as shapes]
            [cljs.core.async :as async
             :refer [>! <! put! chan alts!]]
            [enfocus.core :as ef]
            [goog.events :as events]))

(def startingX 130)
(def startingY 150)
(def startingDX 2)
(def startingDY 4)

(def ballColor "#FFFFFF")

#_(defn updateBallCoordinates! [x y]
  (reset! x (+ @x @dx))
  (reset! y (+ @y @dy)))

#_(defn reverseBallDirection! [axis]
  (reset! axis (- @axis)))

(defn draw! [ctx x y dx dy]
  (.dispatchEvent
    js/document
    (js/CustomEvent.
      "ball-movement"
      (js-obj
        "detail"
        (js-obj
          "x"  x
          "y"  y
          "dx" dx
          "dy" dy))))

  (set! (.-fillStyle ctx) ballColor)
  (shapes/circle ctx x y 10))

(defn inBounds? [y dy height]
  (> (+ y dy) height))

(defn events! []
  (let [draw (lasync/events->chan js/document "draw")
        hit-wall (lasync/events->chan js/document "wall-hit")
        hit-brick (lasync/events->chan js/document "brick-hit")
        hit-paddle (lasync/events->chan js/document "paddle-hit")
        hit-top (lasync/events->chan js/document "hit-top")
        miss (lasync/events->chan js/document "paddle-miss")
        game-over (lasync/events->chan js/document "game-over")]
    (go
      (loop [x startingX
             y startingY
             dx startingDX
             dy startingDY]
        (let [[v c] (alts! [draw
                            hit-wall
                            hit-brick
                            hit-paddle
                            hit-top
                            game-over])]
          (cond
            ;; Change the X axis direction under these circumstances
            (or
              (= c hit-brick)
              (= c hit-paddle)
              (= c hit-top)) (recur x y dx (- dy))

            ;; Change the Y axis direction
            (= c hit-wall) (recur x y (- dx) dy)

            ;; When game is over, pass starting values into the loop
            (= c game-over) (recur
                              startingX
                              startingY
                              startingDX
                              startingDY)
            ;; When the paddle is miss, we need to let the system know
            ;; the ball is now ob.
            (= c miss) (do (.dispatchEvent js/document (js/Event. "ball-ob"))
                           (recur x y dx dy))
            (= c draw) (do
                         (let [new-x (+ x dx)
                               new-y (+ y dy)]
                           (draw!
                             (aget v "event_" "detail" "canvas")
                             new-x
                             new-y
                             dx
                             dy)
                           (recur new-x new-y dx dy)))))))))

(defn init []
  (events!))
