package suzukiKasami;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class RMIServer {
	public static void main(String[] args) {
        try {
        	LocateRegistry.createRegistry(1099); //Start RMI Registry
            TokenManagerInterface tokenManager = new TokenManager();
            Naming.rebind("rmi://localhost/TokenManager", tokenManager);
            System.out.println("TokenManager ready");
            
            for (int i = 0; i < 3; i++) {		//Initializing processes and assigning them to TokenManager
                Process process = new Process(i, tokenManager);
                Naming.rebind("//localhost/Process" + i, process);
            }
            System.out.println("Processes are ready.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
