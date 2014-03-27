// Agent vehicule in project project

/* Initial beliefs and rules */

fail(0).
failLimit(2).

/* Initial goals */

!start.

/* Plans */

+!start : .my_name(Y) & leader(Y) <- 
			.abolish(follow(_));
			!to_goal.
	
+!start : .my_name(X) & not leader(X) <- 
			!follow.

+!to_goal : fail(N) & failLimit(LN) & N >= failLimit & .random(R) & D = math.floor(4*R) <-
			deplacer(D);
			-+fail(0);
			!to_goal.

+!to_goal : .my_name(L) & goal(GX, GY) & location(L, X, Y) & heightmap(A, B, C, D) & comportement(V) &
			ia.choose_direction(Dir, X, Y, GX, GY, A, B, C, D, V) & not attend(_) <-
			deplacer(Dir);
			-+fail(0);
			!to_goal.
			
+!to_goal : attend(T) & Att = T * 500 <-
			.print("J'attends ", Att);
			.wait(Att);
			.abolish(attend(_));
			!to_goal.
	
-!to_goal : fail(N) & N2 = N + 1 <-
			.wait(5);
			-+fail(N2) ;
			!to_goal.

+!follow : fail(N) & failLimit(LN) & N >= failLimit & .random(R) & D = math.floor(4*R) <-
			deplacer(D);
			-+fail(0);
			!follow.

+!follow : .my_name(L) & location(L, X, Y) & follow(S) & location(S, GX, GY)  & 
			ia.choose_direction(Dir, X, Y, GX, GY, true) & not leader(L)  <- 
			deplacer(Dir);
			-+fail(0);
			!follow.

+!follow : not leader(L) & .my_name(L) <-
			!follow.

-!follow : fail(N) & N2 = N + 1 <-
			.wait(5);
			-+fail(N2);
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
