package EditorCodigo;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import librerias.NumeroLinea;

/**
 *
 * @author Ludwig Ivan Ortiz Sierra
 */
public class Pestana extends JScrollPane {
    
    private JTextArea area;
    private String name;
    private NumeroLinea linea;
    
    public Pestana(String n){
        name=n;
        
        area=new JTextArea();
        area.setName("area"+name);
        area.setEditable(true);
        
        setName(name);
        
        linea=new NumeroLinea(area);
        setViewportView(area);
        setRowHeaderView(linea);
    }
    
    public String getNombre(){
        return name;
    }
    
    public JTextArea getArea(){
        return area;
    }
}
