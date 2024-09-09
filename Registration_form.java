import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Registration_form extends JDialog {
    private JTextField ttfname;
    private JTextField tfemail;
    private JTextField tfphone;
    private JTextField tfaddress;
    private JPasswordField pfpassword;
    private JPasswordField pfconfirmpassword2;
    private JButton btnregister;
    private JButton btncancel;
    private JPanel registerpanel;

    public Registration_form(JFrame parent){
        super (parent);
        setTitle("Create a new account");
        setContentPane(registerpanel);
        setMinimumSize(new Dimension(450, 474));
        setModal (true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        btnregister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            registerUser();
            }
        });
        btncancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        setVisible(true);
    }

    private void registerUser() {
        String name=ttfname.getText();
        String email=tfemail.getText();
        String phone=tfphone.getText();
        String address=tfaddress.getText();
        String password=String.valueOf(pfpassword.getPassword());
        String confirmpassword=String.valueOf(pfconfirmpassword2.getPassword());

        if(name.isEmpty() || email.isEmpty()||address.isEmpty()||phone.isEmpty()||password.isEmpty()||confirmpassword.isEmpty()){
            JOptionPane.showMessageDialog(this,
                    "Please enter all fields","Try Again",JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(!password.equals(confirmpassword)){
            JOptionPane.showMessageDialog(this,
                    "Confirm Password does not match","Try Again",JOptionPane.ERROR_MESSAGE);
            return;
        }
        user=addUserToDatabase(name,phone,email,address,password,confirmpassword);
        if(user !=null){
            dispose();
        }
        else{
            JOptionPane.showMessageDialog(this,
                    "Failed to register new user","Try Again",JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
    public User user;
    private User addUserToDatabase(String name, String phone, String email, String address, String password, String confirmpassword) {
        User user=null;
        final String DB_URL="jdbc:mysql://127.0.0.1:3306/login_schema";
        final String USERNAME="root";
        final String PASSWORD="admin123";

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO users (name, email, phone, address, password) "+
            "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, address);
            preparedStatement.setString(5, password);
//Insert row into the table!
            int addedRows = preparedStatement.executeUpdate();
            if (addedRows > 0) {
                user = new User();
                user.name = name;
                user.email = email;
                user.phone = phone;
                user.address = address;
                user.password = password;
            }
            stmt.close();
            conn.close();
        }


        catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    public static void main(String[] args){
        Registration_form myform=new Registration_form(null);
    }
}
