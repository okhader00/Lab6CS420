package suzukiKasami;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.PriorityQueue;
import java.util.Comparator;

public class TokenManager extends UnicastRemoteObject implements TokenManagerInterface {
	private int currentHolder;
	private PriorityQueue<Request> requestQueue;
	
	protected TokenManager() throws RemoteException {
        this.currentHolder = -1; //No holder initially
        this.requestQueue = new PriorityQueue<>(Comparator.comparing(Request::getPriority));
    }
	
	@Override
    public void requestEntry(int processId, int sequenceNumber) throws RemoteException {
        System.out.println("TokenManager received request from Process " + processId + " with sequence number " + sequenceNumber);
        requestQueue.add(new Request(processId, sequenceNumber));	//Add request to the queue whenever one is made
        grantTokenIfEligible();
    }

    @Override
    public void releaseToken(int processId, int sequenceNumber) throws RemoteException {
    	if (currentHolder == processId) {	//Make sure that the process trying to release the token holds it in the first place
            System.out.println("TokenManager received release from Process " + processId);
            currentHolder = -1; //Reset the token holder
            grantTokenIfEligible(); //Check if there's another process waiting
        } else {
            System.out.println("Error: Process " + processId + " does not hold the token.");
        }
    }

    private void grantTokenIfEligible() throws RemoteException {
        if (currentHolder == -1 && !requestQueue.isEmpty()) {	//If no process is currently holding the token
            Request nextRequest = requestQueue.poll();
            currentHolder = nextRequest.getProcessId();	//Grant token to process with next request in the queue
            System.out.println("TokenManager granting token to Process " + currentHolder);
			try {
				ProcessInterface process = (ProcessInterface) Naming.lookup("//localhost/Process" + currentHolder);	//Lookup process and have it receive the grant
				process.receiveGrant();
			} catch (MalformedURLException | RemoteException | NotBoundException e) {
				e.printStackTrace();
			}
        }
        else if (currentHolder != -1) {	//Do not pass token if a process is already holding it
        	System.out.println("TokenManager: Token is currently held by Process " + currentHolder);
        }
        else {
        	System.out.println("TokenManager: No pending requests.");
        }
    }
}

class Request {	//Class to hold requests and more easily process them
    private int processId;
    private int sequenceNumber;

    public Request(int processId, int sequenceNumber) {
        this.processId = processId;
        this.sequenceNumber = sequenceNumber;
    }

    public int getProcessId() {
        return processId;
    }

    public int getPriority() {
        return sequenceNumber;
    }
}