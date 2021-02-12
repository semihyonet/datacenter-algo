package datacenter;
import java.util.ArrayList; // import the ArrayList class

public class Host {
	private int disk;
	private int ram;

	private int usedDisk = 0;
	private int usedRam = 0;

	ArrayList<VirtualMachine> virtualMachines = new ArrayList<VirtualMachine>();
	
	public Host(int disk , int ram) {
		this.disk =disk;
		this.ram = ram;
	}
	
	public boolean addVM(VirtualMachine newVm) {
		if(newVm.getDisk() < (disk- usedDisk) && newVm.getRam() < (ram- usedRam))
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
		if(disk-usedDisk > vm.getDisk()&& ram-usedRam > vm.getRam())
		{
			return true;
		}
		
		return false;
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

	public int getRam() {
		return ram;
	}



}
