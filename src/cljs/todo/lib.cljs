(ns todo.core.lib
  (:require [enfocus.core :as ef]
            [enfocus.events :as events])
  (:require-macros [enfocus.macros :as em]))

;; Circle starting coordinates
(def x (atom 130))
(def y (atom 150))

;; Direction coordinates
(def dx (atom 2))
(def dy (atom 4))

;; Canvas context
(def ctx (first (ef/from "#canvas" #(.getContext % "2d"))))

;; IntervalId of setInterval, initialized to 0
(def intervalId (atom 0))

;; Dimensions of canvas
(def WIDTH (ef/from "#canvas" (ef/get-attr :width)))
(def HEIGHT (ef/from "#canvas" (ef/get-attr :height)))

;; Paddle dimensions
(def paddlex (atom (/ WIDTH 2)))
(def paddleh (atom 10))
(def paddlew (atom 75))

;; Left Right movement button flags
(def rightDown (atom false))
(def leftDown (atom false))

;; Min / Max horizontal (x axis) positions
;; Use Google Closure to find Min offset
(def canvasMinX (.-x (first (ef/from "#canvas" #(js/goog.style.getPageOffset %)))))
(def canvasMaxX (+ canvasMinX WIDTH))


;; Bricks
(def NROWS 5)
(def NCOLS 5)
(def BRICKWIDTH (- (/ WIDTH NCOLS) 1))
(def BRICKHEIGHT 15)
(def PADDING 1)

;; Create an associative array of bricks
;; [[1 1 1 1 1]
;;  [1 1 1 1 1]
;;  [1 1 1 1 1]
;;  [1 1 1 1 1]
;;  [1 1 1 1 1]]
(def bricks (atom (mapv (fn [_] (mapv (fn [_] 1)
                                  (range 0 NCOLS)))
                    (range 0 NROWS))))


;; Event handlers
(defn onKeyDown [evt]
  (if (= 39 (js/parseInt (.-keyCode evt)))
    (reset! rightDown true))

  (if (= 37 (js/parseInt (.-keyCode evt)))
    (reset! leftDown true)))


(defn onKeyUp [evt]
  (if (= 39 (js/parseInt (.-keyCode evt)))
    (reset! rightDown false))

  (if (= 37 (js/parseInt (.-keyCode evt)))
    (reset! leftDown false)))


(defn onMouseMove [evt]
  (let [mouseX (js/parseInt (.-clientX evt))]
    (if (and
          (> mouseX canvasMinX)
          (< mouseX canvasMaxX))
      (reset! paddlex (- mouseX canvasMinX)))))


;; Attach event handlers
(defn keyEvents []
  (ef/at js/document (events/listen :mousemove #(onMouseMove %)))
  (ef/at js/document (events/listen :keydown #(onKeyDown %)))
  (ef/at js/document (events/listen :keyup #(onKeyUp %))))


;; Draw shapes
(defn circle [x y r]
  (.beginPath ctx)
  (.arc ctx x y r 0 (* Math.PI 2) true)
  (.closePath ctx)
  (.fill ctx))


(defn rect [x y w h]
  (.beginPath ctx)
  (.rect ctx x y w h)
  (.closePath ctx)
  (.fill ctx))


;; Clear Canvas
(defn clear []
  (.clearRect ctx 0 0 WIDTH HEIGHT))


(defn drawBricks! []
  (doseq [[rowindex row] (map vector
                           (iterate inc 0)
                           @bricks)
          [eleindex ele] (map vector
                           (iterate inc 0)
                           row)]

    (if (= 1 ele)
      (rect
        (+ (* rowindex (+ BRICKWIDTH PADDING)) PADDING)
        (+ (* eleindex (+ BRICKHEIGHT PADDING)) PADDING)
        BRICKWIDTH
        BRICKHEIGHT))))


(defn drawPaddle! []
  ;; move the paddle if right or left is currently pressed
  (if @rightDown
    (reset! paddlex (+ @paddlex 5))
    (if @leftDown
      (reset! paddlex (- @paddlex 5))))

  (rect @paddlex (- HEIGHT @paddleh) @paddlew @paddleh))


(defn ballTouchingPaddle? []
  (and (> @x @paddlex) (< @x (+ @paddlex @paddlew))))


(defn reverseBallYDirection! []
  (reset! dy (- @dy)))


(defn reverseBallXDirection! []
  (reset! dx (- @dx)))


(defn updateBallCoordinates! []
  (reset! x (+ @x @dx))
  (reset! y (+ @y @dy)))


;; Draw Game
(defn draw []
  ;; Clear the canvas
  (clear)

  ;; Draw ball
  (circle @x @y 10)

  ;; Draw paddle
  (drawPaddle!)

  ;; Draw bricks
  (drawBricks!)

  ;; If ball is about to go out of
  ;; bounds on x axis, reverse direction
  (if (or (> (+ @x @dx) WIDTH)
          (< (+ @x @dx) 0))
    (reverseBallXDirection!))

  ;; If ball is about to go out of bounds
  ;; on y axis, reverse direction
  (if (< (+ @y @dy) 0)
    (reverseBallYDirection!)
    (if (> (+ @y @dy) HEIGHT)
      ;; If ball hits the paddle, reverse ball direction
      (if (ballTouchingPaddle?)
        (reverseBallYDirection!)
        ;; Otherwise ball missed paddle, game over!
        (js/clearInterval @intervalId))))

  ;; Set ball coordinates to directionality
  ;; derived above
  (updateBallCoordinates!))


;; Start the game drawing on canvas
(defn init []
  (reset! intervalId (js/setInterval draw, 10)))

