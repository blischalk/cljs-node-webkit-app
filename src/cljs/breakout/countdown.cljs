(ns breakout.countdown)


;; Flag to determine if a countdown is in progress
(def inProgress (atom false))

;; Starting countdown value
(def countdownStart 3)

;; Length of a second
(def second 1000)

;; Countdown start times length of a second
(def countdownDuration (* countdownStart second))

;; We want the clear text time to be 1 second longer
;; then the countdown because we want to display GO!!!
(def clearTextTime (* (inc countdownStart) second))


;; State of the countdown
(def countdown (atom countdownStart))


(defn draw! [ctx]
  (set! (.-fillStyle ctx) "#CC0000")
  (set! (.-font ctx) "20px Georgia")
  (.fillText ctx @countdown 200 200))

(defn startTextCycle! []
  (reset! countdown countdownStart)
  (let [intId (js/setInterval (fn [] (reset! countdown
                                       (if (= @countdown 1)
                                         "GO!!!"
                                         (dec @countdown))))
                second)]
    (js/setTimeout (fn [] (js/clearInterval intId)) clearTextTime)))

(defn countdownAndCallback! [cb]
  (reset! inProgress true)
  (js/setTimeout (fn []
                   (reset! inProgress false)
                   (cb))
    countdownDuration))

(defn start! [ctx cb]
  "Pauses the game and sets a timeout until the game unpauses itself.
  Pause occurs at beginning of round to allow player time to get ready.
  Takes a canvas context to draw on and a callback to execute after countdown."
  (if-not @inProgress
    (do (countdownAndCallback! cb)
      (startTextCycle!))))