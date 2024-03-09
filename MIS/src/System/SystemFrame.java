/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package System;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.JTableHeader;
/**
 *
 * @author hunter
 */
public class SystemFrame extends javax.swing.JFrame {
    private Connection conn;
    private Statement stmt;
    private DefaultTableModel model;
    /**
     * Creates new form SystemFrame
     */
    public SystemFrame() {
        initComponents();
        initConnection();
        initState();
        viewTableData();
    }
    
    private void initConnection(){
        final String url = "jdbc:mysql://localhost:3307/movie_mis";
        final String usn = "a";
        final String pwd = "a";
        try {
            conn = DriverManager.getConnection(url,usn,pwd);
            stmt = conn.createStatement();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Database Not Connected","ERROR",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void initTable(){
        final String col[] = {"REGID","CUSTOMERFNAME","CUSTOMERLNAME","CUSTOMERGENDER","NUMOFTICKETS","TICKETPRICE","TICKETDISCOUNT","TDP","TICKETSTATUS"};
        Object row[][] = null;
        model = new DefaultTableModel(row,col){
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        dataTable.setModel(model);
        JTableHeader theader = dataTable.getTableHeader();
        theader.setFont(new Font("Dialog Input",Font.BOLD,13));
        theader.setBackground(new Color(38,38,38));
        theader.setForeground(Color.RED);
    }
    
    private void initState(){
        //TEXTFIELD's
        regidTF.setEditable(false);
        cfnTF.setEditable(false);
        clnmTF.setEditable(false);
        cgTF .setEditable(false);
        notTF.setEditable(false);
        tpTF.setEditable(false);
        //BUTTON's
        newbtn.setEnabled(true);
        savebtn.setEnabled(false);
        updatebtn.setEnabled(false);
        deletebtn.setEnabled(false);
        changestatbtn.setEnabled(false);
    }
    
    private void initFieldsValidation(){
        regidTF.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if(Character.isDigit(evt.getKeyChar()) || evt.getExtendedKeyCode()==KeyEvent.VK_BACK_SPACE
                   || evt.getExtendedKeyCode()==KeyEvent.VK_CONTROL){
                    regidTF.setEditable(true);
                }else{
                    regidTF.setEditable(false);
                }
            }
        });
        
        notTF.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if(Character.isDigit(evt.getKeyChar()) || evt.getExtendedKeyCode()==KeyEvent.VK_BACK_SPACE
                   || evt.getExtendedKeyCode()==KeyEvent.VK_CONTROL || evt.getExtendedKeyCode()==KeyEvent.VK_PERIOD){
                    notTF.setEditable(true);
                }else{
                    notTF.setEditable(false);
                }
            }
        });
        
        tpTF.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if(Character.isDigit(evt.getKeyChar()) || evt.getExtendedKeyCode()==KeyEvent.VK_BACK_SPACE
                   || evt.getExtendedKeyCode()==KeyEvent.VK_CONTROL || evt.getExtendedKeyCode()==KeyEvent.VK_PERIOD){
                    tpTF.setEditable(true);
                }else{
                    tpTF.setEditable(false);
                }
            }
        });
    }
    
    private void makeInput(){
        //TEXTFIELD's
        regidTF.setEditable(true);
        cfnTF.setEditable(true);
        clnmTF.setEditable(true);
        cgTF .setEditable(true);
        notTF.setEditable(true);
        tpTF.setEditable(true);
        //BUTTON's
        newbtn.setEnabled(false);
        savebtn.setEnabled(true);
        updatebtn.setEnabled(true);
        deletebtn.setEnabled(true);
        changestatbtn.setEnabled(true);
    }
    
    private void makeEdit(){
        //TEXTFIELD's
        regidTF.setEditable(false);
        cfnTF.setEditable(true);
        clnmTF.setEditable(true);
        cgTF .setEditable(true);
        notTF.setEditable(true);
        tpTF.setEditable(true);
    }
    
    private void displayDataToTextField(){
        int rowSelected = dataTable.getSelectedRow();
        regidTF.setText(String.valueOf(model.getValueAt(rowSelected, 0)));
        cfnTF.setText(String.valueOf(model.getValueAt(rowSelected, 1)));
        clnmTF.setText(String.valueOf(model.getValueAt(rowSelected, 2)));
        cgTF.setText(String.valueOf(model.getValueAt(rowSelected, 3)));
        notTF.setText(String.valueOf(model.getValueAt(rowSelected, 4)));
        tpTF.setText(String.valueOf(model.getValueAt(rowSelected, 5)));
    }
    
    private void clearFields(){
        regidTF.setText("");
        cfnTF.setText("");
        clnmTF.setText("");
        cgTF.setText("");
        notTF.setText("");
        tpTF.setText("");
    }
    
    private Double discount=0.0;
    private Double getDiscountedPrice(){
        String gender = cgTF.getText();
        Double noticket = Double.valueOf(notTF.getText());
        Double tktprice = Double.valueOf(tpTF.getText());
        Double tdprice = 0.0, pprice;
        
        pprice = noticket*tktprice;
        if(gender.equalsIgnoreCase("male")){
            discount = 0.10;
            tdprice = pprice - (pprice*discount);
        }else if(gender.equalsIgnoreCase("female")){
            discount = 0.25;
            tdprice = pprice - (pprice*discount);
        }else{
            tdprice = pprice;
        }
        return tdprice;
    }
    private void saveData(){
        final String insert = "INSERT INTO registration values('"+
                String.valueOf(regidTF.getText())+"','"+
                cfnTF.getText()+"','"+clnmTF.getText()+"','"+
                cgTF.getText()+"','"+Double.valueOf(notTF.getText())+"','"+
                Double.valueOf(tpTF.getText())+"','"+discount+"','"+
                getDiscountedPrice()+"','RESERVED')";
        try {
            stmt.executeUpdate(insert);
            JOptionPane.showMessageDialog(null,"Saved Successfully","INFO",JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Failed to saved","INFO",JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void viewTableData(){
        initTable();
        final String fetch = "SELECT * FROM registration";
        try {
            ResultSet rs = stmt.executeQuery(fetch);
            while(rs.next()){
                model.addRow(new Object[]{
                    rs.getInt("regID"),
                    rs.getString("customer_fname"),
                    rs.getString("customer_lname"),
                    rs.getString("customer_gender"),
                    rs.getDouble("num_of_tickets"),
                    rs.getDouble("ticket_price"),
                    rs.getDouble("ticket_discount"),
                    rs.getDouble("totalDiscountedPayable"),
                    rs.getString("ticketStatus")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Failed to fetch the data from the database","WARNING",JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void updateData(){
        final String updateData = "UPDATE registration set customer_fname='"+cfnTF.getText()
                +"', customer_lname='"+clnmTF.getText()+"', customer_gender='"+cgTF.getText()+
                "', num_of_tickets='"+Double.valueOf(notTF.getText())+"', ticket_price='"+Double.valueOf(tpTF.getText())+
                "', ticket_discount='"+discount+"', totalDiscountedPayable='"+getDiscountedPrice()+
                "', ticketStatus='RESERVED' where regID='"+Double.valueOf(regidTF.getText())+"'"; 
        try{
            stmt.executeUpdate(updateData);
            JOptionPane.showMessageDialog(null,"Updated Successfully","INFO",JOptionPane.INFORMATION_MESSAGE);
        }catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Failed to update","INFO",JOptionPane.INFORMATION_MESSAGE);
        }catch(NumberFormatException nfe){
            JOptionPane.showMessageDialog(null,"Please select a row to update","INFO",JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void deleteInfo(){
        final String delq = "DELETE FROM registration where regid='"+Integer.valueOf(regidTF.getText())+"'";
        try {
            stmt.executeUpdate(delq);
            JOptionPane.showMessageDialog(null,"Info deleted successfully","INFO",JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Failed to delete information","INFO",JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void changeStat(){
        final String newstat = JOptionPane.showInputDialog("NEW STATUS: ");
        if(newstat!=null){
            try {
                int regid = Integer.parseInt(regidTF.getText());
                final String csq="UPDATE registration set ticketStatus='"+newstat+"' where regID='"+regid+"'";
                stmt.executeUpdate(csq);
                JOptionPane.showMessageDialog(null,"Status updated successfully","INFO",JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null,"Failed to update Status","INFO",JOptionPane.INFORMATION_MESSAGE);
            } catch(NumberFormatException nfe){
                JOptionPane.showMessageDialog(null,"Please select which info to update status","INFO",JOptionPane.INFORMATION_MESSAGE);
            }
        }else{
            initState();
            clearFields();
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton4 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        dataTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        newbtn = new javax.swing.JButton();
        savebtn = new javax.swing.JButton();
        updatebtn = new javax.swing.JButton();
        deletebtn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        changestatbtn = new javax.swing.JButton();
        regidTF = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cfnTF = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        clnmTF = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        cgTF = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        notTF = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        tpTF = new javax.swing.JTextField();

        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton4.setText("UPDATE");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        dataTable.setBackground(new java.awt.Color(153, 153, 153));
        dataTable.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        dataTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        dataTable.getTableHeader().setReorderingAllowed(false);
        dataTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dataTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(dataTable);

        jPanel2.setBackground(new java.awt.Color(153, 153, 153));

        newbtn.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        newbtn.setText("NEW");
        newbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newbtnActionPerformed(evt);
            }
        });

        savebtn.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        savebtn.setText("SAVE");
        savebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savebtnActionPerformed(evt);
            }
        });

        updatebtn.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        updatebtn.setText("UPDATE");
        updatebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updatebtnActionPerformed(evt);
            }
        });

        deletebtn.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        deletebtn.setText("DELETE");
        deletebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletebtnActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("REGISTRATION");

        changestatbtn.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        changestatbtn.setText("CHANGE STATUS");
        changestatbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changestatbtnActionPerformed(evt);
            }
        });

        regidTF.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setText("REG ID");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("CUSTOMER FNAME");

        cfnTF.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setText("CUSTOMER LNAME");

        clnmTF.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setText("CUSTOMER GENDER");

        cgTF.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setText("NO. OF TICKETS");

        notTF.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel7.setText("TICKET PRICE");

        tpTF.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cfnTF, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(clnmTF, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cgTF, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(notTF, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tpTF, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(regidTF, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGap(58, 58, 58)
                                    .addComponent(deletebtn)
                                    .addGap(18, 18, 18)
                                    .addComponent(changestatbtn)
                                    .addGap(0, 0, Short.MAX_VALUE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                    .addComponent(newbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(62, 62, 62)
                                    .addComponent(savebtn)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(updatebtn))))
                        .addGap(0, 32, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(63, 63, 63)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(regidTF, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cfnTF, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clnmTF, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cgTF, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(notTF, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tpTF, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updatebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(savebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(changestatbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deletebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 879, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void newbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newbtnActionPerformed
        makeInput();
        clearFields();
    }//GEN-LAST:event_newbtnActionPerformed

    private void savebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_savebtnActionPerformed
        initFieldsValidation();
        saveData();
        clearFields();
        initState();
        viewTableData(); 
    }//GEN-LAST:event_savebtnActionPerformed

    private void updatebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updatebtnActionPerformed
        updateData();
        clearFields();
        initState();
        viewTableData();
    }//GEN-LAST:event_updatebtnActionPerformed

    private void deletebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletebtnActionPerformed
        deleteInfo();
        clearFields();
        initState();
        viewTableData();
    }//GEN-LAST:event_deletebtnActionPerformed

    private void changestatbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changestatbtnActionPerformed
        changeStat();
        initState();
        viewTableData();
    }//GEN-LAST:event_changestatbtnActionPerformed

    private void dataTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dataTableMouseClicked
        makeEdit();
        displayDataToTextField();
    }//GEN-LAST:event_dataTableMouseClicked



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField cfnTF;
    private javax.swing.JTextField cgTF;
    private javax.swing.JButton changestatbtn;
    private javax.swing.JTextField clnmTF;
    private javax.swing.JTable dataTable;
    private javax.swing.JButton deletebtn;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton newbtn;
    private javax.swing.JTextField notTF;
    private javax.swing.JTextField regidTF;
    private javax.swing.JButton savebtn;
    private javax.swing.JTextField tpTF;
    private javax.swing.JButton updatebtn;
    // End of variables declaration//GEN-END:variables
}
