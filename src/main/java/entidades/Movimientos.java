/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

import accesoadatos.ComunDB;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author emers
 */
public class Movimientos {

    private int movimientoID;
    private Date fecha;
    private int cuentaID;
    private String descripcion;
    private BigDecimal debe;
    private BigDecimal haber;

    public int getMovimientoID() {
        return movimientoID;
    }

    public void setMovimientoID(int movimientoID) {
        this.movimientoID = movimientoID;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getCuentaID() {
        return cuentaID;
    }

    public void setCuentaID(int cuentaID) {
        this.cuentaID = cuentaID;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getDebe() {
        return debe;
    }

    public void setDebe(BigDecimal debe) {
        this.debe = debe;
    }

    public BigDecimal getHaber() {
        return haber;
    }

    public void setHaber(BigDecimal haber) {
        this.haber = haber;
    }

    public void cargardatosID(JComboBox<String> comboBox) {
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

    public void crear(JTextField paramFecha, JComboBox<String> paramCuentaID, JTextField Descripcion, JTextField paramDebe, JTextField paramHaber) {
    // Verificar que todos los campos estén llenos
    if (paramFecha.getText().isEmpty()) {
        JOptionPane.showMessageDialog(null, "El campo Fecha es obligatorio.");
        return;
    }
    if (paramCuentaID.getSelectedItem() == null) {
        JOptionPane.showMessageDialog(null, "El campo CuentaID es obligatorio.");
        return;
    }
    if (Descripcion.getText().isEmpty()) {
        JOptionPane.showMessageDialog(null, "El campo Descripción es obligatorio.");
        return;
    }
    if (paramDebe.getText().isEmpty()) {
        JOptionPane.showMessageDialog(null, "El campo Debe es obligatorio.");
        return;
    }
    if (paramHaber.getText().isEmpty()) {
        JOptionPane.showMessageDialog(null, "El campo Haber es obligatorio.");
        return;
    }

    // Insertar el movimiento en la base de datos
    String consulta = "INSERT INTO Movimientos (Fecha, CuentaID, Descripcion, Debe, Haber) VALUES (?, ?, ?, ?, ?)";
    try {
        CallableStatement cs = ComunDB.obtenerConexion().prepareCall(consulta);

        // Convertir la fecha al formato adecuado para la base de datos
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        java.util.Date parsedDate = inputDateFormat.parse(paramFecha.getText());
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = outputDateFormat.format(parsedDate);

        // Establecer los valores de los parámetros
        cs.setString(1, formattedDate);
        cs.setString(2, (String) paramCuentaID.getSelectedItem());
        cs.setString(3, Descripcion.getText());
        cs.setDouble(4, Double.parseDouble(paramDebe.getText()));
        cs.setDouble(5, Double.parseDouble(paramHaber.getText()));

        // Ejecutar la consulta
        cs.execute();

        JOptionPane.showMessageDialog(null, "Se insertó correctamente el movimiento");
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Error: Los valores de Debe y Haber deben ser números.");
    } catch (ParseException e) {
        JOptionPane.showMessageDialog(null, "Error al convertir la fecha: Formato de fecha incorrecto.");
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al insertar el movimiento: " + e.getMessage());
    }
}


    public void mostrarMovimientos(JTable tablaMovimientos) {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("MovimientoID");
        modelo.addColumn("Fecha");
        modelo.addColumn("CuentaID");
        modelo.addColumn("Descripcion");
        modelo.addColumn("Debe");
        modelo.addColumn("Haber");

        tablaMovimientos.setModel(modelo);

        String sql = "SELECT * FROM Movimientos;";

        try (Connection connection = ComunDB.obtenerConexion(); Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Object[] datos = new Object[6];
                datos[0] = rs.getInt("MovimientoID");
                datos[1] = rs.getDate("Fecha");
                datos[2] = rs.getInt("CuentaID");
                datos[3] = rs.getString("Descripcion");
                datos[4] = rs.getBigDecimal("Debe");
                datos[5] = rs.getBigDecimal("Haber");

                modelo.addRow(datos);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar los registros de movimientos: " + e.getMessage());
        }
    }

    public void SeleccionarMovimiento(JTable paramTablaCuenta, JTextField paramMovimientoID, JTextField paramFecha, JComboBox<String> paramCuentaID, JTextField Descripcion, JTextField paramDebe, JTextField paramHaber) {
        try {
            int fila = paramTablaCuenta.getSelectedRow();
            if (fila >= 0) { // Verifica que se ha seleccionado una fila válida
                paramMovimientoID.setText(paramTablaCuenta.getValueAt(fila, 0).toString());
                paramFecha.setText(paramTablaCuenta.getValueAt(fila, 1).toString());

                // Si paramCuentaID es un JComboBox y quieres establecer un elemento seleccionado, usa setSelectedItem
                Object valorCuentaID = paramTablaCuenta.getValueAt(fila, 2);
                if (valorCuentaID != null) {
                    paramCuentaID.setSelectedItem(valorCuentaID.toString());
                } else {
                    paramCuentaID.setSelectedItem(null); // Establecer el JComboBox como nulo si el valor es nulo
                }

                Descripcion.setText(paramTablaCuenta.getValueAt(fila, 3).toString());
                paramDebe.setText(paramTablaCuenta.getValueAt(fila, 4).toString());
                paramHaber.setText(paramTablaCuenta.getValueAt(fila, 5).toString());
            } else {
                JOptionPane.showMessageDialog(null, "Ninguna fila seleccionada");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al seleccionar la fila: " + e.toString());
        }
    }
    
    public void ModificarCuenta(JTable paramTablaCuenta, JTextField paramFecha, JComboBox<String> paramCuentaID, JTextField paramDescripcion, JTextField paramDebe, JTextField paramHaber) {
    try {
        // Check if a row is selected in the table
        int selectedRow = paramTablaCuenta.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "No row selected. Please select a row to modify.");
            return;
        }

        // Retrieve MovimientoID from the selected row
        int movimientoID = (int) paramTablaCuenta.getValueAt(selectedRow, 0);
        setMovimientoID(movimientoID);

        // Convertir cadena de fecha a java.sql.Date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        java.util.Date parsedDate = dateFormat.parse(paramFecha.getText());
        java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());
        setFecha(sqlDate);

        // Extraer valor de JComboBox
        setCuentaID(Integer.parseInt((String) paramCuentaID.getSelectedItem()));

        // Establecer otros campos
        setDescripcion(paramDescripcion.getText());
        setDebe(new BigDecimal(paramDebe.getText())); // Convertir texto a BigDecimal
        setHaber(new BigDecimal(paramHaber.getText())); // Convertir texto a BigDecimal

        String consulta = "UPDATE Movimientos SET Fecha = ?, CuentaID = ?, Descripcion = ?, Debe = ?, Haber = ? WHERE MovimientoID = ?;";

        try {
            CallableStatement cs = ComunDB.obtenerConexion().prepareCall(consulta);
            cs.setDate(1, getFecha());
            cs.setInt(2, getCuentaID());
            cs.setString(3, getDescripcion());
            cs.setBigDecimal(4, getDebe());
            cs.setBigDecimal(5, getHaber());
            cs.setInt(6, getMovimientoID());

            cs.execute();
            JOptionPane.showMessageDialog(null, "Modificación exitosa");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Modificar, error: " + e.toString());
        }
    } catch (ParseException e) {
        JOptionPane.showMessageDialog(null, "Error al convertir la fecha: Formato de fecha incorrecto.");
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Error: Formato de número incorrecto.");
    }
}


    //public void EliminarCuenta() {
    //  ComunDB conexion = new ComunDB();
    //String consulta = "DELETE FROM Movimientos;";
    //try {
    //  CallableStatement cs = conexion.obtenerConexion().prepareCall(consulta);
    //cs.execute();
    //JOptionPane.showMessageDialog(null, "Eliminación exitosa de todas las cuentas");
    //} catch (SQLException e) {
    //  JOptionPane.showMessageDialog(null, "Error al Eliminar, error: " + e.toString());
    //}
    public void EliminarMovimiento(JTextField movimientoIDField) {
        ComunDB conexion = new ComunDB();
        String consulta = "DELETE FROM Movimientos WHERE MovimientoID = ?";
        try {
            int movimientoID = Integer.parseInt(movimientoIDField.getText()); // Obtener el texto y convertirlo a int
            CallableStatement cs = conexion.obtenerConexion().prepareCall(consulta);
            cs.setInt(1, movimientoID); // Usar el valor int aquí
            cs.execute();
            JOptionPane.showMessageDialog(null, "Eliminación exitosa del movimiento con ID: " + movimientoID);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: El MovimientoID debe ser un número.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el movimiento con ID " + movimientoID + ", error: " + e.toString());
        }
    }

}
