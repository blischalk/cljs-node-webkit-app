(ns breakout.paddle
  (:require [breakout.canvas :as canvas]
            [breakout.shapes :as shapes]
            [enfocus.core :as ef]
            [enfocus.events :as events]))

;; Paddle dimensions
(def paddlex (atom (/ canvas/WIDTH 2)))
(def paddleh (atom 10))
(def paddlew (atom 75))
(def paddleColor "#FFFFFF")

;; Left Right movement button flags
(def rightDown (atom false))
(def leftDown (atom false))

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
          (> mouseX canvas/canvasMinX)
          (< mouseX canvas/canvasMaxX))
      (reset! paddlex (- mouseX canvas/canvasMinX)))))

;; Attach event handlers
(defn events! []
  (ef/at js/document (events/listen :mousemove #(onMouseMove %)))
  (ef/at js/document (events/listen :keydown #(onKeyDown %)))
  (ef/at js/document (events/listen :keyup #(onKeyUp %)))
  (.addEventListener js/document "draw"
    (fn [e] (draw! (aget e "detail" "canvas")))))

(defn draw! [ctx]
  ;; move the paddle if right or left is currently pressed
  (if @rightDown
    (reset! paddlex (+ @paddlex 5))
    (if @leftDown
      (reset! paddlex (- @paddlex 5))))

  (set! (.-fillStyle ctx) paddleColor)
  (shapes/rect ctx @paddlex (- canvas/HEIGHT @paddleh) @paddlew @paddleh))

(defn ballTouchingPaddle? [x paddlex paddlew]
  (and (> @x @paddlex) (< @x (+ @paddlex @paddlew))))

(defn init []
  (events!))