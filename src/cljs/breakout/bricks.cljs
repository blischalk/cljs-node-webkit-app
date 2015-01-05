(ns breakout.bricks
  (:require [breakout.canvas :as canvas]
            [breakout.shapes :as shapes]
            [enfocus.core :as ef]
            [enfocus.events :as events]))

;; Bricks
(def NROWS 5)
(def NCOLS 5)
(def BRICKWIDTH (- (/ canvas/WIDTH NCOLS) 1))
(def BRICKHEIGHT 15)
(def PADDING 1)
(def rowheight (+ BRICKHEIGHT PADDING))
(def colwidth (+ BRICKWIDTH PADDING))

(def rowcolors ["#FF1C0A" "#FFFD0A" "#00A308" "#0008DB" "#EB0093"])

(def startingBricks (mapv (fn [_] (mapv (fn [_] 1)
                                  (range 0 NCOLS)))
                    (range 0 NROWS)))

;; Create an associative array of bricks
;; [[1 1 1 1 1]
;;  [1 1 1 1 1]
;;  [1 1 1 1 1]
;;  [1 1 1 1 1]
;;  [1 1 1 1 1]]
(def bricks (atom startingBricks))

(defn draw! [ctx]
  (doseq [[rowindex row] (map vector
                           (iterate inc 0)
                           @bricks)
          [eleindex ele] (map vector
                           (iterate inc 0)
                           row)]

    (set! (.-fillStyle ctx) (rowcolors eleindex))
    (if (= 1 ele)
      (shapes/rect
        ctx
        (+ (* rowindex (+ BRICKWIDTH PADDING)) PADDING)
        (+ (* eleindex (+ BRICKHEIGHT PADDING)) PADDING)
        BRICKWIDTH
        BRICKHEIGHT))))

(defn brickInteraction [x y]
  (let [row (js/Math.floor (/ @y rowheight))
        col (js/Math.floor (/ @x colwidth))]
    (if (brickImpact? row col @bricks @y)
      (.dispatchEvent
        js/document
        (js/CustomEvent.
          "brick-hit"
          (js-obj "detail" (js-obj "row" row "col" col)))))))

(defn resetState! []
  (reset! bricks startingBricks))

(defn events! []
  (.addEventListener js/document "game-over"
    (fn [e] (resetState!)))
  (.addEventListener js/document "draw"
    (fn [e] (draw! (aget e "detail" "canvas"))))
  (.addEventListener js/document "brick-hit"
    (fn [e]
      (removeBrick!
        (aget e "detail" "row")
        (aget e "detail" "col"))) false))

(defn brickImpact? [row col bricks y]
  (let [row (js/parseInt row)
        col (js/parseInt col)]
    (and (< y (* NROWS rowheight))
      (>= row 0)
      (>= col 0)
      (= 1 (get-in bricks [col row])))))

(defn removeBrick! [row col]
  (let [row (js/parseInt row)
        col (js/parseInt col)]
    (swap! bricks update-in [col row] (fn [_] 0))))

(defn init []
  (events!))