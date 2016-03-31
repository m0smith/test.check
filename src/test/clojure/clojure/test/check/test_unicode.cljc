;   Copyright (c) Rich Hickey, Reid Draper, and contributors.
;   All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file epl-v10.html at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(ns clojure.test.check.test-unicode
  (:require #?(:cljs
               [cljs.test :as test :refer-macros [deftest testing is]])
            #?(:clj
               [clojure.test :refer :all])
            [clojure.test.check :as tc]
            [clojure.test.check.generators :as gen #?@(:cljs [:include-macros true])]
            [clojure.test.check.properties :as prop #?@(:cljs [:include-macros true])]
            [clojure.test.check.rose-tree :as rose]
            [clojure.test.check.random :as random]
            [clojure.test.check.clojure-test :as ct #?(:clj :refer :cljs :refer-macros) (defspec)]
            #?(:clj  [clojure.edn :as edn]
               :cljs [cljs.reader :as edn])))


;; Generating Unicode generators
;; --------------------------------------------------------------------------

(defn gen-ustring-test 
  ([g] (g))
  ([g i-gen] 
   (gen/bind i-gen
             (fn [i]
               (gen/bind (g i)
                         (fn [s] [s i]))))))


(deftest generators-unicode-test
  (let [t (fn [generator pred]
            (is (:result (tc/quick-check 100
                           (prop/for-all [x generator]
                             (pred x))))))
        is-uchar-fn #?(:clj (fn [^Character c] (let [r (Character/isDefined c) ^Integer i c] (if r r (do (println i) nil))))  :cljs string?)
        is-fixed-string? (fn [[s i]] (and (string? s) (= i (count s))))
        ]

    (testing "unicode keyword"              (t gen/ukeyword keyword?))
    (testing "unicode keyword with ns"      (t gen/ukeyword-ns keyword?))

    (testing "unicode symbol"               (t gen/usymbol symbol?))
    (testing "unicode symbol with ns"       (t gen/usymbol-ns symbol?))


    (testing "unicode char"                 (t gen/uchar                is-uchar-fn))
    (testing "unicode char-alpha"           (t gen/uchar-alpha          is-uchar-fn))
    (testing "unicode char-numeric"         (t gen/uchar-numeric        is-uchar-fn))
    (testing "unicode char-alphanumeric"    (t gen/uchar-alphanumeric   is-uchar-fn))
    (testing "unicode code-point"           (t gen/code-point           is-uchar-fn))

    (testing "unicode string"               (t (gen-ustring-test gen/ustring)  string?))
    (testing "ustring-from-code-point"      (t gen/ustring-from-code-point string?))
    (testing "string-alphanumeric"          (t gen/ustring-alphanumeric  string?))))



;;  Testing choice
;; ---------------------------------------------------------------------------

(defn- fix-range 
  "The generator creates a pair with a [start length].  Convert it to [start end].  
  If r is not a vector, retrurn r"
  [r]
  (if (vector? r)
    (let [[f s] r] [f (+ f s)])
    r))

(defn- in-range? 
  "Return true if v == r or v is in [a b] inclusive"
  [v r]
  
  (if (vector? r)
    (let [[f s] r] (<= f v s))
    (= v r)))

(defn- valid-choice? 
  "Validate that v is a member of ranges.  If ranges is empty, v must be nil"
  [v ranges]
  (if (seq ranges)
    (some #(in-range? v %) ranges) 
    (not v)))


(def gen-ranges
  "Generate a series of ranges.
   Ranges are a vector of range defs
    A range def is either
      A single character
      A pair (vector) of the start and end of a range"
  (gen/fmap (fn [v] (vec (map fix-range v)))
            (gen/vector (gen/one-of [gen/int (gen/tuple gen/int gen/pos-int)]))))


;;
;;  Validate that choices returns only values from the ranges
;;
(defspec test-choices 200
  (prop/for-all [[ranges value]
                 (gen/bind gen-ranges
                           (fn [ranges]
                             (gen/fmap (fn [v] (vector ranges v))
                                       (gen/choices ranges))))]
                (valid-choice? value ranges)))



(defspec test-fixed-length-ustring 200
  (prop/for-all [[s i ]
                 (gen/bind gen/pos-int
                           (fn [i]
                             (gen/fmap (fn [s] (vector s i))
                                       (gen/ustring i))))]
                
                (and (string? s) (= i (count s)))))
                

