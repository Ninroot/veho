package fwft.azur.azurCore;

public class Main2 {

	public static void main(String[] args) {
		Machine m = new Machine("m1", "127.0.0.1");
		
		RequestPort rp = new RequestPort(m, m);

		Thread tSrp = new Thread(new ServerRequestPort());
		tSrp.start();
		
		for(int i=0; i<5; i++) {
			Thread tCrp = new Thread(new ClientRequestPort(rp));
			tCrp.start();			
		}
		
	}

}
