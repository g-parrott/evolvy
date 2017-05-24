(ns evolver.core
  (:gen-class))

;; make a thing
(defn thing
  [attack speed defense health]
  {:attack attack :speed speed :defense defense :health health})

;; make a thing that makes a thing
(defn gen-new-thing
  [max-attack max-speed max-defense max-health]
  (thing
    (+ 1 (rand-int max-attack))
    (+ 1 (rand-int max-speed))
    (+ 1 (rand-int max-defense))
    (+ 1 (rand-int max-health))))

;; make a thing that combines two things
;; and make sure it doesn't assume how they're combined
(defn combine-things
  [left-thing right-thing number-combiner]
  (thing
    (number-combiner (:attack left-thing) (:attack right-thing))
    (number-combiner (:speed left-thing) (:speed right-thing))
    (number-combiner (:defense left-thing) (:defense right-thing))
    (number-combiner (:defense left-thing) (:defense right-thing))))

;; make a bunch of random things
(defn make-population
  [n max-attack max-speed max-defense max-health]
  (repeatedly n #(gen-new-thing max-attack max-speed max-defense max-health)))

;; make two things fight a round of some battle or something
(defn compete
  [ {left-thing :left right-thing :right } ]
  (def l-hit (> (:attack left-thing) (* (:speed right-thing) (rand))))
  (def r-hit (> (:attack right-thing) (* (:speed left-thing) (rand))))
  {:left
   (thing (:attack left-thing) (:speed left-thing) (:defense left-thing) (or (and r-hit (- (:health left-thing) (:attack right-thing))) (:health left-thing)))
   :right
   (thing (:attack right-thing) (:speed right-thing) (:defense right-thing) (or (and l-hit (- (:health right-thing) (:attack left-thing))) (:health right-thing)))})

;; make things fight battles until one emerges to be the victor
;; return a map with keys :victor and :loser because it may be
;; wise to remember those who lost as well
(defn compete-to-death
  [{left-thing :left right-thing :right}]
  (loop [l left-thing r right-thing]
        (or
          (and (= 0 (:health l)) {:victor right-thing :loser left-thing})
          (and (= 0 (:health r)) {:victor left-thing :loser right-thing}))
          (do
            (def result (compete {:left l :right r}))
            (recur (:left result) (:right result)))))

;; make one poor thing compete with everything else
;; in order to test its ability to survive
;; returns a map containing the :thing which is the thing
;; :wins which is the nubmer of wins
;; :losses which is the number of losses
(defn compete-with-everything-else
  [poor-thing & everything-else]
  (loop [opponents everything-else wins 0 losses 0]
        (if (empty? opponents)
          {:thing poor-thing :wins wins :losses losses}
          (do
            (def result (compete-to-death {:left poor-thing :right (first opponents)}))
            (def poor-thing-won (= poor-thing (:victor result)))
            (recur
              (rest opponents)
              (or
                (and poor-thing-won (+ wins 1))
                wins)
              (or
                (and (not poor-thing-won) (+ losses 1))
                losses))))))

;; hold the grand tournament of competing things
;; returns a sequence of victors
(defn grand-tournament
  ([n max-attack max-speed max-defense max-health]
   (grand-tournament (make-population n max-attack max-speed max-defense max-health)))
  ([all-things]
  (map (fn [n] (apply compete-with-everything-else (nth all-things) all-things)) (range (count all-things)))))

(defn select-individuals
  []
  )

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
