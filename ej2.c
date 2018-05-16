#include <stdio.h>

void
main (void) {
  FILE* cancion1 = fopen ("cancion1.mp3","r");
  FILE* cifrado1 = fopen ("cifrado1","w");
  int c;
  unsigned char* key = "hola";
  int i = 0;
  while (1) {
    c = fgetc (cancion1);
    if (feof (cancion1))
      break;
    unsigned char xorb = c^key[i];
    i = (i+1)%4;
    fwrite (&xorb ,1, 1, cifrado1);
  }
  fclose (cancion1);
  fclose (cifrado1);

  FILE* cancion2 = fopen ("cancion2.mp3","r");
  FILE* llave = fopen ("llave","r");
  FILE* cifrado2 = fopen ("cifrado2","w");
  int c1,c2;
  while (1) {
    c1 = fgetc (cancion2);
    c2 = fgetc (llave);
    if (feof (cancion2))
      break;
    unsigned char xorb = c1^c2;
    fwrite (&xorb ,1, 1, cifrado2);
  }
  fclose (cancion2);
  fclose (llave);
  fclose (cifrado2);

  cifrado1 = fopen ("cifrado1","r");
  cifrado2 = fopen ("cifrado2","r");
  FILE* rolon = fopen ("rolon.mp3","w");
  while (1) {
    c1 = fgetc (cifrado1);
    c2 = fgetc (cifrado2);
    if (feof (cifrado1))
      break;
    unsigned char xorb = c1^c2;
    fwrite (&xorb ,1, 1, rolon);
  }
  fclose (cifrado1);
  fclose (cifrado2);
  fclose (rolon);
}
