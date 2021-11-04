/*
 * Công ty : TEKDE Technology Development
 * Tác giả : Trần Hữu Hoài Vũ
 * Tên file: JDialogColorAssortion.java
 * Ngày tạo: Jan 23, 2013, 10:11:06 AM
 * Ngày sửa: Jan 23, 2013, 10:11:06 AM
 * Diễn giải:
 * Copyright 2010
 */
package domainsearchtool;

import Entity.CheckDomainObject;
import Entity.ColorAssortionObject;
import Util.ColorAssortionCellRenderer;
import Util.FormatNumberCellRenderer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class JDialogColorAssortion extends javax.swing.JDialog
{
    private Context _context = Context.Instance();
    JColorChooser jColor = new JColorChooser(Color.WHITE);
    private String[] arrBackLink =
    {
        "1", "10", "20", "50", "100", "200", "500", "1000", ColorAssortionObject.BACKLINK_MAX
    };
    private JFrameMain _parent;

    /**
     * Creates new form JDialogColorAssortion
     */
    public JDialogColorAssortion(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();
        this._parent = (JFrameMain) parent;
        this.setTitle("Color Assortion");
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(JFrameMain.class.getResource("icon.png")));
        SetValueCombobox();
        EditJColorChooser();
        ConfigTable();
        LoadDataToTable();
    }

    private void SetValueCombobox()
    {
        for (int i = 0; i <= 10; i++)
        {
            jCboPRFrom.addItem(i);
            jCboPRTo.addItem(i);
        }
        for (int i = 0; i < arrBackLink.length - 1; i++)
        {
            jCboBacklinksFrom.addItem(arrBackLink[i]);
        }
        for (String s : arrBackLink)
        {
            jCboBacklinksTo.addItem(s);
        }
        jCboDmoz.addItem(CheckDomainObject.DMOZ_YES);
        jCboDmoz.addItem(CheckDomainObject.DMOZ_NO);
        jCboPRFrom.setSelectedIndex(0);
        jCboPRTo.setSelectedIndex(0);
        jCboBacklinksFrom.setSelectedIndex(0);
        jCboBacklinksTo.setSelectedIndex(0);
        jCboDmoz.setSelectedIndex(0);
    }

    private void LoadDataToTable()
    {
        try
        {
            List<ColorAssortionObject> list = Util.XmlData.LoadListColorAssortObjectFromXml(_context.GetConfig().GetConfigPath() + _context.GetConfig().GetColorAssortConfigFileName());
            for (ColorAssortionObject oColorAssort : list)
            {
                AddRowToTable(oColorAssort);
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(JDialogColorAssortion.class.getName()).log(Level.SEVERE, ex.getClass().getName(), ex);
        }
    }

    private void EditJColorChooser()
    {
        if (Util.UtilClass.IsWindowsOS())
        {
            AbstractColorChooserPanel[] oldPanels = jColor.getChooserPanels();
            AbstractColorChooserPanel chooserTemp = null;
            for (AbstractColorChooserPanel chooserPanel : oldPanels)
            {
                String clsName = chooserPanel.getClass().getName();
                if (clsName.equals("javax.swing.colorchooser.DefaultSwatchChooserPanel"))
                {
                    chooserTemp = chooserPanel;
                }
                jColor.removeChooserPanel(chooserPanel);
            }
            jColor.addChooserPanel(chooserTemp);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jCboDmoz = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        jLabColorDmoz = new javax.swing.JLabel();
        jBtnDmoz = new javax.swing.JButton();
        jBtnSave = new javax.swing.JButton();
        jBtnDelete = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jCboBacklinksFrom = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jCboBacklinksTo = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jLabColorBacklinks = new javax.swing.JLabel();
        jBtnBacklinks = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTabColor = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jCboPRFrom = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jCboPRTo = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jLabColorGooglePR = new javax.swing.JLabel();
        jBtnGooglePR = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 153));

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Dmoz", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11))); // NOI18N
        jPanel4.setOpaque(false);

        jLabel7.setText("Column value:");

        jCboDmoz.setMaximumRowCount(15);

        jLabel9.setText("Color:");

        jLabColorDmoz.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabColorDmoz.setOpaque(true);
        jLabColorDmoz.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mousePressed(java.awt.event.MouseEvent evt)
            {
                jLabColorDmozMousePressed(evt);
            }
        });

        jBtnDmoz.setText("Add");
        jBtnDmoz.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jBtnDmozActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCboDmoz, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabColorDmoz, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jBtnDmoz, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabColorDmoz, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(jCboDmoz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9))
                    .addComponent(jBtnDmoz))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel4Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jBtnDmoz, jCboDmoz, jLabColorDmoz, jLabel7, jLabel9});

        jBtnSave.setText("Save Configure");
        jBtnSave.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jBtnSaveActionPerformed(evt);
            }
        });

        jBtnDelete.setText("Delete Configure");
        jBtnDelete.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jBtnDeleteActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Backlinks", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11))); // NOI18N
        jPanel3.setOpaque(false);

        jLabel4.setText("From:");

        jCboBacklinksFrom.setMaximumRowCount(15);

        jLabel5.setText("To:");

        jCboBacklinksTo.setMaximumRowCount(15);

        jLabel6.setText("Color:");

        jLabColorBacklinks.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabColorBacklinks.setOpaque(true);
        jLabColorBacklinks.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mousePressed(java.awt.event.MouseEvent evt)
            {
                jLabColorBacklinksMousePressed(evt);
            }
        });

        jBtnBacklinks.setText("Add");
        jBtnBacklinks.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jBtnBacklinksActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCboBacklinksFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCboBacklinksTo, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabColorBacklinks, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jBtnBacklinks, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jCboBacklinksFrom, jCboBacklinksTo});

        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(jCboBacklinksFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)
                        .addComponent(jCboBacklinksTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6)
                        .addComponent(jLabColorBacklinks, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jBtnBacklinks))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jBtnBacklinks, jCboBacklinksFrom, jCboBacklinksTo, jLabColorBacklinks, jLabel4, jLabel5, jLabel6});

        jTabColor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {

            }
        ));
        jTabColor.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jTabColor);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Google PR", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11))); // NOI18N
        jPanel2.setOpaque(false);

        jLabel1.setText("From:");

        jCboPRFrom.setMaximumRowCount(15);

        jLabel2.setText("To:");

        jCboPRTo.setMaximumRowCount(15);

        jLabel3.setText("Color:");

        jLabColorGooglePR.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabColorGooglePR.setOpaque(true);
        jLabColorGooglePR.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mousePressed(java.awt.event.MouseEvent evt)
            {
                jLabColorGooglePRMousePressed(evt);
            }
        });

        jBtnGooglePR.setText("Add");
        jBtnGooglePR.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jBtnGooglePRActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCboPRFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCboPRTo, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabColorGooglePR, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jBtnGooglePR, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jCboPRFrom, jCboPRTo});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jCboPRFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)
                        .addComponent(jCboPRTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3)
                        .addComponent(jLabColorGooglePR, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jBtnGooglePR))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jBtnGooglePR, jCboPRFrom, jCboPRTo, jLabColorGooglePR, jLabel1, jLabel2, jLabel3});

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(jBtnSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jBtnDelete)
                        .addGap(77, 77, 77)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jBtnDelete, jBtnSave});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jBtnSave)
                            .addComponent(jBtnDelete)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jBtnDelete, jBtnSave});

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
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        java.awt.Dimension dialogSize = getSize();
        setLocation((screenSize.width-dialogSize.width)/2,(screenSize.height-dialogSize.height)/2);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabColorDmozMousePressed(java.awt.event.MouseEvent evt)//GEN-FIRST:event_jLabColorDmozMousePressed
    {//GEN-HEADEREND:event_jLabColorDmozMousePressed
        if (evt.getButton() == java.awt.event.MouseEvent.BUTTON1)
        {
            JColorChooser.createDialog(this, "Choose color", true, jColor, null, null).setVisible(true);
            Color color = jColor.getColor();
            jLabColorDmoz.setBackground(color);
        }
    }//GEN-LAST:event_jLabColorDmozMousePressed

    private void jBtnDmozActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jBtnDmozActionPerformed
    {//GEN-HEADEREND:event_jBtnDmozActionPerformed
        ColorAssortionObject oColorAssort = new ColorAssortionObject();
        oColorAssort.setiColumnId(ColorAssortionObject.DMOZ_ID);
        oColorAssort.setsColumnName("Dmoz");
        oColorAssort.setoValue(jCboDmoz.getSelectedItem().toString());
        oColorAssort.setoColor(jLabColorDmoz.getBackground());
        AddRowToTable(oColorAssort);
    }//GEN-LAST:event_jBtnDmozActionPerformed

    private void jBtnSaveActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jBtnSaveActionPerformed
    {//GEN-HEADEREND:event_jBtnSaveActionPerformed
        try
        {
            List<ColorAssortionObject> list = GetRowsFromTable();
            _parent._listColorAssort.clear();
            _parent._listColorAssort.addAll(list);
            Util.XmlData.SaveListColorAssortObjectToXml(list,
                    _context.GetConfig().GetConfigPath() + _context.GetConfig().GetColorAssortConfigFileName());
            if (JOptionPane.showConfirmDialog(this, "Save Color Assortion configure was successfully! Do you want to close form?", "Message",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE) == JOptionPane.OK_OPTION)
            {
                this.dispose();
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(JDialogColorAssortion.class.getName()).log(Level.SEVERE, ex.getClass().getName(), ex);
        }
    }//GEN-LAST:event_jBtnSaveActionPerformed

    private void jBtnDeleteActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jBtnDeleteActionPerformed
    {//GEN-HEADEREND:event_jBtnDeleteActionPerformed
        int index = jTabColor.getSelectedRow();
        if (index < 0)
        {
            return;
        }
        ((DefaultTableModel) jTabColor.getModel()).removeRow(index);
        if (jTabColor.getRowCount() > 0)
        {
            jTabColor.setRowSelectionInterval(0, 0);
        }
        else
        {
            jTabColor.clearSelection();
        }
    }//GEN-LAST:event_jBtnDeleteActionPerformed

    private void jLabColorBacklinksMousePressed(java.awt.event.MouseEvent evt)//GEN-FIRST:event_jLabColorBacklinksMousePressed
    {//GEN-HEADEREND:event_jLabColorBacklinksMousePressed
        if (evt.getButton() == java.awt.event.MouseEvent.BUTTON1)
        {
            JColorChooser.createDialog(this, "Choose color", true, jColor, null, null).setVisible(true);
            Color color = jColor.getColor();
            jLabColorBacklinks.setBackground(color);
        }
    }//GEN-LAST:event_jLabColorBacklinksMousePressed

    private void jBtnBacklinksActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jBtnBacklinksActionPerformed
    {//GEN-HEADEREND:event_jBtnBacklinksActionPerformed
        ColorAssortionObject oColorAssort = new ColorAssortionObject();
        oColorAssort.setiColumnId(ColorAssortionObject.BACKLINK_ID);
        oColorAssort.setsColumnName("Backlinks");
        oColorAssort.setiFromValue(Integer.parseInt(jCboBacklinksFrom.getSelectedItem().toString()));
        try
        {
            oColorAssort.setiToValue(Integer.parseInt(jCboBacklinksTo.getSelectedItem().toString()));
        }
        catch (NumberFormatException ex)
        {
            oColorAssort.setiToValue(Integer.parseInt(arrBackLink[arrBackLink.length - 2]));
            oColorAssort.setoValue(arrBackLink[arrBackLink.length - 1]);
        }
        oColorAssort.setoColor(jLabColorBacklinks.getBackground());
        AddRowToTable(oColorAssort);
    }//GEN-LAST:event_jBtnBacklinksActionPerformed

    private void jLabColorGooglePRMousePressed(java.awt.event.MouseEvent evt)//GEN-FIRST:event_jLabColorGooglePRMousePressed
    {//GEN-HEADEREND:event_jLabColorGooglePRMousePressed
        if (evt.getButton() == java.awt.event.MouseEvent.BUTTON1)
        {
            JColorChooser.createDialog(this, "Choose color", true, jColor, null, null).setVisible(true);
            Color color = jColor.getColor();
            jLabColorGooglePR.setBackground(color);
        }
    }//GEN-LAST:event_jLabColorGooglePRMousePressed

    private void jBtnGooglePRActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jBtnGooglePRActionPerformed
    {//GEN-HEADEREND:event_jBtnGooglePRActionPerformed
        ColorAssortionObject oColorAssort = new ColorAssortionObject();
        oColorAssort.setiColumnId(ColorAssortionObject.GOOGLE_ID);
        oColorAssort.setsColumnName("Google Pagerank");
        oColorAssort.setiFromValue((Integer) jCboPRFrom.getSelectedItem());
        oColorAssort.setiToValue((Integer) jCboPRTo.getSelectedItem());
        oColorAssort.setoColor(jLabColorGooglePR.getBackground());
        AddRowToTable(oColorAssort);
    }//GEN-LAST:event_jBtnGooglePRActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnBacklinks;
    private javax.swing.JButton jBtnDelete;
    private javax.swing.JButton jBtnDmoz;
    private javax.swing.JButton jBtnGooglePR;
    private javax.swing.JButton jBtnSave;
    private javax.swing.JComboBox jCboBacklinksFrom;
    private javax.swing.JComboBox jCboBacklinksTo;
    private javax.swing.JComboBox jCboDmoz;
    private javax.swing.JComboBox jCboPRFrom;
    private javax.swing.JComboBox jCboPRTo;
    private javax.swing.JLabel jLabColorBacklinks;
    private javax.swing.JLabel jLabColorDmoz;
    private javax.swing.JLabel jLabColorGooglePR;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTabColor;
    // End of variables declaration//GEN-END:variables

    private List<ColorAssortionObject> GetRowsFromTable()
    {
        List<ColorAssortionObject> lstItem = new ArrayList<>();
        DefaultTableModel model = (DefaultTableModel) jTabColor.getModel();
        for (int i = 0; i < model.getRowCount(); i++)
        {
            ColorAssortionObject oItem = new ColorAssortionObject();
            oItem.setsColumnName(model.getValueAt(i, 0) == null ? null : model.getValueAt(i, 0).toString());
            oItem.setiFromValue(model.getValueAt(i, 1) == null ? 0 : Integer.parseInt(model.getValueAt(i, 1).toString()));
            oItem.setiToValue(model.getValueAt(i, 2) == null ? null : Integer.parseInt(model.getValueAt(i, 2).toString()));
            oItem.setoValue(model.getValueAt(i, 3) == null ? null : model.getValueAt(i, 3));
            oItem.setoColor(model.getValueAt(i, 4) == null ? null : (Color) model.getValueAt(i, 4));
            oItem.setiColumnId(model.getValueAt(i, 5) == null ? null : Integer.parseInt(model.getValueAt(i, 5).toString()));
            lstItem.add(oItem);
        }
        return lstItem;
    }

    private void ConfigTable()
    {
        String[] arrColName =
        {
            "Column Name", "From", "To", "Other", "Color", "ID"
        };
        DefaultTableModel model = new DefaultTableModel();
        for (String s : arrColName)
        {
            model.addColumn(s);
        }
        jTabColor.setModel(model);
        jTabColor.getColumnModel().getColumn(0).setPreferredWidth(150);
        jTabColor.getColumnModel().getColumn(1).setPreferredWidth(60);
        jTabColor.getColumnModel().getColumn(2).setPreferredWidth(60);
        jTabColor.getColumnModel().getColumn(3).setPreferredWidth(60);
        jTabColor.getColumnModel().getColumn(4).setPreferredWidth(60);
        jTabColor.removeColumn(jTabColor.getColumnModel().getColumn(5));
        jTabColor.getColumnModel().getColumn(1).setCellRenderer(FormatNumberCellRenderer.getIntegerRenderer());
        jTabColor.getColumnModel().getColumn(2).setCellRenderer(FormatNumberCellRenderer.getIntegerRenderer());
        jTabColor.getColumnModel().getColumn(3).setCellRenderer(FormatNumberCellRenderer.PositionRenderer(1));
        jTabColor.getColumnModel().getColumn(4).setCellRenderer(new ColorAssortionCellRenderer());

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer()
        {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column)
            {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.CENTER);
                setFont(new Font("Arial", Font.BOLD, 13));
                setBackground(Color.LIGHT_GRAY);
                return c;
            }

            @Override
            public void setValue(Object value)
            {
                //  Format the Object before setting its value in the renderer
                super.setValue(value);
            }
        };
        jTabColor.getColumnModel().getColumn(0).setHeaderRenderer(headerRenderer);
        jTabColor.getColumnModel().getColumn(1).setHeaderRenderer(headerRenderer);
        jTabColor.getColumnModel().getColumn(2).setHeaderRenderer(headerRenderer);
        jTabColor.getColumnModel().getColumn(3).setHeaderRenderer(headerRenderer);
        jTabColor.getColumnModel().getColumn(4).setHeaderRenderer(headerRenderer);
    }

    private void AddRowToTable(ColorAssortionObject oColorAssort)
    {
        DefaultTableModel tableModel = (DefaultTableModel) jTabColor.getModel();
        Object[] arrRow =
        {
            oColorAssort.getsColumnName(), oColorAssort.getiFromValue(), oColorAssort.getiToValue(),
            oColorAssort.getoValue(), oColorAssort.getoColor(), oColorAssort.getiColumnId()
        };
        tableModel.addRow(arrRow);
    }
}
