(ns breakout.canvas
  (:require [breakout.shapes :as shapes]
            [enfocus.core :as ef]))

(def backgroundColor "#000000")

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
  (set! (.-fillStyle ctx) backgroundColor)
  (shapes/rect ctx 0 0 WIDTH HEIGHT))

(defn wallInteraction? [x dx]
  (or (> (+ x dx) WIDTH) (< (+ x dx) 0)))

(defn hitTop? [y dy]
  (< (+ y dy) 0))

(defn inBounds? [y dy]
  (> (+ y dy) HEIGHT))

(defn events! []
  (.addEventListener
    js/document
    "ball-movement"
    (fn [e]
      (let [x (aget e "detail" "x")
            y (aget e "detail" "y")
            dx (aget e "detail" "dx")
            dy (aget e "detail" "dy")]
        (cond
          (wallInteraction? x dx) (.dispatchEvent
                                    js/document
                                    (js/Event. "wall-hit"))
          (hitTop? y dy) (.dispatchEvent
                           js/document (js/Event. "hit-top"))
          (inBounds? y dy) (.dispatchEvent
                             js/document
                             (js/CustomEvent.
                               "in-bounds"
                               (js-obj "detail" (js-obj
                                                  "x" x
                                                  "y" y
                                                  "dx" dx
                                                  "dy" dy)))))))))

(defn init []
  (events!))