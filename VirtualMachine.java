package datacenter;

public class VirtualMachine {
	private int disk;
	private int ram;
	private int id;
	public VirtualMachine(int id, int disk , int ram) {
		this.id = id;
		this.disk =disk;
		this.ram = ram;
	}

	public int getDisk() {
		return disk;
	}

	public int getRam() {
		return ram;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "VirtualMachine id:"+id+ "[disk=" + disk + ", ram=" + ram +"]";
	}
	

}
