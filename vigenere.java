import java.io.*;

public class vigenere {

  public static void main (String[] args) {
    if (0 == args.length)
      return;
    try {
      File file = new File (args[0]);
      // Leemos el archivo de entrada en codificación UTF8
      Reader reader = new InputStreamReader (new FileInputStream (file), "UTF-8");
      // Peor de los casos, 1 caracter por byte
      char[] stream = new char[(int) file.length()];
      reader.read (stream);
      String cifrado = new String (stream);
      // Stuff
      reader.close();
    } catch (IOException e) {
      e.printStackTrace ();
    }
  }

}
