// Agent vehicule in project project

/* Initial beliefs and rules */

choose_direction(3, X, Y, X, GY, _, _, _, _) :- GY >= Y.
choose_direction(2, X, Y, GX, Y, _, _, _, _) :- GX >= X.

choose_direction(2, X, Y, GX, GY, _, _, C, D) :- GX >= X & GY >= Y & C <= D.
choose_direction(3, X, Y, GX, GY, _, _, C, D) :- GX >= X & GY >= Y & C > D.


/* Initial goals */

!start.

/* Plans */

+!start : .my_name(X) & leader(X) <- 
	!to_goal.
	
+!start : .my_name(X) & not leader(X) <- 
	!follow.

+!to_goal : .my_name(L) & goal(GX, GY) & location(L, X, Y) & heightmap(A, B, C, D) & 
			choose_direction(Dir, X, Y, GX, GY, A, B, C, D) <-
	deplacer(Dir);
	!to_goal.

+!follow : .my_name(L) & location(L, X, Y) & follow(S) & location(S, GX, GY)  & 
			choose_direction(Dir, X, Y, GX, GY, 0, 0, 0, 0)<- 
	deplacer(Dir);
	!follow.

-!follow : true <-
	!follow.