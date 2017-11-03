package CifrarioIbrido;

import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.BorderLayout;

import javax.swing.JTabbedPane;
import javax.swing.JPanel;

import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.Box;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.JEditorPane;
import javax.swing.JInternalFrame;
import javax.swing.JTextPane;
import javax.swing.JScrollBar;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JProgressBar;
import javax.swing.border.TitledBorder;
import javax.swing.SwingConstants;
import javax.swing.DropMode;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.border.LineBorder;

import java.awt.Color;

import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JFileChooser;
import javax.swing.JToggleButton;
import javax.swing.JRadioButton;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;

import java.awt.Label;

import javax.swing.JTextArea;

import java.awt.ScrollPane;
import java.awt.TextArea;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.awt.Choice;

public class GUI {

	private JFrame frmCifrarioIbrido;
	private JTextField UsernameTextField;
	private JTextField DeleteTextField;
	private JTextField FileTextField;
	private Incapsula inc = new Incapsula();

	/**
	 * Launch the application.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		Files.deleteIfExists(Paths.get("./publicKeyFile.txt"));
		Files.deleteIfExists(Paths.get("./fileToSend.txt"));
		Files.deleteIfExists(Paths.get("./digKeysFile.txt"));
		Files.deleteIfExists(Paths.get("./myTempFile.txt"));
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frmCifrarioIbrido.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmCifrarioIbrido = new JFrame();
		frmCifrarioIbrido.setTitle("Cifrario Ibrido");
		frmCifrarioIbrido.setBounds(100, 100, 634, 403);
		frmCifrarioIbrido.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmCifrarioIbrido.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmCifrarioIbrido.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JPanel UserPanel = new JPanel();
		tabbedPane.addTab("User", (Icon) null, UserPanel, "Click here in order to add a user on the network");
		UserPanel.setLayout(null);
		
		JPanel InsertUserPanel = new JPanel();
		InsertUserPanel.setBorder(new TitledBorder(null, "Insert User", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		InsertUserPanel.setBounds(6, 6, 285, 302);
		UserPanel.add(InsertUserPanel);
		InsertUserPanel.setLayout(null);
		
		JLabel lblUsername = new JLabel("Username : ");
		lblUsername.setBounds(18, 58, 68, 16);
		InsertUserPanel.add(lblUsername);
		
		JLabel lblKeyDimension = new JLabel("Key Dimension :");
		lblKeyDimension.setBounds(18, 106, 93, 16);
		InsertUserPanel.add(lblKeyDimension);
		
		JLabel lblPadding = new JLabel("Padding :");
		lblPadding.setBounds(18, 156, 55, 16);
		InsertUserPanel.add(lblPadding);
		
		UsernameTextField = new JTextField();		
		UsernameTextField.setDropMode(DropMode.INSERT);
		UsernameTextField.setHorizontalAlignment(SwingConstants.LEFT);
		UsernameTextField.setToolTipText("Username");
		UsernameTextField.setBounds(98, 55, 152, 22);
		InsertUserPanel.add(UsernameTextField);
		UsernameTextField.setColumns(10);
		
		
		JComboBox PaddingComboBox = new JComboBox();
		PaddingComboBox.setToolTipText("Padding mode");
		PaddingComboBox.setModel(new DefaultComboBoxModel(new String[] {"PKCS1Padding", "OAEPPadding"}));
		PaddingComboBox.setBounds(115, 151, 124, 26);
		InsertUserPanel.add(PaddingComboBox);
		
		JComboBox KeyComboBox = new JComboBox();
		KeyComboBox.setToolTipText("Key dimension");
		KeyComboBox.setModel(new DefaultComboBoxModel(new String[] {"1024", "2048"}));
		KeyComboBox.setBounds(115, 101, 124, 26);
		InsertUserPanel.add(KeyComboBox);
		
		JPanel ViewPanel = new JPanel();
		ViewPanel.setBorder(new TitledBorder(null, "User view", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		ViewPanel.setBounds(303, 6, 300, 302);
		UserPanel.add(ViewPanel);
		ViewPanel.setLayout(null);
		
		TextArea textArea = new TextArea();
		textArea.setEditable(false);
		textArea.setBounds(20, 50, 247, 138);
		ViewPanel.add(textArea);
		
		JPanel TransmissionPanel = new JPanel();
		tabbedPane.addTab("Transmission", null, TransmissionPanel, "Click here in order to send a message");
		TransmissionPanel.setLayout(null);
		
		JPanel SendPanel = new JPanel();
		SendPanel.setBorder(new TitledBorder(null, "Send Message", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		SendPanel.setBounds(6, 6, 273, 322);
		TransmissionPanel.add(SendPanel);
		SendPanel.setLayout(null);
		
		JComboBox SenderComboBox = new JComboBox();
		SenderComboBox.setBounds(116, 41, 119, 20);
		SendPanel.add(SenderComboBox);
		SenderComboBox.setEnabled(false);

		
		JComboBox ReceiverComboBox = new JComboBox();
		ReceiverComboBox.setBounds(116, 89, 119, 20);
		SendPanel.add(ReceiverComboBox);
		ReceiverComboBox.setEnabled(false);

		FileTextField = new JTextField();
		FileTextField.setEditable(false);
		FileTextField.setBounds(120, 263, 130, 28);
		SendPanel.add(FileTextField);
		FileTextField.setColumns(10);
		
		JButton btnSend = new JButton("SEND");
		btnSend.setEnabled(false);
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
			}
		});
		btnSend.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnSend.setToolTipText("Send Message");
		btnSend.setBounds(425, 268, 90, 28);
		TransmissionPanel.add(btnSend);
		
		JButton btnAdd = new JButton("ADD");
		btnAdd.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnAdd.setToolTipText("Add user");
		btnAdd.setEnabled(false);
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String username = UsernameTextField.getText();
				String keyDimension = KeyComboBox.getSelectedItem().toString();
				String padding = PaddingComboBox.getSelectedItem().toString();

				try {

					boolean success = inc.addUser(username, Integer.parseInt(keyDimension), padding);
					if (success){
						if (textArea.getText().equals(" "))
							textArea.setText(null);
						textArea.append(username + "\n");
						SenderComboBox.addItem(username);
						ReceiverComboBox.addItem(username);
					
						if(SenderComboBox.getItemCount() > 0)
							SenderComboBox.setEnabled(true);
							
						if(ReceiverComboBox.getItemCount() > 0)
							ReceiverComboBox.setEnabled(true);
						
						if(SenderComboBox.getItemCount() > 0 &&	!FileTextField.getText().equals(""))		
							btnSend.setEnabled(true);
					}
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnAdd.setBounds(138, 210, 90, 28);
		InsertUserPanel.add(btnAdd);
		
		UsernameTextField.getDocument().addDocumentListener(new DocumentListener() {
			
			public void changedUpdate(DocumentEvent e) {
				enableButton();
			}
			public void removeUpdate(DocumentEvent e) {
				enableButton();
			}
			public void insertUpdate(DocumentEvent e) {
				enableButton();
			}
		
		public void enableButton(){
			if (UsernameTextField.getText().equals(""))
				btnAdd.setEnabled(false);
			else
				btnAdd.setEnabled(true);
		}
	});
		
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(GUI.class.getResource("/progetto2/resources/utente.png")));
		label.setBounds(23, 189, 103, 107);
		InsertUserPanel.add(label);
		
		DeleteTextField = new JTextField();
		DeleteTextField.setToolTipText("Delete User");
		DeleteTextField.setBounds(20, 250, 161, 21);
		ViewPanel.add(DeleteTextField);
		DeleteTextField.setColumns(10);
		
		JButton btnDelete = new JButton("DELETE");
		btnDelete.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = DeleteTextField.getText();
				try {
					boolean success = inc.deleteUser(username);
					if(success){
						SenderComboBox.removeItem(username);
						ReceiverComboBox.removeItem(username);
						textArea.setText(null);
						for(User i :inc.getUtenti()){
							textArea.append(i.getName() + "\n");							
						}
					}
					if(inc.getUtenti().size()==0)
						textArea.setText(" ");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				DeleteTextField.setText(null);
				if(SenderComboBox.getItemCount() == 0){
					SenderComboBox.setEnabled(false);
					btnSend.setEnabled(false);
				}
				if(ReceiverComboBox.getItemCount() == 0)
					ReceiverComboBox.setEnabled(false);

			}
		});
		btnDelete.setBounds(193, 251, 90, 21);
		ViewPanel.add(btnDelete);
		
		JLabel lblDeleteUser = new JLabel("Delete User :");
		lblDeleteUser.setBounds(20, 222, 117, 16);
		ViewPanel.add(lblDeleteUser);
		
		JLabel lblUserInNetwork = new JLabel("User in network :");
		lblUserInNetwork.setBounds(20, 28, 111, 16);
		ViewPanel.add(lblUserInNetwork);
		
		JLabel lblSender = new JLabel("Sender : ");
		lblSender.setBounds(19, 43, 59, 16);
		SendPanel.add(lblSender);
		
		JLabel lblReceiver = new JLabel("Receiver :");
		lblReceiver.setBounds(19, 91, 55, 16);
		SendPanel.add(lblReceiver);
		
		JLabel lblAlgorithm = new JLabel("Algorithm : ");
		lblAlgorithm.setBounds(19, 142, 69, 16);
		SendPanel.add(lblAlgorithm);
		
		JComboBox AlgorithmComboBox = new JComboBox();
		AlgorithmComboBox.setModel(new DefaultComboBoxModel(new String[] {"AES", "DES", "DESede"}));
		AlgorithmComboBox.setToolTipText("Insert Algorithm");
		AlgorithmComboBox.setBounds(120, 136, 115, 28);
		SendPanel.add(AlgorithmComboBox);
		
		JLabel lblOperationMode = new JLabel("Operation Mode : ");
		lblOperationMode.setBounds(19, 190, 99, 16);
		SendPanel.add(lblOperationMode);
		
		JComboBox OperationComboBox = new JComboBox();
		OperationComboBox.setModel(new DefaultComboBoxModel(new String[] {"ECB", "CBC", "CFB"}));
		OperationComboBox.setBounds(120, 185, 115, 28);
		SendPanel.add(OperationComboBox);
		
		JLabel lblChooseMessage = new JLabel("Choose Message :");
		lblChooseMessage.setBounds(19, 235, 115, 16);
		SendPanel.add(lblChooseMessage);
		
		JButton btnApriFile = new JButton("Open file");
		btnApriFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String filename;
				OpenFile file = new OpenFile();
				filename = file.pickMe();
				FileTextField.setText(filename);
				
				if(SenderComboBox.getItemCount() > 0 &&	!FileTextField.getText().equals(""))		
					btnSend.setEnabled(true);
				if(FileTextField.getText().equals(""))
					btnSend.setEnabled(false);
			}
		});
		btnApriFile.setBounds(19, 263, 77, 28);
		SendPanel.add(btnApriFile);
	
		JPanel SignPanel = new JPanel();
		SignPanel.setBorder(new TitledBorder(null, "Digital Sign", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		SignPanel.setBounds(291, 6, 312, 228);
		TransmissionPanel.add(SignPanel);
		SignPanel.setLayout(null);
		
		JLabel lblDoYouWant = new JLabel("Do you want  to sign your message?");
		lblDoYouWant.setBounds(18, 37, 209, 16);
		SignPanel.add(lblDoYouWant);
		
		JPanel DSApanel = new JPanel();
		DSApanel.setBorder(new TitledBorder(null, "DSA", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		DSApanel.setBounds(18, 113, 277, 107);
		SignPanel.add(DSApanel);
		DSApanel.setLayout(null);
		
		JComboBox DSAKeyComboBox = new JComboBox();
		DSAKeyComboBox.setModel(new DefaultComboBoxModel(new String[] {"1024", "2048"}));
		DSAKeyComboBox.setBounds(149, 24, 103, 26);
		DSApanel.add(DSAKeyComboBox);
		
		JComboBox TypeComboBox = new JComboBox();
		TypeComboBox.setModel(new DefaultComboBoxModel(new String[] {"SHA1", "SHA224", "SHA256"}));
		TypeComboBox.setBounds(149, 58, 103, 26);
		DSApanel.add(TypeComboBox);
		
		JRadioButton rdbtnYes = new JRadioButton("Yes");
		rdbtnYes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(rdbtnYes.isSelected()){
					DSAKeyComboBox.setEnabled(true);
					TypeComboBox.setEnabled(true);
				}
			}
		});
		rdbtnYes.setBounds(18, 72, 65, 18);
		SignPanel.add(rdbtnYes);
		
		JRadioButton rdbtnNo = new JRadioButton("No");
		rdbtnNo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rdbtnNo.isSelected()){
					DSAKeyComboBox.setEnabled(false);
					TypeComboBox.setEnabled(false);
				}
			}
		});
		rdbtnNo.setSelected(true);
		DSAKeyComboBox.setEnabled(false);
		TypeComboBox.setEnabled(false);
		rdbtnNo.setBounds(112, 72, 115, 18);
		SignPanel.add(rdbtnNo);
		ButtonGroup group = new ButtonGroup();
		group.add(rdbtnYes);
		group.add(rdbtnNo);
		
		JLabel lblKeyDimension_1 = new JLabel("Key Dimension : ");
		lblKeyDimension_1.setBounds(19, 29, 103, 16);
		DSApanel.add(lblKeyDimension_1);
		
		JLabel lblType = new JLabel("Type :");
		lblType.setBounds(19, 63, 55, 16);
		DSApanel.add(lblType);
		
		JLabel lblSendMessage = new JLabel("Send Message :");
		lblSendMessage.setBounds(301, 274, 106, 16);
		TransmissionPanel.add(lblSendMessage);
		
		JLabel label_1 = new JLabel("");
		label_1.setIcon(new ImageIcon(GUI.class.getResource("/progetto2/resources/Ok-icon.png")));
		label_1.setBounds(537, 246, 39, 66);
		TransmissionPanel.add(label_1);
	}
}
