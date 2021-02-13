package datacenter;

import java.util.ArrayList; // import the ArrayList class

public class DataCenter {
	ArrayList<Host> hosts = new ArrayList<Host>();
	int vmIndex = 0; //This is for giving personal ID for each VM
	int hostIndex = 0;
	
	public DataCenter() {
		System.out.println("Hello");
	}

	public void newHost(int disk,int ram) {
		Host newHost = new Host(hostIndex, disk, ram);
		hosts.add(newHost);
		hostIndex++;
	}
	
	public boolean newVM(int disk, int ram){
		VirtualMachine newVm = new VirtualMachine(vmIndex, disk,ram);
		boolean result = false;
		Host currentHost; 
		for (int i = 0; i < hosts.size(); i++)
		{
			currentHost = hosts.get(i);
			if(currentHost.hasEnoughSpace(newVm))
			{
				currentHost.addVM(newVm);
				vmIndex++;
				result = true;
				break;
			}
				
		}
		if(result)
		{
			this.bubbleSortHost();
		}
		
		return result;
	}
	
	public void bubbleSortHost() {
		Host a;
		Host b; 
		for (int i = 0; i < hosts.size()-1; i++)
		{
			for(int j = 0; j <hosts.size()-i-1; j++)
			{
				a = hosts.get(j);
				b = hosts.get(j+1);
				if(a.getAvailableDisk() < b.getAvailableDisk()) // Sort by which has the most available disk space
				{
					this.swapHost(j,j+1);
				}
				else if(a.getAvailableDisk() == b.getAvailableDisk() && a.getAvailableRam() < b.getAvailableRam()) // If disk space is equal, choose the one with more ram
				{
					this.swapHost(j,j+1);
				}
			}
		}
	}
	
	public void swapHost(int a, int b) {
		Host placeHolder = hosts.get(a);
		hosts.set(a, hosts.get(b));
		hosts.set(b, placeHolder);
	}
	
	@Override
	public String toString() {
		String total = "\n";
		for(int i = 0; i < hosts.size(); i++)
		{
			total += hosts.get(i).toString();
			total+= "\n";
		}
		total += "Total Hosts:" + hostIndex + ", Total VM's:" + vmIndex;
		return total;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DataCenter myCenter = new DataCenter();
		myCenter.newHost(1000, 1000);
		myCenter.newHost(1000, 1000);
		myCenter.newHost(1000, 1000);

		System.out.println(myCenter.toString());
		myCenter.newVM(200, 150);
		System.out.println(myCenter.toString());
		myCenter.newVM(200, 150);
		System.out.println(myCenter.toString());
		myCenter.newVM(200, 150);
		System.out.println(myCenter.toString());
		myCenter.newVM(200, 200);
		System.out.println(myCenter.toString());
		System.out.println(myCenter.toString());
		myCenter.newVM(200, 150);
		System.out.println(myCenter.toString());
		myCenter.newVM(200, 150);
		System.out.println(myCenter.toString());
		myCenter.newVM(200, 150);
		System.out.println(myCenter.toString());
		myCenter.newVM(200, 200);
		System.out.println(myCenter.toString());
		myCenter.newVM(200, 150);
		System.out.println(myCenter.toString());
		myCenter.newVM(200, 150);
		System.out.println(myCenter.toString());
		myCenter.newVM(200, 150);
		System.out.println(myCenter.toString());
		myCenter.newVM(200, 200);
		System.out.println(myCenter.toString());
		System.out.println(myCenter.toString());
		myCenter.newVM(200, 150);
		System.out.println(myCenter.toString());
		myCenter.newVM(200, 150);
		System.out.println(myCenter.toString());
		myCenter.newVM(200, 150);
		System.out.println(myCenter.toString());
		myCenter.newVM(200, 200);
		System.out.println(myCenter.toString());
	}

}
