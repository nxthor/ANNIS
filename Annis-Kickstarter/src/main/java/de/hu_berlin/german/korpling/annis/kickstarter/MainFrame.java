/*
 *  Copyright 2009 thomas.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

/*
 * MainFrame.java
 *
 * Created on 26.10.2009, 16:29:20
 */
package de.hu_berlin.german.korpling.annis.kickstarter;

import annis.administration.CorpusAdministration;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author thomas
 */
public class MainFrame extends javax.swing.JFrame
{

  private CorpusAdministration corpusAdministration;
  private SwingWorker<String, String> serviceWorker;

  /** Creates new form MainFrame */
  public MainFrame()
  {
    // init corpusAdministration
    System.setProperty("annis.home", ".");
    ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("annis/administration/AnnisAdminRunner-context.xml");
    this.corpusAdministration = (CorpusAdministration) ctx.getBean("corpusAdministration");

    try
    {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch(Exception ex)
    {
      Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
    }

    initComponents();

    // position window at the center of the screen
    Helper.centerWindow(this);

    serviceWorker = new SwingWorker<String, String>()
    {

      @Override
      protected String doInBackground() throws Exception
      {
        setProgress(1);
        try
        {
          startService();
          setProgress(2);
          startJetty();
          setProgress(3);
        }
        catch(Exception ex)
        {
          return ex.getLocalizedMessage();
        }
        return "";
      }

      @Override
      protected void done()
      {
        try
        {
          if("".equals(this.get()))
          {
            lblStatusService.setText("Annis started");
            lblStatusService.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hu_berlin/german/korpling/annis/kickstarter/crystal_icons/button_ok.png")));
          }
          else
          {
            lblStatusService.setText("Annis start failed: " + this.get());
            lblStatusService.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hu_berlin/german/korpling/annis/kickstarter/crystal_icons/no.png")));
          }
        }
        catch(Exception ex)
        {
          new ExceptionDialog(null, ex).setVisible(true);
        }
      }
    };
    serviceWorker.addPropertyChangeListener(new PropertyChangeListener()
    {

      public void propertyChange(PropertyChangeEvent evt)
      {
        if(serviceWorker.getProgress() == 1)
        {
          lblStatusService.setText("Starting Annis...");
          lblStatusService.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hu_berlin/german/korpling/annis/kickstarter/crystal_icons/quick_restart.png")));
        }
      }
    });

    serviceWorker.execute();
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    btInit = new javax.swing.JButton();
    btImport = new javax.swing.JButton();
    btList = new javax.swing.JButton();
    lblStatusService = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("Annis² Kickstarter");

    btInit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hu_berlin/german/korpling/annis/kickstarter/crystal_icons/db_comit.png"))); // NOI18N
    btInit.setMnemonic('d');
    btInit.setText("Init database");
    btInit.setName("btInit"); // NOI18N
    btInit.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btInitActionPerformed(evt);
      }
    });

    btImport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hu_berlin/german/korpling/annis/kickstarter/crystal_icons/db_add.png"))); // NOI18N
    btImport.setMnemonic('i');
    btImport.setText("Import corpus");
    btImport.setName("btImport"); // NOI18N
    btImport.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btImportActionPerformed(evt);
      }
    });

    btList.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hu_berlin/german/korpling/annis/kickstarter/crystal_icons/month.png"))); // NOI18N
    btList.setMnemonic('l');
    btList.setText("List imported corpora");
    btList.setName("btList"); // NOI18N

    lblStatusService.setFont(new java.awt.Font("DejaVu Sans", 0, 18)); // NOI18N
    lblStatusService.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    lblStatusService.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/hu_berlin/german/korpling/annis/kickstarter/crystal_icons/no.png"))); // NOI18N
    lblStatusService.setText("Annis stopped");
    lblStatusService.setName("lblStatusService"); // NOI18N

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(btImport, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
          .addComponent(btInit, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
          .addComponent(btList, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
          .addComponent(lblStatusService, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(btInit)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(btImport)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(btList)
        .addGap(18, 18, 18)
        .addComponent(lblStatusService)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

    private void btInitActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btInitActionPerformed
    {//GEN-HEADEREND:event_btInitActionPerformed

      InitDialog dlg = new InitDialog(this, true, corpusAdministration);
      dlg.setVisible(true);

    }//GEN-LAST:event_btInitActionPerformed

    private void btImportActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btImportActionPerformed
    {//GEN-HEADEREND:event_btImportActionPerformed

      ImportDialog dlg = new ImportDialog(this, true, corpusAdministration);
      dlg.setVisible(true);

    }//GEN-LAST:event_btImportActionPerformed

  private void startService() throws Exception
  {

    // starts RMI service at bean creation
    new ClassPathXmlApplicationContext(
      "annis/service/internal/AnnisServiceRunner-context.xml");
  }

  private void startJetty() throws Exception
  {
    Server jetty = new Server(8080);
    // add context for our bundled webapp
    WebAppContext context = new WebAppContext("./webapp/", "/Annis-web");
    Map<String, String> initParams = new HashMap<String, String>();
    initParams.put("managerClassName", "annis.security.TestSecurityManager");
    context.setInitParams(initParams);

    jetty.setHandler(context);

    // start
    jetty.start();

  }
  /**
   * @param args the command line arguments
   */
  public static void main(String args[])
  {
    java.awt.EventQueue.invokeLater(new Runnable()
    {

      public void run()
      {
        MainFrame frame = new MainFrame();
        frame.setVisible(true);
      }
    });
  }
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton btImport;
  private javax.swing.JButton btInit;
  private javax.swing.JButton btList;
  private javax.swing.JLabel lblStatusService;
  // End of variables declaration//GEN-END:variables
}