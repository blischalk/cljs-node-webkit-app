(ns breakout.core
  ;; jayq note needed currently but leaving just in case...
  ;; (:use [jayq.core :only [$ css html text]])
  ;; Currently using enfocus instead
  (:require [breakout.lib :as lib]
            [breakout.paddle :as paddle]
            [enfocus.core :as ef]
            [enfocus.events :as events]
            [enfocus.effects :as effects])
  (:require-macros [enfocus.macros :as em]))

;; Very Helpful
;; http://www.spacjer.com/blog/2014/09/12/clojurescript-javascript-interop/

;; Define some constants
(def app-name "Node Webkit Breakout")
(def username (or js/process.env.USERNAME js/process.env.USER))


(defn create-menu! []
  "Creates the OS menu"
  ;; Use requirejs to include the gui module
  (let [nw (js/require "nw.gui")
        ;; Example of calling a method on a property of an object
        win (.get (.-Window nw))
        ;; Good example of newing up a nested object
        ;; Also an example of how to create a pojo
        mb (nw.Menu. (js-obj "type" "menubar"))]

        ;; Conditionally initialize Mac Builtin menus
        (if (= (.-platform js/process) "darwin") (.createMacBuiltin mb
                                                   app-name))

        ;; Example of setting a property on a javascript object
    (set! (.-menu win) mb)))



(em/deftemplate main-nav :compiled "resources/public/templates/main-nav.html"
  [branding]
  [".navbar-brand"] (ef/content branding))


(em/deftemplate main-content :compiled
  "resources/public/templates/main-content.html"
  [username]
  [".username"] (ef/content username))


(em/deftemplate about-content :compiled
  "resources/public/templates/about-content.html"
  [])


(em/deftemplate contact-content :compiled
  "resources/public/templates/contact-content.html"
  [])


(em/defaction page-change [content nav-ele]
  [".starter-template"] (ef/content content)
  [nav-ele] (ef/add-class "active")
  [(str "nav li:not(" nav-ele ")")] (ef/remove-class "active"))


(em/defaction attach-nav-handlers! []
  ["nav .main"] (events/listen :click
                               #(page-change (main-content username) ".main"))
  ["nav .about"] (events/listen :click
                                #(page-change (about-content) ".about"))
  ["nav .contact"] (events/listen :click
                                  #(page-change (contact-content)".contact")) )


(defn update-greeting! []
  "Set the username to the Node process user"
  (ef/at [".username"] (ef/content username)))


(defn create-main-nav! []
  "Add main navigation"
  (ef/at ["body"] (ef/prepend (main-nav app-name))))


(defn add-main-content! []
  "Add main content"
  (ef/at ["#verbiage"] (ef/content (main-content username))))


(defn start []
  "Entry point.  Called when page is loaded"
  (create-menu!)
  (create-main-nav!)
  (add-main-content!)
  (update-greeting!)
  (attach-nav-handlers!)
  (paddle/keyEvents)
  (lib/init))


;; Using window onload instead of calling a main method from client
(set! (.-onload js/window) start)
