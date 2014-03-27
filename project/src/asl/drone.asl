// Agent drone in project project

distance(X1, Y1, X2, Y2, D) :- D = math.abs(X1 - X2) + math.abs(Y1 - Y2).
 
distanceInf(X1, Y1, X2, Y2, L) :- distance(X1, Y1, X2, Y2, D) & D <= L.

dernierePositionM(ID, X, Y) :- .findall( pos(T, POSX, POSY), vehicule(ID, POSX, POSY, T) & militaire(ID), ListePosition) & 
					.max(ListePosition, pos(T, X, Y)).

enoughFuel(BX, BY) :- fuel(F) & .my_name(X) & location(X, POSX, POSY) & positionInitiale(IX, IY) 
						& math.abs(POSX - BX) + math.abs(POSY - BY) + math.abs(IX - BX) + math.abs(IY - BY) 
			<= F - 10. 
						
enoughFuel(N) :- fuel(F) & .my_name(X) & location(X, POSX, POSY) & positionInitiale(IX, IY) 
						& math.abs(IX - POSX) + math.abs(IY - POSY) + 2 * N 
			<= F - 10. 


priorite(leader, 0).
priorite(devant, 1).
priorite(derriere, 1).

porte(3).
menace(8).
ingerable_milieu(2).
ingerable_devant(3).

goHome.

/* Initial goals */
/* Plans */

+!decoller : .my_name(X) & location(X, POSX, POSY) <- 
			+positionInitiale(POSX, POSY);
			decoller;
			.print("I am flying");
			-goHome;
			!choisirMission; 
			!doMission.	
	

+!doMission : .my_name(D) & mission(D,M,L) &  leader(L)<- 
			!posSurveillance; 
			!detecterAdversaire; 
			!verifierMenace;
			!randMove(1); 
			!goHome; 
			!changeMission;
			!doMission.	


//si le leader que je surveillais n'existe plus ou si je n'ai pas de mission, je change de mission
+!doMission : .my_name(D) & ((mission(D,M,L) &  not leader(L)) | not mission(D,_,_)) & leader(_)
	<- !informerAllouer;
		!choisirMission;
		!doMission.

//si plus aucun leader, je rentre
+!doMission : not leader(_) <-
			.send(t, tell, terminer); 
			+goHome;
			!goHome.




/* Mission */

+!informerAllouer : .my_name(D) & mission(D,M,V) <-
			.findall(X,drone(X) & X \== D, L); 
			.send(L, achieve, alloc);.print("I have informed Allouer");
			.abolish(mission(D,M,V));
			.print(L).
	
+!choisirMission : .my_name(D) & mission(D,M,V) <- 
			.print("Jai deja une mission").
			
			
+!choisirMission : .my_name(D) & .random(R) & R <= 0.1 & 
			.findall(tripl(P,M,V), priorite(M,P) & mission(DD,M,V), MissionsDejaAllouees) &
			.findall(tripl(P,M,V), priorite(M,P) & leader(V), ListeMissions) &  
			.difference(ListeMissions, MissionsDejaAllouees, L) & .min(L, tripl(0,M,V))
			<-
			.print("I choose mission L : ", mission(D,M,V));.print(L);
			+mission(D,M,V);
			!informerMission.

+!choisirMission : .my_name(D) & .random(R) & R <= 0.1 & 
			.findall(tripl(P,M,V), priorite(M,P) & mission(DD,M,V), MissionsDejaAllouees) &
			.findall(tripl(P,M,V), priorite(M,P) & leader(V), ListeMissions) &  
			.difference(ListeMissions, MissionsDejaAllouees, L) & 
			.shuffle(L, LS) & .nth(0, LS, tripl(P,M,V))
			<-
			.print("I choose mission R : ", mission(D,M,V));.print(L);
			+mission(D,M,V);
			!informerMission.


+!choisirMission : true <- 
			!choisirMission. 
			
			
+!changeMission : .my_name(D) & mission(D,M,V) & priorite(M,1) & .random(R) & R <= 0.1 
				<- 
				.print("I change mission ...");
				!informerAllouer;
				!choisirMission.

+!changeMission : true.

+!informerMission : .my_name(D) & mission(D, M, V) <- 
			.findall(X,drone(X) & X \== D, L); 
			.send(L, tell, mission(D,M,V));
			.print("I have informed My Mission").
		
+!informerMission : true <- !informerMission. 
	
			
					
+!alloc[source(DD)] : .my_name(D) & mission(D,M,V) & priorite(M,P) & .count(mission(_, _, _), NDM) & P > NDM-1 & not goHome <- 
			.abolish(mission(DD,_,_));
			.abolish(mission(D,_,_));
			.print("I have DeAllocated1 ", DD);
			!choisirMission.
						 
+!alloc[source(DD)] : true <- 
			.abolish(mission(DD,_,_));
			.print("I have DeAllocated2 ", DD).



/* Menaces */
	
+!verifierMenace : positionSurveillance(BX, BY) & menace(L) &
					.findall(pos(D, ID), ennemi(ID) & not dead(ID) & dernierePositionM(ID, POSX, POSY) &  distance(BX, BY, POSX, POSY, D) & D <= L, ListeMenace)& 
					.length(ListeMenace) > 0 & .min(ListeMenace, pos(D, ID)) <-
					 !prevenirLeader(.length(ListeMenace));
					 !tirer(ID).
					 	
//si pas de menace et pas de suspect, je remonte			
+!verifierMenace : altitude(0) & positionSurveillance(BX, BY) & menace(L) & 
						.findall( ID, dernierePositionM(ID, POSX, POSY) & not allie(ID) & not dead(ID) & distanceInf(BX, BY, POSX, POSY, L), Suspects) & 
					   .length(Suspects) == 0 <-
					changerAltitude.

+!verifierMenace.

+!prevenirLeader(T) :  ingerable_milieu(I) & T < I & .my_name(D) & mission(D, M, L).

+!prevenirLeader(T) : ingerable_devant(I) & T >= I & .my_name(D) & mission(D, devant, L) <-
			.print("ATTEND");
			.send(L, tell, attend(T)).
			
+!prevenirLeader(T) : ingerable_milieu(I) & leader(L) & T >= I & .my_name(D) & mission(D, leader, L) <-
			.send(L, tell, probleme). 

+!prevenirLeader(T).

//si le leader est mort entre temps, préviens le nouveau
//-!prevenirLeader(T) : true <-
//!prevenirLeader(T).

//allie, civil, militaire, ... tous identifié par un ID pas des positions car ils peuvent se déplacer
//si je suis bas et que je vois un  n'étant pas allié, je demande son identification
+!detecterAdversaire : altitude(0) & 
					   .findall( ID, militaire(ID) & not allie(ID) & not dead(ID), Suspects) & 
					   .length(Suspects) > 0 <-  
			!suspect(Suspects).
		
//si je suis haut et que je vois un vehicule que je n'ai pas déjà vu, je change d'altitude et l'identifie
+!detecterAdversaire : altitude(1) & .findall( pos(T, POSX, POSY), vehicule(ID, POSX, POSY, T)[source(percept)]  & not civil(ID) & not militaire(ID) , ListePosition)  
			& .length(ListePosition) > 0 & .max(ListePosition, pos(T, POSX, POSY)) <- 
			changerAltitude;
			!goto(POSX, POSY);
			!detecterAdversaire.

//sinon ok
+!detecterAdversaire.


				
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
			!informerAllouer;
			!goto(IX, IY);
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

+!goto(GX, GY) : not mission(GX,GY,_) <- 
			+goHome.

	
-!goto(GX,GY) : true <- 
			!randMove(2); 
			!goto(GX,GY).


/* GOTO monitor position */

//check si leader n'est pas mort entre temps
+!posSurveillance : .my_name(D) & mission(D, M, L) & fieldOfView(F) & not goHome & leader(L) <- 
			.send(L, askOne, location(L,_,_), location(L,POSX,POSY), 1000);
			.send(L, askOne, goal(_,_), goal(BX,BY), 1000);
			ia.positionSurveillance(SX,SY,BX,BY,POSX,POSY,M,F);
			-+positionSurveillance(SX, SY);
			!goto(SX,SY).

//+!posSurveillance : not leader(_)
//			<- !posSurveillance.
	
+!posSurveillance : goHome 
			<- true.
	 
-!posSurveillance : true <- 
			!posSurveillance.


/* Demande identification */

+!suspect([]).
+!suspect([S|Suspects]) : true <- 
			.send(t, achieve, identification(S));
			!suspect(Suspects).

//-------------------------------------------------
//-------------- Partage d'information ------------
//-------------------------------------------------

+allie(ID)[source(t)] : .my_name(N) <-
 			.findall(X,drone(X) & X \== N, L); 
			.send(L, tell, allie(ID)).
			 
+civil(ID)[source(percept)] : .my_name(N) <-
 			.findall(X,drone(X) & X \== N, L); 
			.send(L, tell, civil(ID)).

//si la tour me préviens du type d'un ennemi j'en informe mes collègues
+ennemi(ID)[source(t)] : .my_name(N) <- 
			.findall(X,drone(X) & X \== N, L); 
			.send(L, tell, ennemi(ID)).
			
+militaire(ID)[source(percept)] : .my_name(N) <-
			.findall(X,drone(X) & X \== N, L); 
			.send(L, tell, militaire(ID)).

			
+vehicule(ID, POSX, POSY, T)[source(percept)] : .my_name(N) <-
			.findall(X,drone(X) & X \== N, L);
			.send(L, tell, vehicule(ID, POSX, POSY, T)).
			 
			 
/* Tirer */			 
			 
+!tirer(ID) : not dead(ID) & altitude(1)
					 <-
		changerAltitude;
		!tirer(ID).
		
+!tirer(ID) :  not dead(ID) & dernierePositionM(ID, POSX, POSY) & porte(P) & .my_name(N) & 
			.findall(X,drone(X) & X \== N, L) &
			location(N, MYX, MYY) & distanceInf(MYX, MYY, POSX, POSY, P)
					 <-
			tirer(POSX, POSY);
			+dead(ID);
			.send(L, tell, dead(ID));
			.print("tirer1").
			
			
+!tirer(ID) : not dead(ID) & dernierePositionM(ID, POSX, POSY) & porte(P) & .my_name(N) & 
					location(N, MYX, MYY) & not distanceInf(MYX, MYY, POSX, POSY, P)
					 <-
		ia.choose_direction(Dir, MYX, MYY, POSX, POSY);
		.print("ici1 ", MYX," ", MYY," ", POSX," ", POSY," ",Dir);
		deplacer(Dir);//on se rapproche seulement
		//!goto(POSX,POSY);
		!tirer(ID).

+!tirer(ID) : dead(ID).
		
-!tirer(ID) : not dead(ID) & .findall( pos(T, POSX, POSY), vehicule(ID, POSX, POSY, T) & militaire(ID), ListePosition) & 
					.max(ListePosition, pos(T, X, Y)) & time(TNOW) & T >= TNOW - 500 
		<- .print("je retire", TNOW-T);
			!tirer(ID).
		
-!tirer(ID).

