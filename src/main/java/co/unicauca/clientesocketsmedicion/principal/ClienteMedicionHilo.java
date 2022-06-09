
package co.unicauca.clientesocketsmedicion.principal;

import com.google.gson.Gson;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.mycompany.sistemaservidorclientecomun.Objeto;
import java.util.ArrayList;

/**
 *
 * @author juanc
 */
public class ClienteMedicionHilo extends Thread{
    private DataInputStream in;
    private DataOutputStream out;
    private Objeto o;
    private String EP = "";
    private ArrayList<Objeto> aceptados;
    private ArrayList<Objeto> defectuosos;

    public ClienteMedicionHilo(DataInputStream in, DataOutputStream out, Objeto o) {
        this.in = in;
        this.out = out;
        this.o = o;
        aceptados= new ArrayList<>();
        defectuosos= new ArrayList<>();
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
    
    

    public String getEP() {
        return EP;
    }
    
    @Override
    public void run(){
        try {
            //creamos el objeto cerveza que vamos a transformar a json
           
            ArrayList<Objeto> productosAceptados = new ArrayList<>();
            
            System.out.println("Enviando objeto para guardar desde cliente hacia servidor");
            
            //tranformando el objeto a json para enviar
            Gson gson= new Gson();
            String jsonCerveza=gson.toJson(o);
            
            
            //enviando objeto en forma json del cliente al servidor
            out.writeUTF(jsonCerveza);
            
            //Recibimos mensaje del cliente, sobre aceptacion del producto
            
            this.EP= in.readUTF();
            
            
            //Recibimos cantidad de objetos aceptados
            int tipoTipoAceptacion=in.readInt();
            
            
            //Recibimos json de producto
            
            String jsonAceptados= in.readUTF();
            
            Objeto jObjeto = new Gson().fromJson(jsonAceptados, Objeto.class);
            
            //Agregamos a el arraylist
            
            if(tipoTipoAceptacion==1){
                this.aceptados.add(jObjeto);
            }else{
                this.defectuosos.add(jObjeto);
            }           
            
                    
            
        } catch (IOException ex) {
            Logger.getLogger(ClienteMedicionHilo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
