package pt.isec.PD.Server.Network.Udp;

public class PortSender {

    int tcpPort;

    public PortSender(int tcpPort){
        this.tcpPort = tcpPort;
    }


    public void run(){
        //Thread para o servidor mandar o Tcp Port para o Grds via udp de 20 em 20 segundos
        //Se não receber a mensagem do servidor passado 3 vezes, o Grds esquece o servidor.
    }
}
