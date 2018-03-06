import java.io.*;
import java.util.Arrays;
import java.util.Random;

public class vigenere {

  // PRNG
  private static Random rand = new Random ();
  // Frecuencias
  private static double[] q = new double[27];
  /** Índice de coincidencias del idioma español. */
  public static double ICE = 0.0741;

  public static int longitudClave (String C) {
    // Criptotexto de entrada C = c0c1...cn
    // n + 1 = l
    int l = C.length ();
    // para t = 1,2,...,l
    for (int t = 1; t <= l; t++) {
      // Bloque r aleatorio
      int r = rand.nextInt(t);
      // Longitud del bloque Br
      int k = l % t > r ? l/t + 1 : l/t;
      char[] chars = new char[k];
      for (int i = 0; i < k; i++)
        chars[i] = C.charAt (i*t + r);
      // Bloque Br = c{0+r}c{t+r}c{2t+r}...
      String Br = new String (chars);
      // Contamos frecuencias de cada letra del alfabeto
      Arrays.fill (q, 0.0);
      for (int i = 0; i < k; i++) {
        char ci = Br.charAt (i);
        int s = ci == 'Ñ' ? 26 : (int) (ci - 'A');
        q[s]++;
      }
      for (int i = 0; i < 27; i++)
        q[i] /= k;
      // Calculamos I
      double I = 0.0;
      for (double qi : q)
        I += qi*qi;
      // Valor de t que aproxima la longitud de la clave
      System.out.printf("%f\n",I);
      if (Math.abs (I - vigenere.ICE) < 0.001)
        return t;
    }
    return l;
  }

  public static int desplazamiento (String B) {
    
  }

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
      String cifrado = new String (stream).replaceAll ("[^A-ZÑ]","");
      reader.close();
      System.out.printf("%d\n",vigenere.longitudClave (cifrado));
    } catch (IOException e) {
      e.printStackTrace ();
    }
  }

}
