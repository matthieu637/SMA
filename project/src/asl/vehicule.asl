// Agent vehicule in project project

/* Initial beliefs and rules */

choose_direction(2, X, Y, GX, GY, _, _, C, D) :- GX > X & GY > Y & C <= D.
choose_direction(3, X, Y, GX, GY, _, _, C, D) :- GX > X & GY > Y & C > D.

choose_direction(3, X, Y, X, GY, _, _, _, _) :- GY > Y.
choose_direction(2, X, Y, GX, Y, _, _, _, _) :- GX > X.

/* Initial goals */

!start.

/* Plans */

+!start : .my_name(X) & leader(X) <- 
	!to_goal.
	
+!start : .my_name(X) & not leader(X) <- 
	!follow.

+!to_goal : goal(GX, GY) & location(X, Y) & heightmap(A, B, C, D) & 
			choose_direction(Dir, X, Y, GX, GY, A, B, C, D) <-
	deplacer(Dir);
	!to_goal.

+!follow : .random(R) & X = math.floor(4*R) <- 
	deplacer(X);
	!follow.