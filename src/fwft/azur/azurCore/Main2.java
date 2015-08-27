package fwft.azur.azurCore;

public class Main2 {

	public static void main(String[] args) {
		Machine m1 = new Machine("m1", "127.0.0.1");
		Machine m2 = new Machine("m2", "127.0.0.1");
		
		RequestPort rp = new RequestPort(m1, m2);
		ClientRequestPort crp = new ClientRequestPort(rp);
		ServerRequestPort srp = new ServerRequestPort();

		Thread tCrp = new Thread(crp);
		Thread tSrp = new Thread(srp);
		
		tCrp.start();
		tSrp.start();
	}

}
