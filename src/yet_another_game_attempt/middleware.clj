(ns yet-another-game-attempt.middleware
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(defn show-frame-rate [options]
  (let [; retrieve existing draw function or use empty one if not present
        draw (:draw options (fn []))
        ; updated-draw will replace draw
        updated-draw (fn [state]
                       (draw state) ; call user-provided draw function
                       (q/fill 0)
                       (q/text-num (q/current-frame-rate) 10 10))]
    ; set updated-draw as :draw function
    (assoc options :draw updated-draw)))

