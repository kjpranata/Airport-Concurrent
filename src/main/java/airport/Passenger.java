/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package airport;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kevin Jericho
 */
public class Passenger implements Runnable{
    int passengerId;
    Airport airport;
    boolean found;

    public Passenger(int passengerId, Airport airport) {
        this.passengerId = passengerId;
        this.airport = airport;
    }
    
    public void EnterPlane() throws InterruptedException{
        //found itu gunanya biar passenger pasti ketemu pesawat, kalo ini gada passenger ada yg ketinggalan ilang dia
        while(!found){
            if(airport.priority == 0){
                if(airport.GateA.availablePermits()==0 && airport.CleanerA == 1){
                    airport.maxPassangerA.acquire();
                    found = true;
                    System.out.println("Passanger "+ passengerId+ " has Entered "+  airport.GateAA);
                }else if(airport.GateB.availablePermits()==0 && airport.CleanerB == 1){
                    airport.maxPassangerB.acquire();
                    found = true;
                    System.out.println("Passanger "+ passengerId+ " has Entered "+  airport.GateBB);
                }
            }else{
                if(airport.GateB.availablePermits()==0 && airport.CleanerB == 1){
                    airport.maxPassangerB.acquire();
                    found = true;
                    System.out.println("Passanger "+ passengerId+ " has Entered "+  airport.GateBB);
                }else if(airport.GateA.availablePermits()==0 && airport.CleanerA == 1){
                    airport.maxPassangerA.acquire();
                    found = true;
                    System.out.println("Passanger "+ passengerId+ " has Entered "+  airport.GateAA);
                }
            }
        }
    }
      
    @Override
    public void run() {
        try {
            found = false;
            EnterPlane();
        } catch (InterruptedException ex) {
            Logger.getLogger(Passenger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
