package datacenter;
import java.util.ArrayList; // import the ArrayList class

public class Host {
	private int disk;
	private int ram;
	private int id;

	private int usedDisk = 0;
	private int usedRam = 0;

	private ArrayList<VirtualMachine> virtualMachines = new ArrayList<VirtualMachine>();
	
	public boolean full = false;
	
	public Host(int id, int disk , int ram) {
		this.disk =disk;
		this.ram = ram;
		this.id = id;
	}
	
	public boolean addVM(VirtualMachine newVm) {
		if(newVm.getDisk() <= (disk- usedDisk) && newVm.getRam() <= (ram- usedRam))
		{
			virtualMachines.add(newVm);
			usedDisk += newVm.getDisk();
			usedRam += newVm.getRam();
			if(disk == usedDisk || ram == usedRam)
			{
				full = true;
			}
			this.bubbleSort();
			return true;
		}
		System.out.println("Couldn't add "+ newVm.toString()+ " in Host :"+this.toString());
		return false;
	}
	
	public void removeVM(int id) {
		VirtualMachine indexVM; // Virtual machine to iterate through
		for (int i = 0; i < virtualMachines.size(); i++)
		{
			indexVM = virtualMachines.get(i);
			if (indexVM.getId() == id)
			{
				usedDisk -= indexVM.getDisk();
				usedRam -= indexVM.getRam();
				
				virtualMachines.remove(i);
				break;
			}
		}
	}
	
	public boolean hasEnoughSpace(VirtualMachine vm){
		if(disk-usedDisk >= vm.getDisk()&& ram-usedRam >= vm.getRam())
		{
			return true;
		}
		
		return false;
	}

	public int smallestRamVM(int maxDisk) {
		//maxDisk Argument is for getting the smallest ram that is between the maxDisk range.
		//Therefore we always get a value we might fit down depending out ram
		if(virtualMachines.size() == 0)
		{			
			return -1;
		}
		VirtualMachine smallest = null;
		int smallestIndex = -1;
		for (int i = 1; i < virtualMachines.size(); i++)
		{
			VirtualMachine index = virtualMachines.get(i);
			if(index.getDisk() <= maxDisk)
			{
				if (smallest == null|| index.getRam() < smallest.getRam() )
				{
					smallest = index;
					smallestIndex = i;
				}
			}
			
		}
		return smallestIndex;
	}
	
	public int smallestDiskVM(int maxRam) {
		//maxRam Argument is for getting the smallest disk that is between the maxDisk range.
		//Therefore we always get a value we might fit down depending out ram
		if(virtualMachines.size() == 0)
		{			
			return -1;
		}
		VirtualMachine smallest = null;
		int smallestIndex = -1;
		for (int i = 1; i < virtualMachines.size(); i++)
		{
			VirtualMachine index = virtualMachines.get(i);
			if(index.getDisk() <= maxRam)
			{
				if (smallest == null|| index.getDisk() < smallest.getDisk() )
				{
					smallest = index;
					smallestIndex = i;
				}
			}
			
		}
		return smallestIndex;
	}
	//This function is for searching a specific disk space witch has a acceptable ram
	public int searchDiskVM(int searchedDisk, int maxRam, int vmRam) {
		//maxRam Argument is for getting the smallest disk that is between the maxDisk range.
		//Therefore we always get a value we might fit down depending out ram
		//VM ram is for the VM we consider to swap in, We need to check if it would make any problems when swapped
		if(virtualMachines.size() == 0)
		{
			return -1;
		}
		VirtualMachine smallest = null; // This is for marking the smallest ram we can find
		int smallestIndex = -1;
		for (int i = 0; i < virtualMachines.size(); i++)
		{
			VirtualMachine index = virtualMachines.get(i);
			if(index.getDisk() == searchedDisk && index.getDisk() <= maxRam && usedRam - index.getRam() + vmRam <= this.ram)
			{
				if (  smallest == null || index.getRam() < smallest.getRam())
				{
					smallest = index;
					smallestIndex = i;
				}
			}
			
		}
		return smallestIndex;
	}
	
	public boolean isFit(VirtualMachine vm) {
		if(usedDisk +vm.getDisk() == disk || usedRam +vm.getRam() == ram)
		{
			return true;
		}
		
		return false;
	}

	public void bubbleSort() {
		VirtualMachine a;
		VirtualMachine b;
		for (int i = 0; i < virtualMachines.size()-1 ; i++)
		{
			for (int j = 0; j < virtualMachines.size()- i -1 ; j++)
			{
				a = virtualMachines.get(j);
				b = virtualMachines.get(j+1);
				
				if((a.getDisk() > b.getDisk()) || (a.getDisk()== b.getDisk() && a.getRam() > b.getRam()))
				{
					this.swapVM(j,j+1);
				}
			}
		}
	}
	
	public void swapVM(int a, int b) {
		VirtualMachine shelf = virtualMachines.get(a);
		virtualMachines.set(a, virtualMachines.get(b));
		virtualMachines.set(b, shelf);
	}
	
	
	public int getUsedDisk() {
		return usedDisk;
	}

	public int getUsedRam() {
		return usedRam;
	}

	public int getDisk() {
		return disk;
	}
	public int getAvailableDisk() {
		return disk - usedDisk;
	}
	
	public int getAvailableRam() {
		return ram - usedRam;
	}		

	public int getRam() {
		return ram;
	}

	public int getId() {
		return id;
	}

	public int vmSize() {
		return virtualMachines.size();
	}
	public VirtualMachine getVm(int index)
	{
		return virtualMachines.get(index);
	}
	@Override
	public String toString() {
		String total = "";

		total += "\nHost id: "+ id +"\tMax disk=" + disk + ",Max ram=" + ram + ", usedDisk=" + usedDisk + ", usedRam=" + usedRam
				+ ", virtualMachines=" + virtualMachines + "]";
		return total;
	}



}
