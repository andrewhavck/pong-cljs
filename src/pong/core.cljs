(ns pong.core
  (:require [goog.events :as events]))

(def initial-state {:listeners {}
		    :user :pdl1
		    :ai :pdl2
		    :ball {:x 0 :y 0}
		    :pdl1 {:x 0 :y 0}
		    :pdl2 {:x 0 :y 0}
		    :height 0 
		    :width 0})

(def state (atom initial-state))

(defn add-listener
  "Add listener to the game state"
  [state event f]
  (let [l (-> state :listeners event)]
    (assoc-in state [:listeners event] (conj l f))))

(defn register
  "Register a function to be called when the game state changes"
  [event f]
  (swap! state add-listener event f))

(defn send-event
  "Send message to all subscribing event listeners"
  ([event]
     (send-event event nil))
  ([event message]
     (doseq [f (-> @state :listeners event)]
       (f message))))

(defn move [what pt]
  (let [{x :x y :y} pt
	{oldx :x oldy :y} (what @state)]
    (swap! state #(assoc-in % [what] pt))
    (send-event :re-draw {:what what
			  :x x :y y
			  :oldx oldx :oldy oldy})))

(defn move-pdl [who pt]
  (let [{x :x y :y} pt
	what (who @state)]
   (fn [y]
     (move what {:x x :y y}))))

