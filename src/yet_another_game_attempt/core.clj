(ns yet-another-game-attempt.core
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [yet-another-game-attempt.middleware :refer [show-frame-rate]]))

(defn setup []
  ; Set frame rate to 30 frames per second.
  (q/frame-rate 30)
  ; Set color mode to HSB (HSV) instead of default RGB.
  (q/color-mode :hsb)
  ; setup function returns initial state. It contains
  ; circle color and position.
  {:world {:elements '()}
   :time (q/millis)})

(defn apply-gravity [delta element]
  (update-in element [1] inc))

(defn update-world [delta world]
  (update-in world [:elements] #(map (partial apply-gravity delta) %1)))

(defn update-state [{last-update :time world :world :as state}]
  (let [now (q/millis)
        delta (- now last-update) ]
    (assoc state :time now
                 :world (update-world delta world))))

(defn draw-circle [[x y]]
  (q/fill 255)
  (q/ellipse x y 100 100))

(defn draw-state [{{elements :elements} :world :as state}]
  ; Clear the sketch by filling it with light-grey color.
  (q/background 240)
  (doall (map draw-circle (reverse elements))))

(defn mouse-pressed [state {button :button x :x y :y :as event}]
  (if (= button :left)
    (update-in state [:world :elements] #(conj %1 (vector x y)))
    state))

(q/defsketch yet-another-game-attempt
  :title "You spin my circle right round"
  :size [500 500]
  ; setup function called only once, during sketch initialization.
  :setup setup
  ; update-state is called on each iteration before draw-state.
  :update update-state
  :draw draw-state
  :mouse-pressed mouse-pressed
  :features [:keep-on-top :resizable]
  :display 2
  ; This sketch uses functional-mode middleware.
  ; Check quil wiki for more info about middlewares and particularly
  ; fun-mode.
  :middleware [m/fun-mode show-frame-rate])
