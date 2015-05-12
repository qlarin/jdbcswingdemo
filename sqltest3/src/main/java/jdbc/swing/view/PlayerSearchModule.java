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
	private JPanel panel_1;
	private JButton btnAddPlayer;
	private JButton btnEditPlayer;
	private JButton btnRemovePlayer;

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PlayerSearchModule frame = new PlayerSearchModule();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public PlayerSearchModule() {

		try {
			playerDAO = new PlayerDAO();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error: " + e, "Error",
					JOptionPane.ERROR_MESSAGE);
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

					if (guildName != null && guildName.trim().length() > 0) {
						players = playerDAO.searchPlayers(guildName);
					} else {
						players = playerDAO.getAllPlayers();
					}

					PlayerTableModel model = new PlayerTableModel(players);
					table.setModel(model);
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(PlayerSearchModule.this,
							"Error: " + ex, "Error", JOptionPane.ERROR_MESSAGE);
				}

			}
		});
		panel.add(buttonSearch);

		scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);

		table = new JTable();
		scrollPane.setViewportView(table);

		panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.SOUTH);

		btnAddPlayer = new JButton("Add player");
		btnAddPlayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				PlayerDialog dialog = new PlayerDialog(PlayerSearchModule.this,
						playerDAO);
				dialog.setVisible(true);
			}
		});
		panel_1.add(btnAddPlayer);

		btnEditPlayer = new JButton("Edit player");
		btnEditPlayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int row = table.getSelectedRow();
				if (row < 0) {
					JOptionPane.showMessageDialog(PlayerSearchModule.this,
							"You have to select player", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				Player newPlayer = (Player) table.getValueAt(row,
						PlayerTableModel.OBJECT_COL);
				PlayerDialog dialog = new PlayerDialog(PlayerSearchModule.this,
						playerDAO, newPlayer, true);
				dialog.setVisible(true);

			}
		});
		panel_1.add(btnEditPlayer);

		btnRemovePlayer = new JButton("Remove player");
		btnRemovePlayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					int row = table.getSelectedRow();
					if (row < 0) {
						JOptionPane.showMessageDialog(PlayerSearchModule.this,
								"You have to select player", "Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}

					int response = JOptionPane.showConfirmDialog(
							PlayerSearchModule.this, "Remove this player?",
							"Confirm", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE);

					if (response != JOptionPane.YES_OPTION) {
						return;
					}

					Player newPLayer = (Player) table.getValueAt(row,
							PlayerTableModel.OBJECT_COL);
					playerDAO.deletePlayer(newPLayer.getId());
					refreshPlayersView();

					JOptionPane.showMessageDialog(PlayerSearchModule.this,
							"Player removed succesfully.", "Player Deleted",
							JOptionPane.INFORMATION_MESSAGE);

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(PlayerSearchModule.this,
							"Error removing player: " + ex.getMessage(),
							"Error", JOptionPane.ERROR_MESSAGE);

				}
			}
		});
		panel_1.add(btnRemovePlayer);
	}

	public void refreshPlayersView() {

		try {
			List<Player> players = playerDAO.getAllPlayers();
			PlayerTableModel model = new PlayerTableModel(players);
			table.setModel(model);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error: " + ex, "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
