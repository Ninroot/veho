package azur.veho;

import java.io.File;
import java.io.Serializable;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.jar.Attributes.Name;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.beans.property.SimpleStringProperty;

public class Machine implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1603390231937672788L;
	private SimpleStringProperty name;
	private SimpleStringProperty ipV3;
	private SimpleStringProperty password;
	
	public Machine(SimpleStringProperty name, SimpleStringProperty ipV3) {
		this.name = name;
		this.ipV3 = ipV3;
		this.password = new SimpleStringProperty("azur");
	}
	
	/**
	 * send file 
	 */
	public void send(Machine machineDst, ArrayList<File> fileList) {
		RequestPort requestPort = new RequestPort(Machine.getMyMachine(), machineDst);
		RequestFile requestFile = new RequestFile(fileList);
		
		ClientRequestPort clientRequestPort = new ClientRequestPort(requestPort, requestFile);
		
		Thread tclientRequestPort = new Thread(clientRequestPort);
		tclientRequestPort.start();
	}
	
	/**
	 * return vector of machine from arp
	 */
	public static Vector<Machine> getMachineFromARP() {
		Vector<Machine> machines = new Vector<Machine>();
		String raw = Commande.arp();
		String[] lines = raw.split("\n");

		//machines.add(new Machine("MyLocal", "192.168.1.97"));
		for(String line : lines) {
			Machine m = new Machine(getNameRegex(line), getIpRegex(line));
			machines.add(m);
		}
		
		return machines;
	}
	
	/**
	 * return own machine object
	 * @return
	 */
	public static Machine getMyMachine() {
		return new Machine(new SimpleStringProperty("MyLocal"), new SimpleStringProperty("192.168.1.97"));
	}
	
	public static Machine getMyLocalMachine() {
		String nameIp = null;
		
		try {
			nameIp = Inet4Address.getLocalHost().toString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		return new Machine(Machine.getNameRegex(nameIp), Machine.getIpRegex(nameIp));
	}
	
	/**
	 * return hostname from arp line
	 * @param line
	 * @return
	 */
	private static SimpleStringProperty getNameRegex(String line) {
		String namePattern = "^[a-z]+.[a-z]+"; //http://stackoverflow.com/questions/15875013/extract-ip-addresses-from-strings-using-regex
		Pattern pattern = Pattern.compile(namePattern);
		Matcher matcher = pattern.matcher(line);
		
        if (matcher.find())
            return new SimpleStringProperty(matcher.group(0));
        else
            return new SimpleStringProperty("Unknown");
	}
	
	/**
	 * return ip from arp line
	 * @param line
	 * @return
	 */
	private static SimpleStringProperty getIpRegex(String line) {
		String ipPattern = "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)"; //http://stackoverflow.com/questions/15875013/extract-ip-addresses-from-strings-using-regex
		Pattern pattern = Pattern.compile(ipPattern);
		Matcher matcher = pattern.matcher(line);
		
        if (matcher.find())
            return new SimpleStringProperty(matcher.group(0));
        else
            return new SimpleStringProperty("0.0.0.0");
	}
	
	public String toString() {
		return "name: "+name+" ip:"+ipV3+" password:"+password;
	}
	
	public SimpleStringProperty getName() {
		return name;
	}

	public void setName(SimpleStringProperty name) {
		this.name = name;
	}

	public SimpleStringProperty getIpV3() {
		return ipV3;
	}

	public void setIpV3(SimpleStringProperty ipV3) {
		this.ipV3 = ipV3;
	}

	public SimpleStringProperty getPassword() {
		return password;
	}

	public void setPassword(SimpleStringProperty password) {
		this.password = password;
	}
}
