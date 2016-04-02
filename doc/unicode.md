
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

<table>
<thead>
<th> Standard Generator  </th><th> Unicode Generator    </td><th> Generates                                                                     </th></tr>
</thead>
<tbody>
<tr><td> char                </td><td> uchar                </td><td> valid Unicode characters (char) from \u0000 to \uFFFF.                        </td></tr>
<tr><td> char-asciii         </td><td> uchar-alpha          </td><td> letter Unicode characters.                                                    </td></tr>
<tr><td>                     </td><td> uchar-numeric        </td><td> digit Unicode characters                                                      </td></tr>
<tr><td> char-alphanumeric   </td><td> uchar-alphanumeric   </td><td> letter and digit Unicode characters                                           </td></tr>
<tr><td> string              </td><td> ustring              </td><td> Unicode strings consisting of only chars                                      </td></tr>
<tr><td> string-alphanumeric </td><td> ustring-alphanumeric </td><td> Unicode alphanumeric strings.                                                 </td></tr>
<tr><td>                     </td><td> ustring-choices      </td><td> Unicode strings in the given ranges.                                          </td></tr>
<tr><td> namespace           </td><td> unamespace           </td><td> Unicode strings suitable for use as a Clojure namespace                       </td></tr>
<tr><td> keyword             </td><td> ukeyword             </td><td> Unicode strings suitable for use as a Clojure keyword                         </td></tr>
<tr><td> keyword-ns          </td><td> ukeyword-ns          </td><td> Unicode strings suitable for use as a Clojure keyword with optional namespace </td></tr>
<tr><td> symbol              </td><td> usymbol              </td><td> Unicode strings suitable for use as a Clojure symbol                          </td></tr>
<tr><td> symbol-ns           </td><td> usymbol-ns           </td><td> Unicode strings suitable for use as a Clojure symbol with optional namespace  </td></tr>
 
</tbody>
</table>




  Code-point or int based characters
  <table>
<thead>
<tr><td> Standard Generator </td><td> Unicode Generator       </td><td> Generates</td><tr>
</thead>
<tbody>
<tr><td> string             </td><td> ustring-from-code-point </td><td> Unicode strings consisting of any valid code point. </td></tr>
<tr><td> char               </td><td> code-point              </td><td> A valid Unicode code point                          </td></tr>
</tbody>
</table>

