(ns ^:figwheel-always om-tut.components.editable
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [om-tut.util :as util]))

(defn handle-change [e text _]
  (om/transact! text (fn [_] (.. e -target -value))))

(defn commit-change [_ owner]
  (om/set-state! owner :editing false))

(defn editable [text owner]
  (reify
    om/IInitState
    (init-state [_]
      {:editing false})

    om/IRenderState
    (render-state [_ {:keys [editing]}]
      (dom/li nil
              (dom/span #js {:style (util/display (not editing))} (om/value text))
              (dom/input
                #js {:style (util/display editing)
                     :value (om/value text)
                     :onChange #(handle-change % text owner)
                     :onKeyDown #(when (= (.-key %) "Enter")
                                  (commit-change text owner))
                     :onBlur (fn [_] (commit-change text owner))})
              (dom/button
                #js {:style (util/display (not editing))
                     :onClick #(om/set-state! owner :editing true)}
                "Edit")))))