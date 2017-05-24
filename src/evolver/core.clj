(ns evolver.core
  (:gen-class))

(defn thing
  [attack speed defense health]
  {:attack attack :speed speed :defense defense :health health})

(defn gen-new-thing
  [max-attack max-speed max-defense max-health]
  (thing
    (+ 1 (rand-int max-attack))
    (+ 1 (rand-int max-speed))
    (+ 1 (rand-int max-defense))
    (+ 1 (rand-int max-health))))

(defn combine-things
  [left-thing right-thing number-combiner]
  (thing
    (number-combiner (:attack left-thing) (:attack right-thing))
    (number-combiner (:speed left-thing) (:speed right-thing))
    (number-combiner (:defense left-thing) (:defense right-thing))
    (number-combiner (:defense left-thing) (:defense right-thing))))

(defn make-population
  [n max-attack max-speed max-defense max-health]
  (repeatedly n #(gen-new-thing max-attack max-speed max-defense max-health)))

(defn compete
  [:left left-thing :right right-thing]
  (def l-hit (> (:attack left-thing) (* (:speed right-thing) (rand))))
  (def r-hit (> (:attack right-thing) (* (:speed left-thing) (rand))))
  {:left
   (thing (:attack left-thing) (:speed left-thing) (:defense left-thing) (or (and r-hit (- (:health left-thing) (:attack right-thing))) (:health left-thing)))
   :right
   (thing (:attack right-thing) (:speed right-thing) (:defense right-thing) (or (and l-hit (- (:health right-thing) (:attack left-thing))) (:health right-thing)))})



(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
