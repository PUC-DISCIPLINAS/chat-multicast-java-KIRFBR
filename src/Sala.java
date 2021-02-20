import java.io.BufferedWriter;
import java.util.ArrayList;

public class Sala {
	
	public Sala(String id, BufferedWriter mbm, String nome) {
		this.id = id;
		
		this.membros = new ArrayList<BufferedWriter>();
		this.membros.add(mbm);
		
		this.nomes = new ArrayList<String>();
		this.nomes.add(nome);
	}
	
	public String id;
	public ArrayList<BufferedWriter> membros;
	public ArrayList<String> nomes;
}
