/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.unicamp.cst.demoCST;

import support.ResourcesGenerator;
import ws3dproxy.CommandExecException;
import ws3dproxy.WS3DProxy;
import ws3dproxy.model.Creature;
import ws3dproxy.model.World;
import ws3dproxy.util.Constants;
import ws3dproxy.util.Logger;

public final class Environment {
    
    public String host="localhost";
    public int port = 4011;
    public String robotID="r0";
    public Creature c = null;
    
    public Environment() {
          WS3DProxy proxy = new WS3DProxy();
          try {   
             World w = World.getInstance();
             w.reset();
             World.createFood(0, 350, 75);
             World.createFood(0, 100, 220);
             World.createFood(0, 250, 210);
             c = proxy.createCreature(100,450,0,0);
             c.start();
             grow(w,7);
          } catch (CommandExecException e) {
              
          }
          System.out.println("Robot "+c.getName()+" is ready to go.");
	}
    
    public synchronized void grow(World w, int time) {
        try {
            if (time <= 0) {
                time = Constants.TIMEFRAME;
            }
            w.getDimensionAndDeliverySpot();
            ResourcesGenerator rg = new ResourcesGenerator(time, w.getEnvironmentWidth(), w.getEnvironmentHeight(), w.getDeliverySpot().getX(), w.getDeliverySpot().getY());
            rg.start();
        } catch (CommandExecException ex) {
            Logger.logException(World.class.getName(), ex);
        }
    }
}
