import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Servidor extends Thread {

	//private static ArrayList<BufferedWriter> clientes; not used
	private static ArrayList<Sala> salas;
	
	private static ServerSocket server;
	private String nome;
	private Socket con;
	private InputStream in;
	private InputStreamReader inr;
	private BufferedReader bfr;

	/**
	 * Método construtor
	 * 
	 * @param com do tipo Socket
	 */
	public Servidor(Socket con) {
		this.con = con;
		try {
			in = con.getInputStream();
			inr = new InputStreamReader(in);
			bfr = new BufferedReader(inr);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Método run
	 */
	public void run() {

		try {

			String msg;
			OutputStream ou = this.con.getOutputStream();
			Writer ouw = new OutputStreamWriter(ou);
			BufferedWriter bfw = new BufferedWriter(ouw);
			//clientes.add(bfw);
			nome = msg = bfr.readLine();

			while (!"Sair".equalsIgnoreCase(msg) && msg != null) {
				msg = bfr.readLine();
				String id;
				if(msg.equals("entrar_sala")) {
					id = bfr.readLine();
					System.out.println("--- entrar_sala");
					System.out.println("id: "+id);
					if(!id.equals("")) {
						entrarSala(bfw, id);
						System.out.println("operacao completa !");
					}else {
						System.out.println("operacao nao realizada !");
					}
					
				}else if(msg.equals("sair_sala")) {
					id = bfr.readLine();
					System.out.println("--- sair_sala");
					System.out.println("id: "+id);
					sairSala(bfw, id);
					System.out.println("operacao completa !");
				}else if(msg.equals("listar_membros")) {
					id = bfr.readLine();
					System.out.println("--- listar_membros");
					System.out.println("id: "+id);
					listarMembros(bfw, id);
					System.out.println("operacao completa !");
				}else if(msg.equals("listar_salas")) {
					id = bfr.readLine();
					System.out.println("--- listar_salas");
					System.out.println("id: "+id);
					listarSalas(bfw, id);
					System.out.println("operacao completa !");
				}else if(msg.equals("groupId")) {
					id = bfr.readLine();
					msg = bfr.readLine();
					System.out.println("--- groupId");
					System.out.println("id: "+id);
					System.out.println("msg: "+msg);
					sendToGroup(bfw, msg, id);
					System.out.println("operacao completa !");
				}
				/*else {
					msg = bfr.readLine();
					sendToAll(bfw, msg);
					System.out.println(msg);
				}
				*/
				System.out.println("");
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
	}
	
	public void entrarSala(BufferedWriter bwSaida, String id) throws IOException {
		boolean salaExiste = false;
		// verifica se sala existe
		for(Sala s : salas) {
			if(s.id.equals(id)) {
				salaExiste = true;
			}
		}
		
		// caso sala exista, apenas adiciona o membro
		if(salaExiste) {
			for(Sala s : salas) {
				if(s.id.equals(id)) {
					boolean jahAdd = false;
					for(BufferedWriter mbm : s.membros) { // somente adiciona se ele ainda não foi adicionado
						if(mbm == bwSaida)
							jahAdd = true;
					}
					if(jahAdd) {
						bwSaida.write(nome + " -> já está adicionado na sala"+ "\r\n");
						bwSaida.flush();
					}else {
						s.membros.add(bwSaida);
						s.nomes.add(nome);
						bwSaida.write(nome + " -> entrou na sala de id: "+id+ "\r\n");
						bwSaida.flush();
					}
				}
			}
		}else{ // caso contrário, cria uma nova sala com o id em questao
			Sala s = new Sala(id, bwSaida, nome);
			salas.add(s);
			bwSaida.write(nome + " -> Nao existe! Criada e tu add." + "\r\n");
			bwSaida.flush();
		}
	}

	public void sairSala(BufferedWriter bwSaida, String id) throws IOException {
		// verifica se sala existe
		for(Sala s : salas) {
			if(s.id.equals(id)) { //caso exista, remove o membro em questao
				int position = -1;
				int cont = 0;
				for (BufferedWriter mbm : s.membros) { //encontrar a posicao do membro
					if (mbm == bwSaida){
						position = cont;
					}
					cont ++;
				}
				if(position != -1) {
					s.membros.remove(position);
					s.nomes.remove(position);
					bwSaida.write(nome + " -> saiu da sala de id: "+id+ "\r\n");
					bwSaida.flush();
				}else {
					bwSaida.write(nome + " -> Você não etá na sala - id: "+id+"\r\n");
					bwSaida.flush();
				}
			}
		}
	}

	public void listarMembros(BufferedWriter bwSaida, String id) throws IOException {
		// verifica se sala existe
		boolean existe = false;

		for(Sala s : salas) {
			if(s.id.equals(id)) { // caso exista, exibe os seus membros
				existe = true;
				String listaMembros = "";
				Integer cont = 1;
				for(String nome : s.nomes) {
					listaMembros += cont.toString() + "-" + nome + "\r\n";
					cont++;
				}
				
				if(listaMembros.equals("")) {
					listaMembros = "-- sem membros --";
				}
				
				bwSaida.write("\r\n Membros sala - id "+id+": \r\n" + listaMembros + "\r\n");
				bwSaida.flush();				
			}
		}

		if(!existe) {
			bwSaida.write("\r\n Sala - id "+id+" não existe ! \r\n");
			bwSaida.flush();				
		}
	}
	
	public void listarSalas(BufferedWriter bwSaida, String id) throws IOException {
		// verifica se sala existe
		boolean existe = false;

		String listaSalas = "";
		for(Sala s : salas) {
			existe = true;
			listaSalas += "id: " + s.id + ";\r\n";
		}
		
		if(existe) {
			bwSaida.write("\r\n Lista de Salas: \r\n" + listaSalas + "\r\n");
			bwSaida.flush();
		}else {
			bwSaida.write("\r\n Nenhuma sala cadastrada ! \r\n");
			bwSaida.flush();
		}
	}

	public void sendToGroup(BufferedWriter bwSaida, String msg, String id) throws IOException {
		// verifica se sala existe e se está na sala
		boolean naSala = false;

		for(Sala s : salas) {
			if(s.id.equals(id)) {
				for (BufferedWriter mbm : s.membros) {
					if(mbm == bwSaida) naSala = true;
				}
				
				if(naSala) {
					for (BufferedWriter mbm : s.membros) {
						mbm.write(nome + " sala "+id+" -> " + msg + "\r\n");
						mbm.flush();
					}
				}
			}
		}
		
		if(!naSala) {
			bwSaida.write(nome + " sala "+id+" -> Você não está nessa sala\r\n");
			bwSaida.flush();
		}
	}
	
	/*
	public void sendToAll(BufferedWriter bwSaida, String msg) throws IOException {
		BufferedWriter bwS;

		for (BufferedWriter bw : clientes) {
			bwS = (BufferedWriter) bw;
			if (!(bwSaida == bwS)) {
				bw.write(nome + " -> " + msg + "\r\n");
				bw.flush();
			}
		}
	}
	*/


	/***
	 * Método main
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			// Cria os objetos necessário para instânciar o servidor
			JLabel lblMessage = new JLabel("Porta do Servidor:");
			JTextField txtPorta = new JTextField("12345");
			Object[] texts = { lblMessage, txtPorta };
			JOptionPane.showMessageDialog(null, texts);
			server = new ServerSocket(Integer.parseInt(txtPorta.getText()));
			//clientes = new ArrayList<BufferedWriter>();
			salas = new ArrayList<Sala>();
			JOptionPane.showMessageDialog(null, "Servidor ativo na porta: " + txtPorta.getText());

			while (true) {
				System.out.println("Aguardando conexão...");
				Socket con = server.accept();
				System.out.println("Cliente conectado...");
				Thread t = new Servidor(con);
				t.start();
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

}
