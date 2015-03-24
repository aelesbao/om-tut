(ns ^:figwheel-always om-tut.registry
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [om-tut.util :as util]))

(defn- student-view [student _]
  (reify
    om/IRender
    (render [_]
      (dom/li nil (util/display-name student)))))

(defn- professor-view [professor _]
  (reify
    om/IRender
    (render [_]
      (dom/li nil
              (dom/div nil (util/display-name professor))
              (dom/label nil "Classes")
              (apply dom/ul nil
                     (map #(dom/li nil %) (:classes professor)))))))

(defmulti entry-view (fn [person _] (:type person)))
(defmethod entry-view :student [person owner] (student-view person owner))
(defmethod entry-view :professor [person owner] (professor-view person owner))

(defn- people [data]
  (->> data
       :people
       (mapv (fn [x]
               (if (:classes x)
                 (update-in x [:classes]
                            (fn [cs] (mapv (:classes data) cs)))
                 x)))))

(defn registry-view [data _]
  (reify
    om/IRender
    (render [_]
      (dom/div #js {:id "registry"}
               (dom/h2 nil "Registry")
               (apply dom/ul nil
                      (om/build-all entry-view (people data)))))))