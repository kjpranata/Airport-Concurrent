/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package airport;

import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
    
/**
 *
 * @author Kevin Jericho
 */
public class PlaneGenerator implements Runnable{
    
    Airport airport;
    int planeCount = 0;
    
    public PlaneGenerator(Airport airport){
        this.airport = airport;
    }
    
    String[] name = {"Plane A", "Plane B", "Plane C", "Plane D", "Plane E", "Plane F"};
    
    @Override
    public void run() {
        long timeStart = System.currentTimeMillis();
        Random random = new Random();
        while(planeCount <= 5){
            if (airport.Runway.availablePermits() == 1 && (airport.GateA.availablePermits() == 1 || airport.GateB.availablePermits() == 1)){
                String planeName = name[planeCount];
                airport.AirportPool.submit(new Plane (airport, planeName));
                planeCount++;
                
                if ((planeCount + 1) < name.length) {
                    TimeCollections.waitingTime.put(name[planeCount + 1], new Long[]{System.currentTimeMillis(), 0L});
                }
                try {
//                    Thread.sleep(1000);
                    Thread.sleep(random.nextInt(2000));
                } catch (InterruptedException ex) {
                    Logger.getLogger(PlaneGenerator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        try { 
            Thread.sleep(7000);
        } catch (InterruptedException ex) {
            Logger.getLogger(PlaneGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
         
        try{
            airport.AirportPool.shutdown();
            if(!airport.AirportPool.awaitTermination(16, TimeUnit.SECONDS)){
                long timeFinish = System.currentTimeMillis();
                long minimum = -1;
                long maximum = -1;
                long average = 0;
                Set<String> sets = TimeCollections.waitingTime.keySet();
                for (String name : sets) {
                    Long[] time = TimeCollections.waitingTime.get(name);
                    long delta = time[1] - time[0];
                    if (delta < minimum || minimum == -1) {
                        minimum = delta;
                    }
                    if (maximum < delta) {
                        maximum = delta;
                    }
                    average += delta;
                }
                average = average/TimeCollections.waitingTime.size();

                minimum = minimum /1000;
                maximum = maximum /1000;
                average = average /1000;
                
                
                if(airport.GateA.availablePermits()==1 && airport.GateB.availablePermits()==1){
                    System.out.println("ALL GATES ARE CLEARED");
                }else{
                    System.out.println("GATES ARE NOT CLEARED");
                }
                if(airport.Runway.availablePermits()==1){
                    System.out.println("RUNWAY IS CLEARED");
                }else{
                    System.out.println("RUNWAY IS NOT CLEARED");
                }
                System.out.println("Execution Time : " + ((timeFinish - timeStart) / 1000)+ " Seconds");
                System.out.println("Plane Waiting Time minimum: " + minimum + "s, maximum: " + maximum + "s, average: " + average + "s");
                System.out.println("Plane Served : "+ planeCount);
                System.out.println("Passengers Boarded : "+ (airport.passangerId-1));
                
                airport.AirportPool.shutdownNow();
            }
        }catch(Exception e){
            airport.AirportPool.shutdownNow();
        }
    }
}
