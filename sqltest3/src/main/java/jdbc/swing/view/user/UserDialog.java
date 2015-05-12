package jdbc.swing.view.user;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JPasswordField;

import jdbc.swing.dao.UserDAO;
import jdbc.swing.domain.User;
import jdbc.swing.view.PlayerSearchModule;

public class UserDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField firstNameTextField;
	private JTextField lastNameTextField;
	private JTextField emailTextField;
	private JCheckBox adminCheckBox;

	private PlayerSearchModule playerSearchModule;
	private UserDAO userDAO;	
	private boolean updateMode = false;
	private User previousUser;
	private JPasswordField passwordField;
	private JPasswordField confirmPasswordField;
	private JLabel passwordLabel;
	private JLabel confirmPasswordLabel;

	/**
	 * Create the dialog.
	 */
	public UserDialog(PlayerSearchModule thePlayerSearchModule, UserDAO theUserDAO, User theUser) {
		this(thePlayerSearchModule, theUserDAO, true);
		previousUser = theUser;
		
			setTitle("Update User");						
			populateGui(previousUser);
			hidePasswordFields();
	}
	
	/**
	 * Create the dialog.
	 */
	public UserDialog(PlayerSearchModule thePlayerSearchModule, UserDAO theUserDAO, boolean theUpdateMode) {
		
		this();
		
		playerSearchModule = thePlayerSearchModule;
		userDAO = theUserDAO;
		updateMode = theUpdateMode;
	}
	
	/**
	 * Create the dialog.
	 */
	public UserDialog() {
		setTitle("Add User");
		
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		{
			JLabel lblNewLabel = new JLabel("First Name");
			lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblNewLabel, "2, 2, right, default");
		}
		{
			firstNameTextField = new JTextField();
			contentPanel.add(firstNameTextField, "4, 2, fill, default");
			firstNameTextField.setColumns(10);
		}
		{
			JLabel lblNewLabel_1 = new JLabel("Last Name");
			lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblNewLabel_1, "2, 4, right, default");
		}
		{
			lastNameTextField = new JTextField();
			contentPanel.add(lastNameTextField, "4, 4, fill, default");
			lastNameTextField.setColumns(10);
		}
		{
			JLabel lblNewLabel_2 = new JLabel("Email");
			lblNewLabel_2.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblNewLabel_2, "2, 6, right, default");
		}
		{
			emailTextField = new JTextField();
			contentPanel.add(emailTextField, "4, 6, fill, default");
			emailTextField.setColumns(10);
		}
		{
			JLabel lblNewLabel_3 = new JLabel("Admin");
			lblNewLabel_3.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblNewLabel_3, "2, 8");
		}
		{
			adminCheckBox = new JCheckBox("");
			contentPanel.add(adminCheckBox, "4, 8");
		}
		{
			passwordLabel = new JLabel("Password");
			passwordLabel.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(passwordLabel, "2, 10, right, default");
		}
		{
			passwordField = new JPasswordField();
			contentPanel.add(passwordField, "4, 10, fill, default");
		}
		{
			confirmPasswordLabel = new JLabel("Confirm Password");
			confirmPasswordLabel.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(confirmPasswordLabel, "2, 12, right, default");
		}
		{
			confirmPasswordField = new JPasswordField();
			contentPanel.add(confirmPasswordField, "4, 12, fill, default");
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						saveUser();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	private void populateGui(User theUser) {

		firstNameTextField.setText(theUser.getFirstName());
		lastNameTextField.setText(theUser.getLastName());
		emailTextField.setText(theUser.getEmail());
		adminCheckBox.setSelected(theUser.isAdmin());
	}
	
	private void saveUser() {

		String firstName = firstNameTextField.getText();
		String lastName = lastNameTextField.getText();
		String email = emailTextField.getText();
		boolean admin = adminCheckBox.isSelected();
				
		User theUser = null;
		
		if (updateMode) {
			theUser = previousUser;
			
			theUser.setLastName(lastName);
			theUser.setFirstName(firstName);
			theUser.setEmail(email);
			theUser.setAdmin(admin);
			
		} else {
			String password = new String(passwordField.getPassword());
			String confirmPassword = new String(confirmPasswordField.getPassword());

			if (!password.equals(confirmPassword)) {
				JOptionPane.showMessageDialog(this,
						"Passwords do not match.", "Error",
						JOptionPane.ERROR_MESSAGE);				
				return;
			}
			
			theUser = new User(lastName, firstName, email, admin, password);
		}

		try {
			
			if (updateMode) {
				userDAO.updateUser(theUser);
				
				if (playerSearchModule.getCurrentUserId() == theUser.getId()) {
					playerSearchModule.setAdmin(theUser.isAdmin());
				}
			} else {
				userDAO.addUser(theUser);
			}

			setVisible(false);

			playerSearchModule.refreshUsersView();

			JOptionPane.showMessageDialog(playerSearchModule,
					"User saved succesfully.", "User Saved",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception exc) {
			JOptionPane.showMessageDialog(playerSearchModule,
					"Error saving user: " + exc.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		
	}

	private void hidePasswordFields() {
		
		passwordField.setVisible(false);
		confirmPasswordField.setVisible(false);
	
		passwordLabel.setVisible(false);
		confirmPasswordLabel.setVisible(false);
	}


}