// Agent vehicule in project project

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+!start : .my_name(Y) & leader(Y) <- 
			.findall(X, drone(X), L);
			!to_goal.
	
+!start : .my_name(X) & not leader(X) <- 
			!follow.

+!to_goal : .my_name(L) & goal(GX, GY) & location(L, X, Y) & heightmap(A, B, C, D) & 
			ia.choose_direction(Dir, X, Y, GX, GY, A, B, C, D) <-
			.findall(DX, drone(DX), LD);
			deplacer(Dir);
			!to_goal.
	
-!to_goal : true <-
			!to_goal.

+!follow : .my_name(L) & location(L, X, Y) & follow(S) & location(S, GX, GY)  & 
			ia.choose_direction(Dir, X, Y, GX, GY) & not leader(L)  <- 
			deplacer(Dir);
			!follow.

+!follow : not leader(L) & .my_name(L) <-
			!follow.

-!follow : true <-
			!follow.
	
+!follow : leader(L) & .my_name(L) <-
			!start.
	