(ns breakout.lib
  (:require [breakout.ball :as ball]
            [breakout.bricks :as bricks]
            [breakout.canvas :as canvas]
            [breakout.countdown :as countdown]
            [breakout.paddle :as paddle]
            [breakout.score :as score]
            [breakout.slide :as slide]
            [enfocus.core :as ef]
            [enfocus.events :as events])
  (:require-macros [enfocus.macros :as em]))

;; FIXME: make functions know less about various components of app
;; FIXME: reduce state.  Pass around data structures.  Recursion??


;; IntervalId of setInterval, initialized to 0
(def intervalId (atom 0))

;; DOM Selectors
(def game-over "#game-over")
(def game-over-btn (str game-over " " ".btn"))

;; Contextually enabled replay functionality
(defn addReplay []
  (ef/at [game-over-btn]
    (events/listen :click (fn []
                            (slide/hide game-over)
                            (preGame)))))

(defn gameOver []
  ;; Stop animation loop
  (js/clearInterval @intervalId)
  ;; Show the game over slide
  (slide/show "#game-over")
  ;; Enable the replay functionality
  (addReplay))


(defn ballLifeCycle []
  
  ;; Brick contact
  (bricks/brickInteraction ball/x ball/y)

  ;; If ball is about to go out of
  ;; bounds on x axis, reverse direction
  (ball/wallInteraction canvas/WIDTH)

  ;; If ball is about to go out of bounds
  ;; on y axis, reverse direction
  (if (ball/outOfBounds?)
    (ball/reverseBallDirection! ball/dy)
    (if (ball/inBounds? canvas/HEIGHT)
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

(defn animationLoop []
  (drawGame)
  (ballLifeCycle))

(defn startGame []
  ;; Bring the canvas to foreground
  (slide/show "#canvas")
  ;; Clear any previous animation loop
  (js/clearInterval @intervalId)
  ;; Start a new animation loop
  (let [id (js/setInterval animationLoop 10)]
    (reset! intervalId id)))


(defn events! []
  (.addEventListener js/document "game-over"
    (fn [e] (gameOver)) false)
  (.addEventListener js/document "game-start"
    (fn [e] (startGame)) false))

(defn init []
  ;; Add all the games event handlers
  (events!)
  (paddle/init)
  (bricks/init)
  (ball/init)
  (countdown/init)
  (score/init)
  (preGame))

