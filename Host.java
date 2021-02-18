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
		if(newVm.getDisk() <= (disk- usedDisk) && newVm.getRam() <= (ram- usedRam)) // if it can be inserted
		{
			virtualMachines.add(newVm); //adds into the arraylist
			usedDisk += newVm.getDisk();
			usedRam += newVm.getRam();
			if(disk == usedDisk || ram == usedRam)
			{
				full = true; // It became full  it wont receive any more vm requests
			}
			this.bubbleSort(); // We sort it because a new vm got inserted
			return true;
		}
		System.out.println("Couldn't add "+ newVm.toString()+ " in Host :"+this.toString());
		return false;
	}
	
	public void removeVM(int id) { // THIS IS NOT INDEX , it is the id of the vm
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
	
	public boolean hasEnoughSpace(VirtualMachine vm){ // Checks if it has enough space for a potential vm
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
	
	
	
	@SuppressWarnings("unchecked")
	public ArrayList<ArrayList<VirtualMachine>> searchForMultiple(VirtualMachine targetVm)
	{
		ArrayList<ArrayList<VirtualMachine>> options = new ArrayList<ArrayList<VirtualMachine>>();
		ArrayList<ArrayList<VirtualMachine>> potentials = new ArrayList<ArrayList<VirtualMachine>>();
		ArrayList<VirtualMachine> searchList = new ArrayList<VirtualMachine>(); // We are trying to find applicable combinations of these elements
		VirtualMachine indexVm;
		
		ArrayList<VirtualMachine> indexArr;
		for (int i = 0; i < virtualMachines.size(); i++)
		{
			indexVm = virtualMachines.get(i);
			indexArr = new ArrayList<VirtualMachine>();
			indexArr.add(indexVm);
			if(this.getAvailableDisk() + indexVm.getDisk() >= targetVm.getDisk() && this.getAvailableRam() +indexVm.getRam() >= targetVm.getRam())
			{
				options.add(indexArr);
			}
			else {				
				searchList.add(indexVm);
				potentials.add(indexArr);
			}
		}
		ArrayList<VirtualMachine> newArr;
		int i = 0;
		
		while(i < potentials.size())
		{
			newArr =new ArrayList<VirtualMachine>();
			indexArr = potentials.get(i);
			for (int j = 0; j < searchList.size(); j++)
			{
				indexVm = searchList.get(j);
				if(!Host.arrHasVm(indexArr, indexVm))
				{
					newArr = (ArrayList<VirtualMachine>) indexArr.clone();
					newArr.add(indexVm);
					break;
				}
			}
			if (newArr.size() > 0)
			{
				if(this.isOption(newArr, targetVm))
				{
					if(!Host.isDuplicate(options, newArr)) options.add(newArr);
				}
				else
				{
					if(!Host.isDuplicate(potentials, newArr)) potentials.add(newArr);
				}
			}
			i++;
		}
		
		
		return options;
	}
	private static boolean arrHasVm(ArrayList<VirtualMachine> arr, VirtualMachine vm) {
		for(int i = 0; i < arr.size(); i++) {
			if(arr.get(i).getId() == vm.getId()) {
				return true;
			}
		}
		return false;
	}
	private static boolean isDuplicate(ArrayList<ArrayList<VirtualMachine>> arr,ArrayList<VirtualMachine> target )
	{
		int count;
		for (int i = 0; i < arr.size(); i++ )
		{
			count = 0;
			for(int j = 0; j < target.size(); j++)
			{
				if(Host.arrHasVm(arr.get(i), target.get(j)))
				{
					count++;
				}
				else {
					break;
				}
			}
			if(count == target.size())
			{
				return true;
			}
		}
		return false;
	}
	public boolean isOption(ArrayList<VirtualMachine> arr, VirtualMachine vm) {
		int disk = 0; 
		int ram = 0;
		
		VirtualMachine index ;
		for(int i = 0; i< arr.size(); i++) {
			index = arr.get(i);
			
			disk += index.getDisk();
			ram += index.getRam();
		}
		
		if(vm.getDisk() <= this.disk -this.usedDisk + disk &&vm.getRam() <= this.ram - this.usedRam + ram)
		{
			return true;
		}
		
		return false;
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
