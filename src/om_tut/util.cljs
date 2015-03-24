(ns om-tut.util
  (:require [om.core :as om]))

(defn middle-name [{:keys [middle middle-initial]}]
  (cond
    middle (str " " middle)
    middle-initial (str " " middle-initial ".")))

(defn display-name [{:keys [first last] :as contact}]
  (str last ", " first (middle-name contact)))

(defn display [show]
  (if show
    #js {}
    #js {:display "none"}))