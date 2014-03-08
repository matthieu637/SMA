// Agent sample_agent in project project

/* Initial beliefs and rules */

/* Initial goals */
!attribuer_mission.

/* Plans */


+!attribuer_mission : .findall(X, drone(X), L)
	<- 	.send(L, tell, mission(d1,leader));
		.send(L, tell, mission(d2,devant));
		.send(L, tell, mission(d3,derriere)).

+droneAuSol(X) : true <- remplirFuel(X);
				.send(X, achieve, decoller).
				
+!identification(POSX, POSY)[source(X)] : allie(POSX, POSY) <- .send(X, tell, allie(POSX, POSY)).
+!identification(POSX, POSY)[source(X)] : not allie(POSX, POSY) <- .send(X, achieve, tirer(POSX, POSY)).
