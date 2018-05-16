#!/usr/bin/env python
# -*- coding: utf-8 -*-

import sys

def egcd(a, b):
  """ Algoritmo extendido de Euclides.
  Encuentra números enteros d,s,t que tales que
  as + bt = d = gcd (a,b)
  Parámetros:
  -----------
  a,b : Int
    Números enteros a aplicar el algoritmo
  Regresa:
  --------
  d : Int
    El máximo común divisor de a,b.
  s,t : Int
    Número enteros tales que d = as + bt
  """
  s0, s1, t0, t1 = 1, 0, 0, 1
  while b != 0:
    q, a, b = a // b, b, a % b
    s0, s1 = s1, s0 - q * s1
    t0, t1 = t1, t0 - q * t1
  return a, s0, t0

def main (entrada, salida):
  """Punto de entrada del programa.
  Toma una imagen ENTRADA cifrada usando un alfabeto afín (los bytes)
  y encuentra la llave (a,b) por fuerza bruta.
  """
  imagen_bytes = None
  # Leemos los bytes de la imagen de entrada
  with open (entrada, 'rb') as imagen_cifrada:
    imagen_bytes = bytearray (imagen_cifrada.read ())
  for a in range (2,256):
    g,s,_ = egcd (a,256)
    if g == 1:
      # a es primo relativo con 256, s es su inverso
      for b in range (256):
        # Llave (a,b)
        if (s*(imagen_bytes[0]-b) % 256 == 0xFF
          and s*(imagen_bytes[1]-b) % 256 == 0xD8):
          # El encabezado corresponde a un formato de archivo JPEG
          # 0xFFD8
          imagen = bytearray (map (lambda byte: s*(byte-b)%256, imagen_bytes))
          with open (salida, 'wb') as out:
            out.write (imagen)
          print ('Llave: ({},{})'.format (a,b))
          return 

if __name__ == '__main__':
  if len (sys.argv) > 2:
    e = sys.argv[1] # Archivo de entrada
    s = sys.argv[2] # Archivo de salida
    main (e, s)
