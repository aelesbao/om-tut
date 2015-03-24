(ns ^:figwheel-always om-tut.contacts
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [cljs.core.async :as async :refer [<!]]
            [clojure.string :as string]))

(defn- parse-contact [contact-str]
  (let [[first middle last :as parts] (string/split contact-str #"\s+")
        [first last middle] (if (nil? last) [first middle] [first last middle])
        middle (when middle (string/replace middle "." ""))
        c (if middle (count middle) 0)]
    (when (>= (count parts) 2)
      (cond-> {:first first :last last}
              (== c 1) (assoc :middle-initial middle)
              (>= c 2) (assoc :middle middle)))))

(defn- add-contact [data owner]
  (when-let [new-contact (-> owner (om/get-state :text) parse-contact)]
    (om/transact! data :contacts #(conj % new-contact))
    (om/set-state! owner :text "")))

(defn handle-change [e owner {:keys [text]}]
  (let [value (.. e -target -value)]
    (if-not (re-find #"[0-9]" value)
      (om/set-state! owner :text value)
      (om/set-state! owner :text text))))

(defn- display-name [{:keys [first last]}]
  (str first " " last))

(defn- contact-view [contact _]
  (reify
    om/IRenderState
    (render-state [_ {:keys [delete]}]
      (dom/li #js {:className "contact"}
              (dom/span nil (display-name contact))
              (dom/button #js {:onClick (fn [e]
                                          (async/put! delete @contact)
                                          (.preventDefault e))}
                          "Delete")))))

(defn contacts-view [data owner]
  (reify
    om/IInitState
    (init-state [_]
      {:delete (async/chan)
       :text   ""})

    om/IWillMount
    (will-mount [_]
      (let [delete (om/get-state owner :delete)]
        (go (loop []
              (let [contact (<! delete)]
                (om/transact! data :contacts
                              (fn [xs] (vec (remove #(= contact %) xs))))
                (recur))))))

    om/IRenderState
    (render-state [_ state]
      (dom/div nil
               (dom/h2 nil "Contact list")
               (apply dom/ul nil
                      (om/build-all contact-view (:contacts data) {:init-state state}))
               (dom/div nil
                        (dom/input #js {:type "text" :value (:text state)
                                        :onChange #(handle-change % owner state)})
                        (dom/button #js {:onClick #(add-contact data owner)} "Add contact"))))))