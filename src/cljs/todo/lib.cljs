(ns todo.core.lib
  (:require [enfocus.core :as ef]))

(def x (atom 150))
(def y (atom 150))
(def dx (atom 2))
(def dy (atom 4))
(def ctx (first (ef/from "#canvas" #(.getContext % "2d"))))
(def WIDTH (ef/from "#canvas" (ef/get-attr :width)))
(def HEIGHT (ef/from "#canvas" (ef/get-attr :height)))

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
  (if (or (> (+ @x @dx) WIDTH) (< (+ @x @dx) 0)) (reset! dx (- @dx)))
  (if (or (> (+ @y @dy) HEIGHT) (< (+ @y @dy) 0)) (reset! dy (- @dy)))
  (reset! x (+ @x @dx))
  (reset! y (+ @y @dy)))

(defn init []
  (js/setInterval draw, 10))






(comment (defn draw []
           (.clearRect ctx 0, 0, 300, 300)
           (.beginPath ctx)
           (.arc ctx @x, @y, 10, 0, (* Math.PI 2), true)
           (.closePath ctx)
           (.fill ctx)
           (reset! x (+ @x dx))
           (reset! y (+ @y dy))))


(comment
  ;; Playing around with canvas
  (set! (.-fillStyle ctx) "#00A308")
  (.beginPath ctx)
  (.arc ctx 220, 220, 50, 0, (* Math.PI 2), true)
  (.closePath ctx)
  (.fill ctx)

  (set! (.-fillStyle ctx) "#FF1C0A")
  (.beginPath ctx)
  (.arc ctx 100, 100, 100, 0, (* Math.PI 2), true)
  (.closePath ctx)
  (.fill ctx)

  (set! (.-fillStyle ctx) "rgba(255, 255, 0, .5)")
  (.beginPath ctx)
  (.rect ctx 15, 150, 120, 120)
  (.closePath ctx)
  (.fill ctx) )

