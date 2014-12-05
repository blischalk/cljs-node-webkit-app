(ns todo.events)


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
          (> mouseX canvasMinX)
          (< mouseX canvasMaxX))
      (reset! paddlex (- mouseX canvasMinX)))))


;; Attach event handlers
(defn keyEvents []
  (ef/at js/document (events/listen :mousemove #(onMouseMove %)))
  (ef/at js/document (events/listen :keydown #(onKeyDown %)))
  (ef/at js/document (events/listen :keyup #(onKeyUp %))))