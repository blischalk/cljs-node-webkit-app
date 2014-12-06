(ns breakout.lib
  (:require [breakout.ball :as ball]
            [breakout.bricks :as bricks]
            [breakout.canvas :as canvas]
            [breakout.paddle :as paddle])
  (:require-macros [enfocus.macros :as em]))

;; FIXME: make functions know less about various components of app
;; FIXME: reduce state.  Pass around data structures.  Recursion??

;; IntervalId of setInterval, initialized to 0
(def intervalId (atom 0))


;; Draw Game
(defn draw []
  ;; Clear the canvas
  (canvas/clear!)

  ;; Draw ball
  (ball/draw! canvas/ctx)

  ;; Draw paddle
  (paddle/draw! canvas/ctx)

  ;; Draw bricks
  (bricks/draw! canvas/ctx)

  ;; Brick contact
  (bricks/brickInteraction ball/x
                           ball/y
                           #(ball/reverseBallDirection! ball/dy))


  ;; If ball is about to go out of
  ;; bounds on x axis, reverse direction
  (if (or (> (+ @ball/x @ball/dx) canvas/WIDTH)
        (< (+ @ball/x @ball/dx) 0))
    (ball/reverseBallDirection! ball/dx))

  ;; If ball is about to go out of bounds
  ;; on y axis, reverse direction
  (if (< (+ @ball/y @ball/dy) 0)
    (ball/reverseBallDirection! ball/dy)
    (if (> (+ @ball/y @ball/dy) canvas/HEIGHT)
      ;; If ball hits the paddle, reverse ball direction
      (if (paddle/ballTouchingPaddle? ball/x paddle/paddlex paddle/paddlew)
        (ball/reverseBallDirection! ball/dy)
        ;; Otherwise ball missed paddle, game over!
        (js/clearInterval @intervalId))))

  ;; Set ball coordinates to directionality
  ;; derived above
  (ball/updateBallCoordinates! ball/x ball/y))


;; Start the game drawing on canvas
(defn init []
  (reset! intervalId (js/setInterval draw, 10)))

