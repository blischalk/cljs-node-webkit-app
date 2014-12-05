(ns todo.ball)

;; Circle starting coordinates
(def x (atom 130))
(def y (atom 150))

;; Direction coordinates
(def dx (atom 2))
(def dy (atom 4))


(defn updateBallCoordinates! [x y]
  (reset! x (+ @x @dx))
  (reset! y (+ @y @dy)))


(defn reverseBallDirection! [axis]
  (reset! axis (- @axis)))


(defn- circle [ctx x y r]
  (.beginPath ctx)
  (.arc ctx x y r 0 (* js/Math.PI 2) true)
  (.closePath ctx)
  (.fill ctx))


(defn draw! [ctx]
  (circle ctx @x @y 10))
