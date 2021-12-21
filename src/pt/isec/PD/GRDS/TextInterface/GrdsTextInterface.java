package pt.isec.PD.GRDS.TextInterface;

import pt.isec.PD.GRDS.Model.Grds;

import java.io.IOException;
import java.sql.SQLException;

public class GrdsTextInterface {

    private static Grds grds;


    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException, InterruptedException{
        grds = new Grds();
        grds.UdpThread();
    }
}
