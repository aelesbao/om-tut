(ns ^:figwheel-always om-tut.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [om-tut.contacts :as contacts]))

(enable-console-print!)

(println "Edits to this text should show up in your developer console.")

(defonce app-state (atom {:contacts [{:first "Ben" :last "Bitdiddle" :email "benb@mit.edu"}
                                     {:first "Alyssa" :middle-initial "P" :last "Hacker" :email "aphacker@mit.edu"}
                                     {:first "Eva" :middle "Lu" :last "Ator" :email "eval@mit.edu"}
                                     {:first "Louis" :last "Reasoner" :email "prolog@mit.edu"}
                                     {:first "Cy" :middle-initial "D" :last "Effect" :email "bugs@mit.edu"}
                                     {:first "Lem" :middle-initial "E" :last "Tweakit" :email "morebugs@mit.edu"}]}))

(om/root contacts/contacts-view
         app-state
         {:target (. js/document (getElementById "contacts"))})
