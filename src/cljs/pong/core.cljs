(ns pong.core
  (:require [goog.dom :as dom]
	    [goog.Timer :as timer]
	    [goog.events :as events]))

(def ball-wh 10)
(def pdl-w 16)
(def pdl-h 112)
(def pdl-pad 32) ;padding from wall

(def size (dom/getViewportSize))
(def field-h (.height size))
(def field-w (.width size))

(def net-h field-h)
(def net-w 7)

(def center-x (/ field-w 2))
(def center-y (/ field-h 2))

(def pdl1-x pdl-pad)
(def pdl2-x (- field-w (+ pdl-w pdl-pad)))

(def max-speed 4) 

(defn pt [x y]
  {:x x :y y})

(defn dim [width height]
  {:width width :height height})

(defn vec [vx vy]
  {:vx vx :vy vy})

(def initial-state { :listeners {}
		     :user :pdl1
		     :ai :pdl2
		     :ball {:pt (pt center-x center-y) :vec (vec max-speed 0) :dim (dim ball-wh ball-wh)}
		     :pdl1 {:pt (pt pdl1-x center-y) :vec (vec 0 0) :dim (dim pdl-w pdl-h)}
		     :pdl2 {:pt (pt pdl2-x center-y) :vec (vec 0 0) :dim (dim pdl-w pdl-h)}
		     :net {:pt (pt center-x 0) :dim (dim net-w net-h)}
		     :field {:pt (pt 0 0) :dim (dim field-w field-h)}
		    })

(def state (atom initial-state))

(defn add-listener [state event f]
  (let [l (-> state :listeners event)]
    (assoc-in state [:listeners event] (conj l f))))

(defn register [event f]
  (swap! state add-listener event f))

(defn send-event
  ([event]
     (send-event event nil))
  ([event message]
     (doseq [f (-> @state :listeners event)]
       (f message))))

(defn move [what pt]
  (swap! state #(assoc-in % [what :pt] pt)))

(defn move-pdl [who]
  (let [what (who @state)
        {x :x y :y} (-> @state what :pt)]
    (fn [y]
      (move what (pt x y))))) 

(defn move-ball [pt]
  (move :ball pt)) 

(def move-user-pdl (move-pdl :user)) 
(def move-ai-pdl (move-pdl :ai))

(defn user-move-paddle [event]
  (let [y (- (.clientY event) 38)]
    (move-user-pdl y)))

(defn update-ui []
  (send-event :draw @state))

(defn draw []
  (let [timer (goog.Timer. 500)]
   (do (update-ui)
       (. timer (start))
       (events/listen timer goog.Timer/TICK update-ui))))

(defn start []
  (do (draw)
    (events/listen (dom/getElement "background") events/EventType.MOUSEMOVE user-move-paddle)))

(start)


