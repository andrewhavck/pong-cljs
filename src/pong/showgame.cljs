(ns pong.showgame
  (:require
        [pong.core :as pong]
	[goog.graphics :as graphics]
	[goog.dom :as dom]
	[goog.events :as events]))

(def black "#000")
(def white "#fff")

(def blackStroke (graphics/Stroke. 1 black))
(def blackFill (graphics/SolidFill. black))
(def whiteFill (graphics/SolidFill. white))

(def background (dom/getElement "background"))

(def g
  (doto (graphics/createGraphics "100%" "100%")
        (.render background)))

(def ballL 15)
(def pdlWidth 15)
(def pdlHeight 90)
(def pdlPad 30) ; padding from wall

(def canvas-size (. g (getPixelSize)))
(def width (.width canvas-size))
(def height (.height canvas-size))
(def centerX (/ (.width canvas-size) 2))
(def centerY (/ (.height canvas-size) 2))

(def pdl1X pdlPad)
(def pdl2X (- (.width canvas-size) (+ pdlWidth pdlPad)))

(def move-user-pdl (pong/move-pdl :user {:x pdl1X :y centerY}))
(def move-ai-pdl (pong/move-pdl :ai {:x pdl2X :y centerY}))

(defn rect [x y fill stroke width height]
  (fn [x y width height]
    {:x x :y y
     :fill fill :stroke stroke
     :width width :height height}))
   
(def black-rect (rect 0 0 blackFill blackStroke 0 0))
(def white-rect (rect 0 0 whiteFill blackStroke 0 0))

(defn draw-rect [rect]
 (let [{x :x y :y
       fill :fill stroke :stroke
       width :width height :height} rect]
  (doto (. g (drawRect))
       (.setSize width height)
       (.setPosition x y)
       (.setFill fill)
       (.setStroke stroke))))

(defn user-move-paddle [event]
  (let [y (- (.clientY event) 38)]
   (move-user-pdl y)))

(def initialize-user-event-listener 
  (events/listen background events/EventType.MOUSEMOVE user-move-paddle))

(defn redraw-rect [data width height]
  (let [{x :x y :y
	 oldx :oldx
	 oldy :oldy} data]	   
  (draw-rect (black-rect oldx oldy width height))
  (draw-rect (white-rect x y width height))))

(defmulti redraw :what)

(defmethod redraw :ball [data]
 (redraw-rect data ballL ballL))

(defmethod redraw :default [data]
 (redraw-rect data pdlWidth pdlHeight))

(pong/register :re-draw
 (fn [data]
   (redraw data)))

(draw-rect (black-rect 0 0 "100%" "100%"))  ; draw background
(draw-rect (white-rect centerX 0 5 "100%")) ; draw net
(move-user-pdl centerY)
(move-ai-pdl centerY)
