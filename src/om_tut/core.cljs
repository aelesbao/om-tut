(ns ^:figwheel-always om-tut.core
  (:require [figwheel.client :as fw]
            [om.core :as om :include-macros true]
            [om-tut.contacts :as contacts]
            [om-tut.registry :as registry]))

(enable-console-print!)

(println "Edits to this text should show up in your developer console.")

(defonce app-state (atom {:contacts [{:first "Ben" :last "Bitdiddle" :email "benb@mit.edu"}
                                     {:first "Alyssa" :middle-initial "P" :last "Hacker" :email "aphacker@mit.edu"}
                                     {:first "Eva" :middle "Lu" :last "Ator" :email "eval@mit.edu"}
                                     {:first "Louis" :last "Reasoner" :email "prolog@mit.edu"}
                                     {:first "Cy" :middle-initial "D" :last "Effect" :email "bugs@mit.edu"}
                                     {:first "Lem" :middle-initial "E" :last "Tweakit" :email "morebugs@mit.edu"}]
                          :people   [{:type :student :first "Ben" :last "Bitdiddle" :email "benb@mit.edu"}
                                     {:type :student :first "Alyssa" :middle-initial "P" :last "Hacker" :email "aphacker@mit.edu"}
                                     {:type :professor :first "Gerald" :middle "Jay" :last "Sussman" :email "metacirc@mit.edu" :classes [:6001 :6946]}
                                     {:type :student :first "Eva" :middle "Lu" :last "Ator" :email "eval@mit.edu"}
                                     {:type :student :first "Louis" :last "Reasoner" :email "prolog@mit.edu"}
                                     {:type :professor :first "Hal" :last "Abelson" :email "evalapply@mit.edu" :classes [:6001]}]
                          :classes  {:6001 "The Structure and Interpretation of Computer Programs"
                                     :6946 "The Structure and Interpretation of Classical Mechanics"
                                     :1806 "Linear Algebra"}}))

(om/root contacts/contacts-view app-state
         {:target (. js/document (getElementById "contacts"))})

(om/root registry/registry-view app-state
         {:target (. js/document (getElementById "registry"))})

(fw/start {:on-jsload (fn []
                        ;; (stop-and-start-my app)
                        )})