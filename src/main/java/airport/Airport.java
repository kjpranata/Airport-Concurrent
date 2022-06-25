package airport;
import java.util.concurrent.*;

/**
 *
 * @author Kevin Jericho
 */
public class Airport extends Thread {
    Semaphore Runway =  new Semaphore(1, true);
    Semaphore GateA =  new Semaphore(1, true);
    Semaphore GateB =  new Semaphore(1, true);
    
    Semaphore maxPassangerA = new Semaphore(30, true);
    Semaphore maxPassangerB = new Semaphore(30, true);
    Semaphore Refueler = new Semaphore(1, true);
    
    //Buat save nama pesawat biar bisa di passing/access
    String GateAA;
    String GateBB;
    
    //for pesawat udah di clean and refuel apa belom
    int CleanerA = 0;
    int CleanerB = 0;
    
    //buat generate passanger dr no 1
    int passangerId;
    
    //priority untuk passenger biar masuk ke pesawat yang dock deluan di gate
    int priority = 0;
    
    ExecutorService AirportPool = Executors.newCachedThreadPool();
    
    public static void main(String[] args) {
        Airport Airport = new Airport();
        Airport.start();
    }
    
    public Airport(){
        passangerId = 1;
    }
    
        @Override
    public void run(){
        //executor service built in function 
        AirportPool.submit(new PassengerGenerator(this));
        AirportPool.submit(new PlaneGenerator(this));
    }
}