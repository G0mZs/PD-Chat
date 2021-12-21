package pt.isec.PD.GRDS;

import pt.isec.PD.GRDS.Model.Grds;
import pt.isec.PD.GRDS.TextInterface.GrdsTextInterface;

import java.io.IOException;

public class GrdsApp {
    private static GrdsTextInterface grdsTextInterface;

    public static void main(String[] args){
        Grds grds = new Grds();
        grdsTextInterface = new GrdsTextInterface(grds);
        grdsTextInterface.uiMain();

    }
}
