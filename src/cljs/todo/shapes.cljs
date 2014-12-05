(ns todo.shapes)

;; Draw shapes
(defn rect [ctx x y w h]
  (.beginPath ctx)
  (.rect ctx x y w h)
  (.closePath ctx)
  (.fill ctx))

(defn circle [ctx x y r]
  (.beginPath ctx)
  (.arc ctx x y r 0 (* js/Math.PI 2) true)
  (.closePath ctx)
  (.fill ctx))