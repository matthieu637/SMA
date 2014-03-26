// Agent sample_agent in project project

/* Initial beliefs and rules */

/* Initial goals */
!attribuer_mission.

/* Plans */


+!attribuer_mission : .findall(X, drone(X), L)
	<- 	.send(L, tell, mission(d1,derriere));
		.send(L, tell, mission(d2,leader));
		.send(L, tell, mission(d3,devant)).

+droneAuSol(X) : not terminer <- 
			remplirFuel(X);
			.send(X, achieve, decoller).
				
+!identification(ID)[source(X)] : allie(ID) <- 
			.send(X, tell, allie(ID)).
			
+!identification(ID)[source(X)] : not allie(ID) <- 
			.send(X, tell, ennemi(ID)).
