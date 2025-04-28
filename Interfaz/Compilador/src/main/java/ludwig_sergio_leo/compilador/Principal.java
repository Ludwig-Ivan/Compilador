package ludwig_sergio_leo.compilador;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import librerias.Archivo;
import librerias.Lex;

/**
 * @author Ludwig Ivan Ortiz Sierra
 * @author Sergio Octavio Cervante Mujica
 * @author Leonardo Leon Moreno
 */
public class Principal extends javax.swing.JFrame {

    NumeroLinea NumeroDeLineas;

    // Permite llevar el seguimiento de los archivos abiertos en el compilador
    Vector<Archivo> archivos = new Vector<>();

    public Principal() {
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
        jScrollPane1 = new javax.swing.JScrollPane();
        TablaTokens = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();
        Panel_Consola = new javax.swing.JPanel();
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
        Nuevo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                NuevoMouseClicked(evt);
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
        Guardar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                GuardarMouseClicked(evt);
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
        Abrir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                AbrirMouseClicked(evt);
            }
        });
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
        Ejecutar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                EjecutarMouseClicked(evt);
            }
        });
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

        TablaTokens.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(TablaTokens);

        jTabbedPane1.addTab("Tokens", jScrollPane1);

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

        Panel_Consola.setLayout(new java.awt.BorderLayout());

        jTextArea4.setColumns(20);
        jTextArea4.setRows(5);
        jTextArea4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1), "Consola", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP));
        jScrollPane4.setViewportView(jTextArea4);

        Panel_Consola.add(jScrollPane4, java.awt.BorderLayout.CENTER);

        jSplitPane2.setRightComponent(Panel_Consola);

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

    // Accion del Boton de Abrir Documento
    private void AbrirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AbrirMouseClicked
        JFileChooser fileChooser = new JFileChooser();
        int resultado = fileChooser.showOpenDialog(null);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = fileChooser.getSelectedFile();
            String ruta = archivoSeleccionado.getAbsolutePath();
            System.out.println("Ruta seleccionada: " + ruta);
            Archivo arc = new Archivo(ruta);
            archivos.add(arc);
            Editor.setText(arc.toString());
        }
    }//GEN-LAST:event_AbrirMouseClicked
    // Accion del Boton de Nuevo Documento
    private void NuevoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_NuevoMouseClicked
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // solo directorios
        int resultado = fileChooser.showOpenDialog(null);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            File directorioSeleccionado = fileChooser.getSelectedFile();
            String ruta = directorioSeleccionado.getAbsolutePath();
            System.out.println("Directorio seleccionado: " + ruta);
            String nombreArchivo = JOptionPane.showInputDialog(null, "Ingrese el nombre del nuevo archivo:");
            if (nombreArchivo != null && !nombreArchivo.trim().isEmpty()) {
                Archivo arc = new Archivo(ruta, nombreArchivo, ".lcl");
                archivos.add(arc);
                Editor.setText(arc.toString());
            } else {
                JOptionPane.showMessageDialog(null, "Nombre de archivo no válido.");
            }
        }
    }//GEN-LAST:event_NuevoMouseClicked

    private void GuardarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_GuardarMouseClicked
        if (!(Editor.getText().isBlank()) && !archivos.isEmpty()) {
            Archivo arc = archivos.lastElement();
            arc.write(Editor.getText());
        } else {
            JOptionPane.showMessageDialog(null, "No hay archivo abierto aun");
        }

    }//GEN-LAST:event_GuardarMouseClicked

    private void EjecutarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EjecutarMouseClicked
        if (!archivos.isEmpty()) {
            Lex obl = new Lex();
            Archivo arc = archivos.lastElement();
            HashMap<String, String> obh = obl.Lexico(obl.Pre_Procesado(arc.toArrayLine()));
            TablaTokens.setModel(convertirTM(obh));
        } else {
            JOptionPane.showMessageDialog(null, "No hay archivo abierto aun");
        }
    }//GEN-LAST:event_EjecutarMouseClicked

    private DefaultTableModel convertirTM(HashMap<String, String> tokens) {
        // Definimos las columnas de la tabla
        String[] columnas = {"Lexema", "Componente", "Línea", "Posición"};

        // Creamos el modelo vacío
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

        // Ordenamos el mapa por las llaves (IDs) usando TreeMap
        TreeMap<String, String> tokensOrdenados = new TreeMap<>(tokens);

        for (Map.Entry<String, String> entrada : tokensOrdenados.entrySet()) {
            String datos = entrada.getValue();
            String[] partes = datos.split(",", -1); // "-1" para que no pierda campos vacíos

            if (partes.length == 4) {
                modelo.addRow(new Object[]{partes[0], partes[1], partes[2], partes[3]});
            } else {
                System.out.println("Error en formato de token (ID " + entrada.getKey() + "): " + datos);
            }
        }

        return modelo;
    }

    public static void main(String[] args) {
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
    private javax.swing.JPanel Panel_Consola;
    private javax.swing.JButton Rehacer;
    private javax.swing.JScrollPane ScrollEditor;
    private javax.swing.JTable TablaTokens;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea3;
    private javax.swing.JTextArea jTextArea4;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
