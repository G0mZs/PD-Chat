package pt.isec.PD.Client;

import pt.isec.PD.Client.Model.Chat;
import pt.isec.PD.Client.Network.CommunicationHandler;
import pt.isec.PD.Client.TextInferface.ClientTextInterface;

import java.io.IOException;

public class ClientApp {

    private static ClientTextInterface clientTextInterface;

    public static void main(String[] args) throws IOException {
        Chat chat = new Chat();
        CommunicationHandler handler = new CommunicationHandler(chat);
        clientTextInterface = new ClientTextInterface(handler);
        clientTextInterface.uiMain();


    }
}
