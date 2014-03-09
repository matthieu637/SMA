// Agent drone in project project


enoughFuel(BX, BY) :- fuel(F) & .my_name(X) & location(X, POSX, POSY) & positionInitiale(IX, IY) 
						& math.abs(POSX - BX) + math.abs(POSY - BY) + math.abs(IX - BX) + math.abs(IY - BY) <= F - 10. 
						
enoughFuel(N) :- fuel(F) & .my_name(X) & location(X, POSX, POSY) & positionInitiale(IX, IY) 
						& math.abs(IX - POSX) + math.abs(IY - POSY) + 2 * N <= F - 10. 


/* Initial goals */
/* Plans */

+mission(D,M) : .my_name(D)
	<- !doMission(M).
	
	
+!doMission(M) : .my_name(D) <- !posSurveillance(M); !randMove(5).	
	
+!decoller : .my_name(X) & mission(X,M) & location(X, POSX, POSY) 
				<- +positionInitiale(POSX, POSY);
					decoller;!doMission(M).
					
+!randMove(N) : N=0 <- true.
+!randMove(N) : .random(R) & X = math.floor(4*R) & enoughFuel(1)
	<- deplacer(X); !randMove(N-1).

+!randMove(N) : true <-	true.
-!randMove(N) : true <-	!randMove(N).
	

+!goHome : positionInitiale(IX, IY) <- .print("I go home."); !informerRentrer;.print("I have informed");!gotoDirectly(IX, IY);
			atterir; .print("I have landed");!choisirMission;.print("I have chosen a mission").

+!gotoDirectly(GX, GY) : .my_name(X) & location(X, GX, GY) <- true.
+!gotoDirectly(GX, GY) : .my_name(X) & location(X, POSX, POSY) & ia.choose_direction(D, POSX,  POSY, GX, GY) <- 
				deplacer(D);
				!gotoDirectly(GX, GY).
		
-!gotoDirectly(GX,GY) : true <- !randMove(2);!gotoDirectly(GX, GY).


+!goto(GX, GY) : .my_name(X) & location(X, GX, GY) <- true.

+!goto(GX, GY) : .my_name(X) & location(X, POSX, POSY) & enoughFuel(GX,GY) & ia.choose_direction(D, POSX,  POSY, GX, GY) <- 
		deplacer(D);
		!goto(GX, GY).
		
+!goto(GX, GY) : .my_name(X) & location(X, POSX, POSY) & not enoughFuel(GX,GY) <- !goHome.
	
-!goto(GX,GY) : enoughFuel(GX,GY) <- !randMove(2);!goto(GX, GY).


	
+!suspect(POSX, POSY) : true <- .send(t, achieve, identification(POSX, POSY)).


+!tirer(POSX, POSY) : true <- tirerAdversaire(POSX, POSY).


+!posSurveillance(M): .my_name(D) & leader(L) & mission(D, M) & fieldOfView(F) <- 
	.send(L, askOne, location(L,_,_), location(L,POSX,POSY));
	.send(L, askOne, goal(_,_), goal(BX,BY) );
	ia.positionSurveillance(SX,SY,BX,BY,POSX,POSY,M,F); 
	!goto(SX,SY).

+!posSurveillance(X) : not leader(_)
	<- !posSurveillance(X).
	 
-!posSurveillance(X) : true <- true.

+!informerRentrer : .my_name(D) & mission(D,M) <-
	 	-mission(D,M);
		.findall(X,drone(X) & X \== D, L); 
		.send(L, untell, mission(D,M));
		.send(L, achieve, choisirMission).
	
+!choisirMission : .my_name(D) & .findall(X, not mission(_,X), L) & .max(L, M) 
	<- .print("I choose mission : ", M);+mission(D,M);
	!informerMission.
	

+!informerMission : my_name(D) & mission(D, M)
	<- .findall(X,drone(X) & X \== D, L); 
	.send(L, tell, mission(D, M)).



