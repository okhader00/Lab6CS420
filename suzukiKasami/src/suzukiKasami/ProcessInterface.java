package suzukiKasami;

import java.rmi.*;

public interface ProcessInterface extends Remote {
	void requestCriticalSection() throws RemoteException;
	void releaseCriticalSection() throws RemoteException;
	void receiveGrant() throws RemoteException;
	int getSequenceNumber() throws RemoteException;
}


