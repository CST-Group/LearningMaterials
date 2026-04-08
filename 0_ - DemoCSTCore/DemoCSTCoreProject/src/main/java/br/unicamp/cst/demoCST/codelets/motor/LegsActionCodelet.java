package br.unicamp.cst.demoCST.codelets.motor;

import org.json.JSONObject;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryContainer;
import java.util.Random;
import java.util.logging.Logger;
import org.json.JSONException;
import static support.Constants.*;
import ws3dproxy.model.Creature;

/**
 *  Legs Action Codelet monitors working storage for instructions and acts on the World accordingly.
 *  
 * @author klaus
 *
 */

public class LegsActionCodelet extends Codelet{

	private Memory legsActionMO;
	private double previousTargetx=0;
	private double previousTargety=0;
	private String previousLegsAction="";
        private Creature c;
        double old_angle = 0;
        int k=0;
        static Logger log = Logger.getLogger(LegsActionCodelet.class.getCanonicalName());

	public LegsActionCodelet(Creature nc) {
            c = nc;
            this.name = "LegsActionCodelet";
	}
	
	@Override
	public void accessMemoryObjects() {
            legsActionMO=(MemoryContainer)this.getInput("legsMC");
	}
	
	@Override
	public void proc() {
            System.out.println("Executing proc Legs");

            String comm = (String) legsActionMO.getI();
            if (comm == null) comm = "";
            Random r = new Random();

            if(!comm.equals("") ){
                try {
                    JSONObject command=new JSONObject(comm);
                    if (command.has(ACTION)) {
                        int x=0,y=0;
                        String action=command.getString(ACTION);
                        if(action.equals(EXPLORE)){
                            if (!comm.equals(previousLegsAction)) { 
                            //if (!comm.equals(previousLegsAction)) 
                                 System.out.println("Sending Explore command to agent");
                             try {  
                                   c.rotate(2);     
                             } catch (Exception e) {
                                 e.printStackTrace();
                             }
                            } 
                        }
                        else if(action.equals(GOTO)){
                            if (!comm.equals(previousLegsAction)) {
                                double speed=command.getDouble("SPEED");
                                double targetx=command.getDouble("X");
                                double targety=command.getDouble("Y");
                                if (!comm.equals(previousLegsAction))
                                    System.out.println("Sending move command to agent "+ c.getName()+" : ["+targetx+","+targety+"]");
                                try {
                                     c.moveto(speed, targetx, targety);
                                } catch(Exception e) {
                                    e.printStackTrace();
                                }
                                previousTargetx=targetx;
                                previousTargety=targety;
                            }

                        } else {
                            log.info("Sending stop command to agent");
                            try {
                                 c.moveto(0,0,0);
                            } catch(Exception e) {
                                e.printStackTrace();
                            }  
                        }
                    }
                    previousLegsAction=comm;
                    k++;	
                } catch (JSONException e) {e.printStackTrace();}
            }
            else {
                previousLegsAction = comm;
                log.info("Sending stop command to agent");
                try {
                     //c.moveto(0,0,0);
                } catch(Exception e) {
                    e.printStackTrace();
                }  
            }
	}//end proc

    @Override
    public void calculateActivation() {
        activation = 0.9d;
    }
}
