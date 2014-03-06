// Agent sample_agent in project project

/* Initial beliefs and rules */

/* Initial goals */

/* Plans */

+droneAuSol(X) : true <- remplirFuel(X);
				.send(X, achieve, decoller).
				
+!identification(POSX, POSY)[source(X)] : allie(POSX, POSY) <- .send(X, tell, allie(POSX, POSY)).
+!identification(POSX, POSY)[source(X)] : not allie(POSX, POSY) <- .send(X, achieve, tirer(POSX, POSY)).
