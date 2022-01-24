package pt.isec.PD.Data.Rmi;

import java.io.Serializable;
import java.util.ArrayList;

public class RmiServerDetails implements Serializable {

    private static final long serialVersionUID = 1L;
    private ArrayList<Integer> TcpPorts;
    //tempo

    public RmiServerDetails(){
        this.TcpPorts = new ArrayList<>();
    }

    public ArrayList<Integer> getTcpPorts() {
        return TcpPorts;
    }

    public void setTcpPorts(ArrayList<Integer> tcpPorts) {
        TcpPorts = tcpPorts;
    }
}
