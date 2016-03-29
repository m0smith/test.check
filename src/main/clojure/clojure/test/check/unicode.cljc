;   Copyright (c) Rich Hickey, Reid Draper, and contributors.
;   All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file epl-v10.html at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(ns clojure.test.check.unicode)


;;
;;  Unicode support for test.check
;;
;;  Unicode support is divided into 2 sections: char based and code-point/int based
;;
;;  Ranges and choices
;;  Ranges are a vector of range defs
;;    A range def is either
;;      A single character
;;      A pair (vector) of the start and end of a range
;; 
;;
;;
;;  The char based Unicode support mirrors the normal char and string generators
;;
| Standard Generator  | Unicode Generator    | Generates                                                                     |
|---------------------+----------------------+-------------------------------------------------------------------------------|
| char                | uchar                | valid Unicode characters (char) from \u0000 to \uFFFF.                        |
| char-asciii         | uchar-alpha          | letter Unicode characters.                                                    |
|                     | uchar-numeric        | digit Unicode characters                                                      |
| char-alphanumeric   | uchar-alphanumeric   | letter and digit Unicode characters                                           |
| string              | ustring              | Unicode strings consisting of only chars                                      |
| string-alphanumeric | ustring-alphanumeric | Unicode alphanumeric strings.                                                 |
|                     | ustring-choices      | Unicode strings in the given ranges.                                          |
| namespace           | unamespace           | Unicode strings suitable for use as a Clojure namespace                       |
| keyword             | ukeyword             | Unicode strings suitable for use as a Clojure keyword                         |
| keyword-ns          | ukeyword-ns          | Unicode strings suitable for use as a Clojure keyword with optional namespace |
| symbol              | usymbol              | Unicode strings suitable for use as a Clojure symbol                          |
| symbol-ns           | usymbol-ns           | Unicode strings suitable for use as a Clojure symbol with optional namespace  |
|                     |                      |                                                                               |

;;  Code-point or int based characters

| Standard Generator | Unicode Generator       | Unicode Desc                                                  |
|--------------------+-------------------------+---------------------------------------------------------------|
| string             | ustring-from-code-point | Generates Unicode strings consisting of any valid code point. |
| char               | code-point              | Generates a valid Unicode code point                          |
|                    |                         |                                                               |


;;

;;

(def letter-ranges
  "The ranges of all the LETTERs in Unicode"
  [ [\u0041 \u005a] [\u0061 \u007a] \u00Aa \u00B5 \u00Ba [\u00C0 \u00D6]
   [\u00D8 \u00F6] [\u00F8 \u02C1] [\u02C6 \u02D1] [\u02E0 \u02E4] \u02EC
   \u02EE [\u0370 \u0374] \u0376 \u0377 [\u037A \u037D]
   \u0386 [\u0388 \u038a] \u038C [\u038E \u03A1] [\u03A3 \u03F5]
   [\u03F7 \u0481] [\u048A \u0523] [\u0531 \u0556] \u0559 [\u0561 \u0587]
   [\u05D0 \u05Ea] [\u05F0 \u05F2] [\u0621 \u064a] \u066E \u066F
   [\u0671 \u06D3] \u06D5 \u06E5 \u06E6 \u06EE
   \u06EF [\u06FA \u06FC] \u06FF \u0710 [\u0712 \u072F]
   [\u074D \u07A5] \u07B1 [\u07CA \u07Ea] \u07F4 \u07F5
   \u07Fa [\u0904 \u0939] \u093D \u0950 [\u0958 \u0961]
   \u0971 \u0972 [\u097B \u097F] [\u0985 \u098C] \u098F
   \u0990 [\u0993 \u09A8] [\u09AA \u09B0] \u09B2 [\u09B6 \u09B9]
   \u09BD \u09CE \u09DC \u09DD [\u09DF \u09E1] \u09F0 \u09F1 [\u0A05 \u0A0a] \u0A0F \u0A10
   [\u0A13 \u0A28] [\u0A2A \u0A30] \u0A32 \u0A33 \u0A35 \u0A36 \u0A38 \u0A39 [\u0A59 \u0A5C] \u0A5E
   [\u0A72 \u0A74] [\u0A85 \u0A8D] [\u0A8F \u0A91] [\u0A93 \u0AA8] [\u0AAA \u0AB0]
   \u0AB2 \u0AB3 [\u0AB5 \u0AB9] \u0ABD \u0AD0   \u0AE0 \u0AE1 [\u0B05 \u0B0C] \u0B0F \u0B10
   [\u0B13 \u0B28] [\u0B2A \u0B30] \u0B32 \u0B33 [\u0B35 \u0B39]
   \u0B3D \u0B5C \u0B5D [\u0B5F \u0B61] \u0B71
   \u0B83 [\u0B85 \u0B8a] [\u0B8E \u0B90] [\u0B92 \u0B95] \u0B99 \u0B9a \u0B9C \u0B9E \u0B9F \u0BA3
   \u0BA4 [\u0BA8 \u0BAa] [\u0BAE \u0BB9] \u0BD0 [\u0C05 \u0C0C]
   [\u0C0E \u0C10] [\u0C12 \u0C28] [\u0C2A \u0C33] [\u0C35 \u0C39] \u0C3D
   \u0C58 \u0C59 \u0C60 \u0C61 [\u0C85 \u0C8C]
   [\u0C8E \u0C90] [\u0C92 \u0CA8] [\u0CAA \u0CB3] [\u0CB5 \u0CB9] \u0CBD
   \u0CDE \u0CE0 \u0CE1 [\u0D05 \u0D0C] [\u0D0E \u0D10]
   [\u0D12 \u0D28] [\u0D2A \u0D39] \u0D3D \u0D60 \u0D61
   [\u0D7A \u0D7F] [\u0D85 \u0D96] [\u0D9A \u0DB1] [\u0DB3 \u0DBB] \u0DBD
   [\u0DC0 \u0DC6] [\u0E01 \u0E30] \u0E32 \u0E33 [\u0E40 \u0E46]
   \u0E81 \u0E82 \u0E84 \u0E87 \u0E88
   \u0E8a \u0E8D [\u0E94 \u0E97] [\u0E99 \u0E9F] [\u0EA1 \u0EA3]
   \u0EA5 \u0EA7 \u0EAa \u0EAB [\u0EAD \u0EB0]
   \u0EB2 \u0EB3 \u0EBD [\u0EC0 \u0EC4] \u0EC6
   \u0EDC \u0EDD \u0F00 [\u0F40 \u0F47] [\u0F49 \u0F6C]
   [\u0F88 \u0F8B] [\u1000 \u102a] \u103F [\u1050 \u1055] [\u105A \u105D]
   \u1061 \u1065 \u1066 [\u106E \u1070] [\u1075 \u1081]
   \u108E [\u10A0 \u10C5] [\u10D0 \u10Fa] \u10FC [\u1100 \u1159]
   [\u115F \u11A2] [\u11A8 \u11F9] [\u1200 \u1248] [\u124A \u124D] [\u1250 \u1256]
   \u1258 [\u125A \u125D] [\u1260 \u1288] [\u128A \u128D] [\u1290 \u12B0]
   [\u12B2 \u12B5] [\u12B8 \u12BE] \u12C0 [\u12C2 \u12C5] [\u12C8 \u12D6]
   [\u12D8 \u1310] [\u1312 \u1315] [\u1318 \u135a] [\u1380 \u138F] [\u13A0 \u13F4]
   [\u1401 \u166C] [\u166F \u1676] [\u1681 \u169a] [\u16A0 \u16Ea] [\u1700 \u170C]
   [\u170E \u1711] [\u1720 \u1731] [\u1740 \u1751] [\u1760 \u176C] [\u176E \u1770]
   [\u1780 \u17B3] \u17D7 \u17DC [\u1820 \u1877] [\u1880 \u18A8]
   \u18Aa [\u1900 \u191C] [\u1950 \u196D] [\u1970 \u1974] [\u1980 \u19A9]
   [\u19C1 \u19C7] [\u1A00 \u1A16] [\u1B05 \u1B33] [\u1B45 \u1B4B] [\u1B83 \u1BA0]
   \u1BAE \u1BAF [\u1C00 \u1C23] [\u1C4D \u1C4F] [\u1C5A \u1C7D]
   [\u1D00 \u1DBF] [\u1E00 \u1F15] [\u1F18 \u1F1D] [\u1F20 \u1F45] [\u1F48 \u1F4D]
   [\u1F50 \u1F57] \u1F59 \u1F5B \u1F5D [\u1F5F \u1F7D]
   [\u1F80 \u1FB4] [\u1FB6 \u1FBC] \u1FBE [\u1FC2 \u1FC4] [\u1FC6 \u1FCC]
   [\u1FD0 \u1FD3] [\u1FD6 \u1FDB] [\u1FE0 \u1FEC] [\u1FF2 \u1FF4] [\u1FF6 \u1FFC]
   \u2071 \u207F [\u2090 \u2094] \u2102 \u2107
   [\u210A \u2113] \u2115 [\u2119 \u211D] \u2124 \u2126
   \u2128 [\u212A \u212D] [\u212F \u2139] [\u213C \u213F] [\u2145 \u2149]
   \u214E \u2183 \u2184 [\u2C00 \u2C2E] [\u2C30 \u2C5E]
   [\u2C60 \u2C6F] [\u2C71 \u2C7D] [\u2C80 \u2CE4] [\u2D00 \u2D25] [\u2D30 \u2D65]
   \u2D6F [\u2D80 \u2D96] [\u2DA0 \u2DA6] [\u2DA8 \u2DAE] [\u2DB0 \u2DB6]
   [\u2DB8 \u2DBE] [\u2DC0 \u2DC6] [\u2DC8 \u2DCE] [\u2DD0 \u2DD6] [\u2DD8 \u2DDE]
   \u2E2F \u3005 \u3006 [\u3031 \u3035] \u303B
   \u303C [\u3041 \u3096] [\u309D \u309F] [\u30A1 \u30Fa] [\u30FC \u30FF]
   [\u3105 \u312D] [\u3131 \u318E] [\u31A0 \u31B7] [\u31F0 \u31FF] \u3400
   \u4DB5 \u4E00 \u9FC3 [\uA000 \uA48C] [\uA500 \uA60C]
   [\uA610 \uA61F] \uA62a \uA62B [\uA640 \uA65F] [\uA662 \uA66E]
   [\uA67F \uA697] [\uA717 \uA71F] [\uA722 \uA788] \uA78B \uA78C
   [\uA7FB \uA801] [\uA803 \uA805] [\uA807 \uA80a] [\uA80C \uA822] [\uA840 \uA873]
   [\uA882 \uA8B3] [\uA90A \uA925] [\uA930 \uA946] [\uAA00 \uAA28] [\uAA40 \uAA42]
   [\uAA44 \uAA4B] \uAC00 \uD7A3 [\uF900 \uFA2D] [\uFA30 \uFA6a]
   [\uFA70 \uFAD9] [\uFB00 \uFB06] [\uFB13 \uFB17] \uFB1D [\uFB1F \uFB28]
   [\uFB2A \uFB36] [\uFB38 \uFB3C] \uFB3E \uFB40 \uFB41
   \uFB43 \uFB44 [\uFB46 \uFBB1] [\uFBD3 \uFD3D] [\uFD50 \uFD8F]
   [\uFD92 \uFDC7] [\uFDF0 \uFDFB] [\uFE70 \uFE74] [\uFE76 \uFEFC] [\uFF21 \uFF3a]
   [\uFF41 \uFF5a] [\uFF66 \uFFBE] [\uFFC2 \uFFC7] [\uFFCA \uFFCF] [\uFFD2 \uFFD7]
   [\uFFDA \uFFDC] ])

(def digit-ranges
  "The ranges of all the DIGITs in Unicode."
  [
   [ \u0030 \u0039 ] [ \u0660 \u0669 ] [ \u06F0 \u06F9 ] [ \u07C0 \u07C9 ] [ \u0966 \u096F ]
   [ \u09E6 \u09EF ] [ \u0A66 \u0A6F ] [ \u0AE6 \u0AEF ] [ \u0B66 \u0B6F ] [ \u0BE6 \u0BEF ]
   [ \u0C66 \u0C6F ] [ \u0CE6 \u0CEF ] [ \u0D66 \u0D6F ] 
   ;[ \u0DE6 \u0DEF ]  ; Sinhala Lith Digit
   [ \u0E50 \u0E59 ]
   [ \u0ED0 \u0ED9 ] [ \u0F20 \u0F29 ] [ \u1040 \u1049 ] [ \u1090 \u1099 ] [ \u17E0 \u17E9 ]
   [ \u1810 \u1819 ] [ \u1946 \u194F ] [ \u19D0 \u19D9 ] [ \u1A80 \u1A89 ] [ \u1A90 \u1A99 ]
   [ \u1B50 \u1B59 ] [ \u1BB0 \u1BB9 ] [ \u1C40 \u1C49 ] [ \u1C50 \u1C59 ] [ \uA620 \uA629 ]
   [ \uA8D0 \uA8D9 ] [ \uA900 \uA909 ] [ \uA9D0 \uA9D9 ] 
   ;[ \uA9F0 \uA9F9 ] ; Myanmar Tai Laing Digit 
   [ \uAA50 \uAA59 ]
   [ \uABF0 \uABF9 ] [ \uFF10 \uFF19 ] 

   ;[ \u104A0 \u104A9 ] [ \u11066 \u1106F ] [ \u110F0 \u110F9 ]
   ;[ \u11136 \u1113F ] [ \u111D0 \u111D9 ] [ \u112F0 \u112F9 ] [ \u114D0 \u114D9 ] [ \u11650 \u11659 ]
   ;[ \u116C0 \u116C9 ] [ \u11730 \u11739 ] [ \u118E0 \u118E9 ] [ \u16A60 \u16A69 ] [ \u16B50 \u16B59 ]
   ;[ \u1D7CE \u1D7D7 ] [ \u1D7D8 \u1D7E1 ] [ \u1D7E2 \u1D7EB ] [ \u1D7EC \u1D7F5 ] [ \u1D7F6 \u1D7FF ]
 
])

;;
;; All Unicode defined ranges
;;

(def basic-latin-ranges                             [[\u0020 \u007F]])
(def latin-1-supplement-ranges                      [[\u00A0 \u00FF]])
(def latin-extended-a-ranges                        [[\u0100 \u017F]])
(def latin-extended-b-ranges                        [[\u0180 \u024F]])
(def ipa-extensions-ranges                          [[\u0250 \u02AF]])
(def spacing-modifier-letters-ranges                [[\u02B0 \u02FF]])
(def combining-diacritical-marks-ranges             [[\u0300 \u036F]])
(def greek-and-coptic-ranges                        [[\u0370 \u03FF]])
(def cyrillic-ranges                                [[\u0400 \u04FF]])
(def cyrillic-supplementary-ranges                  [[\u0500 \u052F]])
(def armenian-ranges                                [[\u0530 \u058F]])
(def hebrew-ranges                                  [[\u0590 \u05FF]])
(def arabic-ranges                                  [[\u0600 \u06FF]])
(def syriac-ranges                                  [[\u0700 \u074F]])
(def thaana-ranges                                  [[\u0780 \u07BF]])
(def devanagari-ranges                              [[\u0900 \u097F]])
(def bengali-ranges                                 [[\u0980 \u09FF]])
(def gurmukhi-ranges                                [[\u0A00 \u0A7F]])
(def gujarati-ranges                                [[\u0A80 \u0AFF]])
(def oriya-ranges                                   [[\u0B00 \u0B7F]])
(def tamil-ranges                                   [[\u0B80 \u0BFF]])
(def telugu-ranges                                  [[\u0C00 \u0C7F]])
(def kannada-ranges                                 [[\u0C80 \u0CFF]])
(def malayalam-ranges                               [[\u0D00 \u0D7F]])
(def sinhala-ranges                                 [[\u0D80 \u0DFF]])
(def thai-ranges                                    [[\u0E00 \u0E7F]])
(def lao-ranges                                     [[\u0E80 \u0EFF]])
(def tibetan-ranges                                 [[\u0F00 \u0FFF]])
(def myanmar-ranges                                 [[\u1000 \u109F]])
(def georgian-ranges                                [[\u10A0 \u10FF]])
(def hangul-jamo-ranges                             [[\u1100 \u11FF]])
(def ethiopic-ranges                                [[\u1200 \u137F]])
(def cherokee-ranges                                [[\u13A0 \u13FF]])
(def unified-canadian-aboriginal-syllabics-ranges   [[\u1400 \u167F]])
(def ogham-ranges                                   [[\u1680 \u169F]])
(def runic-ranges                                   [[\u16A0 \u16FF]])
(def tagalog-ranges                                 [[\u1700 \u171F]])
(def hanunoo-ranges                                 [[\u1720 \u173F]])
(def buhid-ranges                                   [[\u1740 \u175F]])
(def tagbanwa-ranges                                [[\u1760 \u177F]])
(def khmer-ranges                                   [[\u1780 \u17FF]])
(def mongolian-ranges                               [[\u1800 \u18AF]])
(def limbu-ranges                                   [[\u1900 \u194F]])
(def tai-le-ranges                                  [[\u1950 \u197F]])
(def khmer-symbols-ranges                           [[\u19E0 \u19FF]])
(def phonetic-extensions-ranges                     [[\u1D00 \u1D7F]])
(def latin-extended-additional-ranges               [[\u1E00 \u1EFF]])
(def greek-extended-ranges                          [[\u1F00 \u1FFF]])
(def general-punctuation-ranges                     [[\u2000 \u206F]])
(def superscripts-and-subscripts-ranges             [[\u2070 \u209F]])
(def currency-symbols-ranges                        [[\u20A0 \u20CF]])
(def combining-diacritical-marks-for-symbols-ranges [[\u20D0 \u20FF]])
(def letterlike-symbols-ranges                      [[\u2100 \u214F]])
(def number-forms-ranges                            [[\u2150 \u218F]])
(def arrows-ranges                                  [[\u2190 \u21FF]])
(def mathematical-operators-ranges                  [[\u2200 \u22FF]])
(def miscellaneous-technical-ranges                 [[\u2300 \u23FF]])
(def control-pictures-ranges                        [[\u2400 \u243F]])
(def optical-character-recognition-ranges           [[\u2440 \u245F]])
(def enclosed-alphanumerics-ranges                  [[\u2460 \u24FF]])
(def box-drawing-ranges                             [[\u2500 \u257F]])
(def block-elements-ranges                          [[\u2580 \u259F]])
(def geometric-shapes-ranges                        [[\u25A0 \u25FF]])
(def miscellaneous-symbols-ranges                   [[\u2600 \u26FF]])
(def dingbats-ranges                                [[\u2700 \u27BF]])
(def miscellaneous-mathematical-symbols-a-ranges    [[\u27C0 \u27EF]])
(def supplemental-arrows-a-ranges                   [[\u27F0 \u27FF]])
(def braille-patterns-ranges                        [[\u2800 \u28FF]])
(def supplemental-arrows-b-ranges                   [[\u2900 \u297F]])
(def miscellaneous-mathematical-symbols-b-ranges    [[\u2980 \u29FF]])
(def supplemental-mathematical-operators-ranges     [[\u2A00 \u2AFF]])
(def miscellaneous-symbols-and-arrows-ranges        [[\u2B00 \u2BFF]])
(def cjk-radicals-supplement-ranges                 [[\u2E80 \u2EFF]])
(def kangxi-radicals-ranges                         [[\u2F00 \u2FDF]])
(def ideographic-description-characters-ranges      [[\u2FF0 \u2FFF]])
(def cjk-symbols-and-punctuation-ranges             [[\u3000 \u303F]])
(def hiragana-ranges                                [[\u3040 \u309F]])
(def katakana-ranges                                [[\u30A0 \u30FF]])
(def bopomofo-ranges                                [[\u3100 \u312F]])
(def hangul-compatibility-jamo-ranges               [[\u3130 \u318F]])
(def kanbun-ranges                                  [[\u3190 \u319F]])
(def bopomofo-extended-ranges                       [[\u31A0 \u31BF]])
(def katakana-phonetic-extensions-ranges            [[\u31F0 \u31FF]])
(def enclosed-cjk-letters-and-months-ranges         [[\u3200 \u32FF]])
(def cjk-compatibility-ranges                       [[\u3300 \u33FF]])
(def cjk-unified-ideographs-extension-a-ranges      [[\u3400 \u4DBF]])
(def yijing-hexagram-symbols-ranges                 [[\u4DC0 \u4DFF]])
(def cjk-unified-ideographs-ranges                  [[\u4E00 \u9FFF]])
(def yi-syllables-ranges                            [[\uA000 \uA48F]])
(def yi-radicals-ranges                             [[\uA490 \uA4CF]])
(def hangul-syllables-ranges                        [[\uAC00 \uD7AF]])
(def cjk-compatibility-ideographs-ranges            [[\uF900 \uFAFF]])
(def alphabetic-presentation-forms-ranges           [[\uFB00 \uFB4F]])
(def arabic-presentation-forms-a-ranges             [[\uFB50 \uFDFF]])
(def variation-selectors-ranges                     [[\uFE00 \uFE0F]])
(def combining-half-marks-ranges                    [[\uFE20 \uFE2F]])
(def cjk-compatibility-forms-ranges                 [[\uFE30 \uFE4F]])
(def small-form-variants-ranges                     [[\uFE50 \uFE6F]])
(def arabic-presentation-forms-b-ranges             [[\uFE70 \uFEFF]])
(def halfwidth-and-fullwidth-forms-ranges           [[\uFF00 \uFFEF]])
(def specials-ranges                                [[\uFFF0 \uFFFF]])

;;
;; The following are code points beyond \uFFFF

(def linear-b-syllabary-ranges                      [[0x10000 0x1007F]])
(def linear-b-ideograms-ranges                      [[0x10080 0x100FF]])
(def aegean-numbers-ranges                          [[0x10100 0x1013F]])
(def old-italic-ranges                              [[0x10300 0x1032F]])
(def gothic-ranges                                  [[0x10330 0x1034F]])
(def ugaritic-ranges                                [[0x10380 0x1039F]])
(def deseret-ranges                                 [[0x10400 0x1044F]])
(def shavian-ranges                                 [[0x10450 0x1047F]])
(def osmanya-ranges                                 [[0x10480 0x104AF]])
(def cypriot-syllabary-ranges                       [[0x10800 0x1083F]])
(def high-surrogates-ranges                         [[0xD800 0xDB7F]])
(def high-private-use-surrogates-ranges             [[0xDB80 0xDBFF]])
(def low-surrogates-ranges                          [[0xDC00 0xDFFF]])
(def private-use-area-ranges                        [[0xE000 0xF8FF]])
(def tags-ranges                                    [[0xE0000 0xE007F]])
(def cjk-compatibility-ideographs-supplement-ranges [[0x2F800 0x2FA1F]])
(def cjk-unified-ideographs-extension-b-ranges      [[0x20000 0x2A6DF]])
(def byzantine-musical-symbols-ranges               [[0x1D000 0x1D0FF]])
(def musical-symbols-ranges                         [[0x1D100 0x1D1FF]])
(def tai-xuan-jing-symbols-ranges                   [[0x1D300 0x1D35F]])
(def mathematical-alphanumeric-symbols-ranges       [[0x1D400 0x1D7FF]])



(def all-unicode-ranges
  (reduce #(apply conj %1 %2) basic-latin-ranges 
          [
           latin-1-supplement-ranges
           latin-extended-a-ranges
           latin-extended-b-ranges
           ipa-extensions-ranges
           spacing-modifier-letters-ranges
           combining-diacritical-marks-ranges
           greek-and-coptic-ranges
           cyrillic-ranges
           cyrillic-supplementary-ranges
           armenian-ranges
           hebrew-ranges
           arabic-ranges
           syriac-ranges
           thaana-ranges
           devanagari-ranges
           bengali-ranges
           gurmukhi-ranges
           gujarati-ranges
           oriya-ranges
           tamil-ranges
           telugu-ranges
           kannada-ranges
           malayalam-ranges
           sinhala-ranges
           thai-ranges
           lao-ranges
           tibetan-ranges
           myanmar-ranges
           georgian-ranges
           hangul-jamo-ranges
           ethiopic-ranges
           cherokee-ranges
           unified-canadian-aboriginal-syllabics-ranges
           ogham-ranges
           runic-ranges
           tagalog-ranges
           hanunoo-ranges
           buhid-ranges
           tagbanwa-ranges
           khmer-ranges
           mongolian-ranges
           limbu-ranges
           tai-le-ranges
           khmer-symbols-ranges
           phonetic-extensions-ranges
           latin-extended-additional-ranges
           greek-extended-ranges
           general-punctuation-ranges
           superscripts-and-subscripts-ranges
           currency-symbols-ranges
           combining-diacritical-marks-for-symbols-ranges
           letterlike-symbols-ranges
           number-forms-ranges
           arrows-ranges
           mathematical-operators-ranges
           miscellaneous-technical-ranges
           control-pictures-ranges
           optical-character-recognition-ranges
           enclosed-alphanumerics-ranges
           box-drawing-ranges
           block-elements-ranges
           geometric-shapes-ranges
           miscellaneous-symbols-ranges
           dingbats-ranges
           miscellaneous-mathematical-symbols-a-ranges
           supplemental-arrows-a-ranges
           braille-patterns-ranges
           supplemental-arrows-b-ranges
           miscellaneous-mathematical-symbols-b-ranges
           supplemental-mathematical-operators-ranges
           miscellaneous-symbols-and-arrows-ranges
           cjk-radicals-supplement-ranges
           kangxi-radicals-ranges
           ideographic-description-characters-ranges
           cjk-symbols-and-punctuation-ranges
           hiragana-ranges
           katakana-ranges
           bopomofo-ranges
           hangul-compatibility-jamo-ranges
           kanbun-ranges
           bopomofo-extended-ranges
           katakana-phonetic-extensions-ranges
           enclosed-cjk-letters-and-months-ranges
           cjk-compatibility-ranges
           cjk-unified-ideographs-extension-a-ranges
           yijing-hexagram-symbols-ranges
           cjk-unified-ideographs-ranges
           yi-syllables-ranges
           yi-radicals-ranges
           hangul-syllables-ranges
           cjk-compatibility-ideographs-ranges
           alphabetic-presentation-forms-ranges
           arabic-presentation-forms-a-ranges
           variation-selectors-ranges
           combining-half-marks-ranges
           cjk-compatibility-forms-ranges
           small-form-variants-ranges
           arabic-presentation-forms-b-ranges
           halfwidth-and-fullwidth-forms-ranges
           specials-ranges
           linear-b-syllabary-ranges
           linear-b-ideograms-ranges
           aegean-numbers-ranges
           old-italic-ranges
           gothic-ranges
           ugaritic-ranges
           deseret-ranges
           shavian-ranges
           osmanya-ranges
           cypriot-syllabary-ranges
           high-surrogates-ranges
           high-private-use-surrogates-ranges
           low-surrogates-ranges
           private-use-area-ranges
           tags-ranges
           cjk-compatibility-ideographs-supplement-ranges
           cjk-unified-ideographs-extension-b-ranges
           byzantine-musical-symbols-ranges
           musical-symbols-ranges
           tai-xuan-jing-symbols-ranges
           mathematical-alphanumeric-symbols-ranges]))


