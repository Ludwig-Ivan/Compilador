package ludwig_sergio_leo.compilador;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

/**
 * @author Ludwig Ivan Ortiz Sierra
 * @author Sergio Octavio Cervante Mujica
 * @author Leonardo Leon Moreno
 */
public class Principal extends javax.swing.JFrame{
    
    NumeroLinea NumeroDeLineas;
    
    public Principal()
    {
        initComponents();
        NumeroDeLineas = new NumeroLinea(Editor);
        ScrollEditor.setRowHeaderView(NumeroDeLineas);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        Nuevo = new javax.swing.JButton();
        Guardar = new javax.swing.JButton();
        Abrir = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        Deshacer = new javax.swing.JButton();
        Rehacer = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        Debug = new javax.swing.JButton();
        Ejecutar = new javax.swing.JButton();
        Terminar = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        jPanel2 = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        ScrollEditor = new javax.swing.JScrollPane();
        Editor = new javax.swing.JTextArea();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea4 = new javax.swing.JTextArea();
        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Compilador");

        jToolBar1.setBackground(new java.awt.Color(204, 204, 204));
        jToolBar1.setBorder(null);
        jToolBar1.setRollover(true);

        Nuevo.setFont(new java.awt.Font("Segoe UI Emoji", 0, 18)); // NOI18N
        Nuevo.setForeground(new java.awt.Color(51, 51, 255));
        Nuevo.setText("➕");
        Nuevo.setFocusable(false);
        Nuevo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        Nuevo.setMargin(new java.awt.Insets(5, 5, 0, 5));
        Nuevo.setVerifyInputWhenFocusTarget(false);
        Nuevo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        Nuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NuevoActionPerformed(evt);
            }
        });
        jToolBar1.add(Nuevo);

        Guardar.setFont(new java.awt.Font("Segoe UI Emoji", 0, 18)); // NOI18N
        Guardar.setForeground(new java.awt.Color(51, 51, 255));
        Guardar.setText("💾");
        Guardar.setFocusable(false);
        Guardar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        Guardar.setMargin(new java.awt.Insets(5, 5, 0, 5));
        Guardar.setVerifyInputWhenFocusTarget(false);
        Guardar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        Guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GuardarActionPerformed(evt);
            }
        });
        jToolBar1.add(Guardar);

        Abrir.setFont(new java.awt.Font("Segoe UI Emoji", 0, 18)); // NOI18N
        Abrir.setForeground(new java.awt.Color(51, 51, 255));
        Abrir.setText("📁");
        Abrir.setFocusable(false);
        Abrir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        Abrir.setMargin(new java.awt.Insets(5, 5, 0, 5));
        Abrir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(Abrir);

        jSeparator2.setPreferredSize(new java.awt.Dimension(15, 0));
        jSeparator2.setSeparatorSize(new java.awt.Dimension(15, 0));
        jToolBar1.add(jSeparator2);

        Deshacer.setFont(new java.awt.Font("Segoe UI Emoji", 0, 18)); // NOI18N
        Deshacer.setForeground(new java.awt.Color(255, 153, 0));
        Deshacer.setText("↩");
        Deshacer.setFocusable(false);
        Deshacer.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        Deshacer.setMargin(new java.awt.Insets(5, 5, 0, 5));
        Deshacer.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(Deshacer);

        Rehacer.setFont(new java.awt.Font("Segoe UI Emoji", 0, 18)); // NOI18N
        Rehacer.setForeground(new java.awt.Color(255, 153, 0));
        Rehacer.setText("↪");
        Rehacer.setFocusable(false);
        Rehacer.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        Rehacer.setMargin(new java.awt.Insets(5, 5, 0, 5));
        Rehacer.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(Rehacer);

        jSeparator3.setPreferredSize(new java.awt.Dimension(15, 0));
        jSeparator3.setSeparatorSize(new java.awt.Dimension(15, 0));
        jToolBar1.add(jSeparator3);

        Debug.setFont(new java.awt.Font("Segoe UI Emoji", 0, 18)); // NOI18N
        Debug.setForeground(new java.awt.Color(204, 0, 204));
        Debug.setText("🐺");
        Debug.setFocusable(false);
        Debug.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        Debug.setMargin(new java.awt.Insets(5, 5, 0, 5));
        Debug.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(Debug);

        Ejecutar.setFont(new java.awt.Font("Segoe UI Emoji", 0, 18)); // NOI18N
        Ejecutar.setForeground(new java.awt.Color(102, 255, 0));
        Ejecutar.setText("▶");
        Ejecutar.setFocusable(false);
        Ejecutar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        Ejecutar.setMargin(new java.awt.Insets(5, 5, 0, 5));
        Ejecutar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(Ejecutar);

        Terminar.setFont(new java.awt.Font("Segoe UI Emoji", 0, 18)); // NOI18N
        Terminar.setForeground(new java.awt.Color(255, 51, 0));
        Terminar.setText("❌");
        Terminar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        Terminar.setMargin(new java.awt.Insets(5, 5, 0, 5));
        Terminar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(Terminar);

        jSeparator4.setPreferredSize(new java.awt.Dimension(15, 0));
        jSeparator4.setSeparatorSize(new java.awt.Dimension(15, 0));
        jToolBar1.add(jSeparator4);

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jSplitPane1.setDividerLocation(600);

        Editor.setBackground(new java.awt.Color(0, 204, 102));
        Editor.setColumns(20);
        Editor.setRows(5);
        Editor.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1), "Editor", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP));
        ScrollEditor.setViewportView(Editor);

        jSplitPane1.setLeftComponent(ScrollEditor);

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane2.setViewportView(jTextArea2);

        jTabbedPane1.addTab("Tokens", jScrollPane2);

        jTextArea3.setColumns(20);
        jTextArea3.setRows(5);
        jScrollPane3.setViewportView(jTextArea3);

        jTabbedPane1.addTab("No se", jScrollPane3);

        jSplitPane1.setRightComponent(jTabbedPane1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
        );

        jSplitPane2.setLeftComponent(jPanel1);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jTextArea4.setColumns(20);
        jTextArea4.setRows(5);
        jTextArea4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1), "Consola", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP));
        jScrollPane4.setViewportView(jTextArea4);

        jPanel3.add(jScrollPane4, java.awt.BorderLayout.CENTER);

        jSplitPane2.setRightComponent(jPanel3);

        jPanel2.add(jSplitPane2, java.awt.BorderLayout.CENTER);

        jMenu4.setText("Archivos");

        jMenuItem1.setText("Cargar");
        jMenu4.add(jMenuItem1);

        jMenuItem2.setText("Eliminar");
        jMenu4.add(jMenuItem2);

        jMenuItem3.setText("Guardar");
        jMenu4.add(jMenuItem3);

        jMenuItem4.setText("Guardar como");
        jMenu4.add(jMenuItem4);

        jMenuBar2.add(jMenu4);

        jMenu5.setText("Editar");
        jMenuBar2.add(jMenu5);

        setJMenuBar(jMenuBar2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void GuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GuardarActionPerformed

    }//GEN-LAST:event_GuardarActionPerformed

    private void NuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NuevoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NuevoActionPerformed
    
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(Principal::new);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Abrir;
    private javax.swing.JButton Debug;
    private javax.swing.JButton Deshacer;
    private javax.swing.JTextArea Editor;
    private javax.swing.JButton Ejecutar;
    private javax.swing.JButton Guardar;
    private javax.swing.JButton Nuevo;
    private javax.swing.JButton Rehacer;
    private javax.swing.JScrollPane ScrollEditor;
    private javax.swing.JButton Terminar;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextArea jTextArea3;
    private javax.swing.JTextArea jTextArea4;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
