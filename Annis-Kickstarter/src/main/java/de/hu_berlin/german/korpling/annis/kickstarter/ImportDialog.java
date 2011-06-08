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

import annis.administration.CorpusAdministration;
import annis.administration.SpringAnnisAdministrationDao;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

/**
 *
 * @author thomas
 */
public class ImportDialog extends javax.swing.JDialog
{

  private CorpusAdministration corpusAdministration;
  private SwingWorker<String, Void> worker;
  private boolean isImporting;

  /** Creates new form ImportDialog */
  public ImportDialog(java.awt.Frame parent, boolean modal, CorpusAdministration corpusAdmin)
  {
    super(parent, modal);

    this.corpusAdministration = corpusAdmin;

    initComponents();

    isImporting = false;
    worker = new SwingWorker<String, Void>()
    {

      @Override
      protected String doInBackground() throws Exception
      {
        try
        {
          corpusAdministration.importCorpora(false, txtInputDir.getText());
        }
        catch (Exception ex)
        {
          ex.printStackTrace();
          return ex.getMessage();
        }
        return "";
      }

      @Override
      protected void done()
      {
        isImporting = false;
        btOk.setEnabled(true);
        btSearchInputDir.setEnabled(true);
        txtInputDir.setEnabled(true);
        pbImport.setIndeterminate(false);
        try
        {
          if ("".equals(this.get()))
          {
            JOptionPane.showMessageDialog(null,
              "Corpus imported.", "INFO", JOptionPane.INFORMATION_MESSAGE);
            setVisible(false);
          }
          else
          {
            new ExceptionDialog(new Exception(
              "Import failed: " + this.get())).setVisible(true);
            setVisible(false);
          }
        }
        catch (Exception ex)
        {
          Logger.getLogger(ImportDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    };

    org.apache.log4j.Logger.getLogger(SpringAnnisAdministrationDao.class).addAppender(
      new AppenderSkeleton() {

      @Override
      protected void append(LoggingEvent event)
      {
        if(event.getLevel().isGreaterOrEqual(org.apache.log4j.Level.INFO))
        {
          lblStatus.setText(event.getMessage().toString());
        }
      }

      @Override
      public boolean requiresLayout()
      {
        return false;
      }

      @Override
      public void close()
      {
      }
    });
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    fileChooser = new javax.swing.JFileChooser();
    jLabel1 = new javax.swing.JLabel();
    txtInputDir = new javax.swing.JTextField();
    btCancel = new javax.swing.JButton();
    btOk = new javax.swing.JButton();
    btSearchInputDir = new javax.swing.JButton();
    pbImport = new javax.swing.JProgressBar();
    jLabel2 = new javax.swing.JLabel();
    lblStatus = new javax.swing.JLabel();

    fileChooser.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setTitle("Import - Annis² Kickstarter");
    setLocationByPlatform(true);

    jLabel1.setText("Directory to import:");

    btCancel.setMnemonic('c');
    btCancel.setText("Cancel");
    btCancel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btCancelActionPerformed(evt);
      }
    });

    btOk.setMnemonic('o');
    btOk.setText("Ok");
    btOk.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btOkActionPerformed(evt);
      }
    });

    btSearchInputDir.setText("...");
    btSearchInputDir.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btSearchInputDirActionPerformed(evt);
      }
    });

    jLabel2.setText("status:");

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(pbImport, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
          .addGroup(layout.createSequentialGroup()
            .addComponent(jLabel1)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(txtInputDir, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(btSearchInputDir, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(layout.createSequentialGroup()
            .addComponent(btCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 274, Short.MAX_VALUE)
            .addComponent(btOk, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(layout.createSequentialGroup()
            .addComponent(jLabel2)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(lblStatus, javax.swing.GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel1)
          .addComponent(txtInputDir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(btSearchInputDir))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(pbImport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel2)
          .addComponent(lblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(btCancel)
          .addComponent(btOk))
        .addContainerGap())
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

    private void btCancelActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btCancelActionPerformed
    {//GEN-HEADEREND:event_btCancelActionPerformed

      if (isImporting)
      {
        worker.cancel(true);
      }
      setVisible(false);
    }//GEN-LAST:event_btCancelActionPerformed

    private void btOkActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btOkActionPerformed
    {//GEN-HEADEREND:event_btOkActionPerformed

      btOk.setEnabled(false);
      btSearchInputDir.setEnabled(false);
      txtInputDir.setEnabled(false);

      pbImport.setIndeterminate(true);

      isImporting = true;
      worker.execute();

    }//GEN-LAST:event_btOkActionPerformed

    private void btSearchInputDirActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btSearchInputDirActionPerformed
    {//GEN-HEADEREND:event_btSearchInputDirActionPerformed

      if (!"".equals(txtInputDir.getText()))
      {
        File dir = new File(txtInputDir.getText());
        if (dir.exists() && dir.isDirectory())
        {
          fileChooser.setSelectedFile(dir);
        }
      }

      if (fileChooser.showDialog(this, "Select") == JFileChooser.APPROVE_OPTION)
      {
        File f = fileChooser.getSelectedFile();
        txtInputDir.setText(f.getAbsolutePath());
      }

    }//GEN-LAST:event_btSearchInputDirActionPerformed
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton btCancel;
  private javax.swing.JButton btOk;
  private javax.swing.JButton btSearchInputDir;
  private javax.swing.JFileChooser fileChooser;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel lblStatus;
  private javax.swing.JProgressBar pbImport;
  private javax.swing.JTextField txtInputDir;
  // End of variables declaration//GEN-END:variables
}
