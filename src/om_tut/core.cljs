(ns ^:figwheel-always om-tut.core
    (:require [om.core :as om :include-macros true]
              [om.dom :as dom :include-macros true]))

(enable-console-print!)

(println "Edits to this text should show up in your developer console.")

;; define your app data so that it doesn't get over-written on reload

(defn- display-name [{:keys [first last]}]
  (str first " " last))

(defn- contact-view [contact owner]
  (om/component
    (dom/li #js {:className "contact"}
      (dom/span nil (display-name contact)))))

(defn- contacts-view [data owner]
  (om/component
    (dom/div nil
      (dom/h2 nil "Contacts")
      (apply dom/ul nil
        (om/build-all contact-view (:contacts data))))))

(defonce app-state (atom {:contacts [{:first "Ben" :last "Bitdiddle" :email "benb@mit.edu"}
                                     {:first "Alyssa" :middle-initial "P" :last "Hacker" :email "aphacker@mit.edu"}
                                     {:first "Eva" :middle "Lu" :last "Ator" :email "eval@mit.edu"}
                                     {:first "Louis" :last "Reasoner" :email "prolog@mit.edu"}
                                     {:first "Cy" :middle-initial "D" :last "Effect" :email "bugs@mit.edu"}
                                     {:first "Lem" :middle-initial "E" :last "Tweakit" :email "morebugs@mit.edu"}]}))

(om/root contacts-view
         app-state
         {:target (. js/document (getElementById "contacts"))})
