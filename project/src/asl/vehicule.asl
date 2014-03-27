// Agent vehicule in project project

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+!start : .my_name(Y) & leader(Y) <- 
			.abolish(follow(_));
			!to_goal.
	
+!start : .my_name(X) & not leader(X) <- 
			!follow.

+!to_goal : .my_name(L) & goal(GX, GY) & location(L, X, Y) & heightmap(A, B, C, D) & 
			ia.choose_direction(Dir, X, Y, GX, GY, A, B, C, D, true) & not attend(_) <-
			deplacer(Dir);
			!to_goal.
			
+!to_goal : attend(T) & Att = T * 500 <-
			.print("J'attends ", Att);
			.wait(Att);
			.abolish(attend(_));
			!to_goal.
	

	
-!to_goal : true <-
			.wait(100);
			!to_goal.

+!follow : .my_name(L) & location(L, X, Y) & follow(S) & location(S, GX, GY)  & 
			ia.choose_direction(Dir, X, Y, GX, GY, true) & not leader(L)  <- 
			deplacer(Dir);
			!follow.

+!follow : not leader(L) & .my_name(L) <-
			!follow.

-!follow : true <-
			.wait(100);
			!follow.
	
+!follow : leader(L) & .my_name(L) <-
			!start.

+probleme : true
	<- !scinder.
	
+!scinder : .findall(X, convoi(X) & not dead(X) , ConvoiRestant) & .length(ConvoiRestant) > 1 & 
				Milieu = math.ceil( .length(ConvoiRestant) /2 ) & .nth(Milieu, ConvoiRestant, Candidat)  & not dejaScinde <-
		.print("Scindons nous sur ",Candidat," !");
		.send(ConvoiRestant, tell, dejaScinde);
		scinder(Candidat).

+!scinder.
-!scinder.
