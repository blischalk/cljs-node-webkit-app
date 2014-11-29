(ns todo.core.lib
  (:require [enfocus.core :as ef]
            [enfocus.events :as events])
  (:require-macros [enfocus.macros :as em]))

(def x (atom 140))
(def y (atom 150))
(def dx (atom 2))
(def dy (atom 4))
(def ctx (first (ef/from "#canvas" #(.getContext % "2d"))))
(def intervalId (atom 0))
(def WIDTH (ef/from "#canvas" (ef/get-attr :width)))
(def HEIGHT (ef/from "#canvas" (ef/get-attr :height)))
(def paddlex (atom (/ WIDTH 2)))
(def paddleh (atom 10))
(def paddlew (atom 75))
(def rightDown (atom false))
(def leftDown (atom false))

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


(defn keyEvents []
  (ef/at js/document (events/listen :keydown #(onKeyDown %)))
  (ef/at js/document (events/listen :keyup #(onKeyUp %))))


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

(defn clear []
  (.clearRect ctx 0 0 WIDTH HEIGHT))

(defn draw []
  (clear)
  (circle @x @y 10)
  (rect @paddlex (- HEIGHT @paddleh) @paddlew @paddleh)
  (if (or (> (+ @x @dx) WIDTH) (< (+ @x @dx) 0)) (reset! dx (- @dx)))
  (if (> (+ @y @dy) HEIGHT)
    (if (and (> @x @paddlex) (< @x < (+ @paddlex paddlew)))
      (reset! dy (- @dy))
      ;; Game Over!
      (js/clearInterval @intervalId) ))
  (reset! x (+ @x @dx))
  (reset! y (+ @y @dy)))

(defn init []
  (reset! intervalId (js/setInterval draw, 10)))

