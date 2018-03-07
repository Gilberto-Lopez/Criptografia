import java.io.*;
import java.util.Arrays;
import java.util.Random;
import java.util.LinkedList;

/* Criptoanálisis para textos cifrados usando el cifrado de Vigenere. */
public class vigenere {

  // PRNG
  private static Random rand = new Random ();
  // Frecuencias A ... N Ñ O ... Z
  private static double[] q = new double[27];
  // Frecuencias de las letras en español
  // Fuente: https://www.sttmedia.com/characterfrequency-spanish
  // Suma 99.96
  private static double[] p = {12.16,1.49,3.87,4.67,14.08,0.69,1.00,1.18,5.98,0.52,0.11,5.24,3.08,6.83,0.17,
    9.2,2.89,1.11,6.41,7.20,4.60,4.69,1.05,0.04,0.14,1.09,0.47};
  // Umbral de aproximación de la longitud de la clave
  private static double EPS = 0.001;
  /** Índice de coincidencias del idioma español. */
  public static final double ICE = 0.0741;

  /* Normalizamos los porcentajes de p. */
  private static void normaliza () {
    for (int i = 0; i < 27; i++)
      p[i] /= 99.96;
  }

  /* Determina el índice de un caracter C (A ... N Ñ O ... Z) en
   * ese orden. */
  private static int indice (char c) {
    return c == 'Ñ' ? 14 : (c <= 'N' ? c - 'A' : c - 'A' + 0x0001);
  }

  /* Dado un índice I (0,1,...,26), determina el caracter que representa. */
  private static char caracter (int i) {
    int j = i == 14 ? 'Ñ' : (i <= 13 ? i + 'A' : i + 'A' - 0x0001);
    return (char)j;
  }

  /* Calcula las frecuencias del bloque B y las escribe en q. */
  private static void frecuencias (String B) {
    // Limpiamos q
    Arrays.fill (q, 0.0);
    int k = B.length ();
    for (int i = 0; i < k; i++) {
      char ci = B.charAt (i);
      q[vigenere.indice (ci)]++;
    }
    for (int i = 0; i < 27; i++)
      q[i] /= k;
  }

  /* Calcula el bloque (columna) COL del texto C, dividido en periodos de
   * longitud T. */
  private static String bloque (String C, int t, int col) {
    int l = C.length ();
    // Longitud del bloque B
    int k = l % t > col ? l/t + 1 : l/t;
    char[] chars = new char[k];
    for (int i = 0; i < k; i++)
      chars[i] = C.charAt (i*t + col);
    // Bloque B = c{0+col}c{t+col}c{2t+col}...
    return new String (chars);
  }

  /**
   * Estima la longitud de la clave usada para cifrar el texto.
   * @param C El texto cifrado.
   * @return La aproximación de la longitud de la clave.
   */
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
      if (Math.abs (I - vigenere.ICE) < vigenere.EPS)
        return t;
    }
    return l;
  }

  /**
   * Estima el desplazamiento usado para cifrar el bloque.
   * @param B El bloque a descifrar.
   * @return El desplzamiento usado para cifrar el bloque.
   */
  public static int desplazamiento (String B) {
    // Frecuencias en el bloque B
    vigenere.frecuencias (B);
    int d = 0;
    double dif = 1.0;
    for (int k = 0; k < 27; k++) {
      double Ik = 0.0;
      for (int i = 0; i < 27; i++)
        Ik += p[i]*q[(i+k) % 27];
      // Valor de k que aproxima el desplazamiento
      double approx = Math.abs (Ik - vigenere.ICE);
      if (approx < dif){
        d = k;
        dif = approx;
      }
    }
    return d;
  }

  /**
   * Escribe en pantalla el texto C descifrado usando la llave K.
   * @param C El texto cifrado.
   * @param k La llave.
   */
  public static void descifrar (String C, int[] k) {
    System.out.print ("Clave: ");
    for (int c : k)
      System.out.print (vigenere.caracter(c));
    System.out.print("\n");
    int l = k.length;
    int m = Math.min (50, C.length());
    int j = 0;
    for (int i = 0; j < m; i++) {
      char ci = C.charAt(i);
      if (ci == 'Ñ' || (ci >= 'A' && ci <= 'Z')) {
        int di = vigenere.indice(ci);
        System.out.print (vigenere.caracter(di-k[j%l]%27));
        j++;
      } else {
        System.out.print (ci);
      }
    }
    System.out.print("\n");
  }

  /* Punto de entrada del programa. */
  public static void main (String[] args) {
    if (0 == args.length)
      return;
    vigenere.normaliza();
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
      // Obtener longitud de la clave (las 3 mejores)
      LinkedList<Integer> longitudes = new LinkedList<>();
      for (int i = 0; i < 3; i++)
        longitudes.add(vigenere.longitudClave (cifrado));
      // Desciframos usando las mejores longitudes y llaves
      for (int longitud : longitudes) {
        // Descrifrar bloques
        int[] llave = new int[longitud];
        for (int r = 0; r < longitud; r++) {
          // Bloque Br a descifrar
          String Br = vigenere.bloque (cifrado, longitud, r);
          // Desplazamiento del bloque
          llave[r] = vigenere.desplazamiento (Br);
        }
        vigenere.descifrar (cifrado, llave);
      }
    } catch (IOException e) {
      e.printStackTrace ();
    }
  }

}
