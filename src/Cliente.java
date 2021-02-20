import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import javax.swing.*;

public class Cliente extends JFrame implements ActionListener, KeyListener {
	private static final long serialVersionUID = 1L;
	private JTextArea texto;
	private JTextField txtMsg;
	private JButton btnSend;
	//private JButton btnSair; not used
	private JButton btnListSalas;
	private JButton btnListMembSala;
	private JButton btnEntrarSala;
	private JButton btnSairSala;
	private JLabel lblHistorico;
	private JLabel lblMsg;
	private JLabel lblIdSala;
	private JPanel pnlContent;
	private Socket socket;
	private OutputStream ou;
	private Writer ouw;
	private BufferedWriter bfw;
	private JTextField txtIP;
	private JTextField txtPorta;
	private JTextField txtNome;
	private JTextField txtIdSala;

	public Cliente() throws IOException {
		JLabel lblMessage = new JLabel("Verificar!");
		txtIP = new JTextField("127.0.0.1"); 
		txtPorta = new JTextField("12345");
		txtNome = new JTextField("Cliente");
		Object[] texts = { lblMessage, txtIP, txtPorta, txtNome, txtIdSala};
		JOptionPane.showMessageDialog(null, texts);
		pnlContent = new JPanel();
		texto = new JTextArea(10, 20);
		texto.setEditable(false);
		texto.setBackground(new Color(240, 240, 240));
		lblHistorico = new JLabel("Histórico");
		lblMsg = new JLabel("Mensagem");
		txtMsg = new JTextField(20);
		txtMsg.addKeyListener(this);
		
		btnSend = new JButton("Enviar");
		btnSend.setToolTipText("Enviar Mensagem");
		btnSend.setPreferredSize(new Dimension(200, 25));
		btnSend.addActionListener(this);
		btnSend.addKeyListener(this);

		//btnSair = new JButton("Sair"); 
		//btnSair.setToolTipText("Sair do Chat");
		//btnSair.setPreferredSize(new Dimension(100, 25));
		//btnSair.addActionListener(this);

		lblIdSala = new JLabel("Id sala");
		txtIdSala = new JTextField(20);
		txtIdSala.addKeyListener(this);
		btnEntrarSala = new JButton("Entrar na sala");
		btnEntrarSala.setToolTipText("Entrar na sala");
		btnEntrarSala.setPreferredSize(new Dimension(200, 25));
		btnEntrarSala.addActionListener(this);
		btnEntrarSala.addKeyListener(this);
		btnSairSala = new JButton("Sair da sala");
		btnSairSala.setToolTipText("Sair da sala");
		btnSairSala.setPreferredSize(new Dimension(200, 25));
		btnSairSala.addActionListener(this);
		btnSairSala.addKeyListener(this);
		btnListMembSala = new JButton("Listar Membros Sala");
		btnListMembSala.setToolTipText("Listar Membros Sala");
		btnListMembSala.setPreferredSize(new Dimension(200, 25));
		btnListMembSala.addActionListener(this);
		btnListMembSala.addKeyListener(this);
		btnListSalas = new JButton("Listar Salas");
		btnListSalas.setToolTipText("Listar Salas");
		btnListSalas.setPreferredSize(new Dimension(200, 25));
		btnListSalas.addActionListener(this);
		btnListSalas.addKeyListener(this);

		
		JScrollPane scroll = new JScrollPane(texto);
		texto.setLineWrap(true);
		
		pnlContent.add(lblHistorico);
		pnlContent.add(scroll);
		pnlContent.add(lblMsg);
		pnlContent.add(txtMsg);
		//pnlContent.add(btnSair);
		pnlContent.add(btnSend);
		pnlContent.add(lblIdSala);
		pnlContent.add(txtIdSala);
		pnlContent.add(btnEntrarSala);
		pnlContent.add(btnSairSala);
		pnlContent.add(btnListMembSala);
		pnlContent.add(btnListSalas);
		pnlContent.setBackground(Color.LIGHT_GRAY);
		
		texto.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.BLUE));
		txtMsg.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.BLUE));
		setTitle(txtNome.getText());
		setContentPane(pnlContent);
		setLocationRelativeTo(null);
		setResizable(false);
		setSize(250, 500);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	/***
	 * Método usado para conectar no server socket, retorna IO Exception caso dê
	 * algum erro.
	 * 
	 * @throws IOException
	 */
	public void conectar() throws IOException {

		socket = new Socket(txtIP.getText(), Integer.parseInt(txtPorta.getText()));
		ou = socket.getOutputStream();
		ouw = new OutputStreamWriter(ou);
		bfw = new BufferedWriter(ouw);
		bfw.write(txtNome.getText() + "\r\n");
		bfw.flush();
	}

	/***
	 * Método usado para enviar mensagem para o server socket
	 * 
	 * @param msg do tipo String
	 * @throws IOException retorna IO Exception caso dê algum erro.
	 */
	public void enviarMensagem(String msg) throws IOException {

		if (msg.equals("Sair")) {
			bfw.write("Desconectado \r\n");
			texto.append("Desconectado \r\n");
		} else {
			bfw.write(msg + "\r\n");
			texto.append(txtNome.getText() + " diz -> " + txtMsg.getText() + "\r\n");
		}
		bfw.flush();
		txtMsg.setText("");
	}

	/**
	 * Método usado para receber mensagem do servidor
	 * 
	 * @throws IOException retorna IO Exception caso dê algum erro.
	 */
	public void escutar() throws IOException {

		InputStream in = socket.getInputStream();
		InputStreamReader inr = new InputStreamReader(in);
		BufferedReader bfr = new BufferedReader(inr);
		String msg = "";

		while (!"Sair".equalsIgnoreCase(msg)) {

			if (bfr.ready()) {
				msg = bfr.readLine();
				if (msg.equals("Sair"))
					texto.append("Servidor caiu! \r\n");
				else
					texto.append(msg + "\r\n");
			}
		}
	}

	/***
	 * Método usado quando o usuário clica em sair
	 * 
	 * @throws IOException retorna IO Exception caso dê algum erro.
	 */
	public void sair() throws IOException {

		enviarMensagem("Sair");
		bfw.close();
		ouw.close();
		ou.close();
		socket.close();
	}
	
	public void entrarSala(String id) throws IOException {
		bfw.write("entrar_sala" + "\r\n");
		bfw.write(id + "\r\n");
		bfw.flush();
	}
	
	public void sairSala(String id) throws IOException {
		bfw.write("sair_sala" + "\r\n");
		bfw.write(id + "\r\n");
		bfw.flush();
	}

	public void listarMembrosSala(String id) throws IOException {
		bfw.write("listar_membros" + "\r\n");
		bfw.write(id + "\r\n");
		bfw.flush();
	}

	public void listarSalas() throws IOException {
		bfw.write("listar_salas" + "\r\n");
		bfw.flush();
	}

	public void sendToGroup(String id, String msg) throws IOException {
		bfw.write("groupId" + "\r\n");
		bfw.write(id + "\r\n");
		bfw.write(msg + "\r\n");
		bfw.flush();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		try {
			if (e.getActionCommand().equals(btnSend.getActionCommand()))
				sendToGroup(txtIdSala.getText(), txtMsg.getText());
			else if (e.getActionCommand().equals(btnEntrarSala.getActionCommand()))
				entrarSala(txtIdSala.getText());
			else if (e.getActionCommand().equals(btnSairSala.getActionCommand()))
				sairSala(txtIdSala.getText());
			else if (e.getActionCommand().equals(btnListMembSala.getActionCommand()))
				listarMembrosSala(txtIdSala.getText());
			else if (e.getActionCommand().equals(btnListSalas.getActionCommand()))
				listarSalas();
			/*else if (e.getActionCommand().equals(btnSair.getActionCommand()))
				sair();
			*/	
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			try {
				sendToGroup(txtIdSala.getText(), txtMsg.getText());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}

	public static void main(String[] args) throws IOException {

		Cliente app = new Cliente();
		app.conectar();
		app.escutar();
	}

}
