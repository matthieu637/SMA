// Agent drone in project project

/* Initial beliefs and rules */

choose_direction(0, X, _, GX, _) :- GX < X.
choose_direction(1, _, Y, _, GY) :- GY < Y.
choose_direction(2, X, _, GX, _) :- GX > X.
choose_direction(3, _, Y, _, GY) :- GY > Y.

enoughFuel :- fuel(F) & .my_name(X) & location(X, POSX, POSY) & positionInitiale(IX, IY) & math.abs(POSX - IX) + math.abs(POSY - IY) <= F - 10. 

/* Initial goals */

/* Plans */

+!decoller : .my_name(X) & location(X, POSX, POSY) <- +positionInitiale(POSX, POSY);
														decoller;
														!move.
+!move : .random(R) & X = math.floor(4*R) <- 
	deplacer(X);
	!checkFuel.
	
-!move : true <-
	!move.
	
+!checkFuel :  enoughFuel <- !move.			
+!checkFuel : not enoughFuel <- !goHome.

+!goHome : positionInitiale(IX, IY) <- .print("I go home."); !goto(IX, IY);
			atterir.
			
+!goto(GX, GY) : .my_name(X) & location(X, GX, GY) <- true.
+!goto(GX, GY) : .my_name(X) & location(X, POSX, POSY) & choose_direction(D, POSX,  POSY, GX, GY) <- deplacer(D) ; !goto(GX, GY).
	
	
+!suspect(POSX, POSY) : true <- .send(t, achieve, identification(POSX, POSY)).


+!tirer(POSX, POSY) : true <- tirerAdversaire(POSX, POSY).
