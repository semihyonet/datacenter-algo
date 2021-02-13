package datacenter;
import java.util.ArrayList; // import the ArrayList class

public class Host {
	private int disk;
	private int ram;
	private int id;

	private int usedDisk = 0;
	private int usedRam = 0;

	ArrayList<VirtualMachine> virtualMachines = new ArrayList<VirtualMachine>();
	
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
			return true;
		}
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

	@Override
	public String toString() {
		String total = "";

		total += "\nHost id: "+ id +"\tMax disk=" + disk + ",Max ram=" + ram + ", usedDisk=" + usedDisk + ", usedRam=" + usedRam
				+ ", virtualMachines=" + virtualMachines + "]";
		return total;
	}



}
