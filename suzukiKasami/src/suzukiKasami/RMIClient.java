package suzukiKasami;

import java.rmi.Naming;

public class RMIClient {
	public static void main(String[] args) {
		try {
			//Lookup and assign processes to process objects
			ProcessInterface process0 = (ProcessInterface) Naming.lookup("//localhost/Process0");
		    ProcessInterface process1 = (ProcessInterface) Naming.lookup("//localhost/Process1");
		    ProcessInterface process2 = (ProcessInterface) Naming.lookup("//localhost/Process2");
		    
		    //Logic for token passing
		    process0.requestCriticalSection();
		    process1.requestCriticalSection();
		    process0.releaseCriticalSection();
		    process2.requestCriticalSection();
		    process1.releaseCriticalSection();
		    process2.releaseCriticalSection();
		    
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
