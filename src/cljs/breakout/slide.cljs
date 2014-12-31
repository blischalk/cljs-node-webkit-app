(ns breakout.slide
  (:require [enfocus.core :as ef]))

;; DOM helpers

(defn show [selector]
  (ef/at [".foreground"] (ef/remove-class "foreground"))
  (ef/at [selector] (ef/add-class "foreground"))
  (ef/at [selector] (ef/set-style :display "block")))

(defn hide [selector]
  (ef/at [selector] (ef/remove-class "foreground"))
  (ef/at [selector] (ef/set-style :display "none")))