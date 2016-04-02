
#  Unicode support for test.check

Unicode support is divided into 2 sections: char based and code-point/int based

## Ranges and choices
  Ranges are a vector of range defs
    A range def is either
    -  A single character
    -  A pair (vector) of the start and end of a range
 
  choices is a generator that choose from a vector of ranges.  For example,
       (choices [1 2 [100 200])
  would return 1 and 2 and the numbers from 100 to 200.  The members of the range pair 100 and 200 in this
  example, can be anything accepted by choose.

## Unicode generators

  The char based Unicode support mirrors the normal char and string generators

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
 

  Code-point or int based characters

 | Standard Generator | Unicode Generator       | Unicode Desc                                                  |
 |--------------------+-------------------------+---------------------------------------------------------------|
 | string             | ustring-from-code-point | Generates Unicode strings consisting of any valid code point. |
 | char               | code-point              | Generates a valid Unicode code point                          |
 

