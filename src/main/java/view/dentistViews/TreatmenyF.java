/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view.dentistViews;

import DAO.TreatmentDAO;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Map;

/**
 *
 * @author nadis
 */
public class TreatmenyF extends javax.swing.JFrame {

    /**
     * Creates new form TreatmenyF
     */
    private int appointmentId;
    private int patientUserId;
    private int dentistUserId;
    private File selectedXRayFile = null;

    private int xOffset = 0;
    private int yOffset = 0;

    private ManagePatients.TreatmentSaveListener saveListener;

    public TreatmenyF() {
        initComponents();
        makeFrameRounded(30, 30);
        enableWindowDragging();
    }

    public TreatmenyF(int appointmentId, int patientUserId, String patientName, String treatmentType, int dentistUserId) {
        this(appointmentId, patientUserId, patientName, treatmentType, dentistUserId, null, null);
    }

    public TreatmenyF(int appointmentId, int patientUserId, String patientName, String treatmentType, int dentistUserId, String dentistName, String appointmentDate) {
        initComponents();

        this.appointmentId = appointmentId;
        this.patientUserId = patientUserId;
        this.dentistUserId = dentistUserId;

        this.setLocationRelativeTo(null);
        makeFrameRounded(30, 30);
        enableWindowDragging();

        patientNamelabel.setText(patientName != null ? patientName : "N/A");

        if (dentistName != null && !dentistName.trim().isEmpty()) {
            dentistNamelabel.setText(dentistName);
        } else {
            dentistNamelabel.setText("Dr. User #" + dentistUserId);
        }

        if (appointmentDate != null && !appointmentDate.trim().isEmpty()) {
            datelabel.setText(appointmentDate);
        } else {
            datelabel.setText(java.time.LocalDate.now().toString());
        }

        if (treatmentType != null && !treatmentType.trim().isEmpty()) {
            treatmentComboBox.setSelectedItem(treatmentType);
        }
        loadExistingTreatmentData();
    }

    private void setSelectedToothInComboBox(String toothNoStr) {
        if (toothNoStr == null || toothNoStr.trim().isEmpty()) {
            return;
        }
        String cleanTooth = toothNoStr.trim();
        for (int i = 0; i < toothNoComboBox.getItemCount(); i++) {
            String item = toothNoComboBox.getItemAt(i);
            if (item != null && (item.startsWith(toothNoStr + " ") || item.equals(toothNoStr))) {
                toothNoComboBox.setSelectedIndex(i);
                break;
            }
        }
    }

    private void loadExistingTreatmentData() {
        System.out.println("Fetching record for Appointment ID: " + this.appointmentId);
        try {
            TreatmentDAO dao = new TreatmentDAO();
            Map<String, Object> record = dao.getTreatmentByAppointmentId(this.appointmentId);
            System.out.println("Retrieved Record: " + record);

            if (record != null && !record.isEmpty()) {
                if (record.get("treatmentName") != null) {
                    treatmentComboBox.setSelectedItem(record.get("treatmentName").toString());
                }

                if (record.get("clinicalNotes") != null) {
                    chinicalNotesTextArea1.setText(record.get("clinicalNotes").toString());
                }

                if (record.get("toothNumber") != null) {
                    String toothStr = record.get("toothNumber").toString();
                    setSelectedToothInComboBox(toothStr);
                    if (selecttoothNolabel1 != null) {
                        selecttoothNolabel1.setText(toothStr);
                    }
                }

                if (record.get("toothStatus") != null) {
                    statusComboBox3.setSelectedItem(record.get("toothStatus").toString().toUpperCase());
                }

                if (record.get("toothNotes") != null) {
                    toothChartNoteTextArea.setText(record.get("toothNotes").toString());
                }

                if (record.get("xrayType") != null) {
                    xRayTypeComboBox.setSelectedItem(record.get("xrayType").toString());
                }

                if (record.get("xrayFilePath") != null) {
                    fileSelectTextField.setText(record.get("xrayFilePath").toString());
                }

                if (record.get("prescriptions") != null) {
                    List<Object[]> prescriptions = (List<Object[]>) record.get("prescriptions");
                    DefaultTableModel model = (DefaultTableModel) medicationRecordTable.getModel();
                    model.setRowCount(0);
                    for (Object[] row : prescriptions) {
                        model.addRow(row);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void makeFrameRounded(int width, int height) {
        setShape(new java.awt.geom.RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), width, height));
    }

    private void enableWindowDragging() {
        java.awt.event.MouseAdapter mouseAdapter = new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                xOffset = e.getX();
                yOffset = e.getY();
            }

            @Override
            public void mouseDragged(java.awt.event.MouseEvent e) {
                int x = e.getXOnScreen();
                int y = e.getYOnScreen();
                setLocation(x - xOffset, y - yOffset);
            }
        };
        moveIconL.addMouseListener(mouseAdapter);
        moveIconL.addMouseMotionListener(mouseAdapter);
        jPanel1.addMouseListener(mouseAdapter);
        jPanel1.addMouseMotionListener(mouseAdapter);
    }

    public void setTreatmentSaveListener(ManagePatients.TreatmentSaveListener listener) {
        this.saveListener = listener;
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
        jLabel2 = new javax.swing.JLabel();
        jMini = new javax.swing.JLabel();
        label1 = new java.awt.Label();
        label2 = new java.awt.Label();
        label17 = new java.awt.Label();
        label10 = new java.awt.Label();
        jScrollPane2 = new javax.swing.JScrollPane();
        toothChartNoteTextArea = new javax.swing.JTextArea();
        toothNoComboBox = new javax.swing.JComboBox<>();
        label9 = new java.awt.Label();
        label24 = new java.awt.Label();
        durationTextField = new javax.swing.JTextField();
        label8 = new java.awt.Label();
        statusComboBox3 = new javax.swing.JComboBox<>();
        label13 = new java.awt.Label();
        label22 = new java.awt.Label();
        selecttoothNolabel1 = new java.awt.Label();
        label21 = new java.awt.Label();
        label15 = new java.awt.Label();
        fileSelectTextField = new javax.swing.JTextField();
        label4 = new java.awt.Label();
        saveTreatmentButton = new javax.swing.JButton();
        label12 = new java.awt.Label();
        jButton1 = new javax.swing.JButton();
        label16 = new java.awt.Label();
        patientNamelabel = new java.awt.Label();
        instrutionTextField = new javax.swing.JTextField();
        dosageTextField = new javax.swing.JTextField();
        label6 = new java.awt.Label();
        cancleButton = new javax.swing.JButton();
        label19 = new java.awt.Label();
        jScrollPane3 = new javax.swing.JScrollPane();
        medicationRecordTable = new javax.swing.JTable();
        xRayTypeComboBox = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        chinicalNotesTextArea1 = new javax.swing.JTextArea();
        label20 = new java.awt.Label();
        label23 = new java.awt.Label();
        label11 = new java.awt.Label();
        label18 = new java.awt.Label();
        locationBrowserButton = new javax.swing.JButton();
        datelabel = new java.awt.Label();
        medicationTextField = new javax.swing.JTextField();
        treatmentComboBox = new javax.swing.JComboBox<>();
        dentistNamelabel = new java.awt.Label();
        moveIconL = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(30, 109, 211));
        jPanel1.setForeground(new java.awt.Color(38, 140, 187));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/close (2).png"))); // NOI18N
        jLabel2.setToolTipText("");
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        jMini.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/minus.png"))); // NOI18N
        jMini.setToolTipText("");
        jMini.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMiniMouseClicked(evt);
            }
        });

        label1.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 18)); // NOI18N
        label1.setForeground(new java.awt.Color(255, 255, 255));
        label1.setText("PATIENT TREATMENT FORM");

        label2.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        label2.setForeground(new java.awt.Color(255, 255, 255));
        label2.setText("Patient:");

        label17.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        label17.setForeground(new java.awt.Color(255, 255, 255));
        label17.setText("Medication");

        label10.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        label10.setForeground(new java.awt.Color(255, 255, 255));
        label10.setText("Tooth Number:");

        toothChartNoteTextArea.setColumns(20);
        toothChartNoteTextArea.setRows(5);
        jScrollPane2.setViewportView(toothChartNoteTextArea);

        toothNoComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Adult Teeth (Permanent) --", "11 - Upper Right Central Incisor", "12 - Upper Right Lateral Incisor", "13 - Upper Right Canine", "14 - Upper Right First Premolar", "15 - Upper Right Second Premolar", "16 - Upper Right First Molar", "17 - Upper Right Second Molar", "18 - Upper Right Third Molar (Wisdom)", "21 - Upper Left Central Incisor", "22 - Upper Left Lateral Incisor", "23 - Upper Left Canine", "24 - Upper Left First Premolar", "25 - Upper Left Second Premolar", "26 - Upper Left First Molar", "27 - Upper Left Second Molar", "28 - Upper Left Third Molar (Wisdom)", "31 - Lower Left Central Incisor", "32 - Lower Left Lateral Incisor", "33 - Lower Left Canine", "34 - Lower Left First Premolar", "35 - Lower Left Second Premolar", "36 - Lower Left First Molar", "37 - Lower Left Second Molar", "38 - Lower Left Third Molar (Wisdom)", "41 - Lower Right Central Incisor", "42 - Lower Right Lateral Incisor", "43 - Lower Right Canine", "44 - Lower Right First Premolar", "45 - Lower Right Second Premolar", "46 - Lower Right First Molar", "47 - Lower Right Second Molar", "48 - Lower Right Third Molar (Wisdom)", "-- Primary Teeth (Baby Teeth) --", "51 - Upper Right Primary Central Incisor", "52 - Upper Right Primary Lateral Incisor", "53 - Upper Right Primary Canine", "54 - Upper Right Primary First Molar", "55 - Upper Right Primary Second Molar", "61 - Upper Left Primary Central Incisor", "62 - Upper Left Primary Lateral Incisor", "63 - Upper Left Primary Canine", "64 - Upper Left Primary First Molar", "65 - Upper Left Primary Second Molar", "71 - Lower Left Primary Central Incisor", "72 - Lower Left Primary Lateral Incisor", "73 - Lower Left Primary Canine", "74 - Lower Left Primary First Molar", "75 - Lower Left Primary Second Molar", "81 - Lower Right Primary Central Incisor", "82 - Lower Right Primary Lateral Incisor", "83 - Lower Right Primary Canine", "84 - Lower Right Primary First Molar", "85 - Lower Right Primary Second Molar" }));

        label9.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        label9.setForeground(new java.awt.Color(255, 255, 255));
        label9.setText("Treatment Name: ");

        label24.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        label24.setForeground(new java.awt.Color(255, 255, 255));
        label24.setText("File:");

        label8.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        label8.setForeground(new java.awt.Color(255, 255, 255));
        label8.setText("TREATMENT DETAILS");

        statusComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "HEALTHY", "FILLED", "EXTRACTED", "IMPLANT", "ROOT_CANAL", "CROWN", "CARIES", "IMPACTED" }));

        label13.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        label13.setForeground(new java.awt.Color(255, 255, 255));
        label13.setText("TOOTH CHART UPDATE");

        label22.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        label22.setForeground(new java.awt.Color(255, 255, 255));
        label22.setText("DENTAL X-RAY UPLOAD:");

        selecttoothNolabel1.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        selecttoothNolabel1.setForeground(new java.awt.Color(255, 255, 255));
        selecttoothNolabel1.setText("No");

        label21.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        label21.setForeground(new java.awt.Color(255, 255, 255));
        label21.setText("Instructions:");

        label15.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        label15.setForeground(new java.awt.Color(255, 255, 255));
        label15.setText("Status:");

        label4.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        label4.setForeground(new java.awt.Color(255, 255, 255));
        label4.setText("Dentist:");

        saveTreatmentButton.setBackground(new java.awt.Color(0, 204, 0));
        saveTreatmentButton.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        saveTreatmentButton.setForeground(new java.awt.Color(255, 255, 255));
        saveTreatmentButton.setText("Save Treatment");
        saveTreatmentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveTreatmentButtonActionPerformed(evt);
            }
        });

        label12.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        label12.setForeground(new java.awt.Color(255, 255, 255));
        label12.setText("Selected Tooth:");

        jButton1.setBackground(new java.awt.Color(0, 0, 255));
        jButton1.setFont(new java.awt.Font("MingLiU_HKSCS-ExtB", 1, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Update Chart");

        label16.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        label16.setForeground(new java.awt.Color(255, 255, 255));
        label16.setText("Notes:");

        patientNamelabel.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        patientNamelabel.setForeground(new java.awt.Color(255, 255, 255));
        patientNamelabel.setText("Name");
        patientNamelabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                patientNamelabelMouseClicked(evt);
            }
        });

        label6.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        label6.setForeground(new java.awt.Color(255, 255, 255));
        label6.setText("Date:");

        cancleButton.setBackground(new java.awt.Color(204, 0, 0));
        cancleButton.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        cancleButton.setForeground(new java.awt.Color(255, 255, 255));
        cancleButton.setText("Cancle");
        cancleButton.setActionCommand("Cancel");
        cancleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancleButtonActionPerformed(evt);
            }
        });

        label19.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        label19.setForeground(new java.awt.Color(255, 255, 255));
        label19.setText("Duration:");

        medicationRecordTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Medication", "Dosage", "Duration", "Instructions"
            }
        ));
        jScrollPane3.setViewportView(medicationRecordTable);

        xRayTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Periapical X-Ray", "Bitewing X-Ray", "Panoramic X-Ray (OPG)", "Occlusal X-Ray", "Cephalometric X-Ray", "Cone Beam Computed Tomography (CBCT)" }));

        chinicalNotesTextArea1.setColumns(20);
        chinicalNotesTextArea1.setRows(5);
        jScrollPane1.setViewportView(chinicalNotesTextArea1);

        label20.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        label20.setForeground(new java.awt.Color(255, 255, 255));
        label20.setText("Dosage:");

        label23.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        label23.setForeground(new java.awt.Color(255, 255, 255));
        label23.setText("X-Ray Type:");

        label11.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        label11.setForeground(new java.awt.Color(255, 255, 255));
        label11.setText("Clinical Notes:");

        label18.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        label18.setForeground(new java.awt.Color(255, 255, 255));
        label18.setText("Medication:");

        locationBrowserButton.setBackground(new java.awt.Color(204, 204, 204));
        locationBrowserButton.setText("Browse...");
        locationBrowserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                locationBrowserButtonActionPerformed(evt);
            }
        });

        datelabel.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        datelabel.setForeground(new java.awt.Color(255, 255, 255));
        datelabel.setText("date");
        datelabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                datelabelMouseClicked(evt);
            }
        });

        treatmentComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Dental Examination", "Teeth Cleaning and Scaling", "Composite Tooth Filling", "Amalgam Tooth Filling", "Simple Tooth Extraction", "Surgical Tooth Extraction", "Root Canal Treatment", "Dental Crown Placement", "Dental Bridge Installation", "Teeth Whitening", "Fluoride Treatment", "Dental Sealant Application", "Periodontal Scaling and Root Planing", "Partial Denture Fitting", "Complete Denture Fitting", "Dental Implant Placement", "Invisalign and Orthodontic Adjustment", "Abscess Drainage and Management", "Night Guard Fitting", "Pulpotomy" }));
        treatmentComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                treatmentComboBoxActionPerformed(evt);
            }
        });

        dentistNamelabel.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        dentistNamelabel.setForeground(new java.awt.Color(255, 255, 255));
        dentistNamelabel.setText("Name");
        dentistNamelabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dentistNamelabelMouseClicked(evt);
            }
        });

        moveIconL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/move.png"))); // NOI18N
        moveIconL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                moveIconLMousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jMini))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(label16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(32, 32, 32)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(label13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(label12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(selecttoothNolabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton1)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(saveTreatmentButton)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(label21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(label20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(dosageTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(instrutionTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(moveIconL))))
                            .addComponent(label17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(label18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(durationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(medicationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(39, 39, 39)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(label23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(xRayTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(cancleButton)
                                            .addComponent(fileSelectTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(locationBrowserButton))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(label15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(12, 12, 12)
                                        .addComponent(statusComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(patientNamelabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(74, 74, 74)
                                            .addComponent(label4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(dentistNamelabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(72, 72, 72)
                                            .addComponent(label6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(datelabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(label9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(label10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(label11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(toothNoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(treatmentComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)))))))))
                .addContainerGap(107, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jMini)
                    .addComponent(moveIconL))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(patientNamelabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dentistNamelabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(datelabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addComponent(label8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(label9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(treatmentComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(toothNoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addComponent(label13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(label12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(selecttoothNolabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(statusComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(label16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(medicationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(label19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(durationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(dosageTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(label17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(13, 13, 13)
                                .addComponent(label20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(label21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(instrutionTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(30, 30, 30)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(label22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xRayTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(fileSelectTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(locationBrowserButton)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancleButton)
                    .addComponent(saveTreatmentButton))
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
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMiniMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMiniMouseClicked
        // TODO add your handling code here:
        this.setState(javax.swing.JFrame.ICONIFIED);
    }//GEN-LAST:event_jMiniMouseClicked

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jLabel2MouseClicked

    private void saveTreatmentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveTreatmentButtonActionPerformed
        // TODO add your handling code here:
        try {
            String treatmentName = treatmentComboBox.getSelectedItem().toString();
            String selectedToothItem = toothNoComboBox.getSelectedItem().toString();

            String toothNumberStr = selectedToothItem.contains(" - ") ? selectedToothItem.split(" - ")[0].trim() : selectedToothItem.trim();
            int toothNoInt = 0;
            try {
                toothNoInt = Integer.parseInt(toothNumberStr);
            } catch (NumberFormatException ignored) {
            }

            String clinicalNotes = chinicalNotesTextArea1.getText();
            String toothStatus = statusComboBox3.getSelectedItem().toString().toUpperCase();
            String toothNotes = toothChartNoteTextArea.getText();
            String xrayType = xRayTypeComboBox.getSelectedItem().toString();

            DefaultTableModel model = (DefaultTableModel) medicationRecordTable.getModel();
            List<Object[]> prescriptionsList = new ArrayList<>();
            for (int i = 0; i < model.getRowCount(); i++) {
                if (model.getValueAt(i, 0) != null && !model.getValueAt(i, 0).toString().trim().isEmpty()) {
                    prescriptionsList.add(new Object[]{
                        model.getValueAt(i, 0).toString(),
                        model.getValueAt(i, 1) != null ? model.getValueAt(i, 1).toString() : "",
                        model.getValueAt(i, 2) != null ? model.getValueAt(i, 2).toString() : "",
                        model.getValueAt(i, 3) != null ? model.getValueAt(i, 3).toString() : ""
                    });
                }
            }

            String xrayFilePath = "";
            if (selectedXRayFile != null && selectedXRayFile.exists()) {
                File destDir = new File("uploads/xrays");
                if (!destDir.exists()) {
                    destDir.mkdirs();
                }

                xrayFilePath = "uploads/xrays/" + System.currentTimeMillis() + "_" + selectedXRayFile.getName();
                File destFile = new File(xrayFilePath);
                Files.copy(selectedXRayFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            TreatmentDAO treatmentDAO = new TreatmentDAO();
            boolean isSaved = treatmentDAO.saveFullTreatmentRecord(
                    appointmentId,
                    patientUserId,
                    dentistUserId,
                    treatmentName,
                    toothNumberStr,
                    clinicalNotes,
                    toothNoInt,
                    toothStatus,
                    toothNotes,
                    prescriptionsList,
                    xrayType,
                    xrayFilePath
            );

            if (isSaved) {
                JOptionPane.showMessageDialog(this, "Treatment record saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

                if (saveListener != null) {
                    saveListener.onTreatmentSaved();
                }
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save treatment record. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_saveTreatmentButtonActionPerformed

    private void patientNamelabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_patientNamelabelMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_patientNamelabelMouseClicked

    private void cancleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancleButtonActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_cancleButtonActionPerformed

    private void treatmentComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_treatmentComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_treatmentComboBoxActionPerformed

    private void dentistNamelabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dentistNamelabelMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_dentistNamelabelMouseClicked

    private void datelabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_datelabelMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_datelabelMouseClicked

    private void moveIconLMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_moveIconLMousePressed
        // TODO add your handling code here:

    }//GEN-LAST:event_moveIconLMousePressed

    private void locationBrowserButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_locationBrowserButtonActionPerformed
        // TODO add your handling code here:
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Dental X-Ray Image");

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files (*.jpg, *.png, *.jpeg)", "jpg", "jpeg", "png");
        fileChooser.setFileFilter(filter);

        int userSelection = fileChooser.showOpenDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            selectedXRayFile = fileChooser.getSelectedFile();
            locationBrowserButton.setText(selectedXRayFile.getAbsolutePath());
        }
    }//GEN-LAST:event_locationBrowserButtonActionPerformed

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
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TreatmenyF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TreatmenyF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TreatmenyF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TreatmenyF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TreatmenyF().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancleButton;
    private javax.swing.JTextArea chinicalNotesTextArea1;
    private java.awt.Label datelabel;
    private java.awt.Label dentistNamelabel;
    private javax.swing.JTextField dosageTextField;
    private javax.swing.JTextField durationTextField;
    private javax.swing.JTextField fileSelectTextField;
    private javax.swing.JTextField instrutionTextField;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jMini;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private java.awt.Label label1;
    private java.awt.Label label10;
    private java.awt.Label label11;
    private java.awt.Label label12;
    private java.awt.Label label13;
    private java.awt.Label label15;
    private java.awt.Label label16;
    private java.awt.Label label17;
    private java.awt.Label label18;
    private java.awt.Label label19;
    private java.awt.Label label2;
    private java.awt.Label label20;
    private java.awt.Label label21;
    private java.awt.Label label22;
    private java.awt.Label label23;
    private java.awt.Label label24;
    private java.awt.Label label4;
    private java.awt.Label label6;
    private java.awt.Label label8;
    private java.awt.Label label9;
    private javax.swing.JButton locationBrowserButton;
    private javax.swing.JTable medicationRecordTable;
    private javax.swing.JTextField medicationTextField;
    private javax.swing.JLabel moveIconL;
    private java.awt.Label patientNamelabel;
    private javax.swing.JButton saveTreatmentButton;
    private java.awt.Label selecttoothNolabel1;
    private javax.swing.JComboBox<String> statusComboBox3;
    private javax.swing.JTextArea toothChartNoteTextArea;
    private javax.swing.JComboBox<String> toothNoComboBox;
    private javax.swing.JComboBox<String> treatmentComboBox;
    private javax.swing.JComboBox<String> xRayTypeComboBox;
    // End of variables declaration//GEN-END:variables
}
