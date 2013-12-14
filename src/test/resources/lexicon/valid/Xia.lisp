(setq *start-symbols* '("s" "np"))
(setq *ltig* '(
(("s" (:rfoot . "s") ("," ",") ("cc" "and") (:subst . "s") ("." ".")) 1
 . 0.33333334)
(("s" (:subst . "np-sbj") ("vp" ("md" "can") ("rb" "not") (:subst . "vp"))) 1
 . 0.33333334)
(("vp" ("vb" "give") (:subst . "np") (:subst . "pp-dtv") (:subst . "pp-tmp")) 1
 . 0.5)
(("pp-tmp" ("in" "by") (:subst . "advp")) 1 . 1.0)
(("advp" ("rb" "then")) 1 . 1.0)
(("pp-dtv" ("in" "to") (:subst . np)) 1 . 1.0)
(("np" ("prp" "you")) 1 . 0.33333334)
(("np" ("prp" "it")) 1 . 0.33333334)
(("np-sbj" ("prp" "i")) 2 . 1.0)
(("s" (:subst . "np-sbj") ("vp" ("vbp" "'ve") (:subst . "vp"))) 1 . 0.33333334)
(("vp" ("vbn" "got") (:subst . "np")) 1 . 0.5)
;; erzeugt Satzambiguitaet
(("s" (:subst . "np-sbj") ("vp" ("vbp" "'ve") (:subst . "vp")) (:subst . "np")) 1 . 0.33333334)


(("vp" ("vbn" "got") (:subst . "np")) 1 . 0.5)
(("np" ("jj" "limited") ("nn" "production") ) 1 . 0.33333334)
(("np" ("jj" "limited") ("nn" "production") (:LFOOT . "np")) 1 . 0.33333334)
(("np" ("jj" "small") ("nn" "car") (:LFOOT . "np")) 1 . 0.33333334)
(("np" ("jj" "small") ("nn" "bike") (:LFOOT . "np")) 1 . 0.33333334)
(("np" ("jj" "small") ("np-in" ("adj" "medium") (:LFOOT . "np"))) 1 . 0.33333334)
))
