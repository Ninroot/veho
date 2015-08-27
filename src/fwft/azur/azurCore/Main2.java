package fwft.azur.azurCore;

public class Main2 {

	public static void main(String[] args) {
		Machine m1 = new Machine("m1", "127.0.0.1");
		Machine m2 = new Machine("m2", "127.0.0.1");
		
		RequestPort rp = new RequestPort(m1, m2);

		Thread tSrp = new Thread(new ServerRequestPort());

		Thread tCrp = new Thread(new ClientRequestPort(rp));
		Thread tCrp2 = new Thread(new ClientRequestPort(rp));
		
		tSrp.start();
		
		tCrp.start();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		tCrp2.start();
		
	}

}
