// Agent drone in project project


enoughFuel(BX, BY) :- fuel(F) & .my_name(X) & location(X, POSX, POSY) & positionInitiale(IX, IY) 
						& math.abs(POSX - BX) + math.abs(POSY - BY) + math.abs(IX - BX) + math.abs(IY - BY) 
			<= F - 10. 
						
enoughFuel(N) :- fuel(F) & .my_name(X) & location(X, POSX, POSY) & positionInitiale(IX, IY) 
						& math.abs(IX - POSX) + math.abs(IY - POSY) + 2 * N 
			<= F - 10. 


priorite(leader, 1).
priorite(devant, 2).
priorite(derriere, 3).
porte(2).

goHome.

/* Initial goals */
/* Plans */

/* Mission */
	
+!doMission : .my_name(D) & mission(D,M) <- !posSurveillance(M); !detecterAdversaire; !goHome; !doMission.	
+!doMission.

//allie, civil, militaire, ... tous identifié par un ID pas des positions car ils peuvent se déplacer
//si je suis bas et que je vois un militaire n'étant pas allié, je demande son identification
+!detecterAdversaire : altitude(0) & 
					   .findall( ID, militaire(ID, _,_,_) & not allie(ID, _ , _, _), Suspects) & 
					   len(Suspects) > 0 <-  
			!suspect(Suspects).
		
//si je suis haut et que je vois un vehicule que je n'ai pas déjà vu, je change d'altitude et l'identifie
+!detecterAdversaire : altitude(1) & vehicule(ID, _,_,_) & not civil(ID, _,_ ,_) & not militaire(ID, _,_ ,_) <- 
			changerAltitude;
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

+!tirer(ID) : .findall( pos(T, POSX, POSY), militaire(ID, POSX, POSY, T), ListePosition) & 
					.max(ListePosition, pos(T, POSX, POSY)) & porte(P) & .my_name(N) & 
					location(N, MYX, MYY) & math.abs(MYX - POSX) + math.abs(MYY - POSY) <= P & altitude(0) & not dead(ID)
					 <-
			+ennemi(ID);
			.findall(X,drone(X) & X \== N, L); 
			.send(L, tell, ennemi(ID));
			tirer(POSX, POSY);
			+dead(ID);
			.abolish(pos(T,POSX,POSY));
			.send(L, tell, dead(ID)).
			
+!tirer(ID) : .findall( pos(T, POSX, POSY), militaire(ID, POSX, POSY, T), ListePosition) & 
					.max(ListePosition, pos(T, POSX, POSY)) & porte(P) & .my_name(N) & 
					location(N, MYX, MYY) & math.abs(MYX - POSX) + math.abs(MYY - POSY) <= P & altitude(1) & not dead(ID)
					 <-
			changerAltitude;
			+ennemi(ID);
			.findall(X,drone(X) & X \== N, L); 
			.send(L, tell, ennemi(ID));
			tirer(POSX, POSY);
			+dead(ID);
			.abolish(pos(T,POSX,POSY));
			.send(L, tell, dead(ID)).
			
+!tirer(ID) : .findall( pos(T, POSX, POSY), militaire(ID, POSX, POSY, T), ListePosition) & 
					.max(ListePosition, pos(T, POSX, POSY)) & porte(P) & .my_name(N) & 
					location(N, MYX, MYY) & math.abs(MYX - POSX) + math.abs(MYY - POSY) > P & altitude(0) & not dead(ID)
					 <-
		ia.choose_direction(Dir, MYX, MYY, POSX, POSY);
		deplacer(Dir);
		!tirer(ID).		

+!tirer(ID) : .findall( pos(T, POSX, POSY), militaire(ID, POSX, POSY, T), ListePosition) & 
					.max(ListePosition, pos(T, POSX, POSY)) & porte(P) & .my_name(N) & 
					location(N, MYX, MYY) & math.abs(MYX - POSX) + math.abs(MYY - POSY) > P & altitude(1) & not dead(ID)
					 <-
		changerAltitude;
		ia.choose_direction(Dir, MYX, MYY, POSX, POSY);
		deplacer(Dir);
		!tirer(ID).	
		
-!tirer(ID) : true
		<- !tirer(ID).

