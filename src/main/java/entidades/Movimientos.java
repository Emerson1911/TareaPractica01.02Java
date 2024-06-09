/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

import accesoadatos.ComunDB;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

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
        String consulta = "SELECT MovimientoID, Fecha, CuentaID, Descripcion, Debe, Haber FROM Movimientos;";
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
        // Insertar la cuenta en la base de datos
        String consulta = "INSERT INTO Movimientos (Fecha, CuentaID, Descripcion, Debe, Haber) VALUES (?, ?, ?, ?, ?)";
        try {
            CallableStatement cs = ComunDB.obtenerConexion().prepareCall(consulta);
            cs.setString(1, paramFecha.getText());
            cs.setString(2, (String) paramCuentaID.getSelectedItem());
            cs.setString(3, Descripcion.getText());
            cs.setDouble(4, Double.parseDouble(paramDebe.getText()));
            cs.setDouble(5, Double.parseDouble(paramHaber.getText()));
            cs.execute();
            JOptionPane.showMessageDialog(null, "Se insertó correctamente el movimiento");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: Los valores de Debe y Haber deben ser números.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al insertar el movimiento: " + e.getMessage());
        }
    }
}