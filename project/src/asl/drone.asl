// Agent drone in project project


enoughFuel :- fuel(F) & .my_name(X) & location(X, POSX, POSY) & positionInitiale(IX, IY) & math.abs(POSX - IX) + math.abs(POSY - IY) <= F - 10. 


/* Initial goals */
/* Plans */

+mission(Y) : true
	<-!surveiller(Y).
	
+!decoller : .my_name(X) & location(X, POSX, POSY) 
				<- +positionInitiale(POSX, POSY);
					decoller;
					!move.
					
+!move : .random(R) & X = math.floor(4*R)
	<- deplacer(X);
	!checkFuel.
	
-!move : true <-
	!move.
	
+!checkFuel(BX,BY):  enoughFuel(BX,BY)<- true.			
+!checkFuel(BX,BY) : not enoughFuel(BX,BY) <- !goHome.

+!goHome : positionInitiale(IX, IY) <- .print("I go home."); !informer_rentrer ;!goto(IX, IY);
			atterir.
			
+!goto(GX, GY) : .my_name(X) & location(X, GX, GY) <- true.
+!goto(GX, GY) : .my_name(X) & location(X, POSX, POSY) & ia.choose_direction(D, POSX,  POSY, GX, GY) <- deplacer(D) ; !goto(GX, GY).
	
	
+!suspect(POSX, POSY) : true <- .send(t, achieve, identification(POSX, POSY)).


+!tirer(POSX, POSY) : true <- tirerAdversaire(POSX, POSY).


+!surveiller(M): leader(L) & location(L, POSX, POSY) & mission(M) & fielOfView(F) & ia.positionSurveillance(SX,SY,BX,BY,POSX,POSY,M,F)
	<-  !goto(SX,SY) ; 
		!surveiller(M).

-!surveiller(X) : true
	<- !surveiller(X).
	
+!informer_rentrer : my_name(D) 
	<- .findall(X,drone(X) & X \== D, L); 
	.send(L, achieve, choisir_mission).
	
+!choisir_mission : .my_name(D) & .findall(X, not mission(_,X), L) & .max(L, M) 
	<- mission(D,M);
	!informer_mission.
	

+!informer_mission : my_name(D) & mission(M)
	<- .findall(X,drone(X) & X \== D, L); 
	.send(L, tell, mission(D, M)).



