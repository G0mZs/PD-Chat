package pt.isec.PD.GRDS.TextInterface;

import pt.isec.PD.GRDS.Model.Grds;

import java.io.IOException;
import java.sql.SQLException;

public class GrdsTextInterface {

    private Grds grds;
    public GrdsTextInterface(Grds grds){
        this.grds = grds;
    }

    public void uiMain(){

        System.out.println("\nGrds Initialized");
        grds.UdpThread();
    }
}
