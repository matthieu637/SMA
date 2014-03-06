// Agent drone in project project

/* Initial beliefs and rules */

choose_direction(0, X, Y, GX, GY) :- GX < X.
choose_direction(1, X, Y, GX, GY) :- GY < Y.
choose_direction(2, X, Y, GX, GY) :- GX > X.
choose_direction(3, X, Y, GX, GY) :- GY > Y.

/* Initial goals */

/* Plans */

+!start : .random(R) & X = math.floor(4*R) <- 
	deplacer(X);
	!rentrer;
	!start.
	
-!start : true <-
	!start.
	
+!goto(GX, GY) : .my_name(X) & location(X, GX, GY) <- true.
+!goto(GX, GY) : .my_name(X) & location(X, POSX, POSY) & choose_direction(D, POSX,  POSY, GX, GY) <- deplacer(D) ; !goto(GX, GY).
	
+!decoller : .my_name(X) & location(X, POSX, POSY) <- +positionInitiale(POSX, POSY);
														decoller;
														!start.
	
+!suspect(POSX, POSY) : true <- .send(t, achieve, identification(POSX, POSY)).

+allie(POSX, POSY).

+!rentrer : fuel(F) & .my_name(X) & location(X, POSX, POSY) & positionInitiale(IX, IY) & math.sqrt((POSX - IX)*(POSX - IX) + (POSY - IY)*(POSY - IY)) > F - 10 <- !goto(IX, IY);
			atterir; .send(t, tell, drone(X, ausol)).

+!tirer(POSX, POSY) : true <- tirerAdversaire(POSX, POSY).