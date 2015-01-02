(ns breakout.lib-async
  (:require [cljs.core.async :as async
             :refer [>! <! put! chan alts!]]
            [goog.events :as events]))

(defn events->chan
  "Given a target DOM element and event type return a channel of
  observed events. Can supply the channel to receive events as third
  optional argument."
  ([el event-type] (events->chan el event-type (chan)))
  ([el event-type c]
    (events/listen el event-type
      (fn [e] (put! c e)))
    c))
