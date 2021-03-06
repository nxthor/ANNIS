/*
 * Copyright 2009-2011 Collaborative Research Centre SFB 632 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.hu_berlin.german.korpling.annis.kickstarter;

import java.awt.Dialog;

/**
 *
 * @author thomas
 */
public class ExceptionDialog extends javax.swing.JDialog
{

  /** Creates new form ExceptionDialog */
  public ExceptionDialog(Exception exception)
  {
    super((Dialog) null, true);
    init(exception, null);
  }
  
  /** Creates new form ExceptionDialog */
  public ExceptionDialog(Exception exception, String caption)
  {
    super((Dialog) null, true);
    init(exception, caption);
  }

  /** Creates new form ExceptionDialog */
  public ExceptionDialog(java.awt.Dialog parent, Exception exception)
  {
    super(parent, true);
    init(exception, null);
  }

  /** Creates new form ExceptionDialog */
  public ExceptionDialog(java.awt.Frame parent, Exception exception)
  {
    super(parent, true);
    init(exception, null);
  }

  private void init(Exception exception, String caption)
  {
    initComponents();

    if(caption != null)
    {
      lblCaption.setText(caption + ":");
    }
    
    if(exception != null)
    {
      lblType.setText(exception.getClass().getName());
      
      txtMessage.setText(exception.getLocalizedMessage());
      txtMessage.setCaretPosition(0);
      StringBuilder details = new StringBuilder();
      details.append(exception.getLocalizedMessage());
      details.append("\nat\n");
      StackTraceElement[] st = exception.getStackTrace();
      for(int i=0; i < st.length; i++)
      {
        details.append(st[i].toString());
        details.append("\n");
      }
      txtDetails.setText(details.toString());
    }
  }


  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblCaption = new javax.swing.JLabel();
        btClose = new javax.swing.JButton();
        spDetails = new javax.swing.JScrollPane();
        txtDetails = new javax.swing.JTextArea();
        btDetails = new javax.swing.JToggleButton();
        spMessage = new javax.swing.JScrollPane();
        txtMessage = new javax.swing.JTextArea();
        lblTypeCaption = new javax.swing.JLabel();
        lblType = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Exception thrown");
        setLocationByPlatform(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        lblCaption.setFont(new java.awt.Font("DejaVu Sans", 1, 13)); // NOI18N
        lblCaption.setText("Exception thrown:");

        btClose.setMnemonic('C');
        btClose.setText("Close");
        btClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCloseActionPerformed(evt);
            }
        });

        txtDetails.setColumns(20);
        txtDetails.setRows(5);
        txtDetails.setText("<no details>");
        spDetails.setViewportView(txtDetails);

        btDetails.setMnemonic('D');
        btDetails.setText("Details");
        btDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDetailsActionPerformed(evt);
            }
        });

        txtMessage.setColumns(20);
        txtMessage.setEditable(false);
        txtMessage.setLineWrap(true);
        txtMessage.setRows(5);
        txtMessage.setText("<no message>");
        txtMessage.setWrapStyleWord(true);
        spMessage.setViewportView(txtMessage);

        lblTypeCaption.setText("Type:");

        lblType.setText("<unknown>");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(spDetails, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 538, Short.MAX_VALUE)
                    .addComponent(lblCaption, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                    .addComponent(spMessage, javax.swing.GroupLayout.DEFAULT_SIZE, 538, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btDetails)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btClose))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblTypeCaption)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblType, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCaption)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spMessage)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTypeCaption)
                    .addComponent(lblType))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btDetails)
                    .addComponent(btClose))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

  private void btDetailsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btDetailsActionPerformed
  {//GEN-HEADEREND:event_btDetailsActionPerformed

    spDetails.setVisible(btDetails.isSelected());
    pack();
    validate();
  }//GEN-LAST:event_btDetailsActionPerformed

  private void btCloseActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btCloseActionPerformed
  {//GEN-HEADEREND:event_btCloseActionPerformed
    
    this.setVisible(false);
    this.dispose();

  }//GEN-LAST:event_btCloseActionPerformed

  private void formWindowOpened(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowOpened
  {//GEN-HEADEREND:event_formWindowOpened

      spDetails.setVisible(false);
      pack();
      validate();

  }//GEN-LAST:event_formWindowOpened

  /**
   * @param args the command line arguments
   */
  public static void main(String args[])
  {
    java.awt.EventQueue.invokeLater(new Runnable()
    {
      @Override
      public void run()
      {
        ExceptionDialog dialog = new ExceptionDialog(new javax.swing.JFrame(), null);
        dialog.addWindowListener(new java.awt.event.WindowAdapter()
        {

          @Override
          public void windowClosing(java.awt.event.WindowEvent e)
          {
            System.exit(0);
          }
        });
        dialog.setVisible(true);
      }
    });
  }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btClose;
    private javax.swing.JToggleButton btDetails;
    private javax.swing.JLabel lblCaption;
    private javax.swing.JLabel lblType;
    private javax.swing.JLabel lblTypeCaption;
    private javax.swing.JScrollPane spDetails;
    private javax.swing.JScrollPane spMessage;
    private javax.swing.JTextArea txtDetails;
    private javax.swing.JTextArea txtMessage;
    // End of variables declaration//GEN-END:variables
}
