(ns ^:figwheel-always om-tut.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [figwheel.client :as fw]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [cljs.core.async :as async :refer [<!]]))

(enable-console-print!)

(println "Edits to this text should show up in your developer console.")

;; define your app data so that it doesn't get over-written on reload

(defn- display-name [{:keys [first last]}]
  (str first " " last))

(defn- contact-view [contact _]
  (reify
    om/IRenderState
    (render-state [_ {:keys [delete]}]
      (dom/li #js {:className "contact"}
              (dom/span nil (display-name contact))
              (dom/button #js {:onClick (fn [_] (async/put! delete @contact))} "Delete")))))

(defn contacts-view [data owner]
  (reify
    om/IInitState
    (init-state [_]
      {:delete (async/chan)})

    om/IWillMount
    (will-mount [_]
      (let [delete (om/get-state owner :delete)]
        (go (loop []
              (let [contact (<! delete)]
                (om/transact! data :contacts
                              (fn [xs] (vec (remove #(= contact %) xs))))
                (recur))))))

    om/IRenderState
    (render-state [_ {:keys [delete]}]
      (dom/div nil
               (dom/h2 nil "Contact list")
               (apply dom/ul nil
                      (om/build-all contact-view (:contacts data)
                                    {:init-state {:delete delete}}))))))

(defonce app-state (atom {:contacts [{:first "Ben" :last "Bitdiddle" :email "benb@mit.edu"}
                                     {:first "Alyssa" :middle-initial "P" :last "Hacker" :email "aphacker@mit.edu"}
                                     {:first "Eva" :middle "Lu" :last "Ator" :email "eval@mit.edu"}
                                     {:first "Louis" :last "Reasoner" :email "prolog@mit.edu"}
                                     {:first "Cy" :middle-initial "D" :last "Effect" :email "bugs@mit.edu"}
                                     {:first "Lem" :middle-initial "E" :last "Tweakit" :email "morebugs@mit.edu"}]}))

(om/root contacts-view
         app-state
         {:target (. js/document (getElementById "contacts"))})
