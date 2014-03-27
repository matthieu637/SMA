// Agent sample_agent in project project

/* Initial beliefs and rules */

/* Initial goals */
!attribuer_mission.

/* Plans */


+!attribuer_mission : .findall(X, drone(X), L) & leader(V)
	<- 	.send(L, tell, mission(d1,derriere,V));
		.send(L, tell, mission(d2,leader,V));
		.send(L, tell, mission(d3,devant,V)).

+droneAuSol(X) : not terminer <- 
			remplirFuel(X);
			.send(X, achieve, decoller).
				
+!identification(ID)[source(X)] : allie(ID) <- 
			.send(X, tell, allie(ID)).
			
+!identification(ID)[source(X)] : not allie(ID) <-
			//.print(ID, " est un ennemi"); 
			.send(X, tell, ennemi(ID)).
