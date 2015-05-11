package jdbc.swing.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import jdbc.swing.dao.PlayerDAO;
import jdbc.swing.domain.Player;

public class PlayerSearchModule extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField guildNameTextField;
	private JButton buttonSearch;
	private JScrollPane scrollPane;
	private JTable table;
	
	private PlayerDAO playerDAO;
	
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PlayerSearchModule frame = new PlayerSearchModule();
					frame.setVisible(true);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}
	
	public PlayerSearchModule() {
		
		try {
			playerDAO = new PlayerDAO();
		}catch(Exception e){
			JOptionPane.showMessageDialog(this, "Error: " + e, "Error", JOptionPane.ERROR_MESSAGE);
		}
		
		setTitle("Player Search Module");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		contentPane.add(panel, BorderLayout.NORTH);
		
		JLabel labelEnterGuildName = new JLabel("Enter guild name");
		panel.add(labelEnterGuildName);
		
		guildNameTextField = new JTextField();
		panel.add(guildNameTextField);
		guildNameTextField.setColumns(10);
		
		buttonSearch = new JButton("Search");
		buttonSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					String guildName = guildNameTextField.getText();
					List<Player> players = null;
					
					if(guildName != null && guildName.trim().length() > 0) {
						players = playerDAO.searchPlayers(guildName);
					}else{
						players = playerDAO.getAllPlayers();
					}
				
					PlayerTableModel model = new PlayerTableModel(players);
					table.setModel(model);
				}catch (Exception ex){
					JOptionPane.showMessageDialog(PlayerSearchModule.this, "Error: " + ex, "Error", JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
		panel.add(buttonSearch);
		
		scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		table = new JTable();
		scrollPane.setViewportView(table);
	}
}
