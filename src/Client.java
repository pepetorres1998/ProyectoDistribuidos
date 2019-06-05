import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class Client {
	public String cpu;
	public String ram;
	public String os;
	public String version;
	public String ip;

	public Client(){
		this.cpu = getCpu();
		this.ram = getAllocatedRam();
		this.os = getOS();
		this.version = getVersion();
		this.ip = getIp();
	}

	public Client(String cpu, String ram, String os, String version, String ip) {
		this.cpu = cpu;
		this.ram = ram;
		this.os = os;
		this.version = version;
		this.ip = ip;
	}

	public String labelText() {
		return String.format("%s %s %s %s %s", cpu, ram, os, version, ip);
	}

	public double getRam() {
		return Double.parseDouble(ram.substring(0, ram.length() - 3));
	}

	public double getCpuDouble() {
		return Double.parseDouble(cpu.substring(0, cpu.length() - 3));
	}

	public String getOS()
	{
		return System.getProperty("os.name").toLowerCase();
	}

	public String getVersion()
	{
		return System.getProperty("os.version");
	}

	public long getTotalRam()
	{
		long ram = 0;
		OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
		for (Method method : operatingSystemMXBean.getClass().getDeclaredMethods()) {
			method.setAccessible(true);
			if (method.getName().startsWith("getTotalPhysicalMemorySize") && Modifier.isPublic(method.getModifiers())) {
				Object value;
				try {
					value = method.invoke(operatingSystemMXBean);
				} catch (Exception e) {
					value = e;
				} // try
				ram = (long) value;
			} // if
		}// for
		return ram;
	}

	public long getFreeRam()
	{
		long ram = 0;
		OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
		for (Method method : operatingSystemMXBean.getClass().getDeclaredMethods()) {
			method.setAccessible(true);
			if (method.getName().startsWith("getFreePhysicalMemorySize") && Modifier.isPublic(method.getModifiers())) {
				Object value;
				try {
					value = method.invoke(operatingSystemMXBean);
				} catch (Exception e) {
					value = e;
				} // try
				ram = (long) value;
			} // if
		}// for
		return ram;
	}

	public String getAllocatedRam()
	{
		return this.humanReadableByteCount(getTotalRam() - getFreeRam(), true);
	}

	public String getCpu()
	{
		Double cpu_load = 0.0;
		OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
		for (Method method : operatingSystemMXBean.getClass().getDeclaredMethods()) {
			method.setAccessible(true);
			if (method.getName().startsWith("getSystemCpuLoad") && Modifier.isPublic(method.getModifiers())) {
				Object value;
				try {
					value = method.invoke(operatingSystemMXBean);
				} catch (Exception e) {
					value = e;
				} // try
				cpu_load = (Double) value;
			} // if
		}// for
		String result = String.format("%.3f", (cpu_load * 100)) + "%";
		return result;
	}

	public String getIp() {
		String ip = "";
		
        Enumeration<NetworkInterface> nets;
		try {
			nets = NetworkInterface.getNetworkInterfaces();
	        for (NetworkInterface netint : Collections.list(nets))
	        {
	            Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
	            for (InetAddress inetAddress : Collections.list(inetAddresses)) {
	            	String inetadd = inetAddress + "";
	            	inetadd = inetadd.substring(1);
		        	if (inetadd.startsWith("192")) {
		        		ip = inetadd;
		        	}
	            }

	        }
	        
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ip;
	}

    static void displayInterfaceInformation(NetworkInterface netint) throws SocketException {
        Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
        for (InetAddress inetAddress : Collections.list(inetAddresses)) {
        }
     }
    
	private static void printUsage() {
		OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
		for (Method method : operatingSystemMXBean.getClass().getDeclaredMethods()) {
			method.setAccessible(true);
			if (method.getName().startsWith("get") && Modifier.isPublic(method.getModifiers())) {
				Object value;
				try {
					value = method.invoke(operatingSystemMXBean);
				} catch (Exception e) {
					value = e;
				} // try
				//System.out.println(method.getName() + " = " + value);
			} // if
		} // for
	}

	public String humanReadableByteCount(long bytes, boolean si) {
		int unit = si ? 1000 : 1024;
		if (bytes < unit) return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
}
