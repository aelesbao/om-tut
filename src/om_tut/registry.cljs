(ns ^:figwheel-always om-tut.registry
  (:require [om.core :as om :include-macros true]
            [om-tools.dom :as dom :include-macros true]
            [om-tools.core :refer-macros [defcomponent defcomponentmethod]]
            [om-tut.components.editable :as e-com]
            [om-tut.util :as util]))

(defmulti entry-view (fn [person _] (:type person)))

(defcomponentmethod entry-view :student [student _]
  (render [_]
    (dom/li
      (util/display-name student))))

(defcomponentmethod entry-view :professor [professor _]
  (render [_]
    (dom/li
      (dom/div (util/display-name professor))
      (dom/label "Classes")
      (apply dom/ul
             (om/build-all e-com/editable (:classes professor))))))

(defn- people [data]
  (->> data
       :people
       (mapv (fn [x]
               (if (:classes x)
                 (update-in x [:classes]
                            (fn [cs] (mapv (:classes data) cs)))
                 x)))))

(defcomponent registry-view [data _]
  (render [_]
    (dom/div {:id "registry"}
             (dom/h2 "Registry")
             (apply dom/ul
                    (om/build-all entry-view (people data))))))

(defcomponent classes-view [data _]
  (render [_]
    (dom/div {:id "classes"}
             (dom/h2 "Classes")
             (apply dom/ul
                    (om/build-all e-com/editable (vals (:classes data)))))))