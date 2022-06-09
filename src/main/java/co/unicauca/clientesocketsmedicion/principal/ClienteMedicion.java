/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package co.unicauca.clientesocketsmedicion.principal;

import com.mycompany.sistemaservidorclientecomun.Objeto;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author juanc
 */
public class ClienteMedicion {
    
    private String ep;
    private ArrayList<Objeto> aceptados;
    private ArrayList<Objeto> defectuosos;

    public ClienteMedicion() {
        aceptados=new ArrayList<>();
        defectuosos=new ArrayList<>();
        ep="";
    }
    
    public void iniciarConexionCliente(Objeto o){
      final String HOST= "127.0.0.1"  ;
      final int PUERTO = 5000;
      DataInputStream in;
      DataOutputStream out;
      
      try{
          Socket sc= new Socket(HOST, PUERTO);

          in = new DataInputStream(sc.getInputStream());
          out = new DataOutputStream(sc.getOutputStream());


          ClienteMedicionHilo hilo= new ClienteMedicionHilo(in,out,o);
          hilo.start();
          hilo.join();
          
          this.ep=hilo.getEP();
          
          if(!hilo.getAceptados().isEmpty()){
              this.aceptados.add(hilo.getAceptados().get(0));
          }else if(!hilo.getDefectuosos().isEmpty()){
              this.defectuosos.add(hilo.getDefectuosos().get(0));
          }
          
          System.out.println("Numero aceptados: "+this.aceptados.size());
          System.out.println("Numero defectuos: "+this.defectuosos.size());


          sc.close();
      
      }catch(IOException | InterruptedException ex){
          Logger.getLogger(ClienteMedicion.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    public ArrayList<Objeto> getAceptados() {
        return aceptados;
    }

    public void setAceptados(ArrayList<Objeto> aceptados) {
        this.aceptados = aceptados;
    }

    public ArrayList<Objeto> getDefectuosos() {
        return defectuosos;
    }

    public void setDefectuosos(ArrayList<Objeto> defectuosos) {
        this.defectuosos = defectuosos;
    }
    
    

    public String getEp() {
        return ep;
    }
    
    
}
