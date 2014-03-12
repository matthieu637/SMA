// Agent drone in project project


enoughFuel(BX, BY) :- fuel(F) & .my_name(X) & location(X, POSX, POSY) & positionInitiale(IX, IY) 
						& math.abs(POSX - BX) + math.abs(POSY - BY) + math.abs(IX - BX) + math.abs(IY - BY) <= F - 10. 
						
enoughFuel(N) :- fuel(F) & .my_name(X) & location(X, POSX, POSY) & positionInitiale(IX, IY) 
						& math.abs(IX - POSX) + math.abs(IY - POSY) + 2 * N <= F - 10. 


priorite(leader, 1).
priorite(devant, 2).
priorite(derriere, 3).

goHome.

/* Initial goals */
/* Plans */

/* Mission */
	
+!doMission : .my_name(D) & mission(D,M) <- !posSurveillance(M); !detecterAdversaire; !randMove(5); !goHome; !doMission.	
+!doMission.

+!detecterAdversaire : altitude(0) & militaire(POSX,POSY) &  allie(POSX,POSY).
+!detecterAdversaire : altitude(0) & militaire(POSX,POSY) & not allie(POSX,POSY)
	<- !suspect(POSX,POSY);
		!detecterAdversaire.

+!detecterAdversaire : altitude(1) & vehicule(POSX,POSY) 
	<- changerAltitude;
	!detecterAdversaire.
	
+!detecterAdversaire : altitude(0) & civil(POSX,POSY)
	<- changerAltitude.
+!detecterAdversaire : altitude(0) 
	<- changerAltitude.
	
+!detecterAdversaire : altitude(1).

-!detecterAdversaire : true
	<- !detecterAdversaire.

+!informerAllouer : .my_name(D)<-
		.findall(X,drone(X) & X \== D, L); 
		.send(L, achieve, alloc);.print("I have informed Allouer");
		.abolish(mission(D,M));
		.print(L).
	
+!choisirMission : .my_name(D) & mission(D,M) <- .print("Jai deja une mission").
+!choisirMission : .my_name(D) & .random(R) & R <= 0.1 & .findall(X, mission(DD,X), S) & .difference([leader,devant,derriere], S, L) & .max(L, M) 
	<- .print("I choose mission : ", M);.print(L);
	+mission(D,M);
	!informerMission.

+!choisirMission : true <- !choisirMission. 

+!informerMission : .my_name(D) & mission(D, M)
	<- .findall(X,drone(X) & X \== D, L); 
	.send(L, tell, mission(D,M));.print("I have informed My Mission").
		
+!informerMission : true <- !informerMission. 
	
+!decoller : .my_name(X) & location(X, POSX, POSY) 
				<- +positionInitiale(POSX, POSY);
					decoller;.print("I am flying");-goHome;!choisirMission; !doMission.					
					
+!alloc[source(DD)] : .my_name(D) & mission(D,M) & priorite(M,P) & .count(mission(_, _), NDM) & P > NDM-1
						 & not goHome <- .abolish(mission(DD,_));.abolish(mission(D,_));.print("I have DeAllocated1 ", DD);!choisirMission.
						 
+!alloc[source(DD)] : .count(mission(_, _), NDM) <- .abolish(mission(DD,_));.print("I have DeAllocated2 ", DD).

				
/* Random move */	
+!randMove(0).
+!randMove(N) : .random(R) & X = math.floor(4*R) & enoughFuel(0) & not goHome
	<- deplacer(X); !randMove(N-1).

+!randMove(N).
-!randMove(N) : true <-	!randMove(N).
	
	
/* GO Home */
+!goHome : goHome & positionInitiale(IX, IY) <- .print("I go home."); !informerAllouer;!goto(IX, IY);
			atterir; .print("I have landed").
+!goHome.
-!goHome.

/* GOTO */
+!goto(GX, GY) : .my_name(X) & location(X, GX, GY) <- true.

+!goto(GX, GY) : .my_name(X) & location(X, POSX, POSY) & goHome & ia.choose_direction(D, POSX,  POSY, GX, GY) <- 
		deplacer(D);
		!goto(GX, GY).
		
+!goto(GX, GY) : .my_name(X) & location(X, POSX, POSY) & enoughFuel(GX,GY) & ia.choose_direction(D, POSX,  POSY, GX, GY) <- 
		deplacer(D);
		!goto(GX, GY).
		
+!goto(GX, GY) : not enoughFuel(GX,GY) <- +goHome.
	
-!goto(GX,GY) : true <- !randMove(2); !goto(GX,GY).





/* GOTO monitor position */

+!posSurveillance(M): .my_name(D) & leader(L) & mission(D, M) & fieldOfView(F) & not goHome <- 
	.send(L, askOne, location(L,_,_), location(L,POSX,POSY));
	.send(L, askOne, goal(_,_), goal(BX,BY) );
	ia.positionSurveillance(SX,SY,BX,BY,POSX,POSY,M,F); 
	!goto(SX,SY).

+!posSurveillance(X) : not leader(_)
	<- !posSurveillance(X).
	
+!posSurveillance(X) : goHome 
	<- true.
	 
-!posSurveillance(X) : true <- true.





/* */

+!suspect(POSX, POSY) : true <- .send(t, achieve, identification(POSX, POSY)).

+!tirer(POSX, POSY) : true <- tirerAdversaire(POSX, POSY).


