package suzukiKasami;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

public class Process extends UnicastRemoteObject implements ProcessInterface {
	private int processId;
	private int sequenceNumber;
	private boolean inCriticalSection;
	private TokenManagerInterface tokenManager;
	
	protected Process(int processId, TokenManagerInterface tokenManager) throws RemoteException {
		this.processId = processId;
		this.sequenceNumber = 0;
		this.inCriticalSection = false;
		this.tokenManager = tokenManager;
	}
	
	@Override
    public void requestCriticalSection() throws RemoteException {
        sequenceNumber++;	//Increment whenever a request is made
        System.out.println("Process " + processId + " requesting critical section with sequence number " + sequenceNumber);
        tokenManager.requestEntry(processId, sequenceNumber);	//Pass request to TokenManager
    }

    @Override
    public void releaseCriticalSection() throws RemoteException {
    	if (inCriticalSection) {	//Make sure process is in critical section before trying to release it
            inCriticalSection = false;	//Release critical section
            System.out.println("Process " + processId + " releasing critical section.");
            tokenManager.releaseToken(processId, sequenceNumber); //Notify the TokenManager
        } else {
            System.out.println("Process " + processId + " is not in the critical section.");
        }
    }

    @Override
    public int getSequenceNumber() throws RemoteException {
        return sequenceNumber;
    }

    @Override
    public void receiveGrant() throws RemoteException {	//Grant access to critical section
        this.inCriticalSection = true;
        System.out.println("Process " + this.processId + " received the token and entered critical section.");
    }
	
}
