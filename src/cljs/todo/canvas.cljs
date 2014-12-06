(ns todo.canvas
  (:require [todo.shapes :as shapes]
            [enfocus.core :as ef]))

(def background-color "#000000")

;; Dimensions of canvas
(def WIDTH (ef/from "#canvas" (ef/get-attr :width)))
(def HEIGHT (ef/from "#canvas" (ef/get-attr :height)))

;; Canvas context
(def ctx (first (ef/from "#canvas" #(.getContext % "2d"))))



;; Min / Max horizontal (x axis) positions
;; Use Google Closure to find Min offset
(def canvasMinX (.-x (first (ef/from "#canvas" #(js/goog.style.getPageOffset %)))))
(def canvasMaxX (+ canvasMinX WIDTH))


;; Clear Canvas
(defn clear! []
  (.clearRect ctx 0 0 WIDTH HEIGHT)
  (set! (.-fillStyle ctx) background-color)
  (shapes/rect ctx 0 0 WIDTH HEIGHT))