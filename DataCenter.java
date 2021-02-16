package datacenter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList; // import the ArrayList class
import java.util.Scanner;  // Import the Scanner class

public class DataCenter {
	ArrayList<Host> hosts = new ArrayList<Host>();
	int vmIndex = 0; //This is for giving personal ID for each VM
	int hostIndex = 0;
	
	public DataCenter() {
	}

	public void newHost(int disk,int ram) { // Adds a new Host
		Host newHost = new Host(hostIndex, disk, ram);
		System.out.println("Added a new host : "+ newHost.toString());
		hosts.add(newHost);
		hostIndex++;
	}
	
	public boolean newVM(int disk, int ram){
		VirtualMachine newVm = new VirtualMachine(vmIndex, disk,ram);
		boolean result = false;
		Host currentHost; 
		
		
		for (int i = 0; i < hosts.size(); i++)// Loops to find if it fits to any virtual machine
		{
			currentHost = hosts.get(hosts.size() - 1- i);  // Loops from the fullest to the emptiest
			System.out.println(currentHost.searchForMultiple(newVm));
			if(currentHost.hasEnoughSpace(newVm) && currentHost.isFit(newVm)) // Checks if it has space and
			{																//if it completes the disk or ram space of the host					System.out.println("\tAdding VM:"+ vmIndex+ " into the host: "+currentHost.getId());
				System.out.println("\tFound a HOST that fit! Adding VM:"+ vmIndex+ " into the host: "+currentHost.getId());

				currentHost.addVM(newVm); // VM will be added into the host and the host will sort it's VM's
				vmIndex++; // Increments the Index 
				result = true; // This means that it's inserted and don't need to get into the later if clauses
				break;
			}
		}
		if(!result) // Loops to find and empty space to be placed in
		{
			for (int i = 0; i < hosts.size(); i++) 	//This loop is for placing the vm into the emptiest host
			{
				currentHost = hosts.get(i);
				if(currentHost.hasEnoughSpace(newVm)) // If it has space it gets inserted
				{
					currentHost.addVM(newVm); // Adds into the host and the host does sort
					vmIndex++;
					System.out.println("\tAdding VM:"+ vmIndex+ " into the host: "+currentHost.getId());
					result = true;
					break;
				}
				
			}
		}
		
		VirtualMachine currentVm ;
		Host otherHost;
		if(!result) // This if clause is for emptying up space for the vm
		{
			for(int i = 0; i < hosts.size(); i++) // Loops to find a host to empty up space for
			{
				currentHost = hosts.get(i); 
				if(!currentHost.full) // If it's full then we stop. We shouldn't touch full VM's
				{
					for(int j = 0; j < currentHost.vmSize(); j++) // Loops to find a VM to swap out
					{
						currentVm = currentHost.getVm(j); 
						if(	currentHost.getAvailableDisk() + currentVm.getDisk()- newVm.getDisk() >= 0  // This checks if new vm will fit after we swap out
						&&currentHost.getAvailableRam()+ currentVm.getRam()- newVm.getRam() >=0)
						{
							for (int k = 0; k < hosts.size(); k++) // This is for searching new Host's that we can transfer the currentVM into
							{
								otherHost = hosts.get(k); 
								if(k != i && !otherHost.full &&  otherHost.hasEnoughSpace(currentVm)) 
								{
									currentHost.removeVM(currentVm.getId()); //Transfers the VM to open up space
									otherHost.addVM(currentVm); 
									
									currentHost.addVM(newVm); // Adds the vm into the currentHost
									result = true;
									break;
								}
							}
						}
						if(result)
						{
							break;
						}
					}
				}
				if (result)
				{
					break;
				}
			}
		}
		
		
		
		if(result) // These checks happen each time a vm is inserted. 
		{
			this.bubbleSortHost(); // Sorts the Host array
			this.searchForFit(); // Swaps vm's to get into the FIT position
		}
		
		else if(!result)
		{
			System.out.println("\tVM was unable to find a host.");
		}
		
		return result;
	}
	
	public void bubbleSortHost() {
		Host a;
		Host b; 
		for (int i = 0; i < hosts.size()-1; i++) // Limit the index
		{
			for(int j = 0; j <hosts.size()-i-1; j++) // Iterate while comparing j with j+1 
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
	
	public void searchForFit()
	{
		Host hostIndex;
		VirtualMachine vmIndex;
		int availableRam;
		int availableDisk;
		int result;
		boolean swapped;
		for (int i = hosts.size()-1; i >= 0; i--) // This is for the host we are trying to make full
		{
			swapped = false;
			hostIndex = hosts.get(i);
			availableRam= hostIndex.getAvailableRam();
			availableDisk= hostIndex.getAvailableDisk();
			if(!hostIndex.full) // if the host is full we dont modify it
			{
				for(int j = 0; j < hostIndex.vmSize() ; j++) // This is for iterating over the vm's the host has
				{// We try to swap a vm which has x size with an x + available disk sized vm which will make our host full
					vmIndex = hostIndex.getVm(j);
					for (int k = 0; k < hosts.size(); k++) 
					{
						if(k != i && !hosts.get(k).full)
						{ // searchDiskVM Method searchs for a applicable vm with respect to the current RAM
					      // Since it might find a VM with a suitable disk space
							result = hosts.get(k).searchDiskVM(availableDisk + vmIndex.getDisk(), availableRam, vmIndex.getRam());
							if (result >= 0 ) 
							{
								if (this.swapVMbetweenHosts(i, j, k, result)) // Params are (Host1 index,vm1 index, host2 index, vm2 index)
								{
									swapped = true;
									break;
								}
							}
						}
					}
					if(swapped)
					{
						break;
					}
					
				}
			}
			
		}
	}
	
	public boolean swapVMbetweenHosts(int host1i,int vmHost1i, int host2i, int vmHost2i )
	{
		Host host1 = hosts.get(host1i);
		VirtualMachine vmHost1 = host1.getVm(vmHost1i);

		Host host2 = hosts.get(host2i);
		VirtualMachine vmHost2 = host2.getVm(vmHost2i);
		
		
		host1.removeVM(vmHost1.getId()); // Removes the vms
		host2.removeVM(vmHost2.getId());
		if (host1.addVM(vmHost2) == false || host2.addVM(vmHost1) == false)
		{
			//host1.removeVM(vmHost2.getId());
			//host2.removeVM(vmHost1.getId());
			host1.addVM(vmHost1); // swaps the hosts
			host2.addVM(vmHost2);
			System.out.println("Couldnt swap"+vmHost1.toString()+" with-> "+vmHost2.toString());
			return false;
		}
		System.out.println("\tSwapped host:"+host1.getId()+"'s "+vmHost1.toString()+" <-with-> host:"+host2.getId()+"'s "+vmHost2.toString());
		return true;
		
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

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DataCenter myCenter = new DataCenter();
	//	myCenter.newHost(1000, 1000);
	//	myCenter.newHost(1000, 1000);
	//	myCenter.newHost(1000, 1000);

	//	myCenter.newVM(400, 150);	
		//for(int i = 1; i <=9  ; i++)
		//{
			//myCenter.newVM(i*50, 150);	
			//System.out.println(myCenter.toString());
//		}
		boolean template = true; // If true it reads from templates
		if(template)
		{
			try {

				File hostFile = new File("./src/datacenter/scenarios/host.txt");
				Scanner hostReader;
				hostReader = new Scanner(hostFile);
				String line;
				String[] lineArr;
				while(hostReader.hasNextLine())
				{
					line = hostReader.nextLine();
					lineArr =line.split("\\s");//splits the string based on whitespace  
					myCenter.newHost(Integer.parseInt(lineArr[0]), Integer.parseInt(lineArr[1])); //Each line must have 2 integers
				}																			//   Which represents 1)Disk and 2)RAM
				File vmFile = new File("./src/datacenter/scenarios/vm.txt");
				Scanner vmReader;
				vmReader = new Scanner(vmFile);
				
				
				while(vmReader.hasNextLine())
				{
					line = vmReader.nextLine();
					lineArr =line.split("\\s");//splits the string based on whitespace  
					myCenter.newVM(Integer.parseInt(lineArr[0]), Integer.parseInt(lineArr[1])); //Each line must have 2 integers
				}																
			} catch (FileNotFoundException e1) {

				e1.printStackTrace();
			}
			
		}
		
		boolean loop = true;     
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);  // Create a Scanner object
		String command;
		int disk;
		int ram;
		while(loop)
		{
			System.out.println("Enter'vm' for adding a new vm.\t\tEnter 'host' for adding a new host\nEnter 'status' to get info about hosts\tEnter 'exit' to end the simulation...");
			command = scanner.nextLine();  // Read user input
			if(command.equals("exit"))
			{
				loop = false;
			}
			if(command.equals("status"))
			{
				System.out.println(myCenter.toString()+"\n");
			}
			if(command.equals("vm"))
			{
				try {
					System.out.println("What's the disk space?");
					command = scanner.nextLine();  // Read user input
					disk = Integer.parseInt(command);
					System.out.println("What's the ram space?");
					command = scanner.nextLine();  // Read user input
					ram = Integer.parseInt(command);
					myCenter.newVM(disk, ram);
				}
				catch(NumberFormatException e)
				{
					System.out.println("Please only write integers for the required disk or ram spaces...");
					continue;
				}
				
			}
			if(command.equals("host"))
			{
				try {
					System.out.println("What's the total disk space?");
					command = scanner.nextLine();  // Read user input
					disk = Integer.parseInt(command);
					System.out.println("What's the total ram space?");
					command = scanner.nextLine();  // Read user input
					ram = Integer.parseInt(command);
					myCenter.newHost(disk, ram);
				}
				catch(NumberFormatException e)
				{
					System.out.println("Please only write integers for the required disk or ram spaces...");
					continue;
				}
				
			}
		}
		System.out.println("Application terminated");
		
	}

}
