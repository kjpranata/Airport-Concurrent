package airport;
import java.util.Date;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kevin Jericho
 */
public class Plane implements Runnable{
    int passangerPopulation = 0;
    Airport airport;
    String Name;    

    public Plane(Airport airport, String Name) {
        this.airport = airport;
        this.Name = Name;
    }
    
    public void EnterRunway() throws InterruptedException{
        boolean hasEntered = false;
        while(hasEntered != true){
            if (airport.Runway.availablePermits() == 1 && (airport.GateA.availablePermits() == 1 || airport.GateB.availablePermits() == 1)){
                airport.Runway.acquire();
                System.out.println("["+new Date()+"]"+ Name +" is requesting for landing.");
                Thread.sleep(250);
                System.out.println(Name +" has landed safely to the runway.");
                Thread.sleep(500);
                
                Long[] time = TimeCollections.waitingTime.get(Name);
                if (time != null) {
                    time[1] = System.currentTimeMillis();
                }
                
                EnterGate();
                hasEntered = true;
                break;
            }else{
                System.out.println(Name +" is waiting to enter the runway.");
                Thread.sleep(500);
            }   
        }
    }
    
    public void EnterGate() throws InterruptedException{
        if(airport.GateA.availablePermits() == 1){
            airport.Runway.release();
            airport.GateA.acquire();
            airport.GateAA = Name;
            System.out.println(Name +" has Entered Gate A");
            
            System.out.println("Passengers from "+ Name +" have been disembarked.");
            Thread.sleep(500);
            System.out.println(Name +" is cleaned and restocked.");
            
            if(airport.Refueler.availablePermits() == 1){
                airport.Refueler.acquire();
                System.out.println(Name+ " has been refueled.");
                Thread.sleep(500);
                airport.Refueler.release();
            }else{
                
                //nunggu sampai refueler udah bisa dipake, biar gantian la isi bensinnya kan itu requirementnya
                while(airport.Refueler.availablePermits() == 0 ){}
                airport.Refueler.acquire();
                System.out.println(Name+ " has been refueled.");
                Thread.sleep(500);
                airport.Refueler.release();
            }
            
            airport.CleanerA = 1;
            
            //pesawat nunggu di gate 12,5 detik baru takeoff
            Thread.sleep(6250);
            
            airport.priority = 1;
            airport.GateA.release();
            airport.maxPassangerA = new Semaphore(30, true);
            System.out.println(Name +" has Left Gate A and entered runway.");
            System.out.println(Name +" has sucessfully took off.");
            
            airport.CleanerA = 0;
        }else if(airport.GateB.availablePermits() == 1){
            airport.Runway.release();
            airport.GateB.acquire();
            airport.GateBB = Name;
            System.out.println(Name +" has Entered Gate B");
            
            System.out.println("Passengers from "+ Name +" have been disembarked.");
            Thread.sleep(500);
            System.out.println(Name+" is cleaned and restocked.");
            
            if(airport.Refueler.availablePermits() == 1){
                airport.Refueler.acquire();
                System.out.println(Name+ " has been refueled.");
                Thread.sleep(500);
                airport.Refueler.release();
            }else{
                while(airport.Refueler.availablePermits() == 0 ){}
                airport.Refueler.acquire();
                System.out.println(Name+ " has been refueled.");
                Thread.sleep(500);
                airport.Refueler.release();
            }
            
            airport.CleanerB = 1;
            
            Thread.sleep(6250);
            
            airport.priority = 0;
            airport.GateB.release();
            airport.maxPassangerB = new Semaphore(30, true);
            System.out.println(Name +" has Left Gate B and entered runway.");
            System.out.println(Name +" has sucessfully took off.");
            airport.CleanerB = 0;
        }
    }
    
    @Override
    public void run() {
        try {
            EnterRunway();
        } catch (InterruptedException ex) {
            Logger.getLogger(Plane.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
} 
