// Agent drone in project project

distance(X1, Y1, X2, Y2, D) :- D = math.abs(X1 - X2) + math.abs(Y1 - Y2).
 
distanceInf(X1, Y1, X2, Y2, L) :- distance(X1, Y1, X2, Y2, D) & D <= L.

dernierePositionM(ID, X, Y) :- .findall( pos(T, POSX, POSY), militaire(ID, POSX, POSY, T), ListePosition) & 
					.max(ListePosition, pos(T, X, Y)).

enoughFuel(BX, BY) :- fuel(F) & .my_name(X) & location(X, POSX, POSY) & positionInitiale(IX, IY) 
						& math.abs(POSX - BX) + math.abs(POSY - BY) + math.abs(IX - BX) + math.abs(IY - BY) 
			<= F - 10. 
						
enoughFuel(N) :- fuel(F) & .my_name(X) & location(X, POSX, POSY) & positionInitiale(IX, IY) 
						& math.abs(IX - POSX) + math.abs(IY - POSY) + 2 * N 
			<= F - 10. 


priorite(leader, 1).
priorite(devant, 2).
priorite(derriere, 3).
porte(3).
menace(10).

goHome.

/* Initial goals */
/* Plans */

/* Mission */
	
+!doMission : .my_name(D) & mission(D,M) <- 
			!posSurveillance(M); 
			!detecterAdversaire; 
			!verifierMenace;
			!randMove(1); 
			!goHome; 
			!doMission.	
+!doMission.

+!verifierMenace : positionSurveillance(BX, BY) & menace(L) &
					.findall(pos(D, ID), ennemi(ID) & not dead(ID) & dernierePositionM(ID, POSX, POSY) &  distance(BX, BY, POSX, POSY, D) & D <= L, ListeMenace) &
					len(ListeMenace) > 0 & .min(ListeMenace, pos(D, ID))
					 <- 
					!tirer(ID).
+!verifierMenace.

//allie, civil, militaire, ... tous identifié par un ID pas des positions car ils peuvent se déplacer
//si je suis bas et que je vois un militaire n'étant pas allié, je demande son identification
+!detecterAdversaire : altitude(0) & 
					   .findall( ID, militaire(ID, _,_,_) & not allie(ID), Suspects) & 
					   len(Suspects) > 0 <-  
			!suspect(Suspects).
		
//si je suis haut et que je vois un vehicule que je n'ai pas déjà vu, je change d'altitude et l'identifie
+!detecterAdversaire : altitude(1) & .findall( pos(T, POSX, POSY), vehicule(ID, POSX, POSY, T)  & not civil(ID, _,_ ,_) & not militaire(ID, _,_ ,_) , ListePosition)  
			& len(ListePosition) > 0 & .max(ListePosition, pos(T, POSX, POSY)) <- 
			changerAltitude;
			!goto(POSX, POSY);
			!detecterAdversaire.

//sinon ok
+!detecterAdversaire.


+!informerAllouer : .my_name(D)<-
			.findall(X,drone(X) & X \== D, L); 
			.send(L, achieve, alloc);.print("I have informed Allouer");
			.abolish(mission(D,M));
			.print(L).
	
+!choisirMission : .my_name(D) & mission(D,M) <- 
			.print("Jai deja une mission").
+!choisirMission : .my_name(D) & .random(R) & R <= 0.1 & .findall(X, mission(DD,X), S) & .difference([leader,devant,derriere], S, L) & .max(L, M) <-
			.print("I choose mission : ", M);.print(L);
			+mission(D,M);
			!informerMission.

+!choisirMission : true <- 
			!choisirMission. 

+!informerMission : .my_name(D) & mission(D, M) <- 
			.findall(X,drone(X) & X \== D, L); 
			.send(L, tell, mission(D,M));
			.print("I have informed My Mission").
		
+!informerMission : true <- !informerMission. 
	
+!decoller : .my_name(X) & location(X, POSX, POSY) <- 
			+positionInitiale(POSX, POSY);
			decoller;
			.print("I am flying");
			-goHome;
			!choisirMission; 
			!doMission.					
					
+!alloc[source(DD)] : .my_name(D) & mission(D,M) & priorite(M,P) & .count(mission(_, _), NDM) & P > NDM-1 & not goHome <- 
			.abolish(mission(DD,_));
			.abolish(mission(D,_));
			.print("I have DeAllocated1 ", DD);
			!choisirMission.
						 
+!alloc[source(DD)] : .count(mission(_, _), NDM) <- 
			.abolish(mission(DD,_));
			.print("I have DeAllocated2 ", DD).

				
/* Random move */	
+!randMove(0).
+!randMove(N) : .random(R) & X = math.floor(4*R) & enoughFuel(0) & not goHome <- 
			deplacer(X); 
			!randMove(N-1).

+!randMove(N).
-!randMove(N) : true <-	
			!randMove(N).
	
	
/* GO Home */
+!goHome : goHome & positionInitiale(IX, IY) <- 
			.print("I go home."); 
			!informerAllouer;!goto(IX, IY);
			atterir; 
			.print("I have landed").
			
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
		
+!goto(GX, GY) : not enoughFuel(GX,GY) <- 
			+goHome.
	
-!goto(GX,GY) : true <- 
			!randMove(2); 
			!goto(GX,GY).





/* GOTO monitor position */

+!posSurveillance(M): .my_name(D) & leader(L) & mission(D, M) & fieldOfView(F) & not goHome <- 
			.send(L, askOne, location(L,_,_), location(L,POSX,POSY));
			.send(L, askOne, goal(_,_), goal(BX,BY) );
			ia.positionSurveillance(SX,SY,BX,BY,POSX,POSY,M,F);
			-+positionSurveillance(SX, SY);
			!goto(SX,SY).

+!posSurveillance(X) : not leader(_)
			<- !posSurveillance(X).
	
+!posSurveillance(X) : goHome 
			<- true.
	 
-!posSurveillance(X) : true <- true.





/* */

+!suspect([]).
+!suspect([S|Suspects]) : true <- 
			.send(t, achieve, identification(S));
			!suspect(Suspects).

+allie(_) : altitude(0) <-
			 changerAltitude.
+dead(_) :  altitude(0) <-
			 changerAltitude.

//si la tour me préviens du type d'un ennemi j'en informe mes collègues
+ennemi(ID)[source(t)] : .my_name(N) <- 
			.findall(X,drone(X) & X \== N, L); 
			.send(L, tell, ennemi(ID)). 
			 
+!tirer(ID) :  not dead(ID) & dernierePositionM(ID, POSX, POSY) & porte(P) & .my_name(N) & 
					location(N, MYX, MYY) & distanceInf(MYX, MYY, POSX, POSY, P) & altitude(0)
					 <-
			tirer(POSX, POSY);
			+dead(ID);
			.send(L, tell, dead(ID));
			.print("tirer1").
			
+!tirer(ID) : not dead(ID) & dernierePositionM(ID, POSX, POSY) & .my_name(N) & 
					location(N, MYX, MYY) & distanceInf(MYX, MYY, POSX, POSY, P) & altitude(1)
					 <-
			changerAltitude;
			tirer(POSX, POSY);
			+dead(ID);
			.send(L, tell, dead(ID));
			.print("tirer2").
			
+!tirer(ID) : not dead(ID) & dernierePositionM(ID, POSX, POSY) & porte(P) & .my_name(N) & 
					location(N, MYX, MYY) & not distanceInf(MYX, MYY, POSX, POSY, P) & altitude(0)
					 <-
		ia.choose_direction(Dir, MYX, MYY, POSX, POSY);
		.print("ici1 ", MYX," ", MYY," ", POSX," ", POSY," ",Dir);
		deplacer(Dir);
		!tirer(ID).

+!tirer(ID) : not dead(ID) & dernierePositionM(ID, POSX, POSY) & porte(P) & .my_name(N) & 
					location(N, MYX, MYY) & not distanceInf(MYX, MYY, POSX, POSY, P) & altitude(1)
					 <-
		changerAltitude;
		.print("ici2");
		ia.choose_direction(Dir, MYX, MYY, POSX, POSY);
		deplacer(Dir);
		!tirer(ID).
		
-!tirer(ID) : not dead(ID)
		<- !tirer(ID).

-!tirer(ID) : dead(ID).
