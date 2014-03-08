(ns chocolatier.engine.tiling
  (:use [chocolatier.utils.logging :only [debug info warn error]])
  (:require [chocolatier.engine.components :refer [Tile Renderable
                                                   BackgroundLayer]]
            [chocolatier.engine.components :as c]
            [chocolatier.engine.state :as s]))


(defn offset
  [m x y]
  (assoc m
    :screen-x (+ (:screen-x m) x)
    :screen-y (+ (:screen-y m) y)))


;; TODO this should set the tiles' screen x and y by translating the
;; TileMap x y coords
(defrecord TileMap [tiles x y]
  Renderable
  (render [this state]
    (let [updated-tiles (map #(c/render % state) (:tiles this))]
      (assoc this :tiles (doall updated-tiles))))

  ;; Apply an offset to the tile map based on player's position
  BackgroundLayer
  (move-layer [this state]
    (let [player (first (filter #(= (:id %) :player)
                                @(:entities state)))
          {:keys [offset-x offset-y]} player
          tiles (:tiles this)
          updated-tiles (map #(offset % offset-x offset-y) tiles)]
      (assoc this :tiles updated-tiles))))

(defrecord BackgroundTile [sprite height width traverse?
                           screen-x screen-y
                           map-x map-y]
  Tile
  (traversable? [this] true)
  
  Renderable
  (render [this state]
    (let [sprite (:sprite this)]
      (if (or (not= (.-position.x sprite) (:screen-x this))
              (not= (.-position.y sprite) (:screen-y this)))
        (do
          (set! (.-position.x sprite) (:screen-x this))
          (set! (.-position.y sprite) (:screen-y this))
          (assoc this :sprite sprite))
        this))))

(defn create-tile! [stage img height width traversable
                    screen-x screen-y
                    map-x map-y]
  ;; (debug "Creating tile" stage img height width x y traversable)
  (let [texture (js/PIXI.Texture.fromImage img)
        sprite (new js/PIXI.TilingSprite texture height width)
        tile (new BackgroundTile sprite height width traversable
                  screen-x screen-y map-x map-y)]
    ;; Initialize the position on the screen
    (set! (.-position.x sprite) screen-x)
    (set! (.-position.y sprite) screen-y)
    (.addChild stage sprite)
    tile))

(defn load-test-tile-map!
  "Create a test tile map of 50 x 50 tiles"
  [stage]
  (let [tiles (for [x (range 0 500 50)
                    y (range 0 500 50)]
                (create-tile! stage "static/images/tile.png" 
                              50 50 true x y x y))]
    (reset! s/tile-map (new TileMap (doall tiles) 0 0))))

(defn load-tile-map!
  "Create a tile map from a hash-map spec.

   {
     :tiles [
       {:width 50 :height 50 :x 0 :y 0 
        :traversable? true 
        :img \"static/images/tile.png\"}
    ]

   }
  "
  [stage map-spec]
  (doseq [{:keys [img height width traversable?
                  screen-x screen-y
                  map-x map-y]} (:tiles map-spec)]
    (create-tile! stage img height width traversable?
                  screen-x screen-y
                  map-x map-y)))
