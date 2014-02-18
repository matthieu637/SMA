// Environment code for project project

import jason.asSyntax.*;
import jason.environment.*;
import java.util.logging.*;

public class Env extends Environment {

    private Logger logger = Logger.getLogger("project."+Env.class.getName());
    
    private CarteModel modele;

    /** Called before the MAS execution with the args informed in .mas2j */
    @Override
    public void init(String[] args) {
		super.init(args);
		modele = new CarteModel(new Interpreteur(this));
    }

    @Override
    public boolean executeAction(String agName, Structure action) {
        logger.info("executing: "+action+", but not implemented!");
        return true;
    }

    /** Called before the end of MAS execution */
    @Override
    public void stop() {
        super.stop();
    }
}
