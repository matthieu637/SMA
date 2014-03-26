// Agent vehicule in project project

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+!start : .my_name(Y) & leader(Y) <- 
			!to_goal.
	
+!start : .my_name(X) & not leader(X) <- 
			!follow.

+!to_goal : .my_name(L) & goal(GX, GY) & location(L, X, Y) & heightmap(A, B, C, D) & 
			ia.choose_direction(Dir, X, Y, GX, GY, A, B, C, D, true) & not attend<-
			deplacer(Dir);
			!to_goal.
			
+!to_goal : attend <-
			.wait(250);
			!to_goal.
	
-!to_goal : true <-
			!to_goal.

+!follow : .my_name(L) & location(L, X, Y) & follow(S) & location(S, GX, GY)  & 
			ia.choose_direction(Dir, X, Y, GX, GY, true) & not leader(L)  <- 
			deplacer(Dir);
			!follow.

+!follow : not leader(L) & .my_name(L) <-
			!follow.

-!follow : true <-
			!follow.
	
+!follow : leader(L) & .my_name(L) <-
			!start.

+!scinder : .findall(X, convoi(X) & not dead(X) , ConvoiRestant) & .length(ConvoiRestant) > 1 & 
				Milieu = math.ceil( .length(ConvoiRestant) /2 ) & .nth(Milieu, ConvoiRestant, Candidat)  & not dejaScinde <-
		.print("Scindons nous sur ",Candidat," !");
		.send(ConvoiRestant, tell, dejaScinde);
		scinder(Candidat).

+!scinder.