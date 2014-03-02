// Agent drone in project project

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+!start : .random(R) & X = math.floor(4*R) <- 
	deplacer(X);
	!start.
	
-!start : true <-
	!start.