package ludwig_sergio_leo.compilador;

import EditorCodigo.Pestana;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.io.File;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import librerias.Archivo;
import librerias.Conversiones;

/**
 * @author Ludwig Ivan Ortiz Sierra
 * @author Sergio Octavio Cervante Mujica
 * @author Leonardo Leon Moreno
 */
public class Principal extends javax.swing.JFrame {

    // Permite llevar el seguimiento de los archivos abiertos en el compilador
    Vector<Archivo> archivos = new Vector<>();
    // Permite llevar el seguimiento de las pestañas abiertas en el compilador
    Vector<Pestana> pestanas=new Vector<>();
    Conversiones obc=new Conversiones();

    public Principal() {
        initComponents();
    }
    
    // Agregar un boton cerrar para las pestanas
    public void agregarBotonCerrar(JTabbedPane tabs, int index) {
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnl.setOpaque(false);

        JLabel titulo = new JLabel(tabs.getTitleAt(index));
        JButton cerrar = new JButton(" X");
        cerrar.setMargin(new Insets(0, 4, 0, 4));
        cerrar.setBorder(null);
        cerrar.setFocusable(false);

        // Accion de Cerrar la Pestana (Adecuar)
        cerrar.addActionListener(e -> {
            int i = tabs.indexOfTabComponent(pnl);
            if (i != -1) {
                tabs.remove(i);
            }
        });

        pnl.add(titulo);
        pnl.add(cerrar);
        tabs.setTabComponentAt(index, pnl);
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
        Principal = new javax.swing.JPanel();
        SplitPanel = new javax.swing.JSplitPane();
        Panel1 = new javax.swing.JPanel();
        SplitPanel2 = new javax.swing.JSplitPane();
        Editor = new javax.swing.JTabbedPane();
        Tablas = new javax.swing.JTabbedPane();
        Tokens = new javax.swing.JScrollPane();
        TablaTokens = new javax.swing.JTable();
        Simbolos = new javax.swing.JScrollPane();
        TablaSimbolos = new javax.swing.JTable();
        Panel2 = new javax.swing.JPanel();
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

        Principal.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        Principal.setLayout(new java.awt.BorderLayout());

        SplitPanel.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        SplitPanel2.setDividerLocation(600);
        SplitPanel2.setLeftComponent(Editor);

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
        Tokens.setViewportView(TablaTokens);

        Tablas.addTab("Tokens", Tokens);

        Simbolos.setName("Simbolos"); // NOI18N

        TablaSimbolos.setModel(new javax.swing.table.DefaultTableModel(
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
        Simbolos.setViewportView(TablaSimbolos);

        Tablas.addTab("Simbolos", Simbolos);

        SplitPanel2.setRightComponent(Tablas);

        javax.swing.GroupLayout Panel1Layout = new javax.swing.GroupLayout(Panel1);
        Panel1.setLayout(Panel1Layout);
        Panel1Layout.setHorizontalGroup(
            Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(SplitPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
        );
        Panel1Layout.setVerticalGroup(
            Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(SplitPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
        );

        SplitPanel.setLeftComponent(Panel1);

        Panel2.setLayout(new java.awt.BorderLayout());

        jTextArea4.setColumns(20);
        jTextArea4.setRows(5);
        jTextArea4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1), "Consola", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP));
        jScrollPane4.setViewportView(jTextArea4);

        Panel2.add(jScrollPane4, java.awt.BorderLayout.CENTER);

        SplitPanel.setRightComponent(Panel2);

        Principal.add(SplitPanel, java.awt.BorderLayout.CENTER);

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
            .addComponent(Principal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Principal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
            pestanas.add(new Pestana(arc.getRuta().getFileName().toString()));
            pestanas.lastElement().getArea().setText(arc.toString());
            Editor.add(pestanas.lastElement());
            agregarBotonCerrar(Editor,pestanas.size()-1);
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
               // Editor.setText(arc.toString());
            } else {
                JOptionPane.showMessageDialog(null, "Nombre de archivo no válido.");
            }
        }
    }//GEN-LAST:event_NuevoMouseClicked

    private void GuardarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_GuardarMouseClicked
//        if (!(Editor.getText().isBlank()) && !archivos.isEmpty()) {
//            Archivo arc = archivos.lastElement();
//            arc.write(Editor.getText());
//        } else {
//            JOptionPane.showMessageDialog(null, "No hay archivo abierto aun");
//        }

    }//GEN-LAST:event_GuardarMouseClicked

    private void EjecutarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EjecutarMouseClicked
//        if (!archivos.isEmpty()) {
//            Lex obl = new Lex();
//            Archivo arc = archivos.lastElement();
//            HashMap<String, String> obh = obl.Lexico(obl.Pre_Procesado(arc.toArrayLine()));
//            TablaTokens.setModel(obc.convertirTM(obh));
//        } else {
//            JOptionPane.showMessageDialog(null, "No hay archivo abierto aun");
//        }
    }//GEN-LAST:event_EjecutarMouseClicked


    public static void main(String[] args) {
        SwingUtilities.invokeLater(Principal::new);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Abrir;
    private javax.swing.JButton Debug;
    private javax.swing.JButton Deshacer;
    private javax.swing.JTabbedPane Editor;
    private javax.swing.JButton Ejecutar;
    private javax.swing.JButton Guardar;
    private javax.swing.JButton Nuevo;
    private javax.swing.JPanel Panel1;
    private javax.swing.JPanel Panel2;
    private javax.swing.JPanel Principal;
    private javax.swing.JButton Rehacer;
    private javax.swing.JScrollPane Simbolos;
    private javax.swing.JSplitPane SplitPanel;
    private javax.swing.JSplitPane SplitPanel2;
    private javax.swing.JTable TablaSimbolos;
    private javax.swing.JTable TablaTokens;
    private javax.swing.JTabbedPane Tablas;
    private javax.swing.JButton Terminar;
    private javax.swing.JScrollPane Tokens;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JTextArea jTextArea4;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
