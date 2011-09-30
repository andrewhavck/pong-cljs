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

(defn pt [x y]
  {:x x :y y})

(defn dim [width height]
  {:width width :height height})

(defn color [fill stroke]
  {:fill fill :stroke stroke})

(defn draw-rect [pt color dim]
 (let [{x :x y :y} pt
       {fill :fill stroke :stroke} color
       {width :width height :height} dim]
  (doto (. g (drawRect))
       (.setSize width height)
       (.setPosition x y)
       (.setFill fill)
       (.setStroke stroke))))

(defn user-move-paddle [event]
  (let [y (- (.clientY event) 38)]
   (pong/move-user-pdl y)))

(defn redraw-rect [data]
  (draw-rect (:oldpt data) (color blackFill blackStroke) (:dim data))
  (draw-rect (:pt data) (color whiteFill blackStroke) (:dim data)))

(pong/register :re-draw
 (fn [data]
   (redraw-rect data)))

(events/listen background events/EventType.MOUSEMOVE user-move-paddle)
(draw-rect (pt 0 0) (color blackFill blackStroke) (dim "100%" "100%"))  ; draw background
(draw-rect (pt "50%" 0) (color whiteFill blackStroke) (dim 5 "100%"))  ; draw net
