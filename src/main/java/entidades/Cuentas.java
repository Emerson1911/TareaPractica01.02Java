/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

import accesoadatos.ComunDB;
import java.awt.HeadlessException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author emers
 */
public class Cuentas {

    private int cuentaID;
    private String numeroCuenta;
    private String nombre;
    private String tipo;
    private int nivel;
    private Integer padre;

    public int getCuentaID() {
        return cuentaID;
    }

    public void setCuentaID(int cuentaID) {
        this.cuentaID = cuentaID;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public Integer getPadre() {
        return padre;
    }

    public void setPadre(Integer padre) {
        this.padre = padre;
    }

    public void cargardatosPadre(JComboBox<String> comboBox) {
        comboBox.removeAllItems();
        comboBox.addItem("Seleccione una cuenta"); // Agregar ítem predeterminado
        String consulta = "SELECT CuentaID, NumeroCuenta, Nombre, Tipo, Nivel, Padre FROM Cuentas;";
        try {
            ComunDB conexion = new ComunDB();
            CallableStatement cs = conexion.obtenerConexion().prepareCall(consulta);
            ResultSet rs = cs.executeQuery();

            // Agregar los datos al JComboBox como String
            while (rs.next()) {
                // Obtener el valor del CuentaID
                int cuentaID = rs.getInt("CuentaID");
                // Agregar el valor como String al JComboBox
                comboBox.addItem(cuentaID != 0 ? String.valueOf(cuentaID) : null);
            }

            rs.close();
            //cs.close(); // Cierra el statement
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los datos del JComboBox: " + e.toString());
        }
    }

    public void crear(JTextField paramNumeroCuenta, JTextField paramNombre, JTextField paramTipo, JTextField paramNivel) {
        // Validar que los campos no estén vacíos
        if (paramNumeroCuenta.getText().isEmpty() || paramNombre.getText().isEmpty()
                || paramTipo.getText().isEmpty() || paramNivel.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Todos los campos son requeridos.");
            return;
        }

        // Validar que el nivel sea un número entero
        int nivel;
        try {
            nivel = Integer.parseInt(paramNivel.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "El valor de Nivel debe ser un número entero.");
            return;
        }

        // Establecer valores en el objeto
        setNumeroCuenta(paramNumeroCuenta.getText());
        setNombre(paramNombre.getText());
        setTipo(paramTipo.getText());
        setNivel(nivel);

        // Insertar la cuenta en la base de datos
        String consulta = "INSERT INTO Cuentas (NumeroCuenta, Nombre, Tipo, Nivel) VALUES (?, ?, ?, ?);";
        try {
            CallableStatement cs = ComunDB.obtenerConexion().prepareCall(consulta);
            cs.setString(1, getNumeroCuenta());
            cs.setString(2, getNombre());
            cs.setString(3, getTipo());
            cs.setInt(4, getNivel());
            cs.execute();

            JOptionPane.showMessageDialog(null, "Se insertó correctamente la Cuenta");
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al insertar la Cuenta: " + e.toString());
        }
    }

    public void MostrarCuenta(JTable paramTablaTotalCuenta) {

        DefaultTableModel modelo = new DefaultTableModel();

        TableRowSorter<TableModel> ordenarTabla = new TableRowSorter<>(modelo);
        paramTablaTotalCuenta.setRowSorter(ordenarTabla);

        var sql = "";
        modelo.addColumn("Id");
        modelo.addColumn("NumeroCuenta");
        modelo.addColumn("Nombre");
        modelo.addColumn("Tipo");
        modelo.addColumn("Nivel");
        modelo.addColumn("Padre");

        paramTablaTotalCuenta.setModel(modelo);

        sql = "SELECT * FROM Cuentas;";

        String[] datos = new String[6];
        Statement st;
        try {
            st = ComunDB.obtenerConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                datos[0] = rs.getString(1);
                datos[1] = rs.getString(2);
                datos[2] = rs.getString(3);
                datos[3] = rs.getString(4);
                datos[4] = rs.getString(5);
                datos[5] = rs.getString(6);

                modelo.addRow(datos);

            }
            paramTablaTotalCuenta.setModel(modelo);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar los registros: " + e.toString());
        }
    }

    public void SeleccionarCuenta(JTable paramTablaCuenta, JTextField paramId, JTextField paramNumeroCuenta, JTextField paramNombre, JTextField paramTipo, JTextField paramNivel) {
        try {
            int fila = paramTablaCuenta.getSelectedRow();
            if (fila >= 0) {
                paramId.setText((paramTablaCuenta.getValueAt(fila, 0)).toString());
                paramNumeroCuenta.setText((paramTablaCuenta.getValueAt(fila, 1)).toString());
                paramNombre.setText((paramTablaCuenta.getValueAt(fila, 2)).toString());
                paramTipo.setText((paramTablaCuenta.getValueAt(fila, 3)).toString());
                paramNivel.setText((paramTablaCuenta.getValueAt(fila, 4)).toString());
//                Object valorPadre = paramTablaCuenta.getValueAt(fila, 5);
//                if (valorPadre != null) {
//                    paramPadre.setSelectedItem(valorPadre.toString());
//                } else {
//                    paramPadre.setSelectedItem(null); // Establecer el JComboBox como nulo si el valor es nulo
//                }
            } else {
                JOptionPane.showMessageDialog(null, "Fila No seleccionada");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error de seleccion, error: " + e.toString());
        }

    }

    public void ModificarCuenta(JTextField paramNumeroCuenta, JTextField paramNombre, JTextField paramTipo, JTextField paramNivel) {
        setNumeroCuenta(paramNumeroCuenta.getText());
        setNombre(paramNombre.getText());
        setTipo(paramTipo.getText());
        setNivel(Integer.parseInt(paramNivel.getText())); // Asegúrate de convertir el texto a entero

        String consulta = "UPDATE Cuentas SET Nombre = ?, Tipo = ?, Nivel = ? WHERE NumeroCuenta = ?;";

        try {
            CallableStatement cs = ComunDB.obtenerConexion().prepareCall(consulta);
            cs.setString(1, getNombre());        // Primer parámetro: Nombre
            cs.setString(2, getTipo());          // Segundo parámetro: Tipo
            cs.setInt(3, getNivel());            // Tercer parámetro: Nivel (asegúrate que getNivel retorna un entero)
            cs.setString(4, getNumeroCuenta());  // Cuarto parámetro: NumeroCuenta

            cs.execute();
            JOptionPane.showMessageDialog(null, "Modificación exitosa");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Modificar, error: " + e.toString());
        }
    }

    public void EliminarCuenta(JTextField paramNumeroCuenta) {
        String numeroCuenta = paramNumeroCuenta.getText();
        ComunDB conexion = new ComunDB();
        String consulta = "DELETE FROM Cuentas WHERE NumeroCuenta = ?;";
        try {
            CallableStatement cs = conexion.obtenerConexion().prepareCall(consulta);
            cs.setString(1, numeroCuenta);
            cs.execute();
            JOptionPane.showMessageDialog(null, "Cuenta " + numeroCuenta + " eliminada exitosamente");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar la cuenta " + numeroCuenta + ": " + e.toString());
        }
    }

}
