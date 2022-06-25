/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package airport;

import java.util.Random;
    
/**
 *
 * @author Kevin Jericho
 */
public class PassengerGenerator implements Runnable{
    
    Airport airport;
    int passangerId = 1;
    
    public PassengerGenerator(Airport airport){
        this.airport = airport;
    }
    
    @Override
    public void run() {
        while(true){
            airport.AirportPool.submit(new Passenger(passangerId, airport));
            passangerId++;
            airport.passangerId = passangerId;
            try{
                Thread.sleep(175*(1+new Random().nextInt(3))); //0.5 - 1.5seconds
            }catch(Exception e){}
        }
    }
}
