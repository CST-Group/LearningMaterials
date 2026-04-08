package br.unicamp.cst.demoCST.codelets.sensors;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryObject;
import static support.Constants.*;
import java.util.List;
import ws3dproxy.model.Creature;
import ws3dproxy.model.Thing;

public class Vision extends Codelet {

    private Memory visionMO;
    private Creature c;
    
    public Vision(Creature nc) {
        c = nc;		
        this.name = "Vision";
    }

    @Override()
    public void accessMemoryObjects() {
        visionMO=(MemoryObject)this.getOutput("visionMO");
    }

    @Override()
    public void calculateActivation() {
    }

    @Override()
    public void proc() {
        System.out.println("Executing proc vision");
        c.updateState();
        synchronized (visionMO) {
            List<Thing> lt = c.getThingsInVision();
            visionMO.setI(lt);
        }
    }
}
