import java.io.*;
import java.util.Arrays;
import java.util.Random;

public class vigenere {

  // PRNG
  private static Random rand = new Random ();
  // Frecuencias (posición 26 para 'Ñ')
  private static double[] q = new double[27];
  // Frecuencias de las letras en español
  private static double[] p = null;
  // Umbral de aproximación
  private static double EPS = 0.001;
  /** Índice de coincidencias del idioma español. */
  public static final double ICE = 0.0741;

  /* Calcula las frecuencias del bloque B y las escribe en q. */
  private static void frecuencias (String B) {
    // Limpiamos q
    Arrays.fill (q, 0.0);
    int k = B.length ();
    for (int i = 0; i < k; i++) {
      char ci = B.charAt (i);
      int s = ci == 'Ñ' ? 26 : (int) (ci - 'A');
      q[s]++;
    }
    for (int i = 0; i < 27; i++)
      q[i] /= k;
  }

  /* Calcula el bloque COL del texto C dividido en periodos de longitud T. */
  private static String bloque (String C, int t, int col) {
    int l = C.length ();
    // Longitud del bloque B
    int k = l % t > col ? l/t + 1 : l/t;
    char[] chars = new char[k];
    for (int i = 0; i < k; i++)
      chars[i] = C.charAt (i*t + col);
    // Bloque B = c{0+r}c{t+r}c{2t+r}...
    return new String (chars);
  }

  public static int longitudClave (String C) {
    // Criptotexto de entrada C = c0c1...cn
    // n + 1 = l
    int l = C.length ();
    // para t = 1,2,...,l
    for (int t = 1; t <= l; t++) {
      // Bloque r aleatorio
      int r = rand.nextInt(t);
      String Br = vigenere.bloque (C, t, r);
      // Contamos frecuencias de cada letra del alfabeto
      vigenere.frecuencias (Br);
      // Calculamos I
      double I = 0.0;
      for (double qi : q)
        I += qi*qi;
      // Valor de t que aproxima la longitud de la clave
      System.out.printf("%f\n",I);
      if (Math.abs (I - vigenere.ICE) < vigenere.EPS)
        return t;
    }
    return l;
  }

  public static int desplazamiento (String B) {
    // Frecuencias en el bloque B
    vigenere.frecuencias (B);
    for (int k = 0; k < 27; k++) {
      double Ik = 0.0;
      for (int j = 0; j < 27; j++)
        Ik += p[j]*q[(j+k) % 27]
      // Valor de k que aproxima la longitud de la clave
      System.out.printf("%f\n",Ik);
      if (Math.abs (Ik - vigenere.ICE) < vigenere.EPS)
        return k;
    }
    return 0;
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
      // Texto cifrado
      String cifrado = new String (stream).replaceAll ("[^A-ZÑ]","");
      reader.close();
      // Obtener longitud de la clave
      int longitud = vigenere.longitudClave (cifrado);
      System.out.printf("%d\n",longitud);
      // Descrifrar bloques
      for (int r = 0; r < longitud; r++) {
        // Bloque Br a descifrar
        String Br = vigenere.bloque (cifrado, longitud, r);
        // Desplazamiento
        int d = vigenere.desplazamiento (Br);
      }
    } catch (IOException e) {
      e.printStackTrace ();
    }
  }

}
