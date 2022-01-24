package pt.isec.PD.Data.Models;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * <p>Utils class.</p>
 *
 * @author
 * @version $Id: $Id
 */
public class Utils {


    /**
     * <p>getaddress.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public static String getaddress() {
        InetAddress ip = null;
        try {
            ip = InetAddress.getLocalHost();
            return ip.getHostAddress();
        } catch (UnknownHostException e) {
            return null;
        }
    }

    /**
     * <p>convertToBytes.</p>
     *
     * @param object a {@link java.lang.Object} object.
     * @return an array of {@link byte} objects.
     * @throws java.io.IOException if any.
     */
    public static byte[] convertToBytes(Object object) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutput out = new ObjectOutputStream(bos)) {
            out.writeObject(object);
            return bos.toByteArray();
        }
    }

    /**
     * <p>convertFromBytes.</p>
     *
     * @param bytes an array of {@link byte} objects.
     * @return a {@link java.lang.Object} object.
     * @throws java.io.IOException if any.
     * @throws java.lang.ClassNotFoundException if any.
     */
    public static Object convertFromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes); ObjectInput in = new ObjectInputStream(bis)) {
            return in.readObject();
        }
    }




}
