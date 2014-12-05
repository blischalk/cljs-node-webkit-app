(ns todo.paddle)


;; Paddle dimensions
(def paddlex (atom (/ WIDTH 2)))
(def paddleh (atom 10))
(def paddlew (atom 75))


;; Draw shapes
(defn rect [x y w h]
  (.beginPath ctx)
  (.rect ctx x y w h)
  (.closePath ctx)
  (.fill ctx))


(defn drawPaddle! []
  ;; move the paddle if right or left is currently pressed
  (if @rightDown
    (reset! paddlex (+ @paddlex 5))
    (if @leftDown
      (reset! paddlex (- @paddlex 5))))

  (rect @paddlex (- HEIGHT @paddleh) @paddlew @paddleh))


(defn ballTouchingPaddle? [x paddlex paddlew]
  (and (> @x @paddlex) (< @x (+ @paddlex @paddlew))))