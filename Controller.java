package datacenter;

import java.util.ArrayList;

public class Controller {

	public Controller() {
		// TODO Auto-generated constructor stub
	}
	
	public static void Error(String error) 
	{
		System.out.println(error);
	}
	
	public static void addVm(VirtualMachine vm, Host host)
	{
		System.out.println("Virtual Machine id:"+vm.getId()+ " added to host:"+ host.getId());
	}
	public static void addHost( Host host)
	{
		System.out.println("Created a new host, Host ID:"+ host.getId());
	}
	//When host becomes fit with a full disk or/and full ram
	public static void hostFit( Host host)
	{
		System.out.println("Host ID: "+ host.getId()+ " became fit with Disk:"+host.getUsedDisk()+" Ram:"+host.getUsedRam());
	}
	//Indicating swap operations ended
	public static void scheduler (Host host)
	{
		System.out.println("Scheduler made Host:"+host.getId()+ " fit");
	}
	// When Schedular swaping vms to make host fit
	public static void swap(Host host1,Host host2, VirtualMachine vm1, VirtualMachine vm2)
	{
		System.out.println("Swapped VM:"+vm1.getId()+" from Host:"+host1.getId()+" with VM:"+vm2.getId()+" from Host: "+host2.getId());
	}
	//Called when swaping multiple vm's in dataCenter.addVM() Part 3
	public static void multipleSwap(Host rootHost, ArrayList<VirtualMachine> option, ArrayList<Host> newArr) {
		System.out.println("Multiple Swap Starting");
		for(int i = 0; i < newArr.size(); i++ )
		{
			System.out.println("Swaped VM:"+option.get(i).getId()+" from Host: "+rootHost.getId()+" into Host: "+newArr.get(i).getId());

		}
		System.out.println("Multiple Swap Concluded.");
	}
	
}
