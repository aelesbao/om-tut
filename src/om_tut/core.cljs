(ns ^:figwheel-always om-tut.core
  (:require [figwheel.client :as fw]
            [om.core :as om :include-macros true]
            [om-tut.registry :as registry]))

(enable-console-print!)

(println "Edits to this text should show up in your developer console.")

(extend-type string
  ICloneable
  (-clone [s] (js/String. s)))

(extend-type js/String
  ICloneable
  (-clone [s] (js/String. s))
  om/IValue
  (-value [s] (str s)))

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
                          :classes  {:6001 "Computer Programs"
                                     :6946 "Classical Mechanics"
                                     :1806 "Linear Algebra"}}))

(om/root registry/registry-view app-state
         {:target (. js/document (getElementById "registry"))})

(om/root registry/classes-view app-state
         {:target (. js/document (getElementById "classes"))})

(fw/start {:load-warninged-code true
           :on-jsload (fn []
                        ;; (stop-and-start-my app)
                        )})