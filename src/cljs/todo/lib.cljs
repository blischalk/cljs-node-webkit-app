(ns todo.lib
  (:require [enfocus.core :as ef]
            [enfocus.events :as events]
            [todo.ball :as ball]
            [todo.bricks :as bricks])
  (:require-macros [enfocus.macros :as em]))

;; IntervalId of setInterval, initialized to 0
(def intervalId (atom 0))

;; Draw Game
(defn draw []
  ;; Clear the canvas
  (clear!)

  ;; Draw ball
  (ball/draw! ctx)

  ;; Draw paddle
  (drawPaddle!)

  ;; Draw bricks
  (drawBricks!)

  ;; Brick contact
  (brickInteraction ball/x ball/y ball/dy rowheight colwidth bricks)

  ;; If ball is about to go out of
  ;; bounds on x axis, reverse direction
  (if (or (> (+ @ball/x @ball/dx) WIDTH)
          (< (+ @ball/x @ball/dx) 0))
    (ball/reverseBallDirection! ball/dx))

  ;; If ball is about to go out of bounds
  ;; on y axis, reverse direction
  (if (< (+ @ball/y @ball/dy) 0)
    (ball/reverseBallDirection! ball/dy)
    (if (> (+ @ball/y @ball/dy) HEIGHT)
      ;; If ball hits the paddle, reverse ball direction
      (if (ballTouchingPaddle? ball/x paddlex paddlew)
        (ball/reverseBallDirection! ball/dy)
        ;; Otherwise ball missed paddle, game over!
        (js/clearInterval @intervalId))))

  ;; Set ball coordinates to directionality
  ;; derived above
  (ball/updateBallCoordinates! ball/x ball/y))


;; Start the game drawing on canvas
(defn init []
  (reset! intervalId (js/setInterval draw, 10)))

