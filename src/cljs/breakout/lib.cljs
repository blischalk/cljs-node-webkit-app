(ns breakout.lib
  (:require [breakout.ball :as ball]
            [breakout.bricks :as bricks]
            [breakout.canvas :as canvas]
            [breakout.countdown :as countdown]
            [breakout.paddle :as paddle])
  (:require-macros [enfocus.macros :as em]))

;; FIXME: make functions know less about various components of app
;; FIXME: reduce state.  Pass around data structures.  Recursion??

;; brick-hit
;; wall-hit
;; ball-ob
;; game-start
;; game-over

;; IntervalId of setInterval, initialized to 0
(def intervalId (atom 0))

(def newRound (atom true))

(defn gameOver []
  (js/clearInterval @intervalId)
  (reset! newRound true))

(defn updateBallPosition! []
  "Moves the ball around the canvas
  and ends the game if the ball misses the paddle."
  ;; Brick contact
  (bricks/brickInteraction ball/x ball/y)


  ;; If ball is about to go out of
  ;; bounds on x axis, reverse direction
  (ball/wallInteraction canvas/WIDTH)

  ;; If ball is about to go out of bounds
  ;; on y axis, reverse direction
  (if (< (+ @ball/y @ball/dy) 0)
    (ball/reverseBallDirection! ball/dy)
    (if (> (+ @ball/y @ball/dy) canvas/HEIGHT)
      ;; If ball hits the paddle, reverse ball direction
      (if (paddle/ballTouchingPaddle? ball/x paddle/paddlex paddle/paddlew)
        (ball/reverseBallDirection! ball/dy)
        ;; Otherwise ball missed paddle, game over!
        (gameOver))))

  ;; Set ball coordinates to directionality
  ;; derived above
  (ball/updateBallCoordinates! ball/x ball/y))


;; Draw Game
(defn drawGame []
  ;; Clear the canvas
  (canvas/clear!)
  
  ;; Draw ball
  (ball/draw! canvas/ctx)

  ;; Draw paddle
  (paddle/draw! canvas/ctx)

  ;; Draw bricks
  (bricks/draw! canvas/ctx))


(defn gameLoop []
  (drawGame)
  ;; If this is a new round, display a countdown
  (if @newRound
    (do
      (countdown/draw! canvas/ctx)
      (countdown/start! canvas/ctx (fn []
                                     (reset! newRound false)
                                     (updateBallPosition!))))
    (updateBallPosition!)))


;; Start the game drawing on canvas
(defn init []
  (let [id (js/setInterval gameLoop 10)]
    (reset! intervalId id)))

