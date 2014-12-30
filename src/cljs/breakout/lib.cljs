(ns breakout.lib
  (:require [breakout.ball :as ball]
            [breakout.bricks :as bricks]
            [breakout.canvas :as canvas]
            [breakout.countdown :as countdown]
            [breakout.paddle :as paddle]
            [enfocus.core :as ef]
            [enfocus.events :as events])
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

;; DOM Selectors
(def game-over "#game-over")
(def game-over-btn (str game-over " " ".btn"))

(defn addReplay []
  (ef/at [game-over-btn] (events/listen :click (fn []
                                                 (hideSlide game-over)
                                                 (preGame)))))

(defn gameOver []
  (js/clearInterval @intervalId)
  (reset! newRound true)
  (showSlide "#game-over")
  (addReplay))

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
        (.dispatchEvent js/document (js/Event. "game-over")))))

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

(defn preGame []
  ;; Draw the initial state of the board so we don't have a white screen
  (drawGame)
  ;; Start the game with a countdown
  (.dispatchEvent js/document (js/Event. "game-countdown")))

(defn gameLoop []
  (drawGame)
  (updateBallPosition!))

(defn startGame []
  (showSlide "#canvas")
  (js/clearInterval @intervalId)
  (let [id (js/setInterval gameLoop 10)]
    (reset! intervalId id)))

(defn showSlide [selector]
  (ef/at [".foreground"] (ef/remove-class "foreground"))
  (ef/at [selector] (ef/add-class "foreground"))
  (ef/at [selector] (ef/set-style :display "block")))

(defn hideSlide [selector]
  (ef/at [selector] (ef/remove-class "foreground"))
  (ef/at [selector] (ef/set-style :display "none")))

(defn addEventListeners! []
  (.addEventListener js/document "game-over"
    (fn [e] (gameOver)) false)
  (.addEventListener js/document "game-start"
    (fn [e] (startGame)) false))

(defn init []
  ;; Add all the games event handlers
  (addEventListeners!)
  (paddle/keyEvents)
  (bricks/events!)
  (ball/events!)
  (countdown/events!)
  (preGame))

