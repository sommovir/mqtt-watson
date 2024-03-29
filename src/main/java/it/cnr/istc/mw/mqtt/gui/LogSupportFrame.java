/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.mw.mqtt.gui;

import it.cnr.istc.mw.mqtt.logic.generals.ConsoleColors;
import it.cnr.istc.mw.mqtt.Main;
import it.cnr.istc.mw.mqtt.WatsonManager;
import it.cnr.istc.mw.mqtt.exceptions.GuiPrintableException;
import it.cnr.istc.mw.mqtt.exceptions.InvalidAttemptToLogException;
import it.cnr.istc.mw.mqtt.exceptions.LogOffException;
import it.cnr.istc.mw.mqtt.logic.events.LoggerEventListener;
import it.cnr.istc.mw.mqtt.logic.logger.LogTitles;
import it.cnr.istc.mw.mqtt.logic.logger.LoggerManager;
import it.cnr.istc.mw.mqtt.logic.logger.LoggingTag;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author Alessio
 */
public class LogSupportFrame extends javax.swing.JFrame implements WindowListener, LoggerEventListener {

    private Date startNoteTypingTime = null;
    /**
     * Creates new form LogSupportFrame
     */
    public LogSupportFrame() {//Cambia il nome dei Jspinner
        initComponents();
        this.setTitle("Log Support v0.2 - " + LoggerManager.getInstance().getAdminName());
        this.setLocationRelativeTo(null);//Centra il frame
        this.setAlwaysOnTop(true);
        this.addWindowListener(this);
        this.setIconImage(Icons.LOG_32.getIcon().getImage());
        SpinnerNumberModel modelAlpha = new SpinnerNumberModel(0.6d, 0d, 1d, 0.1d);
        this.jSpinnerAlpha.setModel(modelAlpha);//Prebdi valori da modelAlpha
        SpinnerNumberModel modelBeta = new SpinnerNumberModel(0.2d, 0d, 1d, 0.1d);
        this.jSpinnerBeta.setModel(modelBeta);//Prebdi valori da modelAlpha
        SpinnerNumberModel modelGamma = new SpinnerNumberModel(1, 0, 10, 1);
        this.jSpinnerGamma.setModel(modelGamma);//Prebdi valori da modelAlpha
        renderLogOnOffButton(LoggerManager.getInstance().isLogActive());
        renderTestOnOffButton(WatsonManager.getInstance().isTestMode());
        LoggerManager.getInstance().addLoggerEventListener(this);
        this.jToggleButton_LogOff.setEnabled(WatsonManager.getInstance().isTestMode());
        setMainCommandsEnabled(LoggerManager.getInstance().isLogActive());
        noteEnabled(LoggerManager.getInstance().isLogging());
        

    }

    
    private void noteEnabled(boolean enabled){
        this.jTextField_Note.setEnabled(enabled);
        this.jButton_Note.setEnabled(enabled);
    }
    
    private void renderLogOnOffButton(boolean logActive) {

        jToggleButton_TestMode.setEnabled(!logActive);
        this.jToggleButton_LogOff.setSelected(logActive);
        //this.jToggleButton_LogOff.setBackground(logActive ? Color.GREEN : null);
        this.jLabel_logOn.setIcon(logActive ? Icons.GREEN_DOT.getIcon() : Icons.RED_DOT.getIcon());
        this.jToggleButton_LogOff.setText(logActive ? "Log ON" : "Log OFF");
        noteEnabled(logActive);
        setMainCommandsEnabled(logActive);
        this.jButton_log_end_pretest.setEnabled(logActive);
        this.jToggleButton_LogOff.setToolTipText(logActive ? "<htmL>La modalità di logging è <font color=green><b>attiva</b></font>, premere per disattivarla.<br>"
                + "La modalità di logging attiva consente di salvare i dati di test in un file di log" : "<htmL>La modalità di logging è <font color=red><b>disattivata</b></font>, premere per attivarla.<br>"
                + "La modalità di logging attiva consente di salvare i dati di test in un file di log");
    }

    private void renderTestOnOffButton(boolean testActive) {

        this.jToggleButton_TestMode.setToolTipText(testActive ? "<htmL>La modalità di test è <font color=green><b>attiva</b></font>, premere per disattivarla.<br>"
                + "La modalità di test attiva non consente più di una connessione attiva al server." : "<htmL>La modalità di test è <font color=red><b>disattivata</b></font>, premere per attivarla.<br>"
                + "La modalità di test attiva non consente più di una connessione attiva al server.");
        this.jToggleButton_TestMode.setSelected(testActive);
        this.jLabel_TestMode.setIcon(testActive ? Icons.GREEN_DOT.getIcon() : Icons.RED_DOT.getIcon());
        this.jToggleButton_TestMode.setText(testActive ? "Test ON" : "Test OFF");
    }

    public void printError(String message) {
        this.jLabel_FeedBack.setForeground(Color.red);
        this.jLabel_FeedBack.setText(message);
    }

    public void printWarning(String message) {
        this.jLabel_FeedBack.setForeground(Color.yellow);
        this.jLabel_FeedBack.setText(message);
    }

    public void printInfo(String message) {
        this.jLabel_FeedBack.setForeground(Color.cyan);
        this.jLabel_FeedBack.setText(message);
    }

    private void newNote() {
        String text = this.jTextField_Note.getText();
        jLabel_FeedBack.setText("");

        if (!LoggerManager.getInstance().isLogActive()) {
            printError("Impossibile eseguire quando il log è OFF");
            System.out.println(LogTitles.GUI.getTitle() + ConsoleColors.ANSI_RED + "Impossibile eseguire quando il log è OFF (per maggiori informazioni consulta help log)" + ConsoleColors.ANSI_RESET);
        } else {
            try {
                if (text.isEmpty()) {
                    printError("Non puoi mandare note vuote");
                } else {
                    LoggerManager.getInstance().logByGUi(LoggingTag.NOTE.getTag() + " " + text,startNoteTypingTime);
                    printInfo(LogTitles.GUI.getTitle() + "New note tag logged");
                }
            } catch (LogOffException | InvalidAttemptToLogException ex) {
                if (ex instanceof GuiPrintableException) {
                    printError(((GuiPrintableException) ex).getGuiErrorMessage());
                }
            }
            this.jTextField_Note.setText("");
            System.out.println(LogTitles.GUI.getTitle() + "Note has been added");
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

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField_Note = new javax.swing.JTextField();
        jButton_Note = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jButton_WallSpeak = new javax.swing.JButton();
        jButton_Wrong = new javax.swing.JButton();
        jButton_Reprompt = new javax.swing.JButton();
        jTextField_WallSpeak = new javax.swing.JTextField();
        jButton_ExtraInput = new javax.swing.JButton();
        jTextField_Extra = new javax.swing.JTextField();
        jButton_WrongInput = new javax.swing.JButton();
        jTextField_WrongInput = new javax.swing.JTextField();
        jButton_log_end_pretest = new javax.swing.JButton();
        jButton_resume = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jSpinnerAlpha = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        jSpinnerBeta = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        jSpinnerGamma = new javax.swing.JSpinner();
        jButton_ResetABG = new javax.swing.JButton();
        jButton_ApplyABG = new javax.swing.JButton();
        jLabel_FeedBack = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        jToggleButton_TestMode = new javax.swing.JToggleButton();
        jLabel_TestMode = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jToggleButton_LogOff = new javax.swing.JToggleButton();
        jLabel_logOn = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 32767));
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        jLabel1.setText("Nota:");

        jTextField_Note.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_NoteActionPerformed(evt);
            }
        });
        jTextField_Note.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField_NoteKeyPressed(evt);
            }
        });

        jButton_Note.setText("Log Note");
        jButton_Note.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_NoteActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Shortcuts", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12), new java.awt.Color(255, 255, 255)) );

        jButton_WallSpeak.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton_WallSpeak.setForeground(new java.awt.Color(204, 204, 0));
        jButton_WallSpeak.setText("<WALL SPEAK>");
        jButton_WallSpeak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_WallSpeakActionPerformed(evt);
            }
        });

        jButton_Wrong.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton_Wrong.setForeground(new java.awt.Color(255, 51, 0));
        jButton_Wrong.setText("<WRONG>");
        jButton_Wrong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_WrongActionPerformed(evt);
            }
        });

        jButton_Reprompt.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton_Reprompt.setForeground(new java.awt.Color(255, 153, 0));
        jButton_Reprompt.setText("<REPROMPT>");
        jButton_Reprompt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_RepromptActionPerformed(evt);
            }
        });

        jTextField_WallSpeak.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField_WallSpeakKeyPressed(evt);
            }
        });

        jButton_ExtraInput.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton_ExtraInput.setForeground(new java.awt.Color(102, 204, 0));
        jButton_ExtraInput.setText("<EXTRA>");
        jButton_ExtraInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ExtraInputActionPerformed(evt);
            }
        });

        jTextField_Extra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_ExtraActionPerformed(evt);
            }
        });
        jTextField_Extra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField_ExtraKeyPressed(evt);
            }
        });

        jButton_WrongInput.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton_WrongInput.setForeground(new java.awt.Color(0, 153, 255));
        jButton_WrongInput.setText("<WRONG INPUT>");
        jButton_WrongInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_WrongInputActionPerformed(evt);
            }
        });

        jTextField_WrongInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField_WrongInputKeyPressed(evt);
            }
        });

        jButton_log_end_pretest.setBackground(new java.awt.Color(0, 51, 153));
        jButton_log_end_pretest.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton_log_end_pretest.setForeground(new java.awt.Color(255, 255, 255));
        jButton_log_end_pretest.setText("log end pretest");
        jButton_log_end_pretest.setEnabled(false);
        jButton_log_end_pretest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_log_end_pretestActionPerformed(evt);
            }
        });

        jButton_resume.setBackground(new java.awt.Color(102, 255, 0));
        jButton_resume.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton_resume.setForeground(new java.awt.Color(0, 51, 204));
        jButton_resume.setText("resume");
        jButton_resume.setEnabled(false);
        jButton_resume.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_resumeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton_Wrong, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton_WrongInput, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton_WallSpeak, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton_ExtraInput, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton_Reprompt, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton_log_end_pretest)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton_resume))
                    .addComponent(jTextField_WallSpeak)
                    .addComponent(jTextField_Extra)
                    .addComponent(jTextField_WrongInput))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_WallSpeak)
                    .addComponent(jTextField_WallSpeak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_ExtraInput)
                    .addComponent(jTextField_Extra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_WrongInput)
                    .addComponent(jTextField_WrongInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_Wrong)
                    .addComponent(jButton_Reprompt)
                    .addComponent(jButton_log_end_pretest)
                    .addComponent(jButton_resume))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField_Note)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton_Note)
                        .addContainerGap())))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField_Note, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_Note))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "General Settings", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12), new java.awt.Color(255, 255, 255)));

        jPanel5.setLayout(new java.awt.GridLayout(4, 2, 10, 10));

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Alpha");
        jPanel5.add(jLabel2);

        jSpinnerAlpha.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(6.0f), Float.valueOf(0.0f), Float.valueOf(6.0f), Float.valueOf(1.0f)));
        jSpinnerAlpha.setToolTipText("<html> <div width = 150px><i> Definisce la soglia minima dell’<font color = blue> intent </font> con il più alto valore di confidence per essere accettato dalla chatbot</i><b> Valore default : 0.6 </b></div>");
        jPanel5.add(jSpinnerAlpha);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Beta");
        jPanel5.add(jLabel3);

        jSpinnerBeta.setToolTipText("<html><div width = 150px><i>Definisce la differenza minima tra gli <font color = red>intents </font> a più alta confidence</i><b> Valore default: 0.2 </b></div>");
        jPanel5.add(jSpinnerBeta);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Gamma");
        jPanel5.add(jLabel4);

        jSpinnerGamma.setToolTipText("<html><div width= 150px><i>Indica il numero massimo di deadlocks nei <font color = green>nodi di Watson </font>, superato questo valore il server attuerà un hard reset.</i><b> Valore default: 1 </b></div>");
        jPanel5.add(jSpinnerGamma);

        jButton_ResetABG.setText("Reset to Default");
        jButton_ResetABG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ResetABGActionPerformed(evt);
            }
        });

        jButton_ApplyABG.setText("Apply");
        jButton_ApplyABG.setToolTipText("<html><b>Applica</b> i cambiamenti sui valori : Alpha - Beta - Gamma");
        jButton_ApplyABG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ApplyABGActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton_ApplyABG, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_ResetABG, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton_ApplyABG)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton_ResetABG)
                .addContainerGap())
        );

        jLabel_FeedBack.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel_FeedBack.setForeground(new java.awt.Color(255, 0, 0));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/logger_logo4.png"))); // NOI18N

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jToggleButton_TestMode.setText("Test Mode ON");
        jToggleButton_TestMode.setFocusable(false);
        jToggleButton_TestMode.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton_TestMode.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton_TestMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton_TestModeActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButton_TestMode);

        jLabel_TestMode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/green16.png"))); // NOI18N
        jToolBar1.add(jLabel_TestMode);
        jToolBar1.add(jSeparator1);

        jToggleButton_LogOff.setText("Log OFF");
        jToggleButton_LogOff.setFocusable(false);
        jToggleButton_LogOff.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton_LogOff.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton_LogOff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton_LogOffActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButton_LogOff);

        jLabel_logOn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/red16.png"))); // NOI18N
        jToolBar1.add(jLabel_logOn);
        jToolBar1.add(filler1);
        jToolBar1.add(jSeparator2);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/openFile.png"))); // NOI18N
        jButton1.setText("Open Log File");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/open24.png"))); // NOI18N
        jButton2.setText("Open Log Folder");
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(21, 21, 21)
                            .addComponent(jLabel_FeedBack, javax.swing.GroupLayout.PREFERRED_SIZE, 528, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(9, 9, 9)
                            .addComponent(jLabel5)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 15, Short.MAX_VALUE)
                        .addComponent(jLabel_FeedBack, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_ApplyABGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ApplyABGActionPerformed
        // TODO add your handling code here:
        //Check di chi cambia
        boolean change = false;
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        if (WatsonManager.getInstance().getMinSingleDeltaThreshold() != Double.parseDouble(jSpinnerAlpha.getValue().toString())) {
            WatsonManager.getInstance().setMinSingleDeltaThreshold(Double.parseDouble(jSpinnerAlpha.getValue().toString()));
            System.out.println(LogTitles.GUI.getTitle() + "alpha settata a: " + df.format(Double.parseDouble(jSpinnerAlpha.getValue().toString())));
            change = true;

            try {
                LoggerManager.getInstance().log(LoggingTag.ALPHA.getTag() + " " + df.format(Double.parseDouble(jSpinnerAlpha.getValue().toString())));
            } catch (LogOffException | InvalidAttemptToLogException ex) {
                System.out.println(ex.getMessage());
            }
        }

        if (WatsonManager.getInstance().getMinDeltaThreshold() != Double.parseDouble(jSpinnerBeta.getValue().toString())) {
            WatsonManager.getInstance().setMinDeltaThreshold(Double.parseDouble(jSpinnerBeta.getValue().toString()));
            System.out.println(LogTitles.GUI.getTitle() + "beta settato a: " + df.format(Double.parseDouble(jSpinnerBeta.getValue().toString())));
            change = true;
            try {
                LoggerManager.getInstance().log(LoggingTag.BETA.getTag() + " " + df.format(Double.parseDouble(jSpinnerBeta.getValue().toString())));
            } catch (LogOffException | InvalidAttemptToLogException ex) {
                System.out.println(ex.getMessage());
            }
        }

        if (WatsonManager.getInstance().getMaxDeadlocks() != Integer.parseInt(jSpinnerGamma.getValue().toString())) {
            WatsonManager.getInstance().setMaxDeadlocks(Integer.parseInt(jSpinnerGamma.getValue().toString()));
            System.out.println(LogTitles.GUI.getTitle() + "gamma settato a: " + Integer.parseInt(jSpinnerGamma.getValue().toString()));
            change = true;
            try {
                LoggerManager.getInstance().log(LoggingTag.GAMMA.getTag() + " " + Integer.parseInt(jSpinnerGamma.getValue().toString()));
            } catch (LogOffException | InvalidAttemptToLogException ex) {
                System.out.println(ex.getMessage());
            }
        }

        if (change) {
            printInfo("Valori cambiati");
        }

        /*WatsonManager.getInstance().setMinSingleDeltaThreshold(Float.parseFloat(x));
        System.out.println("Alpha : " + x);
        String y = jSpinner2.getValue().toString();
        WatsonManager.getInstance().setMinDeltaThreshold(Float.parseFloat(x));
        System.out.println("Beta : " + y);
        String z = jSpinner3.getValue().toString();
        WatsonManager.getInstance().setMaxDeadlocks(Integer.parseInt(z));
        System.out.println("Gamma : " + z);
        printInfo("Wallspeak tag logged + note");*/
    }//GEN-LAST:event_jButton_ApplyABGActionPerformed

    private void jButton_ResetABGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ResetABGActionPerformed

        // TODO add your handling code here:
        //Senza Panel
        String input = JOptionPane.showInputDialog(this, "Sei sicuro di resettare i valori a default?Scrivere CONFERMA per eseguire", "CONFERMA", JOptionPane.WARNING_MESSAGE);
        if (input != null && input.equals("CONFERMA")) {

            WatsonManager.getInstance().setMinSingleDeltaThreshold(0.6); //Alpha
            this.jSpinnerAlpha.setValue(0.6f);
            WatsonManager.getInstance().setMinDeltaThreshold(0.2); //Beta
            this.jSpinnerBeta.setValue(0.2f);
            WatsonManager.getInstance().setMaxDeadlocks(1); //Gamma
            this.jSpinnerGamma.setValue(1f);
            printWarning("Valori settati default");
            System.out.println(LogTitles.GUI.getTitle() + ConsoleColors.ANSI_GREEN + "Reset eseguito" + ConsoleColors.ANSI_RESET);

            try {

                LoggerManager.getInstance().log(LoggingTag.ALPHA.getTag() + " " + 0.6);
                LoggerManager.getInstance().log(LoggingTag.BETA.getTag() + " " + 0.2);
                LoggerManager.getInstance().log(LoggingTag.GAMMA.getTag() + " " + 1);

            } catch (LogOffException | InvalidAttemptToLogException ex) {
                System.out.println(ex.getMessage());
            }
        } else {
            System.out.println("Ciao");
        }
    }//GEN-LAST:event_jButton_ResetABGActionPerformed

    private void setMainCommandsEnabled(boolean enabled) {
        this.jButton_WallSpeak.setEnabled(enabled);
        this.jTextField_WallSpeak.setEnabled(enabled);
        this.jButton_ExtraInput.setEnabled(enabled);
        this.jTextField_Extra.setEnabled(enabled);
        this.jButton_WrongInput.setEnabled(enabled);
        this.jTextField_WrongInput.setEnabled(enabled);
        this.jButton_Wrong.setEnabled(enabled);
        this.jButton_Reprompt.setEnabled(enabled);
    }

    private void jButton_log_end_pretestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_log_end_pretestActionPerformed
        try {
            this.jButton_log_end_pretest.setEnabled(false);
            this.jButton_resume.setEnabled(true);
            LoggerManager.getInstance().pauseLogging();
            printInfo("The pretest marker has been added, no further pretest will be accepted on such file");
            System.out.println(LogTitles.LOGGER.getTitle() + "The pretest marker has been added, no further pretest will be accepted on such file");
            setMainCommandsEnabled(false);
        } catch (LogOffException | InvalidAttemptToLogException ex) {
            printError(ex.getMessage());
        }
    }//GEN-LAST:event_jButton_log_end_pretestActionPerformed

    private void jButton_WrongInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_WrongInputActionPerformed
        // TODO add your handling code here:
        if (!jTextField_WrongInput.getText().isEmpty()) {
            try {
                LoggerManager.getInstance().log(LoggingTag.WRONG_INPUT.getTag() + " " + jTextField_WrongInput.getText());
                System.out.println(LogTitles.GUI.getTitle() + "Wrong input tag logged + ( " + jTextField_WrongInput.getText() + " )");
                printInfo("Wrong input tag logged + note");
                jTextField_WrongInput.setText(null);
            } catch (LogOffException | InvalidAttemptToLogException ex) {
                System.out.println(LogTitles.LOGGER.getTitle() + ex.getMessage());
                if (ex instanceof GuiPrintableException) {
                    printError(((GuiPrintableException) ex).getGuiErrorMessage());
                }
            }
        } else {
            try {
                LoggerManager.getInstance().log(LoggingTag.WRONG_INPUT.getTag());
                System.out.println(LogTitles.GUI.getTitle() + "Wrong input tag logged");
                printInfo("Wrong input tag logged");
            } catch (LogOffException | InvalidAttemptToLogException ex) {
                System.out.println(LogTitles.LOGGER.getTitle() + ex.getMessage());
                if (ex instanceof GuiPrintableException) {
                    printError(((GuiPrintableException) ex).getGuiErrorMessage());
                }
            }
        }
    }//GEN-LAST:event_jButton_WrongInputActionPerformed

    private void jButton_ExtraInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ExtraInputActionPerformed
        if (!jTextField_Extra.getText().isEmpty()) {
            try {
                LoggerManager.getInstance().log(LoggingTag.EXTRA_INPUT.getTag() + " " + jTextField_Extra.getText());
                System.out.println(LogTitles.GUI.getTitle() + "Wallspeak tag logged + ( " + jTextField_Extra.getText() + " )");
                printInfo("Extra tag logged + note");
                jTextField_Extra.setText(null);
            } catch (LogOffException | InvalidAttemptToLogException ex) {
                System.out.println(LogTitles.LOGGER.getTitle() + ex.getMessage());
                if (ex instanceof GuiPrintableException) {
                    printError(((GuiPrintableException) ex).getGuiErrorMessage());
                }
            }
        } else {
            try {
                LoggerManager.getInstance().log(LoggingTag.EXTRA_INPUT.getTag());
                System.out.println(LogTitles.GUI.getTitle() + "Extra Input tag logged");
                printInfo("Extra input tag logged");
            } catch (LogOffException | InvalidAttemptToLogException ex) {
                System.out.println(LogTitles.LOGGER.getTitle() + ex.getMessage());
                if (ex instanceof GuiPrintableException) {
                    printError(((GuiPrintableException) ex).getGuiErrorMessage());
                }
            }
        }
    }//GEN-LAST:event_jButton_ExtraInputActionPerformed

    private void jButton_RepromptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_RepromptActionPerformed
        try {
            // TODO add your handling code here:
            LoggerManager.getInstance().log(LoggingTag.REPROMPT.getTag());
            System.out.println(LogTitles.GUI.getTitle() + "Repromt tag logged");
            printInfo("Repromt eseguito");
        } catch (LogOffException | InvalidAttemptToLogException ex) {
            System.out.println(LogTitles.LOGGER.getTitle() + ex.getMessage());
            if (ex instanceof GuiPrintableException) {
                printError(((GuiPrintableException) ex).getGuiErrorMessage());
            }

        }
    }//GEN-LAST:event_jButton_RepromptActionPerformed

    private void jButton_WrongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_WrongActionPerformed
        try {
            // TODO add your handling code here:
            LoggerManager.getInstance().log(LoggingTag.WRONG_ANSWER.getTag());
            System.out.println(LogTitles.GUI.getTitle() + "Wrong answer tag logged");
            printInfo("Wrong answer logged");
        } catch (LogOffException | InvalidAttemptToLogException ex) {
            System.out.println(LogTitles.LOGGER.getTitle() + ex.getMessage());
            if (ex instanceof GuiPrintableException) {
                printError(((GuiPrintableException) ex).getGuiErrorMessage());
            }
        }
    }//GEN-LAST:event_jButton_WrongActionPerformed

    private void jButton_WallSpeakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_WallSpeakActionPerformed
        // TODO add your handling code here:
        if (!jTextField_WallSpeak.getText().isEmpty()) {
            try {
                LoggerManager.getInstance().log(LoggingTag.WALL_SPEAK.getTag() + " " + jTextField_WallSpeak.getText());
                System.out.println(LogTitles.GUI.getTitle() + "Wallspeak tag logged + ( " + jTextField_WallSpeak.getText() + " )");
                printInfo("Wallspeak tag logged + note");
                jTextField_WallSpeak.setText(null);
            } catch (LogOffException | InvalidAttemptToLogException ex) {
                System.out.println(LogTitles.LOGGER.getTitle() + ex.getMessage());
                if (ex instanceof GuiPrintableException) {
                    printError(((GuiPrintableException) ex).getGuiErrorMessage());
                }
            }
        } else {
            try {
                LoggerManager.getInstance().log(LoggingTag.WALL_SPEAK.getTag());
                System.out.println(LogTitles.GUI.getTitle() + "Wallspeak tag logged");
                printInfo("Wallspeak tag logged");
            } catch (LogOffException | InvalidAttemptToLogException ex) {
                System.out.println(LogTitles.LOGGER.getTitle() + ex.getMessage());
                if (ex instanceof GuiPrintableException) {
                    printError(((GuiPrintableException) ex).getGuiErrorMessage());
                }
            }
        }
    }//GEN-LAST:event_jButton_WallSpeakActionPerformed

    private void jButton_NoteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_NoteActionPerformed
        newNote();
    }//GEN-LAST:event_jButton_NoteActionPerformed

    private void jTextField_NoteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_NoteKeyPressed
        // TODO add your handling code here:s
        if(this.startNoteTypingTime == null){
            this.startNoteTypingTime = new Date();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            newNote();
            this.startNoteTypingTime = null;
        }
    }//GEN-LAST:event_jTextField_NoteKeyPressed

    private void jTextField_NoteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_NoteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_NoteActionPerformed

    private void jTextField_WallSpeakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_WallSpeakActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_WallSpeakActionPerformed

    private void jTextField_WallSpeakKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_WallSpeakKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            if (!jTextField_WallSpeak.getText().isEmpty()) {
            try {
                LoggerManager.getInstance().log(LoggingTag.WALL_SPEAK.getTag() + " " + jTextField_WallSpeak.getText());
                System.out.println("Wallspeak tag logged + ( " + jTextField_WallSpeak.getText() + " )");
                printInfo("Wallspeak tag logged + note");
                jTextField_WallSpeak.setText(null);
            } catch (LogOffException | InvalidAttemptToLogException ex) {
                System.out.println(ex.getMessage());
                if (ex instanceof GuiPrintableException) {
                    printError(((GuiPrintableException) ex).getGuiErrorMessage());
                }
            }
        } else {
            try {
                LoggerManager.getInstance().log(LoggingTag.WALL_SPEAK.getTag());
                System.out.println("Wallspeak tag logged");
                printInfo("Wallspeak tag logged");
            } catch (LogOffException | InvalidAttemptToLogException ex) {
                System.out.println(ex.getMessage());
                if (ex instanceof GuiPrintableException) {
                    printError(((GuiPrintableException) ex).getGuiErrorMessage());
                }
            }
        }
        }
    }//GEN-LAST:event_jTextField_WallSpeakKeyPressed

    /**
     * Ritorna true se il filename non è vuoto, nullo e non inizia per numero e
     * non contiene caratteri speciali come /,+,%. In pratica che sia solo una
     * stringa alfanumerica non iniziante per numero.
     *
     * @param filename
     * @return
     */
    public boolean isValidFileName(String filename) {
        if(filename==null || filename.isEmpty()){
            return false;
        }
        return filename.matches("\\b[a-zA-Z][a-zA-Z0-9]*\\b");
    }

    private void jToggleButton_LogOffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton_LogOffActionPerformed

        if (this.jToggleButton_LogOff.isSelected()) {

            String logfileName = JOptionPane.showInputDialog(this, "Nome logfile", "Inserici il nome del logfile", JOptionPane.INFORMATION_MESSAGE);
            if (isValidFileName(logfileName)) {

                System.out.println(LogTitles.LOGGER.getTitle() + ConsoleColors.ANSI_GREEN + "Logging module is now ACTIVE" + ConsoleColors.ANSI_RESET);
                printWarning("Logging module is now ACTIVE");
                LoggerManager.getInstance().setLogActive(true);
                LoggerManager.getInstance().newLog(logfileName);
                System.out.println(LogTitles.LOGGER.getTitle() + "New log file with name [" + logfileName + "] has been created");
                this.jButton_log_end_pretest.setEnabled(true);
                setMainCommandsEnabled(true);
                noteEnabled(true);
                this.jToggleButton_TestMode.setEnabled(false);
            } else {
                printError("nome file invalido");
            }
        } else {

            if (!LoggerManager.getInstance().isLogActive()) {
                System.out.println(LogTitles.LOGGER.getTitle() + ConsoleColors.ANSI_RED + "Impossibile eseguire quando il log è OFF" + ConsoleColors.ANSI_RESET);
            } else {
                try {
                    String path = LoggerManager.getInstance().getCurrentLogPath();
                    LoggerManager.getInstance().stopLogging();
                    setMainCommandsEnabled(false);
                    LoggerManager.getInstance().openPath(path);
                    System.out.println(LogTitles.LOGGER.getTitle() + "The current logging file has been closed, no further log will be accepted on such file");
                    printWarning("The current logging file has been closed, no further log will be accepted on such file");
                    this.jButton_log_end_pretest.setEnabled(false);
                    noteEnabled(false);
                    this.jToggleButton_TestMode.setEnabled(true);
                } catch (LogOffException | InvalidAttemptToLogException ex) {
                    printError(ex.getGuiErrorMessage());
                }
            }
        }
    }//GEN-LAST:event_jToggleButton_LogOffActionPerformed


    private void jTextField_ExtraKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_ExtraKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!jTextField_Extra.getText().isEmpty()) {
                try {
                    LoggerManager.getInstance().log(LoggingTag.EXTRA_INPUT.getTag() + " " + jTextField_Extra.getText());
                    System.out.println(LogTitles.GUI.getTitle() + "Wallspeak tag logged + ( " + jTextField_Extra.getText() + " )");
                    printInfo("Extra tag logged + note");
                    jTextField_Extra.setText(null);
                } catch (LogOffException | InvalidAttemptToLogException ex) {
                    System.out.println(LogTitles.LOGGER.getTitle() + ex.getMessage());
                    if (ex instanceof GuiPrintableException) {
                        printError(((GuiPrintableException) ex).getGuiErrorMessage());
                    }
                }
            } else {
                try {
                    LoggerManager.getInstance().log(LoggingTag.EXTRA_INPUT.getTag());
                    System.out.println(LogTitles.GUI.getTitle() + "Extra Input tag logged");
                    printInfo("Extra input tag logged");
                } catch (LogOffException | InvalidAttemptToLogException ex) {
                    System.out.println(LogTitles.LOGGER.getTitle() + ex.getMessage());
                    if (ex instanceof GuiPrintableException) {
                        printError(((GuiPrintableException) ex).getGuiErrorMessage());
                    }
                }
            }
        }
    }//GEN-LAST:event_jTextField_ExtraKeyPressed

    private void jTextField_ExtraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_ExtraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_ExtraActionPerformed

    private void jTextField_WrongInputKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_WrongInputKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!jTextField_WrongInput.getText().isEmpty()) {
                try {
                    LoggerManager.getInstance().log(LoggingTag.WRONG_INPUT.getTag() + " " + jTextField_WrongInput.getText());
                    System.out.println(LogTitles.GUI.getTitle() + "Wrong input tag logged + ( " + jTextField_WrongInput.getText() + " )");
                    printInfo("Wrong input tag logged + note");
                    jTextField_WrongInput.setText(null);
                } catch (LogOffException | InvalidAttemptToLogException ex) {
                    System.out.println(LogTitles.LOGGER.getTitle() + ex.getMessage());
                    if (ex instanceof GuiPrintableException) {
                        printError(((GuiPrintableException) ex).getGuiErrorMessage());
                    }
                }
            } else {
                try {
                    LoggerManager.getInstance().log(LoggingTag.WRONG_INPUT.getTag());
                    System.out.println(LogTitles.GUI.getTitle() + "Wrong input tag logged");
                    printInfo("Wrong input tag logged");
                } catch (LogOffException | InvalidAttemptToLogException ex) {
                    System.out.println(LogTitles.LOGGER.getTitle() + ex.getMessage());
                    if (ex instanceof GuiPrintableException) {
                        printError(((GuiPrintableException) ex).getGuiErrorMessage());
                    }
                }
            }
        }
    }//GEN-LAST:event_jTextField_WrongInputKeyPressed

    private void jToggleButton_TestModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton_TestModeActionPerformed
        WatsonManager.getInstance().setTestMode(this.jToggleButton_TestMode.isSelected());
        this.jToggleButton_LogOff.setEnabled(this.jToggleButton_TestMode.isSelected());

    }//GEN-LAST:event_jToggleButton_TestModeActionPerformed

    private void jButton_resumeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_resumeActionPerformed
        if (LoggerManager.getInstance().isLogActive() && LoggerManager.getInstance().isPaused()) {
            printInfo("RESUME DONE, THE OFFICIAL TEST IS STARTED");
            LoggerManager.getInstance().resume();
            setMainCommandsEnabled(true);
            this.jButton_resume.setEnabled(false);
        } else {
            printError("Impossibile eseguire quando il logger non è stato correttamente messo in pausa");
        }
        
    }//GEN-LAST:event_jButton_resumeActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String currentLogPath = LoggerManager.getInstance().getCurrentLogPath();
        if(currentLogPath == null || currentLogPath.isEmpty()){
            printError("non c'è ancora nessun log attivo");
        }else{
            LoggerManager.getInstance().openPath(currentLogPath);
        }
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        LoggerManager.getInstance().openPath(LoggerManager.LOG_FOLDER);
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                System.out.println("---- lf -> " + info.getName());
                if ("Flat Laf Dark".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LogSupportFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LogSupportFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LogSupportFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LogSupportFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LogSupportFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton_ApplyABG;
    private javax.swing.JButton jButton_ExtraInput;
    private javax.swing.JButton jButton_Note;
    private javax.swing.JButton jButton_Reprompt;
    private javax.swing.JButton jButton_ResetABG;
    private javax.swing.JButton jButton_WallSpeak;
    private javax.swing.JButton jButton_Wrong;
    private javax.swing.JButton jButton_WrongInput;
    private javax.swing.JButton jButton_log_end_pretest;
    private javax.swing.JButton jButton_resume;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel_FeedBack;
    private javax.swing.JLabel jLabel_TestMode;
    private javax.swing.JLabel jLabel_logOn;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JSpinner jSpinnerAlpha;
    private javax.swing.JSpinner jSpinnerBeta;
    private javax.swing.JSpinner jSpinnerGamma;
    private javax.swing.JTextField jTextField_Extra;
    private javax.swing.JTextField jTextField_Note;
    private javax.swing.JTextField jTextField_WallSpeak;
    private javax.swing.JTextField jTextField_WrongInput;
    private javax.swing.JToggleButton jToggleButton_LogOff;
    private javax.swing.JToggleButton jToggleButton_TestMode;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        Main.suppressLogSupportGUI();
        System.out.println(LogTitles.GUI.getTitle() + ConsoleColors.ANSI_YELLOW + "[SupportGui] " + ConsoleColors.RED_BRIGHT + "CLOSED" + ConsoleColors.ANSI_RESET);
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    @Override
    public void testModeChanged(boolean mode) {
        renderTestOnOffButton(mode);
    }

    @Override
    public void loggingModeChanged(boolean mode) {
        renderLogOnOffButton(mode);
    }

    @Override
    public void newTagAdded(LoggingTag tag, boolean manually) {

    }

    @Override
    public void pretestEnded() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void logStop() {

    }

    @Override
    public void resume() {

    }
}
