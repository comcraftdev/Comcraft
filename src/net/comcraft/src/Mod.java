package net.comcraft.src;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import com.igormaznitsa.mjvm.MJVMClass;
public class Mod {

    private String ModName = "";
    private String MainClass = "";
    private String ModDescription = "";
    private boolean running=false;
    public Mod(String path) {
        System.out.println("scanning folder "+path);
        try {
            FileConnection fileConnection = (FileConnection) Connector.open(path + "ccmod.info", Connector.READ);
            if (!fileConnection.exists()) {
                return;
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileConnection.openInputStream()));
            ModName = bufferedReader.readLine().trim();
            ModDescription = bufferedReader.readLine().trim();
            MainClass = bufferedReader.readLine().trim();
            bufferedReader.close();
            fileConnection.close();
            System.out.println("Found Mod: "+ModName+"("+ModDescription+") @"+MainClass);
            FileConnection classfcon = (FileConnection) Connector.open(path + MainClass, Connector.READ);
            if (!classfcon.exists()) {
                System.out.println("class file "+MainClass+ " does not exist!");
                return;
            }
            System.out.println("Class file: "+path+MainClass);
            InputStream i=classfcon.openInputStream();
            MJVMClass c=new MJVMClass(i,new ModProcessor(path));
            try {
                c.newInstance();
                running=true;
            }
            catch(Throwable t) {
                t.printStackTrace();
            }
            i.close();
            classfcon.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new ComcraftException(ex);
        }
    }

    public String getModName() {
        return ModName;
    }

    public String getModDescription() {
        return ModDescription;
    }
    public boolean isRunning() {
        return running;
    }
}
