(ns chocolatier.tiling.core
  (:use [chocolatier.utils.logging :only [debug info warn error]])
  (:require [chocolatier.engine.components :refer [Tile UserInput]]
            [chocolatier.engine.state :as s]))


(defrecord BackgroundTile [sprite height width x y traverse?]
  Tile
  (move-by-offset [this offset-x offset-y]
    (let [sprite (:sprite this)
          x (+ (.-position.x sprite) offset-x)
          y (+ (.-position.y sprite) offset-y)]
      (set! (.-position.x sprite) x)
      (set! (.-position.y sprite) y)))
  
  (traversable? [this] true)

  UserInput
  (react-to-user-input [this state time]
    (let [sprite (:sprite this)
          input @(:input state)
          move-rate 1
          move #(condp = %
                  :W (set! (.-position.y sprite)
                           (- (.-position.y sprite) move-rate))
                  :A (set! (.-position.x sprite)
                           (- (.-position.x sprite) move-rate))
                  :S (set! (.-position.y sprite)
                           (+ (.-position.y sprite) move-rate))
                  :D (set! (.-position.x sprite)
                           (+ (.-position.x sprite) move-rate))
                  :& (set! (.-position.y sprite)
                           (- (.-position.y sprite) move-rate))
                  :% (set! (.-position.x sprite)
                           (- (.-position.x sprite) move-rate))
                  ;; Parenths are reserved so wrap it in keyword call
                  (keyword "(") (set! (.-position.y sprite)
                                      (+ (.-position.y sprite) move-rate))
                  :' (set! (.-position.x sprite)
                           (+ (.-position.x sprite) move-rate))
                  ;; Otherwise do nothing
                  nil)]
      (doseq [[k v] input]
        (when (= v "on")
          (move k))))))

(defn create-tile [stage img height width x y traversable]
  (let [texture (js/PIXI.Texture.fromImage img)
        sprite (new js/PIXI.TilingSprite texture height width)
        tile (new BackgroundTile sprite height width x y traversable)]            
    (set! (.-position.x sprite) x)
    (set! (.-position.y sprite) y)
    (.addChild stage sprite)
    (swap! s/tile-map conj tile)))
