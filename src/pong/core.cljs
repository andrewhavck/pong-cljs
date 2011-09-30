(ns pong.core
  (:require [goog.events :as events]
	    [goog.dom :as dom]))

(def ballL 4)
(def pdlWidth 16)
(def pdlHeight 112)
(def pdlPad 32) ;padding from wall

(def size (dom/getViewportSize))
(def centerX (/ (.width size) 2))
(def centerY (/ (.height size) 2))

(def pdl1X pdlPad)
(def pdl2X (- (.width size) (+ pdlWidth pdlPad)))

(def initial-state {:listeners {}
			 :user :pdl1
			 :ai :pdl2
			 :ball {:x centerX :y centerY}
			 :pdl1 {:x pdl1X :y centerY}
			 :pdl2 {:x pdl2X :y centerY}
			 :height (.height size)
			 :width (.width size)})

(def state (atom initial-state))

(defn pt [x y]
  {:x x :y y})

(defn dim [width height]
  {:width width :height height})

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

(defn move [what pt dim]
  (let [oldpt (what @state)]
    (swap! state #(assoc % what pt))
    (send-event :re-draw {:pt pt :oldpt oldpt :dim dim})))

(defn move-pdl [who]
  (let [what (who @state)
        {x :x y :y} (what @state)]
    (fn [y]
      (move what (pt x y) (dim pdlWidth pdlHeight)))))

(defn move-ball [pt]
  (move :ball pt (dim ballL ballL)))

(def move-user-pdl (move-pdl :user)) 
(def move-ai-pdl (move-pdl :ai))

