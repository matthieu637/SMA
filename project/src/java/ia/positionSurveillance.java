package ia;

import jason.JasonException;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.Term;

import modele.Variables;

/**
 * @author Matthieu Zimmer <contact@matthieu-zimmer.net>
 * 
 *         Internal Action.
 */
public class positionSurveillance extends DefaultInternalAction {

	private static final long serialVersionUID = 6650935652929594415L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
		// ia.positionSurveillance(SurveilleX, SurveilleY, ButX, ButY, LocX, LocY, Mission, FieldOfView)
		if (args.length == 8) {
			int i = 2;
			int butx = Integer.parseInt(args[i++].toString());
			int buty = Integer.parseInt(args[i++].toString());
			int lx = Integer.parseInt(args[i++].toString());
			int ly = Integer.parseInt(args[i++].toString());
			String mission = args[i++].toString();
			int fov = Integer.parseInt(args[i++].toString());

			int dx;
			int dy;
			int sx = 0;
			int sy = 0;
			
			switch (mission) {
				case "leader":
					// surveiller le leader
					sx = lx;
					sy = ly;
					break;
					
				case "devant":
					// surveiller devant le leader					
					
					dx = 2 * fov;
					dy = 2 * fov;
					
					dx = Math.min(dx, Math.abs(butx - lx));
					dy = Math.min(dy,  Math.abs(buty - ly));
					
					sx = lx + (int) (Math.signum(butx - lx) * dx);
					sy = ly + (int) (Math.signum(buty - ly) * dy);
					
					break;
					
				case "derriere":
					// surveiller derriere le leader

					dx = 2 * fov;
					dy = 2 * fov;
					
					dx = Math.min(dx, Math.abs(butx - lx));
					dy = Math.min(dy,  Math.abs(buty - ly));
					
					sx = lx - (int) (Math.signum(butx - lx) * dx);
					sy = ly - (int) (Math.signum(buty - ly) * dy);
					
					break;
				default:
					break;
			}
			
			sx = Math.max(0,sx);
			sy = Math.max(0,sy);
			sx = Math.min(Variables.TAILLE_CARTE_X - 1, sx);
			sy = Math.min(Variables.TAILLE_CARTE_Y - 1, sy);
			
			return un.unifies(args[0], new NumberTermImpl(sx)) & un.unifies(args[1], new NumberTermImpl(sy));

		} else
			throw new JasonException("8 arguments nécessaires");
	}
}
