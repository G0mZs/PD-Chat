package pt.isec.PD.GRDS.Model;

import pt.isec.PD.Data.Constants;
import pt.isec.PD.GRDS.Network.UdpGrdsManager;

public class Grds {

    private UdpGrdsManager udpGrdsManager;

    public Grds(){
        udpGrdsManager = new UdpGrdsManager(Constants.UDP_PORT);

    }

    public void UdpThread(){
        udpGrdsManager.start();
    }


}
