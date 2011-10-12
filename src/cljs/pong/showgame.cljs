(ns pong.showgame
  (:require
        [pong.core :as pong]
	[goog.graphics :as graphics]
	[goog.dom :as dom]))

(def black "#000")
(def white "#fff")
(def blackStroke (graphics/Stroke. 1 black))
(def whiteStroke (graphics/Stroke. 1 white))
(def blackFill (graphics/SolidFill. black))
(def whiteFill (graphics/SolidFill. white))

(def g
  (doto (graphics/createGraphics "100%" "100%")
        (.render (dom/getElement "background"))))

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

(defn draw [obj color]
  (draw-rect (:pt obj) color (:dim obj)))

(defn redraw [old new]
  (draw old (color blackFill blackStroke))
  (draw new (color whiteFill blackStroke)))

(def state (atom nil))

(pong/register :draw
 (fn [data]
   (doseq [x '(:ball :pdl1 :pdl2)]
     (cond
      (nil? @state) (draw (data x) (color whiteFill blackStroke))
      :else (redraw (@state x) (data x))))
     (draw (:net data) (color whiteFill whiteStroke))
     (reset! state data)))

;make this better
(draw-rect (pong/pt 0 0) (color blackFill blackStroke) (pong/dim "100%" "100%"))
